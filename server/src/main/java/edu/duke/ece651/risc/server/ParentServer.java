package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;

// Class handles all server side implmentation including ChildServer handling
public class ParentServer implements Runnable{
  private ServerSocket serverSocket = null;
  private List<ChildServer> children;
  private Board board;
  Map<String, List<OrderInterface>> orderMap;
  ExecutorService threads = Executors.newFixedThreadPool(Constants.MAX_PLAYERS);
  private int MAX_PLAYERS = Constants.MAX_PLAYERS;
  private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
  private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES;

  public ParentServer(){
    BoardGenerator genBoard = new BoardGenerator();
    genBoard.createBoard();
    board = genBoard.getBoard();
    children = new ArrayList<ChildServer>();
    orderMap = new HashMap<String, List<OrderInterface>>();
  }

  public ParentServer(int port) throws IOException{
    this();
    serverSocket = new ServerSocket(port);
  }

  public void setSocket(int port) throws IOException{
    serverSocket = new ServerSocket(port);
  }

  public ServerSocket getServerSocket(){
    return serverSocket;
  }
  
  public List<ChildServer> getChildren(){
    return children;
  }

  public double getTURN_WAIT_MINUTES(){
    return Constants.TURN_WAIT_MINUTES;
  }

  //set's for testing
  public void setMAX_PLAYERS(int MAX_PLAYERS){
    this.MAX_PLAYERS = MAX_PLAYERS;
    try{
      threads = Executors.newFixedThreadPool(this.MAX_PLAYERS);
    }
    //For debug --> assume we won't put negative
    catch(Exception e){
      e.printStackTrace();
    }
  }
  public void setTURN_WAIT_MINUTES(double TURN_WAIT_MINUTES){
    this.TURN_WAIT_MINUTES = TURN_WAIT_MINUTES;
  }
  public void setSTART_WAIT_MINUTES(double START_WAIT_MINUTES){
    this.START_WAIT_MINUTES = START_WAIT_MINUTES;
  }
  
  public void waitingForConnections() throws IOException {
    if(serverSocket == null){
      serverSocket = new ServerSocket(Constants.DEFAULT_PORT);
    }

    long startTime = -1;
    //Start time 2.5 minutes after first connection
    long gameStartTime = (long)(START_WAIT_MINUTES*60*1000);
    
    while (children.size() < MAX_PLAYERS && (startTime == -1 || (System.currentTimeMillis()-startTime < gameStartTime))) {
      HumanPlayer newPlayer;
      Connection newPlayerConnection;
      try {
        //Decrease socket timeout so max waiting of gameStartTime
        if(startTime != -1){
          serverSocket.setSoTimeout((int)(gameStartTime - (System.currentTimeMillis()-startTime)));
        }
        //Accept, set timeout to 60 seconds, create player
        Socket playerSocket = serverSocket.accept();
        playerSocket.setSoTimeout((int)(TURN_WAIT_MINUTES*60*1000));
        newPlayer = new HumanPlayer("Player " + Integer.toString(children.size() + 1));
        newPlayerConnection = new Connection(playerSocket);
        newPlayerConnection.getStreamsFromSocket();
        //Send object to client
        newPlayerConnection.sendObject(newPlayer);
      } catch (Exception e) {
        e.printStackTrace(System.out);
        continue;
      }
      System.out.println(newPlayer.getName() + " joined.");
      //Get time of first connection
      if(startTime == -1){
        startTime = System.currentTimeMillis();
      }
      //Add player to list
      children.add(new ChildServer(newPlayer, newPlayerConnection, this));
    }
    System.out.println("All players or time limit, proceeding");
  }
  public Board getBoard(){
    return this.board;
  }
  public void setBoard(Board board){
    this.board = board;
  }
  public Map<String, List<OrderInterface>> getOrderMap(){
    return orderMap;
  }
  // Helper method to add player to global player list - children
  public void addPlayer(ChildServer c){
    children.add(c);
  }
  // Generate initial board, set region groups based on number of players
  public void createStartingGroupsHelper(char groupName, int iStart, int iEnd, List<Region> regionList){
    HumanPlayer player;
    Unit u = new Unit();
    u.setUnits(0);
    player = new HumanPlayer();
    player.setName("Group " + groupName);
    for (int i = iStart; i < iEnd; i++){
      Region r = regionList.get(i); 
      r.assignRegion(player,u);
    }
  }
  // method to create starting groups for game based on number of players
  public void createStartingGroups(){
    int numPlayers = children.size();
    //int numPlayers = 5;
    List<Region> regionList = board.getRegions();
    char groupName = 'A';
    int remainder = Constants.MAX_REGIONS % numPlayers;
    int placementRegions = Constants.MAX_REGIONS - remainder;
    int groupSize = placementRegions / numPlayers;
    int numGroups;
    if (numPlayers == 5){
      numGroups = numPlayers + 1;
    } else {
      numGroups = numPlayers;
    }
    for (int i = 0; i < numGroups; i++){
      createStartingGroupsHelper(groupName,(groupSize * i), (groupSize + (groupSize * i)), regionList);
      groupName++;
    }
  }
  // Close serverSocket for all children
  public void closeAll(){
    for(ChildServer child : children){
      if(child.getPlayerConnection() != null){ child.getPlayerConnection().closeAll(); }
    }
    if(serverSocket != null){
      try{
        serverSocket.close();
      }
      catch(Exception e){
        e.printStackTrace(System.out);
      }
    }
  }

