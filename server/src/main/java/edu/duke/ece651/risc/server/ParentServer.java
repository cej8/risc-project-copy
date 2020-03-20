package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;


public class ParentServer {
  private final int MAX_PLAYERS = 1;
  private final int MAX_REGIONS = 12;
  private final double START_WAIT_MINUTES = 2.5;
  private final double TURN_WAIT_MINUTES = 1;
  private final int DEFAULT_PORT = 12345;
  private ServerSocket serverSocket = null;
  private List<ChildServer> children;
  private Board board;
  Map<String, List<OrderInterface>> orderMap;

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
  
  public List<ChildServer> getChildren(){
    return children;
  }

  public double getTURN_WAIT_MINUTES(){
    return TURN_WAIT_MINUTES;
  }
  
  public void waitingForConnections() throws IOException {
    if(serverSocket == null){
      serverSocket = new ServerSocket(DEFAULT_PORT);
    }

    long startTime = -1;
    //Start time 2.5 minutes after first connection
    long gameStartTime = (long)(START_WAIT_MINUTES*60*1000);
    
    while (children.size() < MAX_PLAYERS && (startTime == -1 || (System.currentTimeMillis()-startTime < gameStartTime))) {
      HumanPlayer newPlayer;
      try {
        if(startTime != -1){
          serverSocket.setSoTimeout((int)(gameStartTime - (System.currentTimeMillis()-startTime)));
        }
        //Accept, set timeout to 60 seconds, create player
        Socket playerSocket = serverSocket.accept();
        playerSocket.setSoTimeout((int)(TURN_WAIT_MINUTES*60*1000));
        newPlayer = new HumanPlayer("Player " + Integer.toString(children.size() + 1), playerSocket);
        //Send object to client
        newPlayer.getConnection().sendObject(newPlayer);
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
      children.add(new ChildServer(newPlayer, this));
    }
    
  }
  public Board getBoard(){
    return this.board;
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
  public void createStartingGroups(){
    int numPlayers = children.size();
    //int numPlayers = 5;
    List<Region> regionList = board.getRegions();
    char groupName = 'A';
    int remainder = MAX_REGIONS % numPlayers;
    int placementRegions = MAX_REGIONS - remainder;
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
  public void closeAll(){
    for(ChildServer child : children){
      child.getPlayer().getConnection().closeAll();
    }
    try{
      serverSocket.close();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  public synchronized boolean assignGroups(String groupName, AbstractPlayer player){
    //Method to set initial groups (groupName must be of form "Group _" where _ is A-E)

    boolean succeed = false;

    //Check valid input
    if(!groupName.matches("^Group [A-E]$")){
      return succeed;
    }
    //If valid then replace all group with player
    for(Region r : board.getRegions()){
      if(r.getOwner().getName().equals(groupName)){
        r.setOwner(player);
        //Only change boolean if something replaced
        succeed = true;
      }
    }

    return succeed;
  }

  public void callThreads() throws InterruptedException{
    //Method to call child threads, will prompt player and add all orders to map
    
    for(int i = 0; i < children.size(); i++){
      children.get(i).start();
    }

    for(int i = 0; i < children.size(); i++){
      children.get(i).join();
    }

  }

  public Set<AbstractPlayer> playersLeft(){
    //Returns set of all players still playing
    Set<AbstractPlayer> players = new HashSet<AbstractPlayer>();
    for(ChildServer child : children){
      if(child.getPlayer().isPlaying()) players.add(child.getPlayer());
    }
    return players;
  }

  public int numPlayersLeft(){
    return playersLeft().size();
  }

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

      //Call conversion method to get proper regions on server's board
      castOrder.convertOrderRegions(board);

      //Add list if not present for order type
      if(!orderMap.containsKey(castOrder.getClass().getName())){
        orderMap.put(castOrder.getClass().getName(), new ArrayList<OrderInterface>());
      }
      //Add order to list
      orderMap.get(castOrder.getClass().getName()).add(castOrder);
    }
  }

  public void applyOrders(){
    //Apply orders to map
    //Mostly hardcoded due to explicit order ordering

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
      orders.get(i).doAction();
    }
    orders.clear();
  }


  public void playGame(){
    
    try{
      //Wait for MAX_PLAYERS to connect
      waitingForConnections();
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return;
    }
    //While regions not owned all by one player
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
    }

    if(numPlayersLeft() == 1){
      //If one player alive then create message --> send
      AbstractPlayer winner = playersLeft().iterator().next();
      StringMessage winnerMessage = new StringMessage(winner.getName() + " is the winner!");

      //Send message to all children
      for(ChildServer child : children){
        try{
          child.getPlayer().getConnection().sendObject(winnerMessage);
        }
        catch(Exception e){}
      }
    }
    //Close all
    closeAll();
  }
  
}
