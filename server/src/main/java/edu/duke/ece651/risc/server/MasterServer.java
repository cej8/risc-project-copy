package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

/*
This is the main "hub" that connects all of the games/players. Main function is as highest level data structure holding references to all players/games. Also maintains outward facing connection for clients to connect to (but does not handle logins/messages).

Maintains open ServerSocket to which clients connect (after which they are passed to a LoginServer instance).

Maintains Map of all active games (parentServers) by gameID as key.

Maintains Map of all players online (activePlayers) by username as key.

Maintains Map of all user's logins/passwords (centralized so LoginServers can access a single object rather than reloading map) with username as key and Pair of <hashedpassword, salt>.
*/



//Highest level server object, accepts incoming connections
//And passes to LoginServer
public class MasterServer extends Thread{
  //Outfacing socket for incoming connections
  private ServerSocket serverSocket = null;
  //Map of usernames to <hashed password, salt>
  private Map<String, Pair<String, String>> loginMap;
  //Map of username to LS managing them
  private Map<String, LoginServer> activePlayers;
  //List of GameID to PS with said GameID
  private Map<Integer, ParentServer> parentServers;
  //Value for next new PS GameID
  private int nextGameID = 1;

  //Location of Serialized loginMap for server restart
  private String loginFile;

  public MasterServer(String loginFile) throws IOException, ClassNotFoundException{
    this.loginFile = loginFile;
    
    loginMap = new HashMap<String, Pair<String, String>>();
    System.out.println("Master : " + "Starting with blank loginMap");
    //Attempt to read loginMap from loginFile, otherwise use blank
    if(!loginFile.equals("")){
      try{
        File file = new File(getClass().getClassLoader().getResource(loginFile).getFile());
        System.out.println("Master : " + "Reading users at " + loginFile);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        loginMap = (HashMap<String, Pair<String, String>>)(ois.readObject());
        ois.close();
        fis.close();
      }
      catch(IOException e){
        System.out.println("Master : " + "Failed to read users at " + loginFile);
        e.printStackTrace();
      }
    }

    activePlayers = new HashMap<String, LoginServer>();
    parentServers = new HashMap<Integer, ParentServer>();
  }

  public MasterServer(String loginFile, int port) throws IOException, ClassNotFoundException{
    this(loginFile);
    serverSocket = new ServerSocket(port);
    System.out.println("Master : " + "Opening with port " + port);
  }

  public void setSocket(int port) throws IOException{
    serverSocket = new ServerSocket(port);
    System.out.println("Master : " + "Opening with port " + port);
  }

  public ServerSocket getServerSocket(){
    return serverSocket;
  }

  public Map<String, Pair<String, String>> getLoginMap(){
    return loginMap;
  }

  public Map<Integer, ParentServer> getParentServers(){
    return parentServers;
  }

  //Method to get specific ParentServer via ID
  public ParentServer getParentServer(int gameID){
    return parentServers.get(gameID);
  }

  public void setLoginFile(String loginFile){
    this.loginFile = loginFile;
  }

  //Method to add new username/password to map
  //Only adds if user not already there then attempts to save to disk
  public synchronized boolean addLogin(String user, Pair<String, String> hashedPasswordAndSalt){
    if(loginMap.containsKey(user)){
      return false;
    }
    else{
      loginMap.put(user, hashedPasswordAndSalt);
      System.out.println("Master : " + "Added login for (" + user + ")");
      saveMap();
      return true;
    }
  }

  //Method to get salt for user from map
  public String getSalt(String user){
    if(!loginMap.containsKey(user)){
      return "";
    }
    return loginMap.get(user).getSecond();
  }

  //Helper to add to activePlayer list if not already there
  //Only use on LoginServer that has passed loginProcess() (has a valid user value)
  public boolean addPlayer(LoginServer ls){
    synchronized(activePlayers){
      if(activePlayers.get(ls.getUser()) == null){
         System.out.println("Master : " + "Added (" + ls.getUser() + ") to active players");
         activePlayers.put(ls.getUser(), ls);
         return true;
      }
      System.out.println("Master : " + "Failed to add (" + ls.getUser() + ") to active players, already present");
      return false;
    }
  }

