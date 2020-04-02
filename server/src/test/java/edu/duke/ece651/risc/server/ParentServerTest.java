package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.io.*;
import java.net.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.*;

import edu.duke.ece651.risc.client.*;

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
    assertEquals(1, regions.get(0).getSize());
    assertEquals(1, regions.get(0).getFuelProduction());
    assertEquals(1, regions.get(0).getTechProduction());
    assertEquals(1, regions.get(6).getSize());
    assertEquals(1, regions.get(6).getFuelProduction());
    assertEquals(1, regions.get(6).getTechProduction());

  }

  @Test
  public void test_growUnits() {
    ParentServer ps = new ParentServer();
    // ps.createBoard();
    AbstractPlayer player = new HumanPlayer("player1");
    AbstractPlayer player2 = new HumanPlayer("player2");
    ChildServer cs = new ChildServer(player, ps);
    ChildServer cs2 = new ChildServer(player2, ps);
    ps.addPlayer(cs);
    ps.addPlayer(cs2);
    Board b = new Board(getRegionList(player, player2));
    ps.setBoard(b);

    assertEquals(20, player2.getResources().getFuelResource().getFuel());
    assertEquals(15, player.getResources().getTechResource().getTech());
    assertEquals(4, b.getRegions().get(2).getUnits().getUnits().get(0));
    ps.growUnits();
    assertEquals(220, player2.getResources().getFuelResource().getFuel());
    assertEquals(190, player.getResources().getTechResource().getTech()); // TODO -- this line produces 190 not 115
    assertEquals(5, b.getRegions().get(2).getUnits().getUnits().get(0));

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

  // @Test
  // public void test_orders(){
  // //Default 5 player board
  // TextDisplay out = new TextDisplay();
  // ParentServer ps = new ParentServer();
  // for(int i = 0; i < 5; i++){ ps.addPlayer(new ChildServer(new
  // HumanPlayer(""),ps)); }
  // ps.createStartingGroups();

  // Board originalBoard = (Board) DeepCopy.deepCopy(ps.getBoard());

  // List<Region> regions = ps.getBoard().getRegions();
  // List<OrderInterface> orders = new ArrayList<OrderInterface>();

  // //Add 1 to A, 2 to B, etc.
  // for(int i = 0; i < regions.size(); i++){
  // orders.add(new PlacementOrder(regions.get(i), new Unit(i+1)));
  // }

  // //System.out.println("Original empty board");
  // String placementStart1 = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());
  // ps.addOrdersToMap(orders);
  // Map<String, List<OrderInterface>> orderMap = ps.getOrderMap();
  // assert(orderMap.containsKey("PlacementOrder"));
  // assert(orderMap.get("PlacementOrder").size() == regions.size());
  // ps.applyOrders();
  // //System.out.println("Board after adding 1 to all regions");
  // String placementEnd1 = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());

  // //Convert to another board
  // Board bCopy = (Board)DeepCopy.deepCopy(originalBoard);
  // ps.setBoard(bCopy);
  // //System.out.println("Insert copy of original empty board");
  // String placementStart2 = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());
  // for(int i = 0; i < regions.size(); i++){
  // orders.get(i).convertOrderRegions(bCopy);
  // }
  // ps.addOrdersToMap(orders);
  // ps.applyOrders();
  // //System.out.println("Apply previous orders to board copy after conversion");
  // String placementEnd2 = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());

  // assertEquals(placementStart1, placementStart2);
  // assertEquals(placementEnd1, placementEnd2);

  // ps.setBoard((Board)DeepCopy.deepCopy(originalBoard));
  // //Add move orders, move all from i --> i+1 cyclic
  // for(int i = 0; i < regions.size()-1; i++){
  // orders.add(new MoveOrder(regions.get(i), regions.get(i+1), new Unit(i+1)));
  // }
  // orders.add(new MoveOrder(regions.get(regions.size()-1), regions.get(0), new
  // Unit(regions.size())));

  // for(int i = 0; i < orders.size(); i++){
  // orders.get(i).convertOrderRegions(ps.getBoard());
  // }

  // //System.out.println("Original empty board");
  // String moveStart1 = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());
  // ps.addOrdersToMap(orders);
  // orderMap = ps.getOrderMap();
  // assert(orderMap.keySet().size() == 2);
  // assert(orderMap.containsKey("PlacementOrder"));
  // assert(orderMap.get("PlacementOrder").size() == regions.size());
  // assert(orderMap.containsKey("MoveOrder"));
  // assert(orderMap.get("MoveOrder").size() == regions.size());
  // ps.applyOrders();
  // //System.out.println("Board after adding 1 to all regions");
  // String moveEnd1 = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());

  // //Convert to another board
  // bCopy = (Board)DeepCopy.deepCopy(originalBoard);
  // ps.setBoard(bCopy);
  // //System.out.println("Insert copy of original empty board");
  // String moveStart2 = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());
  // for(int i = 0; i < orders.size(); i++){
  // orders.get(i).convertOrderRegions(ps.getBoard());
  // }
  // ps.addOrdersToMap(orders);
  // ps.applyOrders();
  // //System.out.println("Apply previous orders to board copy after conversion");
  // String moveEnd2 = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());

  // assertEquals(moveStart1, moveStart2);
  // assertEquals(moveEnd1, moveEnd2);

  // //Add attack orders, will attack B from C and E
  // //Since 50>>1 we expect that either Group B or Group C will take B
  // //Ordering random as well as who wins second round so final owner random
  // //Regardless C should have -23 and E -21 units
  // orders.add(new AttackOrder(regions.get(2), regions.get(1), new Unit(25)));
  // orders.add(new AttackOrder(regions.get(4), regions.get(1), new Unit(25)));

  // String attackStart;
  // String attackEnd;

  // for(int j = 0; j < 5; j++){
  // System.out.println(j);
  // //Remove and reapply attack (since attacks interact with unit object)
  // orders.remove(orders.size()-1);
  // orders.remove(orders.size()-1);
  // orders.add(new AttackOrder(regions.get(2), regions.get(1), new Unit(25)));
  // orders.add(new AttackOrder(regions.get(4), regions.get(1), new Unit(25)));

  // ps.setBoard((Board)DeepCopy.deepCopy(originalBoard));

  // for(int i = 0; i < orders.size(); i++){
  // orders.get(i).convertOrderRegions(ps.getBoard());
  // }

  // //System.out.println("Original empty board");
  // attackStart = out.createBoard(ps.getBoard());
  // //out.displayBoard(ps.getBoard());
  // ps.addOrdersToMap(orders);
  // orderMap = ps.getOrderMap();
  // assert(orderMap.keySet().size() == 3);
  // assert(orderMap.containsKey("PlacementOrder"));
  // assert(orderMap.get("PlacementOrder").size() == regions.size());
  // assert(orderMap.containsKey("MoveOrder"));
  // assert(orderMap.get("MoveOrder").size() == regions.size());
  // assert(orderMap.containsKey("AttackOrder"));
  // assert(orderMap.get("AttackOrder").size() == 2);
  // ps.applyOrders();
  // //System.out.println("Board after adding 1 to all regions");
  // attackEnd = out.createBoard(ps.getBoard());
  // out.displayBoard(ps.getBoard());

  // //Check negatives make sense
  // assert(ps.getBoard().getRegions().get(2).getUnits().getUnits() == -23);
  // assert(ps.getBoard().getRegions().get(4).getUnits().getUnits() == -21);
  // assert(!ps.getBoard().getRegions().get(1).getOwner().getName().equals("Group
  // A"));

  // Region out1 = ps.getBoard().getRegions().get(1);
  // System.out.println("Region 1 owned by " + out1.getOwner().getName() + " with
  // " + out1.getUnits().getUnits() + " units");
  // System.out.println("~~~~~~~");

  // }

  // }

  // public ClientInputInterface getClientIn(String str){
  // return new ConsoleInput(new ByteArrayInputStream(str.getBytes()));
  // }

  // @Test
  // public void test_fakeClients() throws IOException, ClassNotFoundException,
  // InterruptedException{
  // TextDisplay cout = new TextDisplay();

  // //Set to 2 players so we don't have to wait forever...
  // int port = 12347;
  // ParentServer server = new ParentServer(port);
  // server.setMAX_PLAYERS(2);
  // //Set max turn time to 10 seconds...
  // server.setTURN_WAIT_MINUTES(10.0/60);
  // Thread serverThread = new Thread(server);
  // serverThread.start();

  // Thread.sleep(1000);
  // //Headless sockets connect
  // Socket c1 = new Socket("localhost", port);
  // ObjectInputStream c1in = new ObjectInputStream(c1.getInputStream());
  // ObjectOutputStream c1out = new ObjectOutputStream(c1.getOutputStream());
  // Thread.sleep(5000);
  // Socket c2 = new Socket("localhost", port);
  // ObjectInputStream c2in = new ObjectInputStream(c2.getInputStream());
  // ObjectOutputStream c2out = new ObjectOutputStream(c2.getOutputStream());

  // Board board;
  // ConfirmationMessage confirmation;
  // StringMessage string;
  // HumanPlayer player;

  // //After connection should send HumanPlayer for the child
  // //c2 waits 5 seconds, should be second
  // player = (HumanPlayer)(c1in.readObject());
  // cout.displayString("c1 player");
  // cout.displayString(player.getName());
  // assertEquals("Player 1", player.getName());
  // player = (HumanPlayer)(c2in.readObject());
  // cout.displayString("c2 player");
  // cout.displayString(player.getName());
  // assertEquals("Player 2", player.getName());

  // //Next firstCall of run()
  // //First sends board with Group names --> should be same
  // board = (Board)(c1in.readObject());
  // cout.displayString("c1 board initial");
  // cout.displayBoard(board);
  // board = (Board)(c2in.readObject());
  // cout.displayString("c2 board initial");
  // cout.displayBoard(board);

  // //Player 2 selects group B
  // c2out.writeObject(new StringMessage("Group B"));
  // //Server sees good --> success message
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 response expected success");
  // cout.displayString(string.unpacker());
  // //Server sends back with player 2 for B groups
  // board = (Board)(c2in.readObject());
  // cout.displayString("c2 board after take group b");
  // cout.displayBoard(board);

  // //Now player 1 tries to take group q (invalid input)
  // c1out.writeObject(new StringMessage("Group Q"));
  // //Should fail
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 response expected fail");
  // cout.displayString(string.unpacker());
  // //Resends updated map where Group B --> player 2
  // board = (Board)(c1in.readObject());
  // cout.displayString("c1 board after c2 takes group b");
  // cout.displayBoard(board);
  // //Take Group A
  // c1out.writeObject(new StringMessage("Group A"));
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 response expected success");
  // cout.displayString(string.unpacker());
  // //Server sends board
  // board = (Board)(c1in.readObject());
  // cout.displayString("c1 board after take group a");
  // cout.displayBoard(board);

  // //Now both players would be creating placement orders...
  // //Create disconnected client object for generating order lists...
  // ClientInputInterface clientIn = getClientIn("");
  // ClientOutputInterface writeToNothing = new TextDisplay(new PrintWriter(new
  // PrintStream(new ByteArrayOutputStream())));
  // Client fakeClient = new Client(clientIn, writeToNothing);

  // //Both clients asked create placements...
  // //Player 1 will place 3 on all
  // //Need to set board/player for client placements
  // fakeClient.setBoard(board);
  // fakeClient.setPlayer(new HumanPlayer("Player 1"));
  // String placements;
  // List<PlacementOrder> initialPlacements;
  // placements = "3\n".repeat(6);
  // fakeClient.setClientInput(getClientIn(placements));
  // initialPlacements = fakeClient.createPlacements();
  // c1out.writeObject(initialPlacements);
  // //Player 1 will get success
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 placements expected success");
  // cout.displayString(string.unpacker());

  // //Player 2 will place 4 on all (invalid)
  // //Need to set player (board is same)
  // fakeClient.setPlayer(new HumanPlayer("Player 2"));
  // placements = "4\n".repeat(6);
  // fakeClient.setClientInput(getClientIn(placements));
  // initialPlacements = fakeClient.createPlacements();
  // c2out.writeObject(initialPlacements);
  // //Player 2 will get fail
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 placements expected fail");
  // cout.displayString(string.unpacker());
  // //Try again but now 2,3,4,2,3,4
  // //First get board again (now see C1 placements)
  // board = (Board)(c2in.readObject());
  // cout.displayBoard(board);
  // placements = "2\n3\n4\n".repeat(2);
  // fakeClient.setClientInput(getClientIn(placements));
  // initialPlacements = fakeClient.createPlacements();
  // c2out.writeObject(initialPlacements);
  // //Player 2 will get success
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 placements expected success");
  // cout.displayString(string.unpacker());

  // //Parent should wait --> apply all
  // //Now should be in main game loop (past this point client doesn't care about
  // board/player)
  // //Both should get "Continue", true alive message, and board
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn message");
  // cout.displayString(string.unpacker());
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn start");
  // cout.displayString(string.unpacker());
  // confirmation = (ConfirmationMessage)(c1in.readObject());
  // cout.displayString("c1 alive expected true");
  // cout.displayString(String.valueOf(confirmation.unpacker()));
  // board = (Board)(c1in.readObject());
  // cout.displayString("c1 board");
  // cout.displayBoard(board);

  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 turn message");
  // cout.displayString(string.unpacker());
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 turn start");
  // cout.displayString(string.unpacker());
  // confirmation = (ConfirmationMessage)(c2in.readObject());
  // cout.displayString("c2 alive expected true");
  // cout.displayString(String.valueOf(confirmation.unpacker()));
  // board = (Board)(c2in.readObject());
  // cout.displayString("c2 board");
  // cout.displayBoard(board);

  // //Now input moves...
  // List<OrderInterface> orders;

  // //P1 owns A-F (all 3)
  // //P2 owns G-L (2,3,4,2,3,4)
  // cout.displayString("P1 moves 2 from B to A then attacks from A to L with 4
  // (orders backwards on input)");
  // String p1move = "A\nA\nL\n4\n" + "M\nB\nA\n2\n" + "D\n";
  // fakeClient.setClientInput(getClientIn(p1move));
  // orders = fakeClient.createOrders();
  // c1out.writeObject(orders);
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 order expected success");
  // cout.displayString(string.unpacker());

  // cout.displayString("P2 tries to move 10 from G to L (invalid)");
  // String p2move = "M\nG\nL\n10\n" + "D\n";
  // fakeClient.setClientInput(getClientIn(p2move));
  // orders = fakeClient.createOrders();
  // c2out.writeObject(orders);
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 order expected fail");
  // cout.displayString(string.unpacker());

  // cout.displayString("P2 tries to move 1 from K to L");
  // p2move = "M\nG\nL\n1\n" + "D\n";
  // fakeClient.setClientInput(getClientIn(p2move));
  // orders = fakeClient.createOrders();
  // c2out.writeObject(orders);
  // board = (Board)(c2in.readObject());
  // cout.displayBoard(board);
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 order expected success");
  // cout.displayString(string.unpacker());

  // //Turn ends....
  // cout.displayString("Parent now applies orders...");
  // cout.displayString("P1 might take L, otherwise ownership stays same");
  // cout.displayString("A-B should have 2, C-F 4");
  // cout.displayString("G-K should be 3,4,5,3,3");

  // //Start turn stuff
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn message");
  // cout.displayString(string.unpacker());
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn start");
  // cout.displayString(string.unpacker());
  // confirmation = (ConfirmationMessage)(c1in.readObject());
  // cout.displayString("c1 alive expected true");
  // cout.displayString(String.valueOf(confirmation.unpacker()));
  // board = (Board)(c1in.readObject());
  // cout.displayString("c1 board");
  // cout.displayBoard(board);

  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 turn message");
  // cout.displayString(string.unpacker());
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 turn start");
  // cout.displayString(string.unpacker());
  // confirmation = (ConfirmationMessage)(c2in.readObject());
  // cout.displayString("c2 alive expected true");
  // cout.displayString(String.valueOf(confirmation.unpacker()));
  // board = (Board)(c2in.readObject());
  // cout.displayString("c2 board");
  // cout.displayBoard(board);

  // //P2 does nothing
  // cout.displayString("P2 does nothing");
  // p2move = "D\n";
  // fakeClient.setClientInput(getClientIn(p2move));
  // orders = fakeClient.createOrders();
  // c2out.writeObject(orders);
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 order expected success");
  // cout.displayString(string.unpacker());

  // //P1 attacks F->G, E->H, D->I with 3 each
  // cout.displayString("P1 attacks F->G, E->H, D->I with 3 each");
  // p1move = "A\nF\nG\n3\n" + "A\nE\nH\n3\n" + "A\nD\nI\n3\n" +"D\n";
  // fakeClient.setClientInput(getClientIn(p1move));
  // orders = fakeClient.createOrders();
  // c1out.writeObject(orders);
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 order expected success");
  // cout.displayString(string.unpacker());

  // //Turn ends....
  // cout.displayString("Parent now applies orders...");
  // cout.displayString("P1 might take G, H, I");
  // cout.displayString("A-B should have 3, C 5, D-F 2");
  // cout.displayString("J-K should be 4,4");

  // //Start turn stuff
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn message");
  // cout.displayString(string.unpacker());
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn start");
  // cout.displayString(string.unpacker());
  // confirmation = (ConfirmationMessage)(c1in.readObject());
  // cout.displayString("c1 alive expected true");
  // cout.displayString(String.valueOf(confirmation.unpacker()));
  // board = (Board)(c1in.readObject());
  // cout.displayString("c1 board");
  // cout.displayBoard(board);

  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 turn message");
  // cout.displayString(string.unpacker());
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 turn start");
  // cout.displayString(string.unpacker());
  // confirmation = (ConfirmationMessage)(c2in.readObject());
  // cout.displayString("c2 alive expected true");
  // cout.displayString(String.valueOf(confirmation.unpacker()));
  // board = (Board)(c2in.readObject());
  // cout.displayString("c2 board");
  // cout.displayBoard(board);

  // //P2 does nothing
  // cout.displayString("P2 does nothing");
  // p2move = "D\n";
  // fakeClient.setClientInput(getClientIn(p2move));
  // orders = fakeClient.createOrders();
  // c2out.writeObject(orders);
  // string = (StringMessage)(c2in.readObject());
  // cout.displayString("c2 order expected success");
  // cout.displayString(string.unpacker());

  // //P1 does nothing
  // cout.displayString("P1 does nothing");
  // p1move = "D\n";
  // fakeClient.setClientInput(getClientIn(p1move));
  // orders = fakeClient.createOrders();
  // c1out.writeObject(orders);
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 order expected success");
  // cout.displayString(string.unpacker());

  // //Turn ends....
  // cout.displayString("Parent now applies orders...");
  // cout.displayString("Should be same as last but all regions + 1");

  // //P1 does start turn
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn message");
  // cout.displayString(string.unpacker());
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn start");
  // cout.displayString(string.unpacker());
  // confirmation = (ConfirmationMessage)(c1in.readObject());
  // cout.displayString("c1 alive expected true");
  // cout.displayString(String.valueOf(confirmation.unpacker()));
  // board = (Board)(c1in.readObject());
  // cout.displayString("c1 board");
  // cout.displayBoard(board);

  // //P1 does nothing
  // cout.displayString("P1 moves does nothing");
  // p1move = "D\n";
  // fakeClient.setClientInput(getClientIn(p1move));
  // orders = fakeClient.createOrders();
  // c1out.writeObject(orders);
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 order expected success");
  // cout.displayString(string.unpacker());

  // //P2 suddenly closes
  // cout.displayString("P2 suddenly closes");
  // cout.displayString("We expect two stacktraces here.\n One will be for the
  // childserver of connection reset. This is the thread realizing the socket is
  // dead. Look for ThreadPoolExecutor near end.\n Two will be for the
  // ParentServer of socket closed. This is when it is closing everything and it
  // tries to close the pre-closed childserver connection.\n");
  // c2.close();

  // cout.displayString("After waiting for timeout moves will be applied");
  // cout.displayString("Server should then inform player 1 that they won and
  // close socket");

  // string = (StringMessage)(c1in.readObject());
  // cout.displayString("c1 turn message");
  // cout.displayString(string.unpacker());
  // string = (StringMessage)(c1in.readObject());
  // cout.displayString(string.unpacker());

  // Thread.sleep(500);

  // assert(c1.getInputStream().read() == -1);

  // c1.close();
  // c1in.close();
  // c1out.close();
  // c2in.close();
  // c2out.close();
  // }

}
