package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;


public class ParentServer {
  private final int MAX_PLAYERS = 1;
  private final int MAX_REGIONS = 12;
  private ServerSocket serverSocket;
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
  
  public List<ChildServer> getChildren(){
    return children;
  }
  
  public void waitingForConnections() throws IOException {
    while (children.size() < MAX_PLAYERS) {
      HumanPlayer newPlayer;
      try {
        //Accept, set timeout to 60 seconds, create player
        Socket playerSocket = serverSocket.accept();
        playerSocket.setSoTimeout(60*1000);
        newPlayer = new HumanPlayer("Player " + Integer.toString(children.size() + 1), playerSocket);
      } catch (Exception e) {
        e.printStackTrace(System.out);
        continue;
      }
      System.out.println(newPlayer.getName() + " joined.");
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
    //int numPlayers = children.size();
    int numPlayers = 5;
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
      child.getPlayer().closeAll();
    }
    try{
      serverSocket.close();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  public int numAlive(){
    int numAlive = 0;
    for(ChildServer child : children){
      if(child.getPlayer().isPlaying()) numAlive++;
    }
    return numAlive;
  }

  public synchronized boolean assignGroups(String groupName, AbstractPlayer player){
    //Method to set initial groups (groupName must be of form "Group _" where _ is A-E)

    boolean succeed = false;

    if(!groupName.matches("^Group [A-E]$")){
      return succeed;
    }
    
    for(Region r : board.getRegions()){
      if(r.getOwner().getName().equals(groupName)){
        r.setOwner(player);
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

  public boolean isAlive(AbstractPlayer player){
    //Check if player still in game --> at least one region owned
    for(Region r : board.getRegions()){
      if(r.getOwner() == player){
        return true;
      }
    }
    return false;
  }

  public Set<AbstractPlayer> playersLeft(){
    Set<AbstractPlayer> players = new HashSet<AbstractPlayer>();
    for(ChildServer child : children){
      if(child.getPlayer().isPlaying()) players.add(child.getPlayer());
    }
    return players;
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

      //Add list if not present
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
    for(int i = 0; i < orders.size(); i++){
      orders.get(i).doAction();
    }
    orders.clear();
  }


  public void playGame(){
    try{
      waitingForConnections();
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return;
    }
    while(numAlive() > 1){
      try{
        callThreads();
      }
      catch(Exception e){
        e.printStackTrace();
        closeAll();
        return;
      }
      applyOrders();
    }

    AbstractPlayer winner = playersLeft().iterator().next();
    StringMessage winnerMessage = new StringMessage(winner.getName() + " is the winner!");
    
    for(ChildServer child : children){
      try{
        child.getPlayer().sendObject(winnerMessage);
      }
      catch(Exception e){}
    }
    
    closeAll();
  }
  
}
