package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

//Highest level server object, accepts incoming connections
//And passes to LoginServer
public class MasterServer {
  private ServerSocket serverSocket = null;
  private Map<String, Pair<String, String>> loginMap;
  private Map<String, LoginServer> activePlayers;
  private Map<Integer, ParentServer> parentServers;
  private int nextGameID = 1;

  private String loginFile;

  public MasterServer(String loginFile) throws IOException, ClassNotFoundException{
    this.loginFile = loginFile;
    
    loginMap = new HashMap<String, Pair<String, String>>();

    if(!loginFile.equals("")){
      try{
        File file = new File(getClass().getClassLoader().getResource(loginFile).getFile());
        System.out.println("Reading users at " + file.getAbsolutePath());
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        loginMap = (HashMap<String, Pair<String, String>>)(ois.readObject());
        ois.close();
        fis.close();
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }

    activePlayers = new HashMap<String, LoginServer>();
    parentServers = new HashMap<Integer, ParentServer>();
  }

  public MasterServer(String loginFile, int port) throws IOException, ClassNotFoundException{
    this(loginFile);
    serverSocket = new ServerSocket(port);
  }

  public void setSocket(int port) throws IOException{
    serverSocket = new ServerSocket(port);
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

  public void addLoginServer(LoginServer ls){
    synchronized(activePlayers){
      activePlayers.put(ls.getUser(), ls);
    }
  }

  //Method to save map to file "logins" in resources
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
      System.out.println("Successfully saved users at " + file.getAbsolutePath());
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
  public synchronized boolean addLogin(String user, Pair<String, String> hashedPasswordAndSalt){
    if(loginMap.containsKey(user)){
      return false;
    }
    else{
      loginMap.put(user, hashedPasswordAndSalt);
      saveMap();
      return true;
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
        return false;
      }
    }
    return hashedPassword.equals(loginMap.get(user).getFirst());
  }

  //Method to get salt for user from map
  public String getSalt(String user){
    if(!loginMap.containsKey(user)){
      return "";
    }
    return loginMap.get(user).getSecond();
  }

  //Method to check if map contains username
  public boolean checkUserExists(String user){
    return loginMap.containsKey(user);
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
      Socket newPlayerSocket;
      Connection newPlayerConnection;
      LoginServer newLoginServer;
      try {
        //Accept, set timeout to 60 seconds, create player
        newPlayerSocket = serverSocket.accept();
        System.out.println("new player incoming");
        newPlayerSocket.setSoTimeout((int)(Constants.LOGIN_WAIT_MINUTES*60*1000));
        newPlayerConnection = new Connection(newPlayerSocket);
        newPlayerConnection.getStreamsFromSocket();
        //Send object to client
        newLoginServer = new LoginServer(this, newPlayerConnection);
        newLoginServer.start();
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
        continue;
      }
    }
  }

  public synchronized int createNewParentServer(String user, Connection playerConnection) throws IOException{
    ParentServer ps = new ParentServer(nextGameID++, this);
    ps.start();
    ps.tryJoin(user, playerConnection);
    parentServers.put(ps.getGameID(), ps);
    return ps.getGameID();
  }

  public void removePlayer(String username, int gameID){
    synchronized(activePlayers){
      LoginServer ls = activePlayers.get(username);
      if(ls == null) { return; }
      if(ls.getActiveGameID() == gameID){
        activePlayers.remove(ls);
      }
    }
  }

  public void removePlayer(LoginServer ls){
    synchronized(activePlayers){
      activePlayers.remove(ls.getUser());
    }
  }

  public boolean addPlayer(LoginServer ls){
    synchronized(activePlayers){
      if(activePlayers.get(ls.getUser()) == null){
         activePlayers.put(ls.getUser(), ls);
         return true;
      }
      return false;
    }
  }

  public void removeParentServer(Integer gameID){
    parentServers.remove(gameID);
  }

  public void addParentServer(ParentServer ps){
    parentServers.put(ps.getGameID(), ps);
  }

  public void run() throws IOException{
    waitingForConnections();
  }

}
