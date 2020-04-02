package edu.duke.ece651.risc.server;

import java.net.*;
import java.util.*;
import java.io.*;

//Highest level server object, accepts incoming connections
//And passes to LoginServer
public class MasterServer {
  private ServerSocket serverSocket = null;
  private Map<String, Pair> loginMap;
  private List<LoginServer> activePlayers;
  private Map<Integer, ParentServer> parentServers;
  private int nextGameID = 0;

  public MasterServer() throws IOException, ClassNotFoundException{
    ObjectInputStream ois = new ObjectInputStream(getClass().getClassLoader().getResourceAsStream("logins"));
    if(ois){
      loginMap = (HashMap<String, Pair>)(ois.readObject());
    }
    else{
      loginMap = new HashMap<String, Pair>();
    }

    activePlayers = new ArrayList<LoginServer>();
    parentServers = new ArrayList<ParentServer();
  }

  public MasterServer(int port) throws IOException, ClassNotFoundException{
    this();
    serverSocket = new ServerSocket(port);
  }

  public void setSocket(int port) throws IOException{
    serverSocket = new ServerSocket(port);
  }

  public ServerSocket getServerSocket(){
    return serverSocket;
  }

  //Method to save map to file "logins" in resources
  public synchronized void saveMap(){
    try{
      File file = new File(getClass().getClassLoader().getResource("logins").getFile());
      FileOutputStream fos = new FileOutputStream(file);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(logins);
      oos.flush();
      oos.close();
      fos.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  //Method to remove active player (on socket failure)
  public void removeActivePlayer(LoginServer ls){
    synchronized(activePlayers){
      activePlayers.remove(ls);
    }
  }

  //Method to add new username/password to map
  public synchronized boolean addLogin(String user, Pair<String, String> hashedPassword){
    if(logins.containsKey(user)){
      return false;
    }
    else{
      logins.put(user, hashedPassword);
      saveMap();
    }
  }

  //Method to check user login information
  public boolean checkLogin(String user, Pair<String, String> hashedPassword){
    if(!logins.containsKey(user)){
      return false;
    }
    synchronized(activePlayers){
      for(LoginServer ls : activePlayers){
        if(ls.getUser().equals(user)){
          return false;
        }
      }
    }
    return hashedPassword.equals(map.get(user));
  }

  //Method to get salt for user from map
  public String getSalt(String user){
    if(!logins.containsKey(user)){
      return "";
    }
    return logins.get(user).getValue();
  }

  //Method to check if map contains username
  public boolean checkUserExists(String user){
    return logins.containsKey(user);
  }

  //Method to get all games that contain a user
  public List<ParentServer> getGamesIn(String user){
    List<ParentServer> gamesIn = new ArrayList<ParentServer>();
    for(ParentServer ps : parentServers.values()){
      if(ps.hasPlayer(user)){
        gamesIn.add(ps);
      }
    }
    return gamesIn;
  }

  //Method to get all games that haven't started yet
  public List<ParentServer> getOpenGames(){
    List<ParentServer> openGames = new ArrayList<ParentServer>();
    for(ParentServer ps : parentServers.values()){
      if(ps.waitingPlayers()){
        openGames.add(ps);
      }
    }
    return openGames;
  }

  //Method to get specific ParentServer via ID
  public ParentServer getParentServer(int gameID){
    return parentServers.get(gameID);
  }

  //Repeated method to accept incoming connections and forward them to a LoginServer
  public void waitingForConnections() throws IOException{
    if(serverSocket == null){
      serverSocket = new ServerSocket(Constants.DEFAULT_PORT);
    }
    
    while (true) {
      Socket newPlayerSocker;
      Connection newPlayerConnection;
      LoginServer newLoginServer;
      try {
        //Accept, set timeout to 60 seconds, create player
        newPlayerSocket = serverSocket.accept();
        newPlayerSocket.setSoTimeout((int)(TURN_WAIT_MINUTES*60*1000));
        newPlayerConnection = new Connection(playerSocket);
        newPlayerConnection.getStreamsFromSocket();
        //Send object to client
        newLoginServer = new LoginServer(this, newPlayerConnection);
        synchronized(activePlayers){
          activePlayers.add(newLoginServer);
        }
      } catch (Exception e) {
        e.printStackTrace(System.out);
        continue;
      }
    }
  }

  public synchronized int createNewParentServer(String user, Connection playerConnection){
    ParentServer ps = new ParentServer(nextGameID++, this);
    ps.addPlayer(user, playerConnection);
  }

  public void removePlayer(String username, int gameID){
    synchronized(activePlayers){
      for(LoginServer ls : activePlayers){
        if(ls.getUser().equals(username) && ls.getActiveGameID() == gameID){
          activePlayers.remove(ls);
          return;
        }
      }
    }
  }

}
