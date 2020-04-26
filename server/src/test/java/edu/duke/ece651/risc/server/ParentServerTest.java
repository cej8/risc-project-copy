package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.io.*;
import java.net.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.*;

import edu.duke.ece651.risc.client.*;

import org.mindrot.jbcrypt.*;

public class ParentServerTest {
  /*
   * @Test public void test_createStartingGroups(){ ParentServer ps = new
   * ParentServer(); HumanPlayer player = new HumanPlayer();
   * player.setName("Player One"); HumanPlayer playerTwo = new HumanPlayer();
   * playerTwo.setName("Player Two"); ChildServer cs = new ChildServer(player,ps);
   * ChildServer csTwo = new ChildServer(playerTwo, ps); //ps.addPlayer(cs);
   * //ps.addPlayer(csTwo); ps.createStartingGroups(); Board board =
   * ps.getBoard(); List<Region> regions = board.getRegions(); for (int i = 0; i <
   * regions.size(); i++){ System.out.println(i + ": " +
   * regions.get(i).getOwner().getName() + " " +
   * regions.get(i).getUnits().getUnits()); } }
   */
  @Test
  public void test_startingGroups() {
    ParentServer ps = new ParentServer();
    // ps.createBoard();
    HumanPlayer player = new HumanPlayer("player1");
    HumanPlayer player2 = new HumanPlayer("player2");
    ChildServer cs = new ChildServer(player, ps);
    ChildServer cs2 = new ChildServer(player2, ps);
    ps.addPlayer(cs);
    ps.addPlayer(cs2);

    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    List<Region> adjRegion;
    for (int i = 0; i < regions.size(); i++) {
      adjRegion = regions.get(i).getAdjRegions();
      System.out.println(regions.get(i).getName() + ": Group Name: " + regions.get(i).getOwner().getName() + " Units: "
          + regions.get(i).getUnits().getUnits());
      System.out.print("Adj Regions: ");
      for (int j = 0; j < adjRegion.size(); j++) {
        System.out.print(adjRegion.get(j).getName() + " ");
      }
      System.out.println();
    }
    assertEquals(10, regions.get(0).getSize());
    assertEquals(50, regions.get(0).getFuelProduction());
    assertEquals(30, regions.get(0).getTechProduction());
    assertEquals(10, regions.get(6).getSize());
    assertEquals(50, regions.get(6).getFuelProduction());
    assertEquals(30, regions.get(6).getTechProduction());
  }

  @Test
  public void test_growUnits() {
    ParentServer ps = new ParentServer();
    // ps.createBoard();
    ps.addPlayer("player1", null);
    ps.addPlayer("player2", null);
    List<ChildServer> children = ps.getChildren();
    AbstractPlayer player = children.get(0).getPlayer();
    AbstractPlayer player2 = children.get(1).getPlayer();;
    Board b = new Board(getRegionList(player, player2));
    ps.setBoard(b);

    assertEquals(50, player2.getResources().getFuelResource().getFuel());
    assertEquals(30, player.getResources().getTechResource().getTech());
    assertEquals(4, b.getRegions().get(2).getUnits().getUnits().get(0));
    ps.growUnits();
    assertEquals(250, player2.getResources().getFuelResource().getFuel());
    assertEquals(280, player.getResources().getTechResource().getTech()); 
    assertEquals(5, b.getRegions().get(2).getUnits().getUnits().get(0));

  }
  /* @Test
  public void test_plague(){
    ParentServer ps = new ParentServer();
    ps.addPlayer("player1", null);
    //ps.addPlayer("player2", null);
    List<ChildServer> children = ps.getChildren();
    AbstractPlayer player = children.get(0).getPlayer();
    // AbstractPlayer player2 = children.get(1).getPlayer();;
    Board b = new Board(singleRegionList(player));
    
    ps.setBoard(b);
System.out.println("Starting fuel: " + player.getResources().getFuelResource().getFuel());
    ps.setTurn(3);
   
    ps.growUnits();
    ps.applyPlague();
    
    // first plague
    int plagueID = ps.getPlagueID();
    AbstractPlayer plaguePlayer = b.getRegions().get(plagueID).getOwner();
   
    System.out.println("PlagueID: " + ps.getPlagueID());
    System.out.println("Turn 3 fuel: " + plaguePlayer.getResources().getFuelResource().getFuel());
    int plagueFuel = plaguePlayer.getResources().getFuelResource().getFuel();
    assertEquals(plagueID, ps.getPlagueID());
    assertEquals(800,player.getResources().getFuelResource().getFuel());
    ps.setTurn(4);
    ps.growUnits();
    ps.applyPlague();
    // second plague
    assertEquals(plagueID, ps.getPlagueID());
    assertEquals(1550,player.getResources().getFuelResource().getFuel());
    System.out.println("PlagueID: " + ps.getPlagueID());
    System.out.println("Turn 4 fuel: " + plaguePlayer.getResources().getFuelResource().getFuel());
   
    plagueFuel = plaguePlayer.getResources().getFuelResource().getFuel();
    ps.setTurn(5);
    ps.growUnits();
    //assertEquals(1100,plagueFuel);
    System.out.println("PlagueID: " + ps.getPlagueID());
    System.out.println("Turn 5 fuel: " + plaguePlayer.getResources().getFuelResource().getFuel());
   
    ps.applyPlague();
    assertEquals(2300,player.getResources().getFuelResource().getFuel());
    ps.setTurn(6);
    ps.growUnits();
     System.out.println("PlagueID: " + ps.getPlagueID());
    System.out.println("Turn 6 fuel: " + plaguePlayer.getResources().getFuelResource().getFuel());
   
    assertEquals(plagueID, ps.getPlagueID());
    // assertEquals(1700,plaguePlayer.getResources().getFuelResource().getFuel());
    ps.applyPlague();
    // new plague planet
    ps.setTurn(7);
    ps.growUnits();
    ps.applyPlague();
    assertEquals(3700,player.getResources().getFuelResource().getFuel());
    System.out.println("PlaugeID: " + ps.getPlagueID());
    
    }*/
 private List<Region> singleRegionList(AbstractPlayer p1) {
    Region r1 = new Region(p1, new Unit(1));
    r1.setName("r1");
    r1.setSize(1);
    r1.setFuelProduction(100);
    r1.setTechProduction(100);

    Region r2 = new Region(p1, new Unit(2));
    r2.setName("r2");
    r2.setSize(2);
    r2.setFuelProduction(100);

    Region r4 = new Region(p1, new Unit(4));
    r4.setName("r4");
    r4.setSize(4);
    r4.setFuelProduction(100);

    Region r5 = new Region(p1, new Unit(5));
    r5.setName("r5");
    r5.setSize(1);
    r5.setFuelProduction(100);

    Region r3 = new Region(p1, new Unit(3));
    r3.setName("r3");
    r3.setSize(1);
    r3.setFuelProduction(100);

    Region r6 = new Region(p1, new Unit(6));
    r6.setName("r6");
    r6.setSize(6);
    r1.setFuelProduction(100);

    Region r7 = new Region(p1, new Unit(7));
    r7.setName("r7");
    r7.setSize(5);
    r7.setFuelProduction(100);

    Region r8 = new Region(p1, new Unit(8));
    r8.setName("r8");
    r8.setSize(5);
    r8.setFuelProduction(100);

    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r4);
    regions.add(r3);
    regions.add(r5);
    regions.add(r6);
    regions.add(r7);
    regions.add(r8);

