package edu.duke.ece651.risc.server;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.io.*;
import java.net.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.*;

import edu.duke.ece651.risc.client.*;

public class ParentServerTest {
  /* @Test
  public void test_createStartingGroups(){
    ParentServer ps = new ParentServer();
    HumanPlayer player = new HumanPlayer();
    player.setName("Player One");
    HumanPlayer playerTwo = new HumanPlayer();
    playerTwo.setName("Player Two");
    ChildServer cs = new ChildServer(player,ps);
    ChildServer csTwo = new ChildServer(playerTwo, ps);
    //ps.addPlayer(cs);
    //ps.addPlayer(csTwo);
    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    for (int i = 0; i < regions.size(); i++){
      System.out.println(i + ": " + regions.get(i).getOwner().getName() + " " + regions.get(i).getUnits().getUnits());
    }
    }*/
  @Test
  public void test_startingGroups(){
    ParentServer ps = new ParentServer();
    //ps.createBoard();
    HumanPlayer player = new HumanPlayer("player1");
    HumanPlayer player2 = new HumanPlayer("player2");
    ChildServer cs = new ChildServer(player,ps);
    ChildServer cs2 = new ChildServer(player2, ps);
    ps.addPlayer(cs);
    ps.addPlayer(cs2);

    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    List<Region> adjRegion;
    for (int i = 0; i < regions.size(); i++){
      adjRegion = regions.get(i).getAdjRegions();
      System.out.println(regions.get(i).getName() + ": Group Name: " + regions.get(i).getOwner().getName() + " Units: " + regions.get(i).getUnits().getUnits());
      System.out.print("Adj Regions: ");
      for (int j = 0; j < adjRegion.size(); j++) {
        System.out.print(adjRegion.get(j).getName() + " ");
      }
      System.out.println();
    }
  }
   @Test
   public void test_placementOrder(){
    ParentServer ps = new ParentServer();
    HumanPlayer player = new HumanPlayer("player1");
    HumanPlayer player2 = new HumanPlayer("player2");
    ChildServer cs = new ChildServer(player,ps);
    ChildServer cs2 = new ChildServer(player2, ps);
    ps.addPlayer(cs);
    ps.addPlayer(cs2);

    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    for(int i = 0; i < regions.size(); i++) {
      OrderInterface placement = new PlacementOrder(regions.get(i),new Unit(i + 5));
      placement.doAction();
      System.out.println("Placement number: " + (i + 5));
    }
    assertEquals(5,regions.get(0).getUnits().getUnits());
    assertEquals(6,regions.get(1).getUnits().getUnits());
    assertEquals(7,regions.get(2).getUnits().getUnits());
    assertEquals(8,regions.get(3).getUnits().getUnits());
    assertEquals(9,regions.get(4).getUnits().getUnits());
    assertEquals(10,regions.get(5).getUnits().getUnits());
    assertEquals(11,regions.get(6).getUnits().getUnits());
    assertEquals(12,regions.get(7).getUnits().getUnits());
    assertEquals(13,regions.get(8).getUnits().getUnits());
    assertEquals(14,regions.get(9).getUnits().getUnits());
    assertEquals(15,regions.get(10).getUnits().getUnits());
    assertEquals(16,regions.get(11).getUnits().getUnits());
  }

  public void setInputStream(InputStream stream, String str) throws IOException{
    stream.close();
    stream = new ByteArrayInputStream(str.getBytes());
  }
  
