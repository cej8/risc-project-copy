package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import org.mindrot.jbcrypt.*;

/*
This is the object that deals with the initial login and game select for a client. This is done by accessing the user information and active games in the MasterServer and prompting the client.This is also the class that "attaches" the connection to a single ParentServer, allowing the user to connect to a game.

Maintains connection for current player but does not handle passing messages to game (once logged in/selected game then done with active function).

Used as value for seeing which players are connected within MasterServer.
*/



public class LoginServer extends Thread{
  //MS that owns LS
  private MasterServer masterServer;
  //Connection to client
  private Connection playerConnection;
  //String of client's username (starts "")
  private String user;
  //String of gameID trying/has joined
  private int activeGameID;

  //Lock for logins
  private static final Lock loginLock = new ReentrantLock();
  //Lock for registration
  private static final Lock registerLock = new ReentrantLock();
  
  public LoginServer(MasterServer masterServer, Connection playerConnection){
    this.masterServer = masterServer;
    this.playerConnection = playerConnection;
    this.user = "";
    this.activeGameID = -1;
  }

  public Connection getConnection(){
    return playerConnection;
  }

  //Helper method for testing
  public void setUser(String user){
    this.user = user;
  }

  public String getUser(){
    return user;
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
      //Double sure that empty string before
      user = "";
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
          //Add player, return if successful
          if(masterServer.addPlayer(this)){
            playerConnection.sendObject(new StringMessage("Success: Logged in"));
            loginLock.unlock();
            return;
          }
        }
        playerConnection.sendObject(new StringMessage("Fail: User/password incorrect"));
        loginLock.unlock();
        continue;
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
        if(username.matches("^Group [A-F]$") || username.equals("")) {
          playerConnection.sendObject(new StringMessage("Fail: User invalid"));
          registerLock.unlock();
          continue;
        }
        
        //Check if user already exists
        if(!masterServer.checkUserExists(username)){
          user = username;
          //Ensure that no one else logged in as this user in the mean time
          if(masterServer.addPlayer(this)){
            masterServer.addLogin(username, new Pair(password1, salt));
            playerConnection.sendObject(new StringMessage("Success: User created"));
            registerLock.unlock();
            return;
          }
        }
        playerConnection.sendObject(new StringMessage("Fail: User already exists"));
        registerLock.unlock();
        continue;
      }
    }

  }

  //Method to build string for each PS in list
  public String buildGamesInfo(List<ParentServer> games){
    StringBuilder sb = new StringBuilder();
    for(ParentServer ps : games){
      sb.append(ps.getGameID() + " : " + ps.getGameString() + "\n");
    }
    return sb.toString();
  }

  //Get PS with gameID of "gameID"
  public boolean validGameID(List<ParentServer> games, int gameID){
    for(ParentServer ps : games){
      if(ps.getGameID() == gameID){
        return true;
      }
    }
    return false;
  }

  //Method to send messages upon game join
  public void sendJoinMessages() throws IOException{
    System.out.println(user + " joining " + activeGameID);
    //Send join
    playerConnection.sendObject(new StringMessage("Success: Joined " + activeGameID));
    //Send if firstCall of threads
    playerConnection.sendObject(new ConfirmationMessage(masterServer.getParentServer(activeGameID).getFirstCall(user)));
    //Send user's object
    playerConnection.sendObject(new HumanPlayer(user));
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
        gamesList = "0 : New game\n";
        gamesList += buildGamesInfo(gamesIn);
      }

      playerConnection.sendObject(new StringMessage(gamesList));

      //Wait for int return
      int gameID = ((IntegerMessage)(playerConnection.receiveObject())).unpacker().intValue();
      System.out.println(user + " returned " + gameID);
      if(oldBoolean.unpacker()){
        if(validGameID(gamesIn, gameID)){
          ParentServer ps = masterServer.getParentServer(gameID);
          if(ps != null){
            boolean join = ps.tryJoin(user, playerConnection);
            if(join){
              activeGameID = gameID;
              sendJoinMessages();
              return;
            }
          }
        }
        playerConnection.sendObject(new StringMessage("Fail: Failed to join " + gameID));
        continue;
      }
      
      else{
        if(gameID == 0){
          gameID = masterServer.createNewParentServer(user, playerConnection);
          activeGameID = gameID;
          sendJoinMessages();
          return;
        }
        if(validGameID(gamesIn, gameID)){
          ParentServer ps = masterServer.getParentServer(gameID);
          if(ps != null){
            boolean join = ps.tryJoin(user, playerConnection);
            if(join){
              activeGameID = gameID;
              sendJoinMessages();
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
    //Try login/select
    try{
      loginProcess();
      selectGame();
    }
    //If failure then remove LS and close connection
    catch(Exception e){
      masterServer.removePlayer(this);
      playerConnection.closeAll();
    }
  }
  
}
