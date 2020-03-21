package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.*;
import java.io.*;
import java.net.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
  public void test_timeout(){
    String addr = "127.0.0.1";
    int port = 12345;
    Client localConnection = new Client();
    try {
        Thread t = new Thread(new dummyServerSocket(port));
        t.start();
        for (int i = 0; i < 5; i++) {
          Thread.sleep(5000);
          localConnection.makeConnection(addr, port);
          Connection connection = localConnection.getConnection();
          System.out.println("Client connected");
          //ObjectOutputStream outputStream = null;
          ObjectInputStream inputStream = null;
          // Socket socket = connection.getSocket();
          ObjectOutputStream outputStream = connection.getOutputStream();
          System.out.println("Sending request to socket server");
          if (i == 0) {
            outputStream.writeObject("this is a test");
          } if (i == 1){
            Thread.sleep((int)3.5*60*1000);
            outputStream.writeObject("second test, this shouldn't be happening because of timeout");
          }
          else {
            outputStream.writeObject("more than one");
          }
           }
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