  //@Test
  public void test_fakeClients()  throws IOException, ClassNotFoundException, InterruptedException{
    TextDisplay cout = new TextDisplay();

    //Set to 2 players so we don't have to wait forever...
    ParentServer server = new ParentServer();
    server.setMAX_PLAYERS(2);
    //Set max turn time to 10 seconds...
    server.setTURN_WAIT_MINUTES(10.0/(60*1000));
    Thread serverThread = new Thread(server);
    serverThread.start();

    //Headless sockets connect
    Socket c1 = new Socket("localhost", Constants.DEFAULT_PORT);
    ObjectInputStream c1in = new ObjectInputStream(c1.getInputStream());
    ObjectOutputStream c1out = new ObjectOutputStream(c1.getOutputStream());
    Thread.sleep(5000);
    Socket c2 = new Socket("localhost", Constants.DEFAULT_PORT);
    ObjectInputStream c2in = new ObjectInputStream(c2.getInputStream());
    ObjectOutputStream c2out = new ObjectOutputStream(c2.getOutputStream());

    Board board;
    ConfirmationMessage confirmation;
    StringMessage string;
    HumanPlayer player;

    //After connection should send HumanPlayer for the child
    //c2 waits 5 seconds, should be second
    player = (HumanPlayer)(c1in.readObject());
    assertEquals("Player 1", player.getName());
    player = (HumanPlayer)(c2in.readObject());
    assertEquals("Player 2", player.getName());

    //Next firstCall of run()
    //First sends board with Group names --> should be same
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board initial");
    cout.displayBoard(board);
    board = (Board)(c1in.readObject());
    cout.displayString("c2 board initial");
    cout.displayBoard(board);

    //Player 2 selects group B
    c2out.writeObject(new StringMessage("Group B"));
    //Server sees good --> success message
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 response");
    cout.displayString(string.unpacker());
    //Server sends back with player 2 for B groups
    board = (Board)(c2in.readObject());
    cout.displayString("c2 board after take group b");
    cout.displayBoard(board);

    //Now player 1 tries to take group q (invalid input)
    c1out.writeObject(new StringMessage("Group Q"));
    //Should fail
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 response");
    cout.displayString(string.unpacker());
    //Resends updated map where Group B --> player 2
    board = (Board)(c1in.readObject());
    cout.displayString("c1 board after c2 takes group b");
    cout.displayBoard(board);
    //Take Group A
    c1out.writeObject(new StringMessage("Group A"));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 response");
    cout.displayString(string.unpacker());
    //Server sends board
    board = (Board)(c2in.readObject());
    cout.displayString("c1 board after take group a");
    cout.displayBoard(board);

    //Now both players would be creating placement orders...
    //Create disconnected client object for generating order lists...
    InputStream fakeClientIS = new ByteArrayInputStream("".getBytes());
    ClientInputInterface clientIn = new ConsoleInput(fakeClientIS);
    ClientOutputInterface writeToNothing = new TextDisplay(new PrintWriter(new PrintStream(new ByteArrayOutputStream())));
    Client fakeClient = new Client(clientIn, writeToNothing);

    //Both clients asked create placements...
    //Player 1 will place 3 on all
    String placements;
    List<PlacementOrder> initialPlacements;
    placements = "3\n".repeat(6);
    setInputStream(fakeClientIS, placements);
    initialPlacements = fakeClient.createPlacements();
    c1out.writeObject(initialPlacements);
    //Player 1 will get success
    string = (StringMessage)(c1in.readObject());
    cout.displayString("c1 placements");
    cout.displayString(string.unpacker());

    //Player 2 will place 4 on all (invalid)
    placements = "4\n".repeat(6);
    setInputStream(fakeClientIS, placements);
    initialPlacements = fakeClient.createPlacements();
    c2out.writeObject(initialPlacements);
    //Player 2 will get fail
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 bad placements");
    cout.displayString(string.unpacker());
    //Try again but now 1,2,3,1,2,3
    placements = "1\n2\n3\n".repeat(2);
    setInputStream(fakeClientIS, placements);
    initialPlacements = fakeClient.createPlacements();
    c2out.writeObject(initialPlacements);
    //Player 2 will get success
    string = (StringMessage)(c2in.readObject());
    cout.displayString("c2 good placements");
    cout.displayString(string.unpacker());

    //Parent should wait --> apply all
    
    
  }

}
