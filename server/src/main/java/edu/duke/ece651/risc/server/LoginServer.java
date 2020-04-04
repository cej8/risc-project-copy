package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import org.mindrot.jbcrypt.*;

public class LoginServer extends Thread{

  private MasterServer masterServer;
  private Connection playerConnection;
  private String user;
  private int activeGameID;

  private static final Lock loginLock = new ReentrantLock();
  private static final Lock registerLock = new ReentrantLock();
  
  public LoginServer(MasterServer masterServer, Connection playerConnection){
    this.masterServer = masterServer;
    this.playerConnection = playerConnection;
  }

  public String getUser(){
    return user;
  }

  public Connection getConnection(){
    return playerConnection;
  }

  public void setUser(String user){
    this.user = user;
  }

  public int getActiveGameID(){
    return activeGameID;
  }

  //Should get through login
  //Does not include encryption on messages
  //Does not obfuscate responses by matching length/message count
  public void loginProcess() throws IOException, ClassNotFoundException{
    //Get here when client first connects to Master
    //Need to inform connection success
    playerConnection.sendObject(new StringMessage("Success: connected to server"));

    while(true){
      //Then wait for login/register boolean
      //True : login, False : register
      ConfirmationMessage loginBoolean = (ConfirmationMessage)(playerConnection.receiveObject());
      
      //Either way will receive username
      String username = ((StringMessage)(playerConnection.receiveObject())).unpacker();
    
      //If true --> expect login
      if(loginBoolean.unpacker()){
        //Check if user exists
        String salt = masterServer.getSalt(username);
        //Send salt (or "" if no user...)
        playerConnection.sendObject(new StringMessage(salt));
        
        //Otherwise wait for returning hashed password
        String hashedPassword = ((StringMessage)(playerConnection.receiveObject())).unpacker();
        loginLock.lock();
        boolean loginSuccess = masterServer.checkLogin(username, hashedPassword);
        if(loginSuccess){
          user = username;
          playerConnection.sendObject(new StringMessage("Success: Logged in"));
          loginLock.unlock();
          return;
        }
        else{
          playerConnection.sendObject(new StringMessage("Fail: User/password incorrect"));
          loginLock.unlock();
          continue;
        }
      }
      //If false --> expect register
      else{
        //Make new salt --> send to client
        String salt = BCrypt.gensalt();
        playerConnection.sendObject(new StringMessage(salt));
        
        String password1 = ((StringMessage)(playerConnection.receiveObject())).unpacker();
        String password2 = ((StringMessage)(playerConnection.receiveObject())).unpacker();
        //If passwords don't match then fail
        if(!password1.equals(password2)){
          playerConnection.sendObject(new StringMessage("Fail: Passwords do not match"));
          continue;
        }
        
        //Enforce lock to prevent double creation
        registerLock.lock();
        //Ensure not starting group name
        if(username.matches("^Group [A-F]$")) {
          playerConnection.sendObject(new StringMessage("Fail: User invalid"));
          registerLock.unlock();
          continue;
        }
        
        //Check if user already exists
        if(masterServer.checkUserExists(username)){
          playerConnection.sendObject(new StringMessage("Fail: User already exists"));
          registerLock.unlock();
          continue;
        }
        else{
          user = username;
          masterServer.addLogin(username, new Pair(password1, salt));
          playerConnection.sendObject(new StringMessage("Success: User created"));
          registerLock.unlock();
          return;
        }
        
      }
    }

  }

  public String buildGamesInfo(List<ParentServer> games){
    StringBuilder sb = new StringBuilder();
    for(ParentServer ps : games){
      sb.append(ps.getGameID() + " : " + ps.getGameTime() + "\n");
    }
    return sb.toString();
  }

  public boolean validGameID(List<ParentServer> games, int gameID){
    for(ParentServer ps : games){
      if(ps.getGameID() == gameID){
        return true;
      }
    }
    return false;
  }
  
  //First prompts for rejoining game or new game
  //Then will give list of games already in or games not started (with start new)
  public void selectGame() throws IOException, ClassNotFoundException{
    while(true){
      //Wait for old/new boolean
      //True : old, False : new
      ConfirmationMessage oldBoolean = (ConfirmationMessage)(playerConnection.receiveObject());

      String gamesList = "";
      List<ParentServer> gamesIn;
      
      if(oldBoolean.unpacker()){
        //If true then get games currently in
        gamesIn = masterServer.getGamesIn(user);
        gamesList = buildGamesInfo(gamesIn);
      }

      else{
        //If false then get games that haven't started
        gamesIn = masterServer.getOpenGames(user);
        //Put "-1 new game" at start
        gamesList = "-1 : New game\n";
        gamesList += buildGamesInfo(gamesIn);
      }

      playerConnection.sendObject(new StringMessage(gamesList));

      //Wait for int return
      int gameID = ((IntegerMessage)(playerConnection.receiveObject())).unpacker().intValue();
      System.out.println(gameID);
      if(oldBoolean.unpacker()){
        if(validGameID(gamesIn, gameID)){
          ParentServer ps = masterServer.getParentServer(gameID);
          if(ps != null){
            boolean join = ps.tryJoin(user, playerConnection);
            if(join){
              playerConnection.sendObject(new StringMessage("Success: Joined " + gameID));
              activeGameID = gameID;
              playerConnection.sendObject(new ConfirmationMessage(masterServer.getParentServer(gameID).getFirstCall(user)));
              playerConnection.sendObject(new HumanPlayer(user));
              return;
            }
          }
        }
        playerConnection.sendObject(new StringMessage("Fail: Failed to join " + gameID));
        continue;
      }
      
      else{
        if(gameID == -1){
          gameID = masterServer.createNewParentServer(user, playerConnection);
          playerConnection.sendObject(new StringMessage("Success: created game " + gameID));
          playerConnection.sendObject(new ConfirmationMessage(masterServer.getParentServer(gameID).getFirstCall(user)));
          playerConnection.sendObject(new HumanPlayer(user));
          return;
        }
        if(validGameID(gamesIn, gameID)){
          ParentServer ps = masterServer.getParentServer(gameID);
          if(ps != null){
            boolean join = ps.tryJoin(user, playerConnection);
            if(join){
              playerConnection.sendObject(new StringMessage("Success: Joined " + gameID));
              activeGameID = gameID;playerConnection.sendObject(new ConfirmationMessage(masterServer.getParentServer(gameID).getFirstCall(user)));
              playerConnection.sendObject(new HumanPlayer(user));
              return;
            }
          }
        }
        playerConnection.sendObject(new StringMessage("Fail: Failed to join " + gameID));
        continue;
      }
    }
  }
  
  @Override
  public void run(){
    try{
      loginProcess();
      selectGame();
    }
    catch(Exception e){
      masterServer.removePlayer(this);
      playerConnection.closeAll();
    }
  }
  
}
