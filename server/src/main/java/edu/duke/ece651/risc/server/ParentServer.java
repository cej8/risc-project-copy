package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/*

This object maintains a single game's state, which the board and players (ChildServers) therein.

Essentially waits for players to be attached by LoginServers (via tryJoin method) then coordinates calling ChildServers (which communicate with clients).

Maintains all data for single game instance such as the board, the usernames of players connected (players), the threads communicating with said players (children), and the set of orders to apply at the end of a turn (orderMap).

All write level changes to the games board are done within this class. All orders passed to orderMap are assumed to be valid (as validated within ChildServers).

This object also decides when the game is over and handles sending the "winnerMessage" and ending relationship with all the clients (and telling MasterServer to remove the game/ParentServer from the game list).
*/

// Class handles all server side implmentation including ChildServer handling
public class ParentServer extends Thread{
  //List of ChildServers which talk to clients/handle turns
  private List<ChildServer> children;
  //List of ChildServer's player names
  private List<String> players;
  //Game board
  private Board board;
  //Map of order names to list of those orders
  private Map<String, List<OrderInterface>> orderMap;
  //Threads for calling childservers
  private ExecutorService threads = Executors.newFixedThreadPool(Constants.MAX_PLAYERS);

  //Local version of constants (allows changing for testing)
  private int MAX_PLAYERS = Constants.MAX_PLAYERS;
  private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
  private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES;
  private boolean FOG_OF_WAR = Constants.FOG_OF_WAR;
  private int MAX_MISSED = Constants.MAX_MISSED;
  
  //boolean for if still in waitingForPlayers
  private boolean notStarted = true;
  //Value of server's gameID
  private int gameID;
 
  //SB for turns return string
  private List<String> turnResults;
  private int turnNumber = 1;
  
  //MS that owns PS
  private MasterServer masterServer;
  //Time when waitingForPlayers started or game start
  private long gameStart;

  // Value of plagueID representing which number planet in list has plague
  private int plagueID;

  //Lock for adding to child list
  private final ReentrantLock childServerAddLock = new ReentrantLock();
  
  public ParentServer(){
    BoardGenerator genBoard = new BoardGenerator();
    genBoard.createBoard();
    board = genBoard.getBoard();
    children = new ArrayList<ChildServer>();
    players = new ArrayList<String>();
    orderMap = new HashMap<String, List<OrderInterface>>();
    this.plagueID = 0;
    turnResults = Collections.nCopies(MAX_PLAYERS, "Turn 0:\n");
  }

  public ParentServer(int gameID, MasterServer masterServer){
    this();
    this.gameID = gameID;
    this.masterServer = masterServer;
  }

  public List<ChildServer> getChildren() {
    return children;
  }

  //Method to add player's childserver to list
  public void addPlayer(ChildServer c){
    childServerAddLock.lock();
    children.add(c);
    childServerAddLock.unlock();
  }

  //Method to add username/connection to game by creating new childserver
  //Used by LoginServer
  public synchronized void addPlayer(String username, Connection playerConnection){
    HumanPlayer player = new HumanPlayer(username);
    addPlayer(new ChildServer(player, playerConnection, this));
    players.add(username);
  }
 
  //Method to remove player from list of users
  public void removePlayer(String username){
    players.remove(username);
  }

