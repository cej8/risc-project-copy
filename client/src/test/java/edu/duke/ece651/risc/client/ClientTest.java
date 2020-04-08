package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.*;
import java.io.*;
import java.net.*;

import org.mindrot.jbcrypt.*;

import static org.mockito.Mockito.*;
import org.mockito.stubbing.*;
import org.mockito.invocation.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ClientTest {
  @Test
  public void tet_connection() throws IOException{
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    Board board = getTestBoard(player1);
    ArrayList<Object> inputs = new ArrayList<Object>();
    inputs.add(board);
    Socket mockSocket = MockTests.setupMockSocket(inputs);
    ConnectionManager makeConnection = new ConnectionManager();
    Connection connection = makeConnection.getConnection();
    Client client = new Client(connection);
  }
  @Test
  public void test_makeConnectionClass() throws IOException{
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    Board board = getTestBoard(player1);
    ArrayList<Object> inputs = new ArrayList<Object>();
    inputs.add(board);
    Socket mockSocket = MockTests.setupMockSocket(inputs);
    //Client client = new Client();
    //client.makeConnection(mockSocket);
    ConnectionManager makeConnection = new ConnectionManager();
    makeConnection.makeConnection(mockSocket);
    Connection connection = makeConnection.getConnection();
    Client client = new Client(connection);
   
    try {
      assertTrue(client.getBoard().getRegions().isEmpty());
      client.updateClientBoard();
      //make sure region list same size
      assertEquals(board.getRegions().size(), client.getBoard().getRegions().size());
      //test to make sure region added all regions by name
      Set<String> regionNames = new HashSet<String>();
      for (Region r : board.getRegions()){
        regionNames.add(r.getName());
      }
      for (Region r : client.getBoard().getRegions()){
        assertTrue(regionNames.contains(r.getName()));
      }

    } catch (Exception e) { //TODO -- test exception handling
      e.printStackTrace(System.out);
      return;
    } finally {
      try {
        client.getConnection().closeAll(); //TODO - mocking closing the connection
      } catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }
  }
  @Test
  public void test_updateBoard() throws IOException {
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    Board board = getTestBoard(player1);
    ArrayList<Object> inputs = new ArrayList<Object>();
    inputs.add(board);
    Socket mockSocket = MockTests.setupMockSocket(inputs);
    // Client client = new Client();
    //client.makeConnection(mockSocket);
    ConnectionManager makeConnection = new ConnectionManager();
    makeConnection.makeConnection(mockSocket);
    Connection connection = makeConnection.getConnection();
    Client client = new Client(connection);
   
    try {
      assertTrue(client.getBoard().getRegions().isEmpty());
      client.updateClientBoard();
      //make sure region list same size
      assertEquals(board.getRegions().size(), client.getBoard().getRegions().size());
      //test to make sure region added all regions by name
      Set<String> regionNames = new HashSet<String>();
      for (Region r : board.getRegions()){
        regionNames.add(r.getName());
      }
      for (Region r : client.getBoard().getRegions()){
        assertTrue(regionNames.contains(r.getName()));
      }

    } catch (Exception e) { //TODO -- test exception handling
      e.printStackTrace(System.out);
      return;
    } finally {
      try {
        client.getConnection().closeAll(); //TODO - mocking closing the connection
      } catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }    
  }
  
  private Board getTestBoard(AbstractPlayer player1) {
    Unit unit = new Unit(10);
    Unit adjUnit = new Unit(15);
    Unit adjUnit2 = new Unit(20);
    Region region = new Region(player1, unit);
    region.setName("Earth");
    Region adjRegion = new Region(player1, adjUnit);
    adjRegion.setName("Mars");
    Region adjRegion2 = new Region(player1, adjUnit2);
    adjRegion2.setName("Pluto");
    List<Region> regions = new ArrayList<Region>();
    regions.add(region);
    regions.add(adjRegion);
    regions.add(adjRegion2);
    region.setAdjRegions(regions);
    adjRegion.setAdjRegions(regions);
    //create board, make sure get/set works 
    Board board = new Board(regions);
    return board;
  }

  @Test
  public void test_ClientSimple() throws IOException{
    // Client client = new Client();
    ArrayList<Object> objs = new ArrayList<Object>();
    StringMessage message = new StringMessage("test sending string message");
    objs.add(message);
    Socket mockSocket = MockTests.setupMockSocket(objs);
    //    client.makeConnection("localhost", 12345);
    //client.makeConnection(mockSocket);
    ConnectionManager makeConnection = new ConnectionManager();
    makeConnection.makeConnection(mockSocket);
    Connection connection = makeConnection.getConnection();
    Client client = new Client(connection);
    try {
    
      message = (StringMessage) (client.getConnection().receiveObject());
      System.out.println(message.unpacker());
      assertEquals("test sending string message", message.unpacker());
    
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return;
    } finally {
      try {
        client.getConnection().closeAll();
      } catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }
  }


  class dummyServerSocket implements Runnable{
    public ServerSocket serverSocket;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public boolean spin = true;

    public dummyServerSocket(int port){
      try{
        this.serverSocket = new ServerSocket(port);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }

    public void run(){
      while(socket == null){
        try{
          socket = serverSocket.accept();
          oos = new ObjectOutputStream(socket.getOutputStream());
          ois = new ObjectInputStream(socket.getInputStream());
          oos.close();
          ois.close();
          socket.close();
        }
        catch(Exception e){
          return;
        }
      }
      try{
        Thread.sleep(500);
        serverSocket.close();
        ois.close();
        oos.close();
      }
      catch(Exception e){
        e.printStackTrace();
      }
     }
  }
  
  @Test
  public void test_assorted(){
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput();
    ConnectionManager connect = new ConnectionManager();
    Connection c = connect.getConnection();
    Client inputClient = new Client(ci, td, c);
    assertEquals(td, inputClient.getClientOutput());
    assertEquals(ci, inputClient.getClientInput());
    assertTrue(inputClient.getBoard() != null);
    assertTrue(inputClient.getConnection() != null);
    inputClient.getConnection().closeAll();

    String addr = "127.0.0.1";
    int port = 12346;
    //Client localConnection = new Client();
    ConnectionManager localConnection = new ConnectionManager();
    //makeConnection.makeConnection(mockSocket);
    Connection connection = localConnection.getConnection();
    Client client = new Client(connection);

    try{
      Thread t = new Thread(new dummyServerSocket(port));
      t.start();
      Thread.sleep(5000);
      
      localConnection.makeConnection(addr, port);
      assertEquals(localConnection.getConnection().getSocket().getInetAddress().getHostAddress(), addr);
      System.out.println(localConnection.getConnection().getSocket().getInetAddress().getHostAddress());
      assertEquals(localConnection.getConnection().getSocket().getPort(), port);
      
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
    localConnection.getConnection().closeAll();
    client.getClientInput().close();
    //localConnection.getClientInput().close();
    }
  
//TODO: refactor for new units
  // @Test
  // public void test_createPlacements() throws IOException{
  //   InputStream input = new FileInputStream(new File("src/test/resources/testCreatePlacements.txt"));
  //   TextDisplay td = new TextDisplay();
  //   ConsoleInput ci = new ConsoleInput(input);
  //   Client client = new Client(ci, td);
    
  //      //setup Player
  //   InputStream mockInputStream = mock(InputStream.class); 
  //   OutputStream mockOutputStream = mock(OutputStream.class); 

  //   Socket mockClientSocket1 = mock(Socket.class);
  //   // AbstractPlayer player1 = new HumanPlayer("Player 1", mockClientSocket1);
  //   HumanPlayer player1 = new HumanPlayer("Player 1");
    
  //   Socket mockClientSocket2 = mock(Socket.class);
  //   // AbstractPlayer player2 = new HumanPlayer("Player 2", mockClientSocket2);
  //   HumanPlayer player2 = new HumanPlayer("Player 2");

  //     when(mockClientSocket1.getInputStream()).thenReturn(mockInputStream);
  //   when(mockClientSocket1.getOutputStream()).thenReturn(mockOutputStream);
  //     when(mockClientSocket2.getInputStream()).thenReturn(mockInputStream);
  //   when(mockClientSocket2.getOutputStream()).thenReturn(mockOutputStream);
  //   //set up units/regions/adjancies
  //   Unit unit1 = new Unit(10);
  //   Unit unit2 = new Unit(15); 
  //   Unit unit3 = new Unit(20);

  //   Region region1 = new Region(player1, unit1);
  //   region1.setName("A");
  //   Region region2 = new Region(player2, unit2);
  //   region2.setName("B");
  //   Region region3 = new Region(player1, unit3);
  //   region3.setName("C");

  //   List<Region> allRegions = new ArrayList<Region>();
  //   List<Region> adjRegions1 = new ArrayList<Region>();
  //   List<Region> adjRegions2 = new ArrayList<Region>();
  //   List<Region> adjRegions3 = new ArrayList<Region>();

  //   adjRegions1.add(region2);
  //   adjRegions1.add(region3);
  //   region1.setAdjRegions(adjRegions1);
  //   adjRegions2.add(region1);
  //   adjRegions2.add(region3);
  //   region2.setAdjRegions(adjRegions2);
  //   adjRegions3.add(region1);
  //   adjRegions3.add(region2);
  //   region3.setAdjRegions(adjRegions3);
  //   allRegions.add(region1);
  //   allRegions.add(region2);
  //   allRegions.add(region3);
    
  //   //create board, make sure get/set works 
  //   Board board = new Board(allRegions);
  //   client.setBoard(board);

  //   //Bad input, 4, 20
  //   //Should say player 1, ask for A, say not number, ask for A, ask for C
  //   client.setPlayer(player1);
  //   List<PlacementOrder> out1 = client.createPlacements();
  //   //Empty input, 1.2, .3, 4., 5
  //   //Should say player 2, (ask for B, say not number)*4, ask for B
  //   client.setPlayer(player2);
  //   List<PlacementOrder> out2 = client.createPlacements();

  //   assertEquals(out1.get(0).getUnits().getUnits(), Integer.valueOf(4));
  //   assertEquals(out1.get(0).getDestination().getName(), region1.getName());
  //   assertEquals(out1.get(1).getUnits().getUnits(), Integer.valueOf(20));
  //   assertEquals(out1.get(1).getDestination().getName(), region3.getName());
  //   assertEquals(out2.get(0).getUnits().getUnits(), Integer.valueOf(5));
  //   assertEquals(out2.get(0).getDestination().getName(), region2.getName());

  //   client.getClientInput().close();
  // }

  //TODO: refactor for new units
  //   @Test
  // public void test_createOrders() throws IOException{
  //   InputStream input = new FileInputStream(new File("src/test/resources/testCreateOrders.txt"));
  //   TextDisplay td = new TextDisplay();
  //   ConsoleInput ci = new ConsoleInput(input);
  //   Client client = new Client(ci, td);
    
  //      //setup Player
  //   InputStream mockInputStream = mock(InputStream.class); 
  //   OutputStream mockOutputStream = mock(OutputStream.class); 

  //   Socket mockClientSocket1 = mock(Socket.class);
  //   // AbstractPlayer player1 = new HumanPlayer("Player 1", mockClientSocket1);
  //   HumanPlayer player1 = new HumanPlayer("Player 1");
    
  //   Socket mockClientSocket2 = mock(Socket.class);
  //   // AbstractPlayer player2 = new HumanPlayer("Player 2", mockClientSocket2);
  //   HumanPlayer player2 = new HumanPlayer("Player 2");

  //     when(mockClientSocket1.getInputStream()).thenReturn(mockInputStream);
  //   when(mockClientSocket1.getOutputStream()).thenReturn(mockOutputStream);
  //     when(mockClientSocket2.getInputStream()).thenReturn(mockInputStream);
  //   when(mockClientSocket2.getOutputStream()).thenReturn(mockOutputStream);
  //   //set up units/regions/adjancies
  //   Unit unit1 = new Unit(10);
  //   Unit unit2 = new Unit(15); 
  //   Unit unit3 = new Unit(20);

  //   Region region1 = new Region(player1, unit1);
  //   region1.setName("A");
  //   Region region2 = new Region(player2, unit2);
  //   region2.setName("B");
  //   Region region3 = new Region(player1, unit3);
  //   region3.setName("C");

  //   List<Region> allRegions = new ArrayList<Region>();
  //   List<Region> adjRegions1 = new ArrayList<Region>();
  //   List<Region> adjRegions2 = new ArrayList<Region>();
  //   List<Region> adjRegions3 = new ArrayList<Region>();

  //   adjRegions1.add(region2);
  //   adjRegions1.add(region3);
  //   region1.setAdjRegions(adjRegions1);
  //   adjRegions2.add(region1);
  //   adjRegions2.add(region3);
  //   region2.setAdjRegions(adjRegions2);
  //   adjRegions3.add(region1);
  //   adjRegions3.add(region2);
  //   region3.setAdjRegions(adjRegions3);
  //   allRegions.add(region1);
  //   allRegions.add(region2);
  //   allRegions.add(region3);
    
  //   //create board, make sure get/set works 
  //   Board board = new Board(allRegions);
  //   client.setBoard(board);

  //   client.setPlayer(player1);
  //   SDOrderCreator oc = new SDOrderCreator(client);
  //   List<OrderInterface> out = oc.createOrders();

  //   //Expect following:
  //   //Prompt move type, bad input
  //   //Prompt move type, bad input
  //   //Move order selected
  //   //Bad input region 1
  //   //Bad input region 2
  //   //(Bad integer)*4
  //   //Prompt move type
  //   //Attack order selected
  //   //Bad integer
  //   //Select done

  //   //Move is 5 from A to B
  //   //Attack is 3 from A to C

  //   MoveOrder move = (MoveOrder)(out.get(0));
  //   AttackOrder attack = (AttackOrder)(out.get(1));
    
  //   assertEquals(move.getUnits().getUnits(), Integer.valueOf(5));
  //   assertEquals(move.getSource().getName(), region1.getName());
  //   assertEquals(move.getDestination().getName(), region2.getName());
  //   assertEquals(attack.getUnits().getUnits(), Integer.valueOf(3));
  //   assertEquals(attack.getSource().getName(), region1.getName());
  //   assertEquals(attack.getDestination().getName(), region3.getName());
    

  //   client.getClientInput().close();
  // }

  @Test
  void test_playGame() throws IOException{
    InputStream input = new FileInputStream(new File("src/test/resources/testPlayGame.txt"));
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(input);
    //    Client client = new Client(ci, td);
    ConnectionManager makeConnection = new ConnectionManager();
   
       //setup Player
    InputStream mockInputStream = mock(InputStream.class); 
    OutputStream mockOutputStream = mock(OutputStream.class); 

    Socket mockClientSocket1 = mock(Socket.class);
    HumanPlayer player1 = new HumanPlayer("Player 1");

    Socket mockClientSocket2 = mock(Socket.class);
    HumanPlayer player2 = new HumanPlayer("Player 2");

    HumanPlayer ga = new HumanPlayer("Group A");
    HumanPlayer gb = new HumanPlayer("Group B");
    
    Region region1 = new Region(ga, new Unit(0));
    region1.setName("A");
    Region region2 = new Region(gb, new Unit(0));
    region2.setName("B");
    Region region3 = new Region(gb, new Unit(0));
    region3.setName("C");

    List<Region> allRegions = new ArrayList<Region>();
    List<Region> adjRegions1 = new ArrayList<Region>();
    List<Region> adjRegions2 = new ArrayList<Region>();
    List<Region> adjRegions3 = new ArrayList<Region>();

    adjRegions1.add(region2);
    adjRegions1.add(region3);
    region1.setAdjRegions(adjRegions1);
    adjRegions2.add(region1);
    adjRegions2.add(region3);
    region2.setAdjRegions(adjRegions2);
    adjRegions3.add(region1);
    adjRegions3.add(region2);
    region3.setAdjRegions(adjRegions3);
    allRegions.add(region1);
    allRegions.add(region2);
    allRegions.add(region3);
    
    //create board, make sure get/set works 
    Board board = new Board(allRegions);

    Region region1b = new Region(player2, new Unit(0));
    region1b.setName("A");
    Region region2b = new Region(player1, new Unit(0));
    region2b.setName("B");
    Region region3b = new Region(player1, new Unit(0));
    region3b.setName("C");

    List<Region> allRegions2 = new ArrayList<Region>();
    List<Region> adjRegions1b = new ArrayList<Region>();
    List<Region> adjRegions2b = new ArrayList<Region>();
    List<Region> adjRegions3b = new ArrayList<Region>();

    adjRegions1b.add(region2b);
    adjRegions1b.add(region3b);
    region1b.setAdjRegions(adjRegions1b);
    adjRegions2b.add(region1b);
    adjRegions2b.add(region3b);
    region2b.setAdjRegions(adjRegions2b);
    adjRegions3b.add(region1b);
    adjRegions3b.add(region2b);
    region3b.setAdjRegions(adjRegions3b);
    allRegions2.add(region1b);
    allRegions2.add(region2b);
    allRegions2.add(region3b);

    Board board2 = new Board(allRegions2);

    Region region1c = new Region(player2, new Unit(3));
    region1c.setName("A");
    Region region2c = new Region(player1, new Unit(2));
    region2c.setName("B");
    Region region3c = new Region(player1, new Unit(1));
    region3c.setName("C");

    List<Region> allRegions3 = new ArrayList<Region>();
    List<Region> adjRegions1c = new ArrayList<Region>();
    List<Region> adjRegions2c = new ArrayList<Region>();
    List<Region> adjRegions3c = new ArrayList<Region>();

    adjRegions1c.add(region2c);
    adjRegions1c.add(region3c);
    region1c.setAdjRegions(adjRegions1c);
    adjRegions2c.add(region1c);
    adjRegions2c.add(region3c);
    region2c.setAdjRegions(adjRegions2c);
    adjRegions3c.add(region1c);
    adjRegions3c.add(region2c);
    region3c.setAdjRegions(adjRegions3c);
    allRegions3.add(region1c);
    allRegions3.add(region2c);
    allRegions3.add(region3c);

    Board board3 = new Board(allRegions3);

    ArrayList<Object> objs = new ArrayList<Object>();
    /* DEPRECATED LOGIN FLOW, NOW IN CLIENT LOGIN
    //First connect success
    objs.add(new StringMessage("Success: connected"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Fail: invalid user/password"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Fail: user already exists"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Success: login"));
    //Send games
    objs.add(new StringMessage("games...."));
    //Send fail
    objs.add(new StringMessage("Fail: bad gameid"));
    //Send games
    objs.add(new StringMessage("games...."));
    //Send success
    objs.add(new StringMessage("Success: good game"));
    //Send firstturn
    objs.add(new ConfirmationMessage(true));
    */
    
    //Send player
    objs.add(player1);
    //Enter chooseRegions
    //Send board
    objs.add(board);
    //Client responds with groupName (assume bad)
    objs.add(new StringMessage("Fail: bad group"));
    //Retry board
    objs.add(board);
    //Client responds with groupName (assume good)
    //Give A to player2, B/C to player1
    objs.add(new StringMessage("Success: good group"));
    //Send board
    objs.add(board2);
    //Client does placements (1 on B, 3 on C)
    //Assume response bad for some reason
    objs.add(new StringMessage("Fail: bad placement"));
    //Client does placements (2 on B, 1 on C), assume 3 on A
    objs.add(board2);
    objs.add(new StringMessage("Success: good placement"));
    //Turn message
    objs.add(new StringMessage("Turn 1: placements"));
    //Client enters loop --> send continue
    objs.add(new StringMessage("Continue"));
    //Send alive
    objs.add(new ConfirmationMessage(true));
    //Enter createOrders loop
    //Send board
    objs.add(board3);
    //Enter createOrders
    //Assume bad
    objs.add(new StringMessage("Fail: bad orders"));
    //Assume good
    objs.add(board3);
    objs.add(new StringMessage("Success: good orders"));
    //Turn message
    objs.add(new StringMessage("Turn 2: first orders"));
    //Assume player somehow died
    objs.add(new StringMessage("Continue"));
    objs.add(new ConfirmationMessage(false));
    //Now prompts user --> long input, wrong input, Y
    objs.add(board3);
    //Shouldn't prompt for moves this time...
    objs.add(new StringMessage("Success: spectate"));
    //Now player 2 wins
    //Turn message
    objs.add(new StringMessage("Turn 3: final"));
    objs.add(new StringMessage("Player 2 wins or something"));
    //Game ends

    
    Socket mockSocket = MockTests.setupMockSocket(objs);
        
    makeConnection.makeConnection(mockSocket);
    Connection connection = makeConnection.getConnection();
    Client client = new Client(ci,td,connection, true);
    client.playGame();

  }

  
  @Test
  void test_playGameNoSpectate() throws IOException{
    InputStream input = new FileInputStream(new File("src/test/resources/testPlayGameNoSpectate.txt"));
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(input);
    //    Client client = new Client(ci, td);
    ConnectionManager makeConnection = new ConnectionManager();
   
       //setup Player
    InputStream mockInputStream = mock(InputStream.class); 
    OutputStream mockOutputStream = mock(OutputStream.class); 

    Socket mockClientSocket1 = mock(Socket.class);
    HumanPlayer player1 = new HumanPlayer("Player 1");

    Socket mockClientSocket2 = mock(Socket.class);
    HumanPlayer player2 = new HumanPlayer("Player 2");

    HumanPlayer ga = new HumanPlayer("Group A");
    HumanPlayer gb = new HumanPlayer("Group B");
    
    Region region1 = new Region(ga, new Unit(0));
    region1.setName("A");
    Region region2 = new Region(gb, new Unit(0));
    region2.setName("B");
    Region region3 = new Region(gb, new Unit(0));
    region3.setName("C");

    List<Region> allRegions = new ArrayList<Region>();
    List<Region> adjRegions1 = new ArrayList<Region>();
    List<Region> adjRegions2 = new ArrayList<Region>();
    List<Region> adjRegions3 = new ArrayList<Region>();

    adjRegions1.add(region2);
    adjRegions1.add(region3);
    region1.setAdjRegions(adjRegions1);
    adjRegions2.add(region1);
    adjRegions2.add(region3);
    region2.setAdjRegions(adjRegions2);
    adjRegions3.add(region1);
    adjRegions3.add(region2);
    region3.setAdjRegions(adjRegions3);
    allRegions.add(region1);
    allRegions.add(region2);
    allRegions.add(region3);
    
    //create board, make sure get/set works 
    Board board = new Board(allRegions);

    Region region1b = new Region(player2, new Unit(0));
    region1b.setName("A");
    Region region2b = new Region(player1, new Unit(0));
    region2b.setName("B");
    Region region3b = new Region(player1, new Unit(0));
    region3b.setName("C");

    List<Region> allRegions2 = new ArrayList<Region>();
    List<Region> adjRegions1b = new ArrayList<Region>();
    List<Region> adjRegions2b = new ArrayList<Region>();
    List<Region> adjRegions3b = new ArrayList<Region>();

    adjRegions1b.add(region2b);
    adjRegions1b.add(region3b);
    region1b.setAdjRegions(adjRegions1b);
    adjRegions2b.add(region1b);
    adjRegions2b.add(region3b);
    region2b.setAdjRegions(adjRegions2b);
    adjRegions3b.add(region1b);
    adjRegions3b.add(region2b);
    region3b.setAdjRegions(adjRegions3b);
    allRegions2.add(region1b);
    allRegions2.add(region2b);
    allRegions2.add(region3b);

    Board board2 = new Board(allRegions2);

    Region region1c = new Region(player2, new Unit(3));
    region1c.setName("A");
    Region region2c = new Region(player1, new Unit(2));
    region2c.setName("B");
    Region region3c = new Region(player1, new Unit(1));
    region3c.setName("C");

    List<Region> allRegions3 = new ArrayList<Region>();
    List<Region> adjRegions1c = new ArrayList<Region>();
    List<Region> adjRegions2c = new ArrayList<Region>();
    List<Region> adjRegions3c = new ArrayList<Region>();

    adjRegions1c.add(region2c);
    adjRegions1c.add(region3c);
    region1c.setAdjRegions(adjRegions1c);
    adjRegions2c.add(region1c);
    adjRegions2c.add(region3c);
    region2c.setAdjRegions(adjRegions2c);
    adjRegions3c.add(region1c);
    adjRegions3c.add(region2c);
    region3c.setAdjRegions(adjRegions3c);
    allRegions3.add(region1c);
    allRegions3.add(region2c);
    allRegions3.add(region3c);

    Board board3 = new Board(allRegions3);

    ArrayList<Object> objs = new ArrayList<Object>();
    /* DEPRECATED LOGIN FLOW, NOW IN CLIENT LOGIN
    //First connect success
    objs.add(new StringMessage("Success: connected"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Fail: invalid user/password"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Fail: user already exists"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Success: login"));
    //Send games
    objs.add(new StringMessage("games...."));
    //Send fail
    objs.add(new StringMessage("Fail: bad gameid"));
    //Send games
    objs.add(new StringMessage("games...."));
    //Send success
    objs.add(new StringMessage("Success: good game"));
    //Send firstturn
    objs.add(new ConfirmationMessage(true));
    */
    
    //Send player
    objs.add(player1);
    //Enter chooseRegions
    //Send board
    objs.add(board);
    //Client responds with groupName (assume bad)
    objs.add(new StringMessage("Fail: bad group"));
    //Retry board
    objs.add(board);
    //Client responds with groupName (assume good)
    //Give A to player2, B/C to player1
    objs.add(new StringMessage("Success: good group"));
    //Send board
    objs.add(board2);
    //Client does placements (1 on B, 3 on C)
    //Assume response bad for some reason
    objs.add(new StringMessage("Fail: bad placement"));
    //Client does placements (2 on B, 1 on C), assume 3 on A
    objs.add(board2);
    objs.add(new StringMessage("Success: good placement"));
    //Turn message
    objs.add(new StringMessage("Turn 1: placements"));
    //Client enters loop --> send continue
    objs.add(new StringMessage("Continue"));
    //Send alive
    objs.add(new ConfirmationMessage(true));
    //Enter createOrders loop
    //Send board
    objs.add(board3);
    //Enter createOrders
    //Assume bad
    objs.add(new StringMessage("Fail: bad orders"));
    //Assume good
    objs.add(board3);
    objs.add(new StringMessage("Success: good orders"));
    //Turn message
    objs.add(new StringMessage("Turn 2: first orders"));
    //Assume player somehow died
    objs.add(new StringMessage("Continue"));
    objs.add(new ConfirmationMessage(false));
    //Now prompts user --> long input, wrong input, N
    //Client closes

    
    Socket mockSocket = MockTests.setupMockSocket(objs);
        
    makeConnection.makeConnection(mockSocket);
    Connection connection = makeConnection.getConnection();
    Client client = new Client(ci,td,connection, true);
    client.playGame();

  }


  
  
  @Test
  void test_playGameLongGroups() throws IOException{
    Scanner mockSlowGroup = mock(Scanner.class);
    when(mockSlowGroup.nextLine()).thenAnswer(new Answer(){
        @Override
        public Object answer(InvocationOnMock invocation){
          try{
            Thread.sleep(1000);
          }
          catch(Exception e){
            e.printStackTrace();
          }
         return "Group B";
        }
      });
      
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(mockSlowGroup);
    //    Client client = new Client(ci, td);
    ConnectionManager makeConnection = new ConnectionManager();
   
       //setup Player
    InputStream mockInputStream = mock(InputStream.class); 
    OutputStream mockOutputStream = mock(OutputStream.class); 

    Socket mockClientSocket1 = mock(Socket.class);
    HumanPlayer player1 = new HumanPlayer("Player 1");

    Socket mockClientSocket2 = mock(Socket.class);
    HumanPlayer player2 = new HumanPlayer("Player 2");

    HumanPlayer ga = new HumanPlayer("Group A");
    HumanPlayer gb = new HumanPlayer("Group B");
    
    Region region1 = new Region(ga, new Unit(0));
    region1.setName("A");
    Region region2 = new Region(gb, new Unit(0));
    region2.setName("B");
    Region region3 = new Region(gb, new Unit(0));
    region3.setName("C");

    List<Region> allRegions = new ArrayList<Region>();
    List<Region> adjRegions1 = new ArrayList<Region>();
    List<Region> adjRegions2 = new ArrayList<Region>();
    List<Region> adjRegions3 = new ArrayList<Region>();

    adjRegions1.add(region2);
    adjRegions1.add(region3);
    region1.setAdjRegions(adjRegions1);
    adjRegions2.add(region1);
    adjRegions2.add(region3);
    region2.setAdjRegions(adjRegions2);
    adjRegions3.add(region1);
    adjRegions3.add(region2);
    region3.setAdjRegions(adjRegions3);
    allRegions.add(region1);
    allRegions.add(region2);
    allRegions.add(region3);
    
    //create board, make sure get/set works 
    Board board = new Board(allRegions);

    Region region1b = new Region(player2, new Unit(0));
    region1b.setName("A");
    Region region2b = new Region(player1, new Unit(0));
    region2b.setName("B");
    Region region3b = new Region(player1, new Unit(0));
    region3b.setName("C");

    List<Region> allRegions2 = new ArrayList<Region>();
    List<Region> adjRegions1b = new ArrayList<Region>();
    List<Region> adjRegions2b = new ArrayList<Region>();
    List<Region> adjRegions3b = new ArrayList<Region>();

    adjRegions1b.add(region2b);
    adjRegions1b.add(region3b);
    region1b.setAdjRegions(adjRegions1b);
    adjRegions2b.add(region1b);
    adjRegions2b.add(region3b);
    region2b.setAdjRegions(adjRegions2b);
    adjRegions3b.add(region1b);
    adjRegions3b.add(region2b);
    region3b.setAdjRegions(adjRegions3b);
    allRegions2.add(region1b);
    allRegions2.add(region2b);
    allRegions2.add(region3b);

    Board board2 = new Board(allRegions2);

    Region region1c = new Region(player2, new Unit(3));
    region1c.setName("A");
    Region region2c = new Region(player1, new Unit(2));
    region2c.setName("B");
    Region region3c = new Region(player1, new Unit(1));
    region3c.setName("C");

    List<Region> allRegions3 = new ArrayList<Region>();
    List<Region> adjRegions1c = new ArrayList<Region>();
    List<Region> adjRegions2c = new ArrayList<Region>();
    List<Region> adjRegions3c = new ArrayList<Region>();

    adjRegions1c.add(region2c);
    adjRegions1c.add(region3c);
    region1c.setAdjRegions(adjRegions1c);
    adjRegions2c.add(region1c);
    adjRegions2c.add(region3c);
    region2c.setAdjRegions(adjRegions2c);
    adjRegions3c.add(region1c);
    adjRegions3c.add(region2c);
    region3c.setAdjRegions(adjRegions3c);
    allRegions3.add(region1c);
    allRegions3.add(region2c);
    allRegions3.add(region3c);

    Board board3 = new Board(allRegions3);

    ArrayList<Object> objs = new ArrayList<Object>();
    
    
    //Send player
    objs.add(player1);
    //Enter chooseRegions
    //Send board
    objs.add(board);

    
    Socket mockSocket = MockTests.setupMockSocket(objs);
        
    makeConnection.makeConnection(mockSocket);
    Connection connection = makeConnection.getConnection();
    Client client = new Client(ci,td,connection, true);
    //Set to a half a second
    client.setTURN_WAIT_MINUTES(0.5/60);
    client.playGame();

  }

  
  @Test
  void test_playGameLongPlacements() throws IOException{
    Scanner mockSlow = mock(Scanner.class);
    when(mockSlow.nextLine()).thenAnswer(new Answer(){
        private List<String> vals = Arrays.asList("Group B", "4", "2");
        private int count = 0;
        
        public Object answer(InvocationOnMock invocation){
          if(count == 2){
            try{
              Thread.sleep(1000);
            }
            catch(Exception e){
              e.printStackTrace();
            }
          }
          return vals.get(count++);
        }
      });
      
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(mockSlow);
    //    Client client = new Client(ci, td);
    ConnectionManager makeConnection = new ConnectionManager();
   
       //setup Player
    InputStream mockInputStream = mock(InputStream.class); 
    OutputStream mockOutputStream = mock(OutputStream.class); 

    Socket mockClientSocket1 = mock(Socket.class);
    HumanPlayer player1 = new HumanPlayer("Player 1");

    Socket mockClientSocket2 = mock(Socket.class);
    HumanPlayer player2 = new HumanPlayer("Player 2");

    HumanPlayer ga = new HumanPlayer("Group A");
    HumanPlayer gb = new HumanPlayer("Group B");
    
    Region region1 = new Region(ga, new Unit(0));
    region1.setName("A");
    Region region2 = new Region(gb, new Unit(0));
    region2.setName("B");
    Region region3 = new Region(gb, new Unit(0));
    region3.setName("C");

    List<Region> allRegions = new ArrayList<Region>();
    List<Region> adjRegions1 = new ArrayList<Region>();
    List<Region> adjRegions2 = new ArrayList<Region>();
    List<Region> adjRegions3 = new ArrayList<Region>();

    adjRegions1.add(region2);
    adjRegions1.add(region3);
    region1.setAdjRegions(adjRegions1);
    adjRegions2.add(region1);
    adjRegions2.add(region3);
    region2.setAdjRegions(adjRegions2);
    adjRegions3.add(region1);
    adjRegions3.add(region2);
    region3.setAdjRegions(adjRegions3);
    allRegions.add(region1);
    allRegions.add(region2);
    allRegions.add(region3);
    
    //create board, make sure get/set works 
    Board board = new Board(allRegions);

    Region region1b = new Region(player2, new Unit(0));
    region1b.setName("A");
    Region region2b = new Region(player1, new Unit(0));
    region2b.setName("B");
    Region region3b = new Region(player1, new Unit(0));
    region3b.setName("C");

    List<Region> allRegions2 = new ArrayList<Region>();
    List<Region> adjRegions1b = new ArrayList<Region>();
    List<Region> adjRegions2b = new ArrayList<Region>();
    List<Region> adjRegions3b = new ArrayList<Region>();

    adjRegions1b.add(region2b);
    adjRegions1b.add(region3b);
    region1b.setAdjRegions(adjRegions1b);
    adjRegions2b.add(region1b);
    adjRegions2b.add(region3b);
    region2b.setAdjRegions(adjRegions2b);
    adjRegions3b.add(region1b);
    adjRegions3b.add(region2b);
    region3b.setAdjRegions(adjRegions3b);
    allRegions2.add(region1b);
    allRegions2.add(region2b);
    allRegions2.add(region3b);

    Board board2 = new Board(allRegions2);

    
    ArrayList<Object> objs = new ArrayList<Object>();
    
    //Send player
    objs.add(player1);
    //Enter chooseRegions
    //Send board
    objs.add(board);
    //Client responds with groupName (assume good)
    //Give A to player2, B/C to player1
    objs.add(new StringMessage("Success: good group"));
    //Send board
    objs.add(board2);

    
    Socket mockSocket = MockTests.setupMockSocket(objs);
        
    makeConnection.makeConnection(mockSocket);
    Connection connection = makeConnection.getConnection();
    Client client = new Client(ci,td,connection, true);
    //Set to half a second
    client.setTURN_WAIT_MINUTES(.5/60);
    client.playGame();
  }

  @Test
  void test_playGameLongOrder() throws IOException{
    Scanner mockSlow = mock(Scanner.class);
    when(mockSlow.nextLine()).thenAnswer(new Answer(){
        private List<String> vals = Arrays.asList("Group B", "4", "2", "D");
        private int count = 0;
        
        public Object answer(InvocationOnMock invocation){
          if(count == 3){
            try{
              Thread.sleep(1000);
            }
            catch(Exception e){
              e.printStackTrace();
            }
          }
          return vals.get(count++);
        }
      });
      
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(mockSlow);
    //    Client client = new Client(ci, td);
    ConnectionManager makeConnection = new ConnectionManager();
      InputStream mockInputStream = mock(InputStream.class); 
    OutputStream mockOutputStream = mock(OutputStream.class); 

    Socket mockClientSocket1 = mock(Socket.class);
    HumanPlayer player1 = new HumanPlayer("Player 1");

    Socket mockClientSocket2 = mock(Socket.class);
    HumanPlayer player2 = new HumanPlayer("Player 2");

    HumanPlayer ga = new HumanPlayer("Group A");
    HumanPlayer gb = new HumanPlayer("Group B");
    
    Region region1 = new Region(ga, new Unit(0));
    region1.setName("A");
    Region region2 = new Region(gb, new Unit(0));
    region2.setName("B");
    Region region3 = new Region(gb, new Unit(0));
    region3.setName("C");

    List<Region> allRegions = new ArrayList<Region>();
    List<Region> adjRegions1 = new ArrayList<Region>();
    List<Region> adjRegions2 = new ArrayList<Region>();
    List<Region> adjRegions3 = new ArrayList<Region>();

    adjRegions1.add(region2);
    adjRegions1.add(region3);
    region1.setAdjRegions(adjRegions1);
    adjRegions2.add(region1);
    adjRegions2.add(region3);
    region2.setAdjRegions(adjRegions2);
    adjRegions3.add(region1);
    adjRegions3.add(region2);
    region3.setAdjRegions(adjRegions3);
    allRegions.add(region1);
    allRegions.add(region2);
    allRegions.add(region3);
    
    //create board, make sure get/set works 
    Board board = new Board(allRegions);

    Region region1b = new Region(player2, new Unit(0));
    region1b.setName("A");
    Region region2b = new Region(player1, new Unit(0));
    region2b.setName("B");
    Region region3b = new Region(player1, new Unit(0));
    region3b.setName("C");

    List<Region> allRegions2 = new ArrayList<Region>();
    List<Region> adjRegions1b = new ArrayList<Region>();
    List<Region> adjRegions2b = new ArrayList<Region>();
    List<Region> adjRegions3b = new ArrayList<Region>();

    adjRegions1b.add(region2b);
    adjRegions1b.add(region3b);
    region1b.setAdjRegions(adjRegions1b);
    adjRegions2b.add(region1b);
    adjRegions2b.add(region3b);
    region2b.setAdjRegions(adjRegions2b);
    adjRegions3b.add(region1b);
    adjRegions3b.add(region2b);
    region3b.setAdjRegions(adjRegions3b);
    allRegions2.add(region1b);
    allRegions2.add(region2b);
    allRegions2.add(region3b);

    Board board2 = new Board(allRegions2);

    Region region1c = new Region(player2, new Unit(3));
    region1c.setName("A");
    Region region2c = new Region(player1, new Unit(2));
    region2c.setName("B");
    Region region3c = new Region(player1, new Unit(1));
    region3c.setName("C");

    List<Region> allRegions3 = new ArrayList<Region>();
    List<Region> adjRegions1c = new ArrayList<Region>();
    List<Region> adjRegions2c = new ArrayList<Region>();
    List<Region> adjRegions3c = new ArrayList<Region>();

    adjRegions1c.add(region2c);
    adjRegions1c.add(region3c);
    region1c.setAdjRegions(adjRegions1c);
    adjRegions2c.add(region1c);
    adjRegions2c.add(region3c);
    region2c.setAdjRegions(adjRegions2c);
    adjRegions3c.add(region1c);
    adjRegions3c.add(region2c);
    region3c.setAdjRegions(adjRegions3c);
    allRegions3.add(region1c);
    allRegions3.add(region2c);
    allRegions3.add(region3c);

    Board board3 = new Board(allRegions3);

    ArrayList<Object> objs = new ArrayList<Object>();
    /* DEPRECATED LOGIN FLOW, NOW IN CLIENT LOGIN
    //First connect success
    objs.add(new StringMessage("Success: connected"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Fail: invalid user/password"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Fail: user already exists"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Success: login"));
    //Send games
    objs.add(new StringMessage("games...."));
    //Send fail
    objs.add(new StringMessage("Fail: bad gameid"));
    //Send games
    objs.add(new StringMessage("games...."));
    //Send success
    objs.add(new StringMessage("Success: good game"));
    //Send firstturn
    objs.add(new ConfirmationMessage(true));
    */
    
    //Send player
    objs.add(player1);
    //Enter chooseRegions
    //Send board
    objs.add(board);
    //Client responds with groupName (assume good)
    //Give A to player2, B/C to player1
    objs.add(new StringMessage("Success: good group"));
    //Send board
    objs.add(board2);
    objs.add(new StringMessage("Success: good placement"));
    //Turn message
    objs.add(new StringMessage("Turn 1: placements"));
    //Client enters loop --> send continue
    objs.add(new StringMessage("Continue"));
    //Send alive
    objs.add(new ConfirmationMessage(true));
    //Enter createOrders loop
    //Send board
    objs.add(board3);
    //Enter createOrders

    
    Socket mockSocket = MockTests.setupMockSocket(objs);
        
    makeConnection.makeConnection(mockSocket);
    Connection connection = makeConnection.getConnection();
    Client client = new Client(ci,td,connection, true);
    //Set to half a second
    client.setTURN_WAIT_MINUTES(.5/60);
    client.playGame();
  }

  class DummyServerSocket implements Runnable{
    public ServerSocket serverSocket;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public boolean spin = true;

    public DummyServerSocket(int port){
      try{
        this.serverSocket = new ServerSocket(port);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }

    public void run(){
      while(socket == null){
        try{
          socket = serverSocket.accept();
          oos = new ObjectOutputStream(socket.getOutputStream());
          ois = new ObjectInputStream(socket.getInputStream());
        }
        catch(Exception e){
          return;
        }
      }
      try{
        Thread.sleep(500);
        serverSocket.close();
        socket.close();
        ois.close();
        oos.close();
      }
      catch(Exception e){
        e.printStackTrace();
      }
     }
  }

  
  
  @Test
  void test_startDC() throws IOException{
    int port = 12350;
    try{
      Thread t = new Thread(new DummyServerSocket(port));
      t.start();
      Thread.sleep(5000);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput();
    //    Client client = new Client(ci, td);
    ConnectionManager makeConnection = new ConnectionManager("localhost", port);
    makeConnection.run();
    
    Connection connection = makeConnection.getConnection();
    Client client = new Client(ci,td,connection, true);
    try{
      Thread.sleep(1000);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    //Set to half a second
    client.setTURN_WAIT_MINUTES(.5/60);
    client.setSTART_WAIT_MINUTES(.5/60);
    client.setLOGIN_WAIT_MINUTES(.5/60);
    System.out.println("Expect SocketClosed or EOF");
    client.run();
  }

  
}