    List<Region> adj1 = new ArrayList<Region>();

    adj1.add(r2);
    adj1.add(r3);
    adj1.add(r7);

    r1.setAdjRegions(adj1);

    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r4);
    r2.setAdjRegions(adj2);

    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r1);
    adj3.add(r5);
    adj3.add(r4);
    r3.setAdjRegions(adj3);

    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r2);
    adj4.add(r6);
    adj4.add(r3);
    adj4.add(r7);

    r4.setAdjRegions(adj4);

    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r3);
    adj5.add(r6);
    r5.setAdjRegions(adj5);

    List<Region> adj6 = new ArrayList<Region>();
    adj6.add(r4);
    adj6.add(r5);
    adj6.add(r8);

    r6.setAdjRegions(adj6);

    List<Region> adj7 = new ArrayList<Region>();
    adj7.add(r4);
    adj7.add(r1);
    adj7.add(r8);
    r7.setAdjRegions(adj7);

    List<Region> adj8 = new ArrayList<Region>();
    adj8.add(r7);
    adj8.add(r6);
    r8.setAdjRegions(adj8);
    return regions;
  }

  private List<Region> getRegionList(AbstractPlayer p1, AbstractPlayer p2) {
    Region r1 = new Region(p1, new Unit(1));
    r1.setName("r1");
    r1.setSize(1);
    r1.setFuelProduction(100);
    r1.setTechProduction(100);

    Region r2 = new Region(p1, new Unit(2));
    r2.setName("r2");
    r2.setSize(2);
    r2.setFuelProduction(100);

    Region r4 = new Region(p1, new Unit(4));
    r4.setName("r4");
    r4.setSize(4);
    r4.setFuelProduction(100);

    Region r5 = new Region(p1, new Unit(5));
    r5.setName("r5");
    r5.setSize(1);
    r5.setFuelProduction(100);

    Region r3 = new Region(p2, new Unit(3));
    r3.setName("r3");
    r3.setSize(1);
    r3.setFuelProduction(100);

    Region r6 = new Region(p1, new Unit(6));
    r6.setName("r6");
    r6.setSize(6);
    r1.setFuelProduction(100);

    Region r7 = new Region(p1, new Unit(7));
    r7.setName("r7");
    r7.setSize(5);
    r7.setFuelProduction(100);

    Region r8 = new Region(p2, new Unit(8));
    r8.setName("r8");
    r8.setSize(5);
    r8.setFuelProduction(100);

    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r4);
    regions.add(r3);
    regions.add(r5);
    regions.add(r6);
    regions.add(r7);
    regions.add(r8);

    List<Region> adj1 = new ArrayList<Region>();

    adj1.add(r2);
    adj1.add(r3);
    adj1.add(r7);

    r1.setAdjRegions(adj1);

    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r4);
    r2.setAdjRegions(adj2);

    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r1);
    adj3.add(r5);
    adj3.add(r4);
    r3.setAdjRegions(adj3);

    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r2);
    adj4.add(r6);
    adj4.add(r3);
    adj4.add(r7);

    r4.setAdjRegions(adj4);

    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r3);
    adj5.add(r6);
    r5.setAdjRegions(adj5);

    List<Region> adj6 = new ArrayList<Region>();
    adj6.add(r4);
    adj6.add(r5);
    adj6.add(r8);

    r6.setAdjRegions(adj6);

    List<Region> adj7 = new ArrayList<Region>();
    adj7.add(r4);
    adj7.add(r1);
    adj7.add(r8);
    r7.setAdjRegions(adj7);

    List<Region> adj8 = new ArrayList<Region>();
    adj8.add(r7);
    adj8.add(r6);
    r8.setAdjRegions(adj8);
    return regions;
  }

  @Test
  public void test_placementOrder() {
    ParentServer ps = new ParentServer();
    HumanPlayer player = new HumanPlayer("player1");
    HumanPlayer player2 = new HumanPlayer("player2");
    ChildServer cs = new ChildServer(player, ps);
    ChildServer cs2 = new ChildServer(player2, ps);
    cs.getParentServer();
    cs.setParentServer(ps);
    ps.addPlayer(cs);
    ps.addPlayer(cs2);

    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    for (int i = 0; i < regions.size(); i++) {
      OrderInterface placement = new PlacementOrder(regions.get(i), new Unit(i + 5));
      placement.doAction();

      System.out.println("Placement number: " + (i + 5));
    }
    assertEquals(5, regions.get(0).getUnits().getUnits().get(0));
    assertEquals(6, regions.get(1).getUnits().getUnits().get(0));
    assertEquals(7, regions.get(2).getUnits().getUnits().get(0));
    assertEquals(8, regions.get(3).getUnits().getUnits().get(0));
    assertEquals(9, regions.get(4).getUnits().getUnits().get(0));
    assertEquals(10, regions.get(5).getUnits().getUnits().get(0));
    assertEquals(11, regions.get(6).getUnits().getUnits().get(0));
    assertEquals(12, regions.get(7).getUnits().getUnits().get(0));
    assertEquals(13, regions.get(8).getUnits().getUnits().get(0));
    assertEquals(14, regions.get(9).getUnits().getUnits().get(0));
    assertEquals(15, regions.get(10).getUnits().getUnits().get(0));
    assertEquals(16, regions.get(11).getUnits().getUnits().get(0));
  }

  // @Test
  // public void test_assorted(){
  // //Tests some random Parent/Child functionality
  // //Default constructor
  // ParentServer ps = new ParentServer();
  // assertEquals(ps.getServerSocket(), null);
  // try{
  // //Try setting socket
  // ps.setSocket(12345);
  // assertEquals(ps.getServerSocket().getLocalPort(), 12345);
  // //Try int constructor
  // ps = new ParentServer(12346);
  // assertEquals(ps.getServerSocket().getLocalPort(), 12346);
  // //Try adding player to children
  // assertEquals(ps.getChildren(), new ArrayList<ChildServer>());
  // HumanPlayer debugPlayer = new HumanPlayer("test");
  // List<ChildServer> cs = new ArrayList<ChildServer>();
  // cs.add(new ChildServer(debugPlayer, ps));
  // ps.addPlayer(cs.get(0));
  // assertEquals(ps.getChildren(), cs);
  // assertEquals(ps.getChildren().get(0).getPlayer().getName(), "test");
  // assertEquals(ps.getChildren().get(0).getParentServer(), ps);
  // assertEquals(ps.getChildren().get(0).getPlayer().getName(),
  // cs.get(0).getPlayer().getName());
  // assertEquals(ps.getChildren().get(0).getParentServer(),
  // cs.get(0).getParentServer());
  // Connection debugConn = new Connection();
  // ps.getChildren().get(0).setPlayerConnection(debugConn);
  // Connection outConn = ps.getChildren().get(0).getPlayerConnection();
  // assert(ps.numPlayersLeft() == 1);
  // assertEquals(ps.playersLeft().iterator().next().getName(),
  // ps.getChildren().get(0).getPlayer().getName());

  // //Add another player
  // ps.addPlayer(new ChildServer(new HumanPlayer("test2"), ps));
  // //Check playersLeft
  // Set<AbstractPlayer> pleft = ps.playersLeft();
  // assert(ps.numPlayersLeft() == 2);
  // Iterator<AbstractPlayer> it = pleft.iterator();
  // String firstName = it.next().getName();
  // String secondName = it.next().getName();
  // assert(firstName.equals("test") || firstName.equals("test2"));
  // assert(secondName.equals("test") || secondName.equals("test2"));
  // assert(!firstName.equals(secondName));

  // ps.getChildren().get(0).getPlayer().setPlaying(false);

  // Set<AbstractPlayer> pleft2 = ps.playersLeft();
  // assert(ps.numPlayersLeft() == 1);
  // assert(pleft2.iterator().next().getName().equals("test2"));

  // ps.closeAll();
  // }
  // catch(Exception e){
  // e.printStackTrace();
  // }
  // }

  @Test
  public void test_assignGroups() {
    TextDisplay out = new TextDisplay();
    ParentServer ps = new ParentServer();
    for (int i = 0; i < 5; i++) {
      ps.addPlayer(new ChildServer(new HumanPlayer(""), ps));
    }
    // Get 5 player map
    ps.createStartingGroups();
    // Set A, F to real players
    assert (ps.assignGroups("Group A", new HumanPlayer("Set groups 1")));
    assert (ps.assignGroups("Group F", new HumanPlayer("Set groups 2")));
    // Expand D to be D+E
    assert (ps.assignGroups("Group E", new HumanPlayer("Group D")));
    // Bad set cases
    assert (!ps.assignGroups("Group b", new HumanPlayer("Bad set 1")));
    assert (!ps.assignGroups("Group A", new HumanPlayer("Bad set 2")));
    assert (!ps.assignGroups("Group Q", new HumanPlayer("Bad set 3")));
    assert (!ps.assignGroups("Group ", new HumanPlayer("Bad set 4")));
    assert (!ps.assignGroups("", new HumanPlayer("Bad set 5")));
    // Should have (A,B) as "Set groups 1" and (K,L) as "Set groups 2"
    // Group D should have D+E (G,H,I,J)
    out.displayBoard(ps.getBoard());
    assert (ps.playerHasARegion(new HumanPlayer("Set groups 1")));
    assert (ps.playerHasARegion(new HumanPlayer("Set groups 2")));
    assert (ps.getBoard().getNumRegionsOwned(new HumanPlayer("Set groups 1")) == 2);
    assert (ps.getBoard().getNumRegionsOwned(new HumanPlayer("Set groups 2")) == 2);
    assert (ps.getBoard().getNumRegionsOwned(new HumanPlayer("Group D")) == 4);
    assert (ps.getBoard().getNumRegionsOwned(new HumanPlayer("Group A")) == 0);

  }

   @Test
   public void test_orders(){
   //Default 5 player board
   TextDisplay out = new TextDisplay();
   ParentServer ps = new ParentServer();
   for(int i = 0; i < 5; i++){
     ps.addPlayer("" + Character.toString('A'+i), null);
     ps.getChildren().get(i).getPlayer().setPlayerResource(new PlayerResources(10000, 10000));
   }


   ps.createStartingGroups();
   for(int i = 0; i < 5; i++){
     ps.assignGroups("Group " + Character.toString('A'+i), ps.getChildren().get(i).getPlayer());
   }
   Board originalBoard = (Board) DeepCopy.deepCopy(ps.getBoard());
   for(ChildServer child : ps.getChildren()){
     child.setClientBoard(originalBoard);
   }

   List<Region> regions = ps.getBoard().getRegions();
   List<OrderInterface> orders = new ArrayList<OrderInterface>();

   //Add 1 to A, 2 to B, etc.
   for(int i = 0; i < regions.size(); i++){
   orders.add(new PlacementOrder(regions.get(i), new Unit(i+1)));
   }

   //System.out.println("Original empty board");
   String placementStart1 = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());
   ps.addOrdersToMap(orders);
   Map<String, List<OrderInterface>> orderMap = ps.getOrderMap();
   assert(orderMap.containsKey("NotCombat"));
   assert(orderMap.get("NotCombat").size() == regions.size());
   ps.applyOrders();
   //System.out.println("Board after adding 1 to all regions");
   String placementEnd1 = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());

   //Convert to another board
   Board bCopy = (Board)DeepCopy.deepCopy(originalBoard);
   ps.setBoard(bCopy);
   //System.out.println("Insert copy of original empty board");
   String placementStart2 = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());
   for(int i = 0; i < regions.size(); i++){
   orders.get(i). findValuesInBoard(bCopy);
   }
   ps.addOrdersToMap(orders);
   ps.applyOrders();
   //System.out.println("Apply previous orders to board copy after conversion");
   String placementEnd2 = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());

   assertEquals(placementStart1, placementStart2);
   assertEquals(placementEnd1, placementEnd2);

   ps.setBoard((Board)DeepCopy.deepCopy(originalBoard));
   //Add move orders, move all from i --> i+1 cyclic
   for(int i = 0; i < regions.size()-3; i+=2){
     orders.add(new MoveOrder(regions.get(i), regions.get(i+1), new Unit(i+1)));
   }

   for(int i = 0; i < orders.size(); i++){
     orders.get(i).findValuesInBoard(ps.getBoard());
   }

   //System.out.println("Original empty board");
   String moveStart1 = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());
   ps.addOrdersToMap(orders);
   orderMap = ps.getOrderMap();
   assert(orderMap.keySet().size() == 1);
   assert(orderMap.containsKey("NotCombat"));
   assert(orderMap.get("NotCombat").size() == regions.size()+5);
   ps.applyOrders();
   //System.out.println("Board after adding 1 to all regions");
   String moveEnd1 = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());

   //Convert to another board
   bCopy = (Board)DeepCopy.deepCopy(originalBoard);
   ps.setBoard(bCopy);
   //System.out.println("Insert copy of original empty board");
   String moveStart2 = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());
   for(int i = 0; i < orders.size(); i++){
   orders.get(i). findValuesInBoard(ps.getBoard());
   }
   ps.addOrdersToMap(orders);
   ps.applyOrders();
   //System.out.println("Apply previous orders to board copy after conversion");
   String moveEnd2 = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());

   //Add attack orders, will attack B from C and E
   //Since 50>>1 we expect that either Group B or Group C will take B
   //Ordering random as well as who wins second round so final owner random
   //Regardless C should have -23 and E -21 units
   orders.add(new AttackMove(regions.get(2), regions.get(1), new Unit(25)));
   orders.add(new AttackCombat(regions.get(2), regions.get(1), new Unit(25)));
   orders.add(new AttackMove(regions.get(4), regions.get(1), new Unit(25)));
   orders.add(new AttackCombat(regions.get(4), regions.get(1), new Unit(25)));

   String attackStart;
   String attackEnd;

   ps.setBoard((Board)DeepCopy.deepCopy(originalBoard));

   for(int i = 0; i < orders.size(); i++){
   orders.get(i). findValuesInBoard(ps.getBoard());
   }

   //System.out.println("Original empty board");
   attackStart = out.createBoard(ps.getBoard());
   //out.displayBoard(ps.getBoard());
   ps.addOrdersToMap(orders);
   orderMap = ps.getOrderMap();