  public Board getBoard() {
    return this.board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  //Used only for testing
  public Map<String, List<OrderInterface>> getOrderMap() {
    return orderMap;
  }

  //Used only for testing
  public void setMAX_PLAYERS(int MAX_PLAYERS){
    this.MAX_PLAYERS = MAX_PLAYERS;
    try {
      threads = Executors.newFixedThreadPool(this.MAX_PLAYERS);
      turnResults = Collections.nCopies(MAX_PLAYERS, "Turn 0:\n");
    }
    // For debug --> assume we won't put negative
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Used only for testing
  public double getTURN_WAIT_MINUTES() {
    return TURN_WAIT_MINUTES;
  }

  //Used only for testing
  public void setTURN_WAIT_MINUTES(double TURN_WAIT_MINUTES) {
    this.TURN_WAIT_MINUTES = TURN_WAIT_MINUTES;
  }

  //Used only for testing
  public void setSTART_WAIT_MINUTES(double START_WAIT_MINUTES) {
    this.START_WAIT_MINUTES = START_WAIT_MINUTES;
  }

  //Used only for testing
  public boolean getFOG_OF_WAR(){
    return FOG_OF_WAR;
  }

  //Used only for testing
  public void setFOG_OF_WAR(boolean FOG_OF_WAR){
    this.FOG_OF_WAR = FOG_OF_WAR;
  }
  
  //Used only for testing
  public int getMAX_MISSED(){
    return MAX_MISSED;
  }

  //Used only for testing
  public void setMAX_MISSED(int MAX_MISSED){
    this.MAX_MISSED = MAX_MISSED;
  }

  public void setNotStarted(boolean notStarted){
    this.notStarted = notStarted;
  }

  public int getGameID(){
    return gameID;
  }

  public MasterServer getMasterServer(){
    return masterServer;
  }

  //Function to close all children's connections on game end
  public void closeAll() {
    for (ChildServer child : children) {
      if (child.getPlayerConnection() != null) {
        child.getPlayerConnection().closeAll();
        masterServer.removePlayer(child.getPlayer().getName(), gameID);
      }
    }
  }

  //Function to get if the server is still waiting
  //Needs to check MAX_PLAYERS for race on waitingForPlayers while loop exit
  public boolean waitingPlayers(){
    return notStarted && children.size() < MAX_PLAYERS;
  }

  //Function to check if players list has a specific player
  public boolean hasPlayer(String player){
    return players.contains(player);
  }

  //Function to get the "Game String"
  //Used for LoginServer's buildGameInfo
  //Essentially gives the game until start (or how long game has been going)
  //Also shows connected players
  public String getGameString(){
    StringBuilder sb = new StringBuilder();
    if(notStarted){
      long timeLeft = (long)(60*1000*START_WAIT_MINUTES) - (System.currentTimeMillis()-gameStart);
      sb.append(String.format("%02d:%02d until game starts", 
                           TimeUnit.MILLISECONDS.toMinutes(timeLeft) - 
                           TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft)),
                           TimeUnit.MILLISECONDS.toSeconds(timeLeft) - 
                           TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft))));
    }
    else{
      long timeStart = System.currentTimeMillis()-gameStart;
      sb.append(String.format("%02d:%02d:%02d since game start", 
                           TimeUnit.MILLISECONDS.toHours(timeStart),
                           TimeUnit.MILLISECONDS.toMinutes(timeStart) - 
                           TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeStart)),
                           TimeUnit.MILLISECONDS.toSeconds(timeStart) - 
                           TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeStart))));
    }
    sb.append(" : Players [");
    for(int i = 0; i < players.size(); i++){
      sb.append(players.get(i) + ", ");
    }
    //Remove trailling ", "
    sb.deleteCharAt(sb.length()-1);
    sb.deleteCharAt(sb.length()-1);
    sb.append("]");
    return sb.toString();
  }

  //Get's the "firstCall" value for a player from their childserver
  public boolean getFirstCall(String user){
    return children.get(players.indexOf(user)).getFirstCall();
  }
  
  //Function to wait for either MAX_PLAYERS to join or START_WAIT_MINUTES to elapse
  public void waitingForPlayers() {

    //Get time of function start
    long startTime = System.currentTimeMillis();;
    //Start time 2.5 minutes after first connection
    long gameStartTime = (long)(START_WAIT_MINUTES*60*1000);

    //gameStart is timer for getGameString
    gameStart = System.currentTimeMillis();
    
    //Spin until either all join or time out
    while (children.size() < MAX_PLAYERS && (System.currentTimeMillis()-startTime < gameStartTime)) {
      //Set timeout to .5 seconds --> try to read
      //If timeout then still there (otherwise socket has failed)
      childServerAddLock.lock();
      ConnectionTester cT = new ConnectionTester(children, masterServer, gameID);
      cT.peekConnections();
      childServerAddLock.unlock();
      //Wait for .5s so others can access children
      try{
        Thread.sleep(500);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    //Once out then game will start
    notStarted = false;
    System.out.println(gameID + " : " + "All players or time limit, proceeding");
    //Reset timer for game start time
    gameStart = System.currentTimeMillis();
  }

  //Method to try to join game, called by LoginServer
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
      if (r.getOwner().getName().equals(player.getName())) {
        return true;
      }
    }
    return false;
  }

  //Method to move through orders (all valid) and add to Map
  public synchronized void addOrdersToMap(List<OrderInterface> orders) {
    // Method to add orders to map
    for (OrderInterface order : orders) {
      // Not sure if better way to do casting...
      OrderInterface castOrder;
      if (order.getPriority() == Constants.PLACEMENT_PRIORITY) {
        castOrder = (PlacementOrder) (order);
      } else if (order.getPriority() == Constants.ATTACK_MOVE_PRIORITY) {
        castOrder = (AttackMove) (order);

      } else if (order.getPriority() == Constants.ATTACK_COMBAT_PRIORITY) {
        castOrder = (AttackCombat) (order);
      }

      else if (order.getPriority() == Constants.MOVE_PRIORITY) {
        castOrder = (MoveOrder) (order);
      }
      else if (order.getPriority() == Constants.UPGRADE_UNITS_PRIORITY) {
        castOrder = (UnitBoost) (order);
      }
       else if (order.getPriority() == Constants.UPGRADE_TECH_PRIORITY) {
        castOrder = (TechBoost) (order);
      }
         else if (order.getPriority() == Constants.UPGRADE_RESOURCE_PRIORITY) {
        castOrder = (ResourceBoost) (order);
      }
   
       else if (order.getPriority() == Constants.SPYUPGRADE_PRIORITY) {
        castOrder = (SpyUpgradeOrder) (order);
      }
       else if (order.getPriority() == Constants.SPYMOVE_PRIORITY) {
        castOrder = (SpyMoveOrder) (order);
      }
       else if (order.getPriority() == Constants.CLOAK_PRIORITY) {
        castOrder = (CloakOrder) (order);
      }
       else if (order.getPriority() == Constants.RAID_PRIORITY){
        castOrder = (RaidOrder) (order);
      }
     
      else {
        continue;
      }

      String className = castOrder.getClass().getName();
      className = className.substring(className.lastIndexOf('.') + 1);
      //Handle non-combat in order
      if(!className.equals("AttackCombat") && !className.equals("RaidOrder")){
        className = "NotCombat";
      }

      // Add list if not present for order type
      if (!orderMap.containsKey(className)) {
        orderMap.put(className, new ArrayList<OrderInterface>());
      }

      //Combine attacks to same region by same player
      if(className.equals("AttackCombat")){
        boolean foundOrder = false;
        SourceDestinationUnitOrder sduOrderNew = (SourceDestinationUnitOrder) castOrder;
        //Create copy of source to prevent A-->B, B-->C issue where B is taken by A (then B->C would be A's)
        sduOrderNew.setSource((Region)DeepCopy.deepCopy(sduOrderNew.getSource()));
        for(OrderInterface combatOrder : orderMap.get("AttackCombat")){
          if(foundOrder){ break; }
          //If both have same source owner and go to same region
          //Then order in list gets new order's units added to it
          SourceDestinationUnitOrder sduOrderOld = (SourceDestinationUnitOrder) combatOrder;
           if(sduOrderNew.getSource().getOwner().getName().equals(sduOrderOld.getSource().getOwner().getName()) &&
             sduOrderNew.getDestination().getName().equals(sduOrderOld.getDestination().getName())){
             List<Integer> oldOrderUnits = sduOrderOld.getUnits().getUnits();
             List<Integer> newOrderUnits = sduOrderNew.getUnits().getUnits();
             //Add newOrderUnits to oldOrderUnits
             for(int i = 0; i < newOrderUnits.size(); i++){
               oldOrderUnits.set(i, oldOrderUnits.get(i)+newOrderUnits.get(i));
             }
             //Set oldOrderUnits to sum
             foundOrder = true;
          }
        }
        //if found then do not add (immediately continue)
        if(foundOrder){ continue; }
      }

      // Add order to list
      orderMap.get(className).add(castOrder);

    }
  }

  public void applyOrders() {
    // Apply orders to map
    // Mostly hardcoded due to explicit order ordering

    turnResults = new ArrayList<String>(Collections.nCopies(players.size(), "Turn " + turnNumber + ":\n"));

    //Do all not combat first then attackCombat random ordered
    if(orderMap.containsKey("NotCombat")){
      applyOrderList(orderMap.get("NotCombat"));
    }
    if(orderMap.containsKey("RaidOrder")){
      Collections.shuffle(orderMap.get("RaidOrder"));
      applyOrderList(orderMap.get("RaidOrder"));
    }
    if(orderMap.containsKey("AttackCombat")){
      Collections.shuffle(orderMap.get("AttackCombat"));
      applyOrderList(orderMap.get("AttackCombat"));
    }

  }

  public void applyOrderList(List<OrderInterface> orders) {
    // Simply call doAction for each order

    //Strings for attackCombat update special case
    String attackName = "";
    String defendName = "";
    String attackedRegionName = "";
    for (int i = 0; i < orders.size(); i++) {
      //Special case --> force update for ChildServer involved in AttackCombat
      if(orders.get(i).getPriority() == Constants.ATTACK_COMBAT_PRIORITY){
          AttackCombat castOrder = (AttackCombat) orders.get(i);
          //Get attack/defend player names
          attackName = castOrder.getSource().getOwner().getName();
          defendName = castOrder.getDestination().getOwner().getName();
          //Get name of attacked region to update
          attackedRegionName = castOrder.getDestination().getName();
      }


      //List of substrings making up order's text result
      List<String> results = orders.get(i).doAction();
      //If FOG_OF_WAR then apply (if not initial placements)
      if(FOG_OF_WAR && turnNumber > 0){
        //Special case --> force update for ChildServer involved in AttackCombat
        //Avoids issue where losing single region not adjancent to any others
        if(orders.get(i).getPriority() == Constants.ATTACK_COMBAT_PRIORITY){
          //Ensure actual player (not just Group A)
          if(players.contains(attackName)){
            Board attackBoard = children.get(players.indexOf(attackName)).getClientBoard();
            attackBoard.getRegionByName(attackedRegionName).copyInformation(board.getRegionByName(attackedRegionName));
          }
          //Ensure actual player (not just Group A)
          if(players.contains(defendName)){
            Board defendBoard = children.get(players.indexOf(defendName)).getClientBoard();
            defendBoard.getRegionByName(attackedRegionName).copyInformation(board.getRegionByName(attackedRegionName));
          }
        }


        //stringVisibility is list<set> of player names who can see each
        //substring of order's results
        List<Set<String>> stringVisibility = orders.get(i).getPlayersVisibleTo();
        //Can see is union of all within this set
        Set<String> canSee = new HashSet<>();
        for(int j = 0; j < stringVisibility.size(); j++){
          canSee.addAll(stringVisibility.get(j));
        }
        //For players
        for(int j = 0; j < players.size(); j++){
          String playerTurnString = turnResults.get(j);
          //If in canSee then give them string for proper turn result
          if(canSee.contains(players.get(j))){
            //For each substring
            for(int k = 0; k < results.size(); k++){
              //If player is visible to substring then add
              if(stringVisibility.get(k).contains(players.get(j))){
                playerTurnString += results.get(k);
              }
              else{
                playerTurnString += "(Unknown)";
              }
            }
            playerTurnString += "\n";
            turnResults.add(j, playerTurnString);
          }
          //if not playing then dead --> see all
          else if(!children.get(j).getPlayer().isPlaying()){
            for(String s : results){
              playerTurnString += s;
            }
            playerTurnString += "\n";
            turnResults.add(j, playerTurnString);
          }
        }
      }
      //If no FOW then add all to all
      else{
        //For players
        for(int j = 0; j < players.size(); j++){
          String playerTurnString = turnResults.get(j);
          //For each substring
          for(int k = 0; k < results.size(); k++){
            //If player is visible to substring then add
            playerTurnString += results.get(k);
          }
          playerTurnString += "\n";
        }
      }
    }

    orders.clear();
  }

  // Method to call child threads, will prompt player and add all orders to map
  //This DOES NOT timeout based on thread age, will wait for all threads to return
  //ChildServers internally handle their own timeouts by leveraging socket timeouts
  //based on TURN_WAIT_MINUTES as a maximum for time spent within run() method
  public void callThreads() throws InterruptedException {
    //Toggle to not finished to prevent race on ConnectionTester call
    for(ChildServer child : children){
      child.setFinishedTurn(false);
    }
    System.out.println(gameID + " : " + "Calling threads");
    List<Callable<Object>> todo = new ArrayList<Callable<Object>>(children.size());
    for (int i = 0; i < children.size(); i++) {
      todo.add(Executors.callable(children.get(i)));
      // Insert message into children
      children.get(i).setTurnMessage(turnResults.get(i));
    }
    //Create thread to check sockets for those who submit turns
    ConnectionTester cT = new ConnectionTester(children, masterServer, gameID);
    Thread t = new Thread(cT);
    t.start();
    threads.invokeAll(todo);
    //Stop the listener
    cT.stopLoop();
    //Wait until cT has actually stopped run() loop
    while(!cT.hasStopped()) {Thread.sleep(10);};
    System.out.println(gameID + " : " + "Threads finished");
  }
  public void growResources(Region r){
    //grow resources by fuel production times mulitplier for region level
    double multiplier=r.getRegionLevel().getMultiplier();
    r.getOwner().getResources().getFuelResource().addFuel((int)(r.getFuelProduction()*multiplier));
    r.getOwner().getResources().getTechResource().addTech((int)(r.getTechProduction()*multiplier));
   

  }
  // method to add additional unit after round complete to all regions on board
  public void growUnits() {
    for (Region r : board.getRegions()) {
      // increment number of basic units
      if(players.contains(r.getOwner().getName())){
        if(!r.getPlague()){ // if plague == false on Region add production to player
            growResources(r);
        }
        r.getUnits().getUnits().set(0, r.getUnits().getUnits().get(0) + 1);
      }
      //Set all spies as not moved for next turn
      r.setAllSpiesFalse();
      //decrease cloaking for cloaked by 1
      if(r.getCloakTurns() > 0){
        r.setCloakTurns(r.getCloakTurns()-1);
      }
    }
  }

  // method to apply plague to region
  public void applyPlague(){
    if(turnNumber<6){
      return;//only begin after turn 6
    }
    if (turnNumber%3 == 0){
      // reset past plague to false
      board.getRegions().get(plagueID).setPlague(false);
      Random r = new Random();
      this.plagueID = r.nextInt(board.getRegions().size());
      board.getRegions().get(plagueID).setPlague(true);
    }
    // otherwise do nothing
  }

  // set turn used for testing
  public void setTurn(int i){
    this.turnNumber = i;
  }
  public int getPlagueID(){
    return this.plagueID;
  }

  //Method to get freshest version of player from board
  //Needed to update CS version of player so most up to date
  //(resources changed from doAction) is sent
  void updatePlayersInChildServers(){
    for(Region r : board.getRegions()){
      AbstractPlayer p = r.getOwner();
      int pIndex = players.indexOf(p.getName());
      if(pIndex != -1){
        children.get(pIndex).setPlayer(p);
      }
    }
  }


  // method that controls game play
  public void playGame(){
    //Wait for MAX_PLAYERS to connect or timeout
    waitingForPlayers();
    
    //While regions not owned all by one player
    createStartingGroups();
    board.initializeSpies(players);
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
      // Evolution 3: Plague
      applyPlague();    
      updatePlayersInChildServers();
    }
    if (numPlayersLeft() <= 1) {
      // If one player alive then create message --> send
      StringMessage winnerMessage = new StringMessage("Somehow no one is left?");
      if(playersLeft().iterator().hasNext()){
        AbstractPlayer winner = playersLeft().iterator().next();
        winnerMessage = new StringMessage(winner.getName() + " is the winner!");
      }
      System.out.println(gameID + " : " + winnerMessage.unpacker());
      // Send message to all children
      for (int i = 0; i < children.size(); i++) {
        ChildServer child = children.get(i);
        try {
          child.getPlayerConnection().sendObject(child.getPlayer());
          child.getPlayerConnection().sendObject(new StringMessage(turnResults.get(i).toString()));
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
    System.out.println(gameID + " : Game started");
    System.out.println(gameID + " : MAX_PLAYERS: " + MAX_PLAYERS);
    System.out.println(gameID + " : TURN_WAIT_MINUTES:" + TURN_WAIT_MINUTES);
    System.out.println(gameID + " : START_WAIT_MINUTES:" + START_WAIT_MINUTES);
    System.out.println(gameID + " : FOG_OF_WAR:" + FOG_OF_WAR);
    System.out.println(gameID + " : MAX_MISSED:" + MAX_MISSED);
    playGame();
    System.out.println(gameID + " : Game ended");
  }
}