  public synchronized boolean assignGroups(String groupName, AbstractPlayer player){
    //Method to set initial groups (groupName must be of form "Group _" where _ is A-E)

    //Check valid input
    if(!groupName.matches("^Group [A-F]$")){
      return false;
    }

    boolean foundRegion = false;
    //If valid then replaceb all group with player
    for(Region r : board.getRegions()){
      if(r.getOwner().getName().equals(groupName)){
        r.setOwner(player);
        //Only change boolean if something replaced
        foundRegion = true;
      }
    }

    return foundRegion;
  }

  public void callThreads() throws InterruptedException{
    //Method to call child threads, will prompt player and add all orders to map
    System.out.println("Calling threads");
    List<Callable<Object>> todo = new ArrayList<Callable<Object>>(children.size());
    for(int i = 0; i < children.size(); i++){
      todo.add(Executors.callable(children.get(i)));
    }
    threads.invokeAll(todo);
    System.out.println("Threads finished");
  }

  public Set<AbstractPlayer> playersLeft(){
    //Returns set of all players still playing
    Set<AbstractPlayer> players = new HashSet<AbstractPlayer>();
    for(ChildServer child : children){
      if(child.getPlayer().isPlaying()) players.add(child.getPlayer());
    }
    return players;
  }
  // return number of players left 'alive' in game
  public int numPlayersLeft(){
    return playersLeft().size();
  }
  // returns boolean true if player owns a region
  public boolean playerHasARegion(AbstractPlayer player){
    for(Region r : board.getRegions()){
      if(r.getOwner().getName() == player.getName()){
        return true;
      }
    }
    return false;
  }

  public synchronized void addOrdersToMap(List<OrderInterface> orders){
    //Method to add orders to map
    for(OrderInterface order : orders){
      //Not sure if better way to do casting...
      OrderInterface castOrder;
      if(order instanceof PlacementOrder){
        castOrder = (PlacementOrder)(order);
      }
      else if(order instanceof AttackOrder){
        castOrder = (AttackOrder)(order);
      }
      else if(order instanceof MoveOrder){
        castOrder = (MoveOrder)(order);
      }
      else{
        continue;
      }

      String className = castOrder.getClass().getName();
      className = className.substring(className.lastIndexOf('.') + 1);
      
      //Add list if not present for order type
      if(!orderMap.containsKey(className)){
        orderMap.put(className, new ArrayList<OrderInterface>());
      }
      //Add order to list
      orderMap.get(className).add(castOrder);
    }
  }

  public void applyOrders(){
    //Apply orders to map
    //Mostly hardcoded due to explicit order ordering

    //Reshuffle all subLists
    for(String key : orderMap.keySet()){
      List<OrderInterface> orders = orderMap.get(key);
      Collections.shuffle(orders);
    }
    if(orderMap.containsKey("PlacementOrder")){
      applyOrderList(orderMap.get("PlacementOrder"));
    }
    if(orderMap.containsKey("MoveOrder")){
      applyOrderList(orderMap.get("MoveOrder"));
    }
    if(orderMap.containsKey("AttackOrder")){
      applyOrderList(orderMap.get("AttackOrder"));
    }    
  }

  public void applyOrderList(List<OrderInterface> orders){
    //Simply call doAction for each order
    for(int i = 0; i < orders.size(); i++){
      orders.get(i).doSourceAction();
    }
    for(int i = 0; i < orders.size(); i++){
      orders.get(i).doDestinationAction();
    }
    orders.clear();
  }
  // method to add additional unit after round complete to all regions on board
  public void growUnits(){
    for(Region r : board.getRegions()){
      r.setUnits(new Unit(r.getUnits().getUnits()+1));
    }
  }
  // method that controls game play
  public void playGame(){
    try{
      //Wait for MAX_PLAYERS to connect or timeout
      waitingForConnections();
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return;
    }
    //While regions not owned all by one player
    createStartingGroups();
    boolean notFirstCall = false;
    while(numPlayersLeft() > 1){
      try{
        //Prompt users
        callThreads();
      }
      catch(Exception e){
        e.printStackTrace();
        closeAll();
        return;
      }
      //Apply orders
      applyOrders();
      if(notFirstCall){
        growUnits();
      }
      notFirstCall = true;
    }
    if(numPlayersLeft() == 1){
      //If one player alive then create message --> send
      AbstractPlayer winner = playersLeft().iterator().next();
      StringMessage winnerMessage = new StringMessage(winner.getName() + " is the winner!");
      //Send message to all children
      for(ChildServer child : children){
        try{
          child.getPlayerConnection().sendObject(winnerMessage);
        }
        catch(Exception e){}
      }
    }
    //Close all
    closeAll();
  }
  // enables game to be runnable
  @Override
  public void run(){
    System.out.println("MAX_PLAYERS: " + MAX_PLAYERS);
    System.out.println("TURN_WAIT_MINUTES:" + TURN_WAIT_MINUTES);
    System.out.println("START_WAIT_MINUTES:" + START_WAIT_MINUTES);
    playGame();
    System.out.println("~~~GAMEOVER~~~");
  }
}
