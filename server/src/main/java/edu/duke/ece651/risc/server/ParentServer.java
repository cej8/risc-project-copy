package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;

// Class handles all server side implmentation including ChildServer handling
public class ParentServer extends Thread{
  private List<ChildServer> children;
  private List<String> players;
  private Board board;
  private Map<String, List<OrderInterface>> orderMap;
  private ExecutorService threads = Executors.newFixedThreadPool(Constants.MAX_PLAYERS);
  private int MAX_PLAYERS = Constants.MAX_PLAYERS;
  private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
  private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES;
  
  private boolean notStarted = true;
  private int gameID;

  private StringBuilder turnResults;
  private int turnNumber = 1;

  private MasterServer masterServer;
  private long gameStart;

  public ParentServer(){
    BoardGenerator genBoard = new BoardGenerator();
    genBoard.createBoard();
    board = genBoard.getBoard();
    children = new ArrayList<ChildServer>();
    players = new ArrayList<String>();
    orderMap = new HashMap<String, List<OrderInterface>>();
    turnResults = new StringBuilder("Turn 0: Start of game\n");
  }

  public ParentServer(int gameID, MasterServer masterServer){
    this();
    this.gameID = gameID;
    this.masterServer = masterServer;
  }

  public List<ChildServer> getChildren() {
    return children;
  }

  public double getTURN_WAIT_MINUTES() {
    return TURN_WAIT_MINUTES;
  }

  public int getGameID(){
    return gameID;
  }

  public void setNotStarted(boolean notStarted){
    this.notStarted = notStarted;
  }

  public MasterServer getMasterServer(){
    return masterServer;
  }

  public boolean hasPlayer(String player){
    return players.contains(player);
  }

  public boolean waitingPlayers(){
    return notStarted && children.size() < MAX_PLAYERS;
  }

