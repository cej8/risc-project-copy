package edu.duke.ece651.risc.client;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.ConfirmationMessage;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.StringMessage;
import edu.duke.ece651.risc.shared.Unit;

public class TimeOutTest {
  /* @Test
  public void test_createPlacementsTimeout() throws IOException{
    InputStream input = new FileInputStream(new File("src/test/resources/testCreatePlacements.txt"));
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(input);
    Client client = new Client(ci, td);
    // TESTING TIMEOUT??
    int timeout = (int) Constants.START_WAIT_MINUTES;
    //  client.setSocketTimeout(timeout*60*1000);
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    AbstractPlayer player2 = new HumanPlayer("Player 2");
    Board board = getTestBoardTwo(player1, player2);
    client.setBoard(board);
    try {
      System.out.println("Timeout started");
      Thread.sleep((timeout+1)*1000*60);
      System.out.println("Timeout ended");
    } catch (InterruptedException ex){
      Thread.currentThread().interrupt();
    }
    List<PlacementOrder> client.createPlacements();
    System.out.println("Placements created");
  }*/
  @Test
  void test_timeout() throws IOException {
    String addr = "127.0.0.1";
    int port = 12345;
    InputStream input = new FileInputStream(new File("src/test/resources/testPlayGame.txt"));
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(input);
    Client localConnection = new Client(ci,td);
    // setup player
    HumanPlayer player1 = new HumanPlayer("Player 1");
    HumanPlayer player2 = new HumanPlayer("Player 2");
    // For no owner initial regions
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
    //Assume player somehow died
    objs.add(new StringMessage("Continue"));
    objs.add(new ConfirmationMessage(false));
    //Now prompts user --> long input, wrong input, Y
    objs.add(board3);
    //Shouldn't prompt for moves this time...
    objs.add(new StringMessage("Success: spectate"));
    //Now player 2 wins
    objs.add(new StringMessage("Player 2 wins or something"));
    //Game ends

    // send objects one at a time (objs)
    try {
        Thread t = new Thread(new dummyServerSocket(port));
        t.start();
        for (int i = 0; i < objs.size(); i++) {
          Thread.sleep(5000);
          localConnection.makeConnection(addr, port);
          Connection connection = localConnection.getConnection();
          System.out.println("Client connected");
          ObjectOutputStream outputStream = connection.getOutputStream();
          ObjectInputStream inputStream = connection.getInputStream();
          String message = null;
          System.out.println("Sending request to socket server");
          if (i == 0) {
            outputStream.writeObject("Added Player");
            // message = (String) inputStream.readObject();
            //System.out.println("Message: " + message);
          } if (i == 1){
            outputStream.writeObject("Sent board");
          } if (i == 2){
            outputStream.writeObject("Fail: bad group");
          }
          if(i == 3){
            // outputStream.writeObject("Sent board");
            Thread.sleep((int)3.5*60*1000);
            // message = (String) inputStream.readObject();
            //System.out.println("Message: " + message);
            outputStream.writeObject("This shouldn't be happening because of timeout");
            }
          else {
            outputStream.writeObject("more than one");
            // message = (String) inputStream.readObject();
            //System.out.println("Message: " + message);
          }
           }
           System.out.println("Thread about to stop");
        t.stop();
    } catch(Exception e){
    }
    
  }
  class dummyServerSocket implements Runnable{
    public ServerSocket serverSocket;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public Client localConnection;

    public dummyServerSocket(int port){
      try { 
        this.serverSocket = new ServerSocket(port);
        System.out.println("Waiting for connection...");
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    public void run(){
      while(socket == null){
        try{
          System.out.println("Waiting for the client request");
          socket = serverSocket.accept();
          oos = new ObjectOutputStream(socket.getOutputStream());
          ois = new ObjectInputStream(socket.getInputStream());
          String message = (String) ois.readObject();
          System.out.println("Message recieved: " + message);
          oos.writeObject("Hi Client " + message);
          ois.close();
          oos.close();
          socket.close();
          socket = null;
        }
        catch(Exception e){
          e.printStackTrace();
          System.out.println(e.toString());
          return;
        }
      }
        System.out.println("Shutting down socket server");
        //serverSocket.close();
    }
  }
}