  //Remove player from activePlayers by username/gameID
  //Ensures to only remove if they are in PS with gameID
  public void removePlayer(String username, int gameID){
    synchronized(activePlayers){
      System.out.println("Master : " + "Trying to remove " + username + " in game " + gameID);
      LoginServer ls = activePlayers.get(username);
      if(ls == null) { return; }
      if(ls.getActiveGameID() == gameID){
        System.out.println("Master : " + "Removing " + username + " in game " + gameID);
        activePlayers.remove(username);
      }
    }
  }

  //Remove from activePlayers by LoginServer
  public void removePlayer(LoginServer ls){
    synchronized(activePlayers){
      activePlayers.remove(ls.getUser());
    }
  }

  //Creates new PS and adds user/connection (that created it) to it
  //Method called by LoginServer to create new game
  public synchronized int createNewParentServer(String user, Connection playerConnection) throws IOException{
    ParentServer ps = new ParentServer(nextGameID++, this);
    System.out.println("Master : " + "Created new game (" + (nextGameID-1) + ")");
    ps.start();
    ps.tryJoin(user, playerConnection);
    parentServers.put(ps.getGameID(), ps);
    return ps.getGameID();
  }

  //Add PS to list of games
  public void addParentServer(ParentServer ps){
    parentServers.put(ps.getGameID(), ps);
  }

  //Remove PS by gameID
  public void removeParentServer(Integer gameID){
    System.out.println("Master : " + "Removing game (" + (gameID) + ")");
    parentServers.remove(gameID);
  }

  //Method to save map to file in loginFile within resources folder
  public synchronized void saveMap(){
    if(loginFile.equals("")){ return; }
    try{
      File file = new File(getClass().getClassLoader().getResource(loginFile).getFile());
      FileOutputStream fos = new FileOutputStream(file);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(loginMap);
      oos.flush();
      oos.close();
      fos.close();
      System.out.println("Master : " + "Successfully saved users at " + loginFile);
    }
    catch(Exception e){
      e.printStackTrace();
      System.out.println("Master : " + "Failed to save users at " + loginFile);
    }
  }

  //Method to check user login information
  public boolean checkLogin(String user, String hashedPassword){
    //Check if user in database
    if(!loginMap.containsKey(user)){
      return false;
    }
    //Check if already logged in
    synchronized(activePlayers){
      if(activePlayers.get(user) != null){
        System.out.println("Master : " + user + " tried to login but already logged in");
        return false;
      }
    }
    //Otherwise check if hashed passwords match
    System.out.println("Master : " + user + " trying to log in");
    return hashedPassword.equals(loginMap.get(user).getFirst());
  }

  //Method to check if map contains username
  public boolean checkUserExists(String user){
    return loginMap.containsKey(user);
  }

  //Method to get all games that contain a specific user
  public List<ParentServer> getGamesIn(String user){
    List<ParentServer> gamesIn = new ArrayList<ParentServer>();
    for(ParentServer ps : parentServers.values()){
      if(ps.hasPlayer(user)){
        gamesIn.add(ps);
      }
    }
    return gamesIn;
  }

  //Method to get all games that haven't started yet (and user is not in)
  public List<ParentServer> getOpenGames(String user){
    List<ParentServer> openGames = new ArrayList<ParentServer>();
    for(ParentServer ps : parentServers.values()){
      if(ps.waitingPlayers() && !ps.hasPlayer(user)){
        openGames.add(ps);
      }
    }
    return openGames;
  }

  //Repeated method to accept incoming connections and forward them to a LoginServer
  public void waitingForConnections(){
    if(serverSocket == null){
      try{
        serverSocket = new ServerSocket(Constants.DEFAULT_PORT);
      }
      catch(Exception e){
        e.printStackTrace();
        return;
      }
    }
    
    while (true) {
      Socket newPlayerSocket;
      Connection newPlayerConnection;
      LoginServer newLoginServer;
      try {
        //Accept, set timeout to 60 seconds, create player
        newPlayerSocket = serverSocket.accept();
        System.out.println("Master : " + "New connection incoming");
        newPlayerSocket.setSoTimeout((int)(Constants.LOGIN_WAIT_MINUTES*60*1000));
        newPlayerConnection = new Connection(newPlayerSocket);
        newPlayerConnection.getStreamsFromSocket();
        //Create new LS to handle login/game select
        newLoginServer = new LoginServer(this, newPlayerConnection);
        newLoginServer.start();
      }
      catch (Exception e) {
        System.out.println("Master : " + "Failure in waitingForConnections, closing");
        e.printStackTrace(System.out);
        continue;
      }
    }
  }

  @Override
  public void run(){
    waitingForConnections();
  }

}