System.out.println(orderMap.keySet());
   assert(orderMap.keySet().size() == 2);
   assert(orderMap.containsKey("NotCombat"));
   assert(orderMap.get("NotCombat").size() == regions.size()+5+2);
   assert(orderMap.containsKey("AttackCombat"));
   assert(orderMap.get("AttackCombat").size() == 2);

   ps.applyOrders();
   //System.out.println("Board after adding 1 to all regions");
   attackEnd = out.createBoard(ps.getBoard());
   // out.displayBoard(ps.getBoard());
   //Check negatives make sense
   assert(ps.getBoard().getRegions().get(2).getUnits().getUnits().get(0) == -31);
   assert(ps.getBoard().getRegions().get(4).getUnits().getUnits().get(0) == -35);
   assert(!ps.getBoard().getRegions().get(1).getOwner().getName().equals("A"));

   Region out1 = ps.getBoard().getRegions().get(1);
   System.out.println("Region 1 owned by " + out1.getOwner().getName() + " with" + out1.getUnits().getUnits() + " units");


   }

   public ClientInputInterface getClientIn(String str){
   return new ConsoleInput(new ByteArrayInputStream(str.getBytes()));
   }

    @Test
    public void test_fakeClients() throws IOException, ClassNotFoundException,
    InterruptedException{
    TextDisplay cout = new TextDisplay();

    //Set to 2 players so we don't have to wait forever...
    int port = 12347;
    MasterServer ms = new MasterServer("", port);
    ParentServer server = new ParentServer(1, ms);
    ms.addParentServer(server);
    server.setMAX_PLAYERS(2);
    server.setMAX_MISSED(1);
    server.setFOG_OF_WAR(false);
    server.setSTART_WAIT_MINUTES(1.0);
    BoardGenerator genBoard = new BoardGenerator();
    genBoard.createBoard_legacy();
    server.setBoard(genBoard.getBoard());
    //Set max turn time to 10 seconds...
    server.setTURN_WAIT_MINUTES(10.0/60);
    Thread masterThread = new Thread(ms);
    Thread serverThread = new Thread(server);
    ms.start();
    serverThread.start();

    Thread.sleep(1000);
    //Headless sockets connect
    Socket c1 = new Socket("localhost", port);
    ObjectInputStream c1in = new ObjectInputStream(c1.getInputStream());
    ObjectOutputStream c1out = new ObjectOutputStream(c1.getOutputStream());
    Thread.sleep(5000);
    Socket c2 = new Socket("localhost", port);
    ObjectInputStream c2in = new ObjectInputStream(c2.getInputStream());
    ObjectOutputStream c2out = new ObjectOutputStream(c2.getOutputStream());

    Board board;
    ConfirmationMessage confirmation;
    StringMessage string;
    HumanPlayer player;

    //After connection need to register as new players (player1, player2);
    //Player 1 register
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    c1out.writeObject(new ConfirmationMessage(false));
    c1out.writeObject(new StringMessage("Player 1"));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Salt");
    cout.displayString(string.unpacker());
    c1out.writeObject(new StringMessage(BCrypt.hashpw("123", string.unpacker())));
    c1out.writeObject(new StringMessage(BCrypt.hashpw("123", string.unpacker())));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    //Player 2 register
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    c2out.writeObject(new ConfirmationMessage(false));
    c2out.writeObject(new StringMessage("Player 2"));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Salt");
    cout.displayString(string.unpacker());
    c2out.writeObject(new StringMessage(BCrypt.hashpw("123", string.unpacker())));
    c2out.writeObject(new StringMessage(BCrypt.hashpw("123", string.unpacker())));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());

    //Player 1 join bad in prog then good
    c1out.writeObject(new ConfirmationMessage(true));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Games List In");
    cout.displayString(string.unpacker());
    c1out.writeObject(new IntegerMessage(1));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Fail");
    cout.displayString(string.unpacker());

    c1out.writeObject(new ConfirmationMessage(false));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Games List New");
    cout.displayString(string.unpacker());
    c1out.writeObject(new IntegerMessage(1));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());

    //Player 2 join bad new, dc, re-login, then good
    c2out.writeObject(new ConfirmationMessage(false));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Games List New");
    cout.displayString(string.unpacker());
    c2out.writeObject(new IntegerMessage(3));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Failure");
    cout.displayString(string.unpacker());

    c2.close();
    c2in.close();
    c2out.close();   
    c2 = new Socket("localhost", port);
    c2in = new ObjectInputStream(c2.getInputStream());
    c2out = new ObjectOutputStream(c2.getOutputStream());

    string = (StringMessage)(c2in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    c2out.writeObject(new ConfirmationMessage(true));
    c2out.writeObject(new StringMessage("Player 2"));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Salt");
    cout.displayString(string.unpacker());
    c2out.writeObject(new StringMessage(BCrypt.hashpw("123", string.unpacker())));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());


    c2out.writeObject(new ConfirmationMessage(false));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Games List New");
    cout.displayString(string.unpacker());
    c2out.writeObject(new IntegerMessage(1));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c2in.readObject());

    //After connection should send HumanPlayer for the child
    //c2 waits 5 seconds, should be second
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());
    player = (HumanPlayer)(c2in.readObject());
    cout.displayString("c2 player");
    cout.displayString(player.getName());
    assertEquals("Player 2", player.getName());

    //Next firstCall of run()
    //First sends board with Group names --> should be same
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board initial");
    cout.displayBoard(board);
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board initial");
    cout.displayBoard(board);

    //Player 2 selects group B
    c2out.writeObject(new StringMessage("Group B"));
    //Server sees good --> success message
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 response expected success");
    cout.displayString(string.unpacker());
    //Server sends back with player 2 for B groups
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board after take group b");
    cout.displayBoard(board);

    //Now player 1 tries to take group q (invalid input)
    c1out.writeObject(new StringMessage("Group Q"));
    //Should fail
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 response expected fail");
    cout.displayString(string.unpacker());
    //Resends updated map where Group B --> player 2
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board after c2 takes group b");
    cout.displayBoard(board);
    //Take Group A
    c1out.writeObject(new StringMessage("Group A"));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 response expected success");
    cout.displayString(string.unpacker());
    //Server sends board
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board after take group a");
    cout.displayBoard(board);

    //Now both players would be creating placement orders...
    //Create disconnected client object for generating order lists...
    ClientInputInterface clientIn = getClientIn("");
    PrintWriter nowhere = new PrintWriter(new PrintStream(new ByteArrayOutputStream()));
    ClientOutputInterface writeToNothing = new TextDisplay(nowhere);
    Client fakeClient = new Client(clientIn, writeToNothing, null);

    OrderCreator placement = OrderFactoryProducer.getOrderCreator("P", fakeClient);
    OrderHelper orderhelper = new OrderHelper(fakeClient);

    //Both clients asked create placements...
    //Player 1 will place 3 on all
    //Need to set board/player for client placements
    fakeClient.setBoard(board);
    fakeClient.setPlayer(new HumanPlayer("Player 1"));
    String placements;
    List<OrderInterface> initialPlacements = new ArrayList<OrderInterface>();
    placements = "3\n".repeat(6);
    fakeClient.setClientInput(getClientIn(placements));
    placement.addToOrderList(initialPlacements);
    c1out.writeObject(initialPlacements);
    //Player 1 will get success
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 placements expected success");
    cout.displayString(string.unpacker());

    //Player 2 will place 4 on all (invalid)
    //Need to set player (board is same)
    fakeClient.setPlayer(new HumanPlayer("Player 2"));
    initialPlacements = new ArrayList<OrderInterface>();
    placements = "4\n".repeat(6);
    fakeClient.setClientInput(getClientIn(placements));
    placement.addToOrderList(initialPlacements);
    c2out.writeObject(initialPlacements);
    //Player 2 will get fail
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 placements expected fail");
    cout.displayString(string.unpacker());
    //Try again but now 2,3,4,2,3,4
    //First get board again (now see C1 placements)
    board = (Board)(c2in.readObject());
    cout.displayBoard(board);
    initialPlacements = new ArrayList<OrderInterface>();
    placements = "2\n3\n4\n".repeat(2);
    fakeClient.setClientInput(getClientIn(placements));
    placement.addToOrderList(initialPlacements);
    c2out.writeObject(initialPlacements);
    //Player 2 will get success
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 placements expected success");
    cout.displayString(string.unpacker());

    //Parent should wait --> apply all
    //Now should be in main game loop (past this point client doesn't care about
    //board/player)
    //Both should get player, "Continue", true alive message, and board
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());
    player = (HumanPlayer)(c2in.readObject());
    cout.displayString("c2 player");
    cout.displayString(player.getName());
    assertEquals("Player 2", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c2in.readObject());
    cout.displayString("c2 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board");
    cout.displayBoard(board);

    //Now input moves...
    List<OrderInterface> orders;

    //P1 owns A-F (all 3)
    //P2 owns G-L (2,3,4,2,3,4)
    cout.displayString("P1 moves 2 from B to A then attacks from A to L with 4 (orders backwards on input)");
    String p1move = "A\nA\nL\n4\n" + "M\nB\nA\n2\n" + "D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    cout.displayString("P2 tries to move 1 from K to L");
    String p2move = "M\nG\nL\n1\n" + "D\n";
    fakeClient.setClientInput(getClientIn(p2move));
    orders = orderhelper.createOrders();
    c2out.writeObject(orders);
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 order expected fail");
    cout.displayString(string.unpacker());

    //Turn ends....
    cout.displayString("Parent now applies orders...");
    cout.displayString("P1 might take L, otherwise ownership stays same");
    cout.displayString("A-B should have 2, C-F 4");
    cout.displayString("G-K should be 3,4,5,3,3");

    //Start turn stuff
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());
    player = (HumanPlayer)(c2in.readObject());
    cout.displayString("c2 player");
    cout.displayString(player.getName());
    assertEquals("Player 2", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c2in.readObject());
    cout.displayString("c2 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board");
    cout.displayBoard(board);

    //P2 does nothing
    cout.displayString("P2 does nothing");
    p2move = "D\n";
    fakeClient.setClientInput(getClientIn(p2move));
    orders = orderhelper.createOrders();
    c2out.writeObject(orders);
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 order expected success");
    cout.displayString(string.unpacker());

    //P1 attacks F->G, E->H, D->I with 3 each
    cout.displayString("P1 attacks F->G, E->H, D->I with 3 each");
    p1move = "A\nF\nG\n3\n" + "A\nE\nH\n3\n" + "A\nD\nI\n3\n" +"D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    //Turn ends....
    cout.displayString("Parent now applies orders...");
    cout.displayString("P1 might take G, H, I");
    cout.displayString("A-B should have 3, C 5, D-F 2");
    cout.displayString("J-K should be 4,4");

    //Start turn stuff
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());
    player = (HumanPlayer)(c2in.readObject());
    cout.displayString("c2 player");
    cout.displayString(player.getName());
    assertEquals("Player 2", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c2in.readObject());
    cout.displayString("c2 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board");
    cout.displayBoard(board);

    //P2 does nothing
    cout.displayString("P2 does nothing");
    p2move = "D\n";
    fakeClient.setClientInput(getClientIn(p2move));
    orders = orderhelper.createOrders();
    c2out.writeObject(orders);
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 order expected success");
    cout.displayString(string.unpacker());

    //P1 does nothing
    cout.displayString("P1 does nothing");
    p1move = "D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    //Turn ends....
    cout.displayString("Parent now applies orders...");
    cout.displayString("Should be same as last but all regions + 1");

    //P1 does start turn
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    //P1 does nothing
    cout.displayString("P1 moves does nothing");
    p1move = "D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    //P2 suddenly closes
    cout.displayString("P2 suddenly closes");
    cout.displayString("We expect childserver of connection reset. This is the thread realizing the socket is dead.\n");
    c2.close();

    cout.displayString("After waiting for timeout moves will be applied");
    cout.displayString("Server should wait until MAX_MISSED then inform player 1 that they won and close socket");

    for(int i = 0; i < server.getMAX_MISSED(); i++){
    //P1 does start turn
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    //P1 does nothing
    cout.displayString("P1 moves does nothing");
    p1move = "D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());
    }

    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 winner message");
    cout.displayString(string.unpacker());

    Thread.sleep(500);

    assert(c1.getInputStream().read() == -1);

    c1.close();
    c1in.close();
    c1out.close();
    c2in.close();
    c2out.close();
    }

  @Test
  public void test_NotCombat(){
    //Test to make sure orders are properly cast and attackCombat are combined
    AbstractPlayer player1 = new HumanPlayer("player1");
    AbstractPlayer player2 = new HumanPlayer("player2");
    Unit unit = new Unit(0);
    Unit adjUnit = new Unit(10);
    Region region1 = new Region(player1, unit);
    region1.setName("Earth");
    Region region2 = new Region(player2, adjUnit);
    region2.setName("Wind");
    List<Region> adjRegion1 = new ArrayList<Region>();
    adjRegion1.add(region2);
    region1.setAdjRegions(adjRegion1);
    List<Region> adjRegion2 = new ArrayList<Region>();
    adjRegion2.add(region1);
    region2.setAdjRegions(adjRegion2);
    List<Region> regions = new ArrayList<Region>();
    regions.add(region1);
    regions.add(region2);
    Board board = new Board(regions);
    //Above creations two regions, adjacent to each other
    //Earth owned by player1 with 0 units
    //Wind owned by player2 with 10 units
    
    ParentServer ps = new ParentServer();
    ps.addPlayer("player1", null);
    ps.addPlayer("player2", null);
    ps.setBoard(board);
    //Attack from Wind to Earth with 3 then 2 units, move 1 from Wind to Wind
    //Expect two attackMoves (one with 3 one with 2) from Wind
    //Expect one move
    //Expect one attackCombat with 5 from Wind
    List<OrderInterface> orders = new ArrayList<OrderInterface>();
    orders.add(new AttackMove(regions.get(1), regions.get(0), new Unit(3)));
    orders.add(new AttackCombat(regions.get(1), regions.get(0), new Unit(3)));
    orders.add(new AttackMove(regions.get(1), regions.get(0), new Unit(2)));
    orders.add(new AttackCombat(regions.get(1), regions.get(0), new Unit(2)));
    orders.add(new MoveOrder(regions.get(1), regions.get(1), new Unit(1)));
    ps.addOrdersToMap(orders);
    Map<String, List<OrderInterface>> orderMap = ps.getOrderMap();
    assert(orderMap.keySet().size() == 2);
    assert(orderMap.get("NotCombat").size() == 3);
    assert(orderMap.get("AttackCombat").size() == 1);
    List<Integer> expUnits = new ArrayList<Integer>();
    expUnits.add(new Integer(5));
    for(int i = 0; i < 6; i++){ expUnits.add(new Integer(0)); }
    SourceDestinationUnitOrder attackOrder = (SourceDestinationUnitOrder) orderMap.get("AttackCombat").get(0);
    assert(attackOrder.getUnits().getUnits().equals(expUnits));
    assert(orderMap.get("NotCombat").get(0) instanceof AttackMove);
    assert(orderMap.get("NotCombat").get(1) instanceof AttackMove);
    assert(orderMap.get("NotCombat").get(2) instanceof MoveOrder);
  }

    @Test
    void test_spectate() throws IOException, ClassNotFoundException,
    InterruptedException{
    TextDisplay cout = new TextDisplay();

    //Set to 2 players so we don't have to wait forever...
    int port = 12352;
    MasterServer ms = new MasterServer("", port);
    ParentServer server = new ParentServer(1, ms);
    ms.addParentServer(server);
    server.setMAX_PLAYERS(3);
    server.setMAX_MISSED(3);

    AbstractPlayer player1 = new HumanPlayer("Group A");
    AbstractPlayer player2 = new HumanPlayer("Group B");
    AbstractPlayer player3 = new HumanPlayer("Group C");
    Region region1 = new Region(player1, new Unit(0));
    region1.setName("A");
    Region region2 = new Region(player2, new Unit(0));
    region2.setName("B");
    Region region3 = new Region(player3, new Unit(0));
    region3.setName("C");
    List<Region> adjRegion1 = new ArrayList<Region>();
    adjRegion1.add(region2);
    adjRegion1.add(region3);
    region1.setAdjRegions(adjRegion1);
    List<Region> adjRegion2 = new ArrayList<Region>();
    adjRegion2.add(region1);
    adjRegion2.add(region3);
    region2.setAdjRegions(adjRegion2);
    List<Region> adjRegion3 = new ArrayList<Region>();
    adjRegion3.add(region1);
    adjRegion3.add(region2);
    region3.setAdjRegions(adjRegion3);
    List<Region> regions = new ArrayList<Region>();
    regions.add(region1);
    regions.add(region2);
    regions.add(region3);
    Board boardNew = new Board(regions);
    boardNew.initializeSpies(Arrays.asList("Player 1", "Player 2", "Player 3"));
    //Above creates 3 regions in triangle

    //Set max turn time to 10 seconds...
    server.setTURN_WAIT_MINUTES(10.0/60);
    Thread masterThread = new Thread(ms);
    Thread serverThread = new Thread(server);
    ms.start();
    serverThread.start();

    Thread.sleep(1000);
    //Headless sockets connect
    Socket c1 = new Socket("localhost", port);
    ObjectInputStream c1in = new ObjectInputStream(c1.getInputStream());
    ObjectOutputStream c1out = new ObjectOutputStream(c1.getOutputStream());
    Thread.sleep(5000);
    Socket c2 = new Socket("localhost", port);
    ObjectInputStream c2in = new ObjectInputStream(c2.getInputStream());
    ObjectOutputStream c2out = new ObjectOutputStream(c2.getOutputStream());
    Thread.sleep(5000);
    Socket c3 = new Socket("localhost", port);
    ObjectInputStream c3in = new ObjectInputStream(c3.getInputStream());
    ObjectOutputStream c3out = new ObjectOutputStream(c3.getOutputStream());

    Board board;
    ConfirmationMessage confirmation;
    StringMessage string;
    HumanPlayer player;

    //After connection need to register as new players (player1, player2);
    //Player 1 register
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    c1out.writeObject(new ConfirmationMessage(false));
    c1out.writeObject(new StringMessage("Player 1"));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Salt");
    cout.displayString(string.unpacker());
    c1out.writeObject(new StringMessage("123"));
    c1out.writeObject(new StringMessage("123"));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());

    //Player 2 register
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    c2out.writeObject(new ConfirmationMessage(false));
    c2out.writeObject(new StringMessage("Player 2"));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Salt");
    cout.displayString(string.unpacker());
    c2out.writeObject(new StringMessage("123"));
    c2out.writeObject(new StringMessage("123"));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());

    //Player 3 register
    string = (StringMessage)(c3in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    c3out.writeObject(new ConfirmationMessage(false));
    c3out.writeObject(new StringMessage("Player 3"));
    string = (StringMessage)(c3in.readObject());
    cout.displayString("Salt");
    cout.displayString(string.unpacker());
    c3out.writeObject(new StringMessage("123"));
    c3out.writeObject(new StringMessage("123"));
    string = (StringMessage)(c3in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());

    //Player 1 joins, player 2 joins
    c1out.writeObject(new ConfirmationMessage(false));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Games List");
    cout.displayString(string.unpacker());
    c1out.writeObject(new IntegerMessage(1));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());

    //Player 2 join
    c2out.writeObject(new ConfirmationMessage(false));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Games List");
    cout.displayString(string.unpacker());
    c2out.writeObject(new IntegerMessage(1));
    string = (StringMessage)(c2in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c2in.readObject());

    //Player 3 join
    c3out.writeObject(new ConfirmationMessage(false));
    string = (StringMessage)(c3in.readObject());
    cout.displayString("Games List");
    cout.displayString(string.unpacker());
    c3out.writeObject(new IntegerMessage(1));
    string = (StringMessage)(c3in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c3in.readObject());

    //After connection should send HumanPlayer for the child
    //c2 waits 5 seconds, should be second
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    player = (HumanPlayer)(c2in.readObject());
    cout.displayString("c2 player");
    cout.displayString(player.getName());
    assertEquals("Player 2", player.getName());

    player = (HumanPlayer)(c3in.readObject());
    cout.displayString("c3 player");
    cout.displayString(player.getName());
    assertEquals("Player 3", player.getName());

    Thread.sleep(2000);
    server.setBoard(boardNew);

    //Next firstCall of run()
    //First sends board with Group names --> should be same
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board initial");
    cout.displayBoard(board);
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board initial");
    cout.displayBoard(board);
    board = (Board)(c3in.readObject());
    cout.displayString("c3 board initial");
    cout.displayBoard(board);

    //Player 2 selects group B
    c2out.writeObject(new StringMessage("Group B"));
    //Server sees good --> success message
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 response expected success");
    cout.displayString(string.unpacker());
    //Server sends back with player 2 for B groups
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board after take group b");
    cout.displayBoard(board);

    //Now player 1 tries to take group q (invalid input)
    c1out.writeObject(new StringMessage("Group Q"));
    //Should fail
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 response expected fail");
    cout.displayString(string.unpacker());
    //Resends updated map where Group B --> player 2
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board after c2 takes group b");
    cout.displayBoard(board);
    //Take Group A
    c1out.writeObject(new StringMessage("Group A"));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 response expected success");
    cout.displayString(string.unpacker());
    //Server sends board
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board after take group a");
    cout.displayBoard(board);
    //Take Group C
    c3out.writeObject(new StringMessage("Group C"));
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 response expected success");
    cout.displayString(string.unpacker());
    //Server sends board
    board = (Board)(c3in.readObject());
    cout.displayString("c3 board after take group c");
    cout.displayBoard(board);

    //Now both players would be creating placement orders...
    //Create disconnected client object for generating order lists...
    ClientInputInterface clientIn = getClientIn("");
    ClientOutputInterface writeToNothing = new TextDisplay(new PrintWriter(new
    PrintStream(new ByteArrayOutputStream())));
    Client fakeClient = new Client(clientIn, writeToNothing, null);

    OrderCreator placement = OrderFactoryProducer.getOrderCreator("P", fakeClient);
    OrderHelper orderhelper = new OrderHelper(fakeClient);

    //Both clients asked create placements...
    //Player 1 will place 3 on all
    //Need to set board/player for client placements
    fakeClient.setBoard(board);
    fakeClient.setPlayer(new HumanPlayer("Player 1"));
    String placements;
    List<OrderInterface> initialPlacements = new ArrayList<OrderInterface>();
    placements = "3\n";
    fakeClient.setClientInput(getClientIn(placements));
    placement.addToOrderList(initialPlacements);
    c1out.writeObject(initialPlacements);
    //Player 1 will get success
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 placements expected success");
    cout.displayString(string.unpacker());

    //p2 place 3
    fakeClient.setPlayer(new HumanPlayer("Player 2"));
    initialPlacements = new ArrayList<OrderInterface>();
    placements = "3\n";
    fakeClient.setClientInput(getClientIn(placements));
    placement.addToOrderList(initialPlacements);
    c2out.writeObject(initialPlacements);
    //Player 2 will get success
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 placements expected success");
    cout.displayString(string.unpacker());

    //p3 place 3
    fakeClient.setPlayer(new HumanPlayer("Player 3"));
    initialPlacements = new ArrayList<OrderInterface>();
    placements = "3\n";
    fakeClient.setClientInput(getClientIn(placements));
    placement.addToOrderList(initialPlacements);
    c3out.writeObject(initialPlacements);
    //Player 2 will get success
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 placements expected success");
    cout.displayString(string.unpacker());

    //Parent should wait --> apply all
    //Now should be in main game loop (past this point client doesn't care about
    //board/player)
    //Both should get player, "Continue", true alive message, and board

    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    player = (HumanPlayer)(c2in.readObject());
    cout.displayString("c2 player");
    cout.displayString(player.getName());
    assertEquals("Player 2", player.getName());

    player = (HumanPlayer)(c3in.readObject());
    cout.displayString("c3 player");
    cout.displayString(player.getName());
    assertEquals("Player 3", player.getName());


    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c2in.readObject());
    cout.displayString("c2 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board");
    cout.displayBoard(board);
    
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c3in.readObject());
    cout.displayString("c3 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c3in.readObject());
    cout.displayString("c3 board");
    cout.displayBoard(board);

    //Now give player 1 100 units
    board = server.getBoard();
    board.getRegions().get(0).setUnits(new Unit(100));
    board.getRegions().get(0).getOwner().setPlayerResource(new PlayerResources(100000,100000));
    server.setBoard(board);

    //Now input moves...
    List<OrderInterface> orders;
    fakeClient.setBoard(board);

    cout.displayString("P1 nothing");
    String p1move = "D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    cout.displayString("P2 nothing");
    String p2move = "D\n";
    fakeClient.setClientInput(getClientIn(p2move));
    orders = orderhelper.createOrders();
    c2out.writeObject(orders);
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 order expected success");
    cout.displayString(string.unpacker());

    
    cout.displayString("P3 nothing");
    String p3move = "D\n";
    fakeClient.setClientInput(getClientIn(p3move));
    orders = orderhelper.createOrders();
    c3out.writeObject(orders);
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 order expected success");
    cout.displayString(string.unpacker());
    

    //Start turn stuff

    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    player = (HumanPlayer)(c2in.readObject());
    cout.displayString("c2 player");
    cout.displayString(player.getName());
    assertEquals("Player 2", player.getName());

    player = (HumanPlayer)(c3in.readObject());
    cout.displayString("c3 player");
    cout.displayString(player.getName());
    assertEquals("Player 3", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c2in.readObject());
    cout.displayString("c2 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board");
    cout.displayBoard(board);
    
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c3in.readObject());
    cout.displayString("c3 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c3in.readObject());
    cout.displayString("c3 board");
    cout.displayBoard(board);

    fakeClient.setBoard(board);

    //P2 does nothing
    cout.displayString("P2 does nothing");
    p2move = "D\n";
    fakeClient.setClientInput(getClientIn(p2move));
    orders = orderhelper.createOrders();
    c2out.writeObject(orders);
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 order expected success");
    cout.displayString(string.unpacker());
    
    cout.displayString("P3 nothing");
    p3move = "D\n";
    fakeClient.setClientInput(getClientIn(p3move));
    orders = orderhelper.createOrders();
    c3out.writeObject(orders);
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 order success");
    cout.displayString(string.unpacker());

    //P1 attacks C with 99
    cout.displayString("P1 attacks c with 99");
    p1move = "A\nA\nC\n99\n" +"D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    //Turn ends....
    cout.displayString("Parent now applies orders...");
    cout.displayString("C should taken by A");

    //Start turn stuff

    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    player = (HumanPlayer)(c2in.readObject());
    cout.displayString("c2 player");
    cout.displayString(player.getName());
    assertEquals("Player 2", player.getName());

    player = (HumanPlayer)(c3in.readObject());
    cout.displayString("c3 player");
    cout.displayString(player.getName());
    assertEquals("Player 3", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c2in.readObject());
    cout.displayString("c2 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board");
    cout.displayBoard(board);

    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c3in.readObject());
    cout.displayString("c3 alive expected false");
    cout.displayString(String.valueOf(confirmation.unpacker()));

    fakeClient.setBoard(board);

    //P2 does nothing
    cout.displayString("P2 does nothing");
    p2move = "D\n";
    fakeClient.setClientInput(getClientIn(p2move));
    orders = orderhelper.createOrders();
    c2out.writeObject(orders);
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 order expected success");
    cout.displayString(string.unpacker());

    //P1 does nothing
    cout.displayString("P1 does nothing");
    p1move = "D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    //P3 will spectate
    //Send back true and get board
    c3out.writeObject(new ConfirmationMessage(true));
    board = (Board)(c3in.readObject());
    cout.displayString("c3 board");
    cout.displayBoard(board);
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 spectate success");
    cout.displayString(string.unpacker());

    //P1/3 does start turn
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    player = (HumanPlayer)(c3in.readObject());
    cout.displayString("c3 player");
    cout.displayString(player.getName());
    assertEquals("Player 3", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);


    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c3in.readObject());
    cout.displayString("c3 alive expected false");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c3in.readObject());
    cout.displayString("c3 board");
    cout.displayBoard(board);

    //P1 does nothing
    cout.displayString("P1 moves does nothing");
    p1move = "D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    //P3 spectate
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 spectate success");
    cout.displayString(string.unpacker());

    //P2 suddenly closes
    cout.displayString("P2 suddenly closes");
    cout.displayString("We expect childserver of connection reset. This is the thread realizing the socket is dead.\n");
    c2.close();

    cout.displayString("After waiting for timeout moves will be applied");
    cout.displayString("Server should wait until MAX_MISSED then inform player 1 that they won and close socket");

    for(int i = 0; i < server.getMAX_MISSED(); i++){
    //P1/3 does start turn
    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    player = (HumanPlayer)(c3in.readObject());
    cout.displayString("c3 player");
    cout.displayString(player.getName());
    assertEquals("Player 3", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c1in.readObject());
    cout.displayString("c1 alive expected true");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board");
    cout.displayBoard(board);

    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn start");
    cout.displayString(string.unpacker());
    confirmation = (ConfirmationMessage)(c3in.readObject());
    cout.displayString("c3 alive expected false");
    cout.displayString(String.valueOf(confirmation.unpacker()));
    board = (Board)(c3in.readObject());
    cout.displayString("c3 board");
    cout.displayBoard(board);

    //P1 does nothing
    cout.displayString("P1 moves does nothing");
    p1move = "D\n";
    fakeClient.setClientInput(getClientIn(p1move));
    orders = orderhelper.createOrders();
    c1out.writeObject(orders);
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 order expected success");
    cout.displayString(string.unpacker());

    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 spectate expected success");
    cout.displayString(string.unpacker());
    }

    player = (HumanPlayer)(c1in.readObject());
    cout.displayString("c1 player");
    cout.displayString(player.getName());
    assertEquals("Player 1", player.getName());

    player = (HumanPlayer)(c3in.readObject());
    cout.displayString("c3 player");
    cout.displayString(player.getName());
    assertEquals("Player 3", player.getName());

    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 winner message");
    cout.displayString(string.unpacker());

    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 turn message");
    cout.displayString(string.unpacker());
    string = (StringMessage)(c3in.readObject());
    cout.displayString("c3 winner message");
    cout.displayString(string.unpacker());


    Thread.sleep(500);

    assert(c1.getInputStream().read() == -1);

    c1.close();
    c1in.close();
    c1out.close();
    c2in.close();
    c2out.close();
    c3.close();
    c3in.close();
    c3out.close();
    }

  @Test
  public void test_assorted() throws IOException{
    ParentServer ps = new ParentServer();
    ps.setMAX_PLAYERS(1);
    Connection pc = new Connection();
    assert(ps.tryJoin("a", pc) == true);
    assert(ps.tryJoin("b", pc) == false);
    ps.setNotStarted(false);
    assert(ps.tryJoin("b", pc) == false);
    ps.setNotStarted(true);
    assert(ps.tryJoin("a", pc) == true);
    ps.removePlayer("a");

    ps.setMAX_PLAYERS(5);
    assert(ps.tryJoin("p1", pc) == true);
    assert(ps.tryJoin("p2", pc) == true);
    assert(ps.tryJoin("p3", pc) == true);
    ps.getChildren().get(2).getPlayer().setPlaying(false);


    Board board = new Board();
    AbstractPlayer p1 = new HumanPlayer("p1");
    AbstractPlayer p2 = new HumanPlayer("p2");
    AbstractPlayer p3 = new HumanPlayer("p3");
    Unit u1 = new Unit(10);
    Unit u2 = new Unit(10);
    Unit u3 = new Unit(10);
    Unit u4 = new Unit(10);
    Region r1 = new Region(p1, u1);
    Region r2 = new Region(p2, u2);
    Region r3 = new Region(p2, u3);
    Region r4 = new Region(p3, u4);
    r1.setName("r1");
    r2.setName("r2");
    r3.setName("r3");
    r4.setName("r4");
    //Square
    r1.setAdjRegions(Arrays.asList(r4, r2));
    r2.setAdjRegions(Arrays.asList(r1, r3));
    r3.setAdjRegions(Arrays.asList(r2, r4));
    r4.setAdjRegions(Arrays.asList(r3, r1));
    board.setRegions(Arrays.asList(r1, r2, r3, r4));
    board.initializeSpies(Arrays.asList("p1", "p2", "p3"));
    ps.setBoard(board);
    for(ChildServer child : ps.getChildren()){
      child.setClientBoard(ps.getBoard());
    }

    MoveOrder mo = new MoveOrder(r2, r3, new Unit(1));
    AttackMove am = new AttackMove(r2, r1, new Unit(1));
    AttackCombat ac = new AttackCombat(r2, r1, new Unit(1));
    PlacementOrder po = new PlacementOrder(r3, new Unit(1));
    UnitBoost ub = new UnitBoost(r3, new Unit(1));
    SpyUpgradeOrder su = new SpyUpgradeOrder(r2);
    SpyMoveOrder sm = new SpyMoveOrder(r2, r3, p2);
    CloakOrder co = new CloakOrder(r2);
    TechBoost tb = new TechBoost(p2);
    RaidOrder ro = new RaidOrder(r2, r1);
    TeleportOrder to = new TeleportOrder(r2, r3, new Unit(1));
    ResourceBoost rbo = new ResourceBoost(r2);
    AttackCombat ac2 = new AttackCombat(r1, r2, new Unit(1));
    AttackCombat ac3 = new AttackCombat(r2, r1, new Unit(1));

    List<OrderInterface> orders = new ArrayList<OrderInterface>();
    orders.add(mo);
    orders.add(am);
    orders.add(ac);
    orders.add(po);
    orders.add(ub);
    orders.add(su);
    orders.add(sm);
    orders.add(co);
    orders.add(tb);
    orders.add(ro);
    orders.add(to);
    orders.add(rbo);
    orders.add(ac2);
    orders.add(ac3);
    ps.addOrdersToMap(orders);
    Map<String, List<OrderInterface>> om = ps.getOrderMap();

    assert(om.get("NotCombat").containsAll(Arrays.asList(mo,am,po,ub,su,sm,co,tb,to,rbo)));
    assert(om.get("AttackCombat").containsAll(Arrays.asList(ac,ac2)));
    assert(om.get("RaidOrder").containsAll(Arrays.asList(ro)));
    AttackCombat acc = (AttackCombat) om.get("AttackCombat").get(0);
    AttackCombat acc2 = (AttackCombat) om.get("AttackCombat").get(1);
    assert(acc.getUnits().getUnits().get(0) == 2);

    ps.applyOrders();
    ps.growUnits();
    ps.setTurn(0);
    ps.getPlagueID();

  }

}