  public String getGameTime(){
    if(notStarted){
      long timeLeft = (long)(60*1000*START_WAIT_MINUTES) - (System.currentTimeMillis()-gameStart);
      return String.format("%02d:%02d", 
                           TimeUnit.MILLISECONDS.toMinutes(timeLeft) - 
                           TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft)),
                           TimeUnit.MILLISECONDS.toSeconds(timeLeft) - 
                           TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft)));
    }
    else{
      long timeStart = System.currentTimeMillis()-gameStart;
      return String.format("%02d:%02d:%02d", 
                           TimeUnit.MILLISECONDS.toHours(timeStart),
                           TimeUnit.MILLISECONDS.toMinutes(timeStart) - 
                           TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeStart)),
                           TimeUnit.MILLISECONDS.toSeconds(timeStart) - 
                           TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeStart)));
    }
  }

  //set's for testing
  public void setMAX_PLAYERS(int MAX_PLAYERS){
    this.MAX_PLAYERS = MAX_PLAYERS;
    try {
      threads = Executors.newFixedThreadPool(this.MAX_PLAYERS);
    }
    // For debug --> assume we won't put negative
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setTURN_WAIT_MINUTES(double TURN_WAIT_MINUTES) {
    this.TURN_WAIT_MINUTES = TURN_WAIT_MINUTES;
  }

  public void setSTART_WAIT_MINUTES(double START_WAIT_MINUTES) {
    this.START_WAIT_MINUTES = START_WAIT_MINUTES;
  }

  public boolean getFirstCall(String user){
    return children.get(players.indexOf(user)).getFirstCall();
  }
  
  public void waitingForPlayers() {

    long startTime = System.currentTimeMillis();;
    //Start time 2.5 minutes after first connection
    long gameStartTime = (long)(START_WAIT_MINUTES*60*1000);

    gameStart = System.currentTimeMillis();
    
    while (children.size() < MAX_PLAYERS && (System.currentTimeMillis()-startTime < gameStartTime)) {
      //Set timeout to .5 seconds --> try to read
      //If timeout then still there
      synchronized(children){
        for(ChildServer child : children){
          Connection playerConnection = child.getPlayerConnection();
          if(playerConnection != null){
            try{
              playerConnection.getSocket().setSoTimeout(500);
              playerConnection.receiveObject();
            }
            catch(Exception e){
              if(!(e instanceof SocketTimeoutException)){
                playerConnection.closeAll();
                playerConnection = null;
                masterServer.removePlayer(child.getPlayer().getName(), gameID);
              }
            }
          }
        }
      }
      try{
        Thread.sleep(500);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    notStarted = false;
    System.out.println("All players or time limit, proceeding on " + gameID);
    gameStart = System.currentTimeMillis();
  }

  public Board getBoard() {
    return this.board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public Map<String, List<OrderInterface>> getOrderMap() {
    return orderMap;
  }

  // Helper method to add player to global player list - children
  public void addPlayer(ChildServer c){
    synchronized(children){
      children.add(c);
    }
  }

  public void removePlayer(String username){
    players.remove(username);
  }

  //Method to add username/connection to game by creating new childserver
  public synchronized void addPlayer(String username, Connection playerConnection){
    HumanPlayer player = new HumanPlayer(username);
    addPlayer(new ChildServer(player, playerConnection, this));
    players.add(username);
  }

  //Method to try to join game
  public synchronized boolean tryJoin(String username, Connection playerConnection) throws IOException{
    //If game not started then trying to join as new player
    if(notStarted){
      //Confirm not already in game and still space
      if(players.contains(username)){
        children.get(players.indexOf(username)).setPlayerConnection(playerConnection);
        return true;
      }
      if(children.size() >= MAX_PLAYERS){
        return false;
      }
      //If there is then add
      addPlayer(username, playerConnection);
      return true;
    }
    //Otherwise game in progress and trying to rejoin
    else{
      //Confirm valid player
      if(!players.contains(username)){
        return false;
      }
      //If is then give proper childserver connection
      children.get(players.indexOf(username)).setPlayerConnection(playerConnection);
      return true;
    }
  }

  public boolean tryJoin(LoginServer ls) throws IOException{
    return tryJoin(ls.getUser(), ls.getConnection());
  }
  
  // Generate initial board, set region groups based on number of players
  public void createStartingGroupsHelper(char groupName, int iStart, int iEnd, List<Region> regionList) {
    HumanPlayer player;
    Unit u = new Unit();
    // u.setUnits(0);
    player = new HumanPlayer();
    player.setName("Group " + groupName);
    for (int i = iStart; i < iEnd; i++) {
      Region r = regionList.get(i);
      r.assignRegion(player, u);
    }
  }

  // method to create starting groups for game based on number of players
  public void createStartingGroups() {
    int numPlayers = children.size();
    // int numPlayers = 5;
    List<Region> regionList = board.getRegions();
    char groupName = 'A';
    int remainder = Constants.MAX_REGIONS % numPlayers;
    int placementRegions = Constants.MAX_REGIONS - remainder;
    int groupSize = placementRegions / numPlayers;
    int numGroups;
    if (numPlayers == 5) {
      numGroups = numPlayers + 1;
    } else {
      numGroups = numPlayers;
    }
    for (int i = 0; i < numGroups; i++) {
      createStartingGroupsHelper(groupName, (groupSize * i), (groupSize + (groupSize * i)), regionList);
      groupName++;
    }
  }

  // Close serverSocket for all children
  public void closeAll() {
    for (ChildServer child : children) {
      if (child.getPlayerConnection() != null) {
        child.getPlayerConnection().closeAll();
        masterServer.removePlayer(child.getPlayer().getName(), gameID);
      }
    }
  }

  public synchronized boolean assignGroups(String groupName, AbstractPlayer player) {
    // Method to set initial groups (groupName must be of form "Group _" where _ is
    // A-E)

    // Check valid input
    if (!groupName.matches("^Group [A-F]$")) {
      return false;
    }

    boolean foundRegion = false;
    // If valid then replaceb all group with player
    for (Region r : board.getRegions()) {
      if (r.getOwner().getName().equals(groupName)) {
        r.setOwner(player);
        // Only change boolean if something replaced
        foundRegion = true;
      }
    }

    return foundRegion;
  }

  public void callThreads() throws InterruptedException {
    // Method to call child threads, will prompt player and add all orders to map
    System.out.println("Calling threads");
    List<Callable<Object>> todo = new ArrayList<Callable<Object>>(children.size());
    for (int i = 0; i < children.size(); i++) {
      todo.add(Executors.callable(children.get(i)));
      // Insert message into children
      children.get(i).setTurnMessage(turnResults.toString());
    }
    threads.invokeAll(todo);
    System.out.println("Threads finished");
  }

  public Set<AbstractPlayer> playersLeft() {
    // Returns set of all players still playing
    Set<AbstractPlayer> players = new HashSet<AbstractPlayer>();
    for (ChildServer child : children) {
      if (child.getPlayer().isPlaying())
        players.add(child.getPlayer());
    }
    return players;
  }

  // return number of players left 'alive' in game
  public int numPlayersLeft() {
    return playersLeft().size();
  }

  // returns boolean true if player owns a region
  public boolean playerHasARegion(AbstractPlayer player) {
    for (Region r : board.getRegions()) {
      if (r.getOwner().getName() == player.getName()) {
        return true;
      }
    }
    return false;
  }

  public synchronized void addOrdersToMap(List<OrderInterface> orders) {
    // Method to add orders to map
    for (OrderInterface order : orders) {
      // Not sure if better way to do casting...
      OrderInterface castOrder;
      if (order instanceof PlacementOrder) {
        castOrder = (PlacementOrder) (order);
      } else if (order instanceof AttackMove) {
        castOrder = (AttackMove) (order);
      } else if (order instanceof AttackCombat) {
        castOrder = (AttackCombat) (order);
      }

      else if (order instanceof MoveOrder) {
        castOrder = (MoveOrder) (order);
      } else {
        continue;
      }

      String className = castOrder.getClass().getName();
      className = className.substring(className.lastIndexOf('.') + 1);

      // Add list if not present for order type
      if (!orderMap.containsKey(className)) {
        orderMap.put(className, new ArrayList<OrderInterface>());
      }
      // Add order to list
      orderMap.get(className).add(castOrder);
    }
  }

  public void applyOrders() {
    // Apply orders to map
    // Mostly hardcoded due to explicit order ordering

    turnResults = new StringBuilder("Turn " + turnNumber + ":\n");

    // Reshuffle all subLists
    for (String key : orderMap.keySet()) {
      List<OrderInterface> orders = orderMap.get(key);

      Collections.shuffle(orders);
    }
    if (orderMap.containsKey("PlacementOrder")) {
      applyOrderList(orderMap.get("PlacementOrder"));
    }
    if (orderMap.containsKey("MoveOrder")) {
      applyOrderList(orderMap.get("MoveOrder"));
    }
    if (orderMap.containsKey("AttackOrder")) {
      applyOrderList(orderMap.get("AttackOrder"));
    }
  }

  public void applyOrderList(List<OrderInterface> orders) {
    // Simply call doAction for each order
    for (int i = 0; i < orders.size(); i++) {
      turnResults.append(orders.get(i).doAction());
    }
    // for(int i = 0; i < orders.size(); i++){
    // turnResults.append(orders.get(i).doDestinationAction());
    // }
    orders.clear();
  }
  // method to add additional unit after round complete to all regions on board

  public void growUnits() {
    for (Region r : board.getRegions()) {
      // increment number of basic units
      if(players.contains(r.getOwner().getName())){
        r.getOwner().getResources().getFuelResource().addFuel(r.getFuelProduction());
        r.getOwner().getResources().getTechResource().addTech(r.getTechProduction());
        r.getUnits().getUnits().set(0, r.getUnits().getUnits().get(0) + 1);
      }
    }

  }

  // method that controls game play
  public void playGame(){
    //Wait for MAX_PLAYERS to connect or timeout
    waitingForPlayers();
    
    //While regions not owned all by one player
    createStartingGroups();
    while (turnNumber == 1 || numPlayersLeft() > 1) {
      try {
        // Prompt users
        callThreads();
      } catch (Exception e) {
        e.printStackTrace();
        closeAll();
        return;
      }
      // Apply orders
      applyOrders();
      if (turnNumber > 1) {
        growUnits();
      }
      turnNumber++;
    }
    if (numPlayersLeft() == 1) {
      // If one player alive then create message --> send
      AbstractPlayer winner = playersLeft().iterator().next();
      StringMessage winnerMessage = new StringMessage(winner.getName() + " is the winner!");
      // Send message to all children
      for (ChildServer child : children) {
        try {
          child.getPlayerConnection().sendObject(new StringMessage(turnResults.toString()));
          child.getPlayerConnection().sendObject(winnerMessage);
        } catch (Exception e) {
        }
      }
    }
    // Close all
    closeAll();
    masterServer.removeParentServer(gameID);
  }

  // enables game to be runnable
  @Override
  public void run() {
    System.out.println("MAX_PLAYERS: " + MAX_PLAYERS);
    System.out.println("TURN_WAIT_MINUTES:" + TURN_WAIT_MINUTES);
    System.out.println("START_WAIT_MINUTES:" + START_WAIT_MINUTES);
    playGame();
    System.out.println("~~~GAMEOVER~~~");
  }
}
