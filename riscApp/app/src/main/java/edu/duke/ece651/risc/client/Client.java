package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;
import org.mindrot.jbcrypt.*;

public class Client extends Thread implements ClientInterface {
  private Connection connection;
  private Board board;
  private boolean isPlaying = true;
  private ClientInputInterface clientInput;
  private ClientOutputInterface clientOutput;
  private HumanPlayer player;
  private String address;
  private int port;

  private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
  private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES+.1;
  private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;

  private boolean firstCall = true;

  public Client() {
    clientInput = new ConsoleInput();
    clientOutput = new TextDisplay();
    board = new Board();
    connection = new Connection();
  }
  // for testing
  public Client(Connection connection){
    this();
    this.connection = connection;
  }
  // constructor for abstracted out makeConnection class 
  public Client(ClientInputInterface clientInput, ClientOutputInterface clientOutput,Connection connection) {
    board = new Board();
    this.clientInput = clientInput;
    this.clientOutput = clientOutput;
    this.connection = connection;
    this.firstCall = true;
  }
  // constructor for abstracted out makeConnection class 
  public Client(ClientInputInterface clientInput, ClientOutputInterface clientOutput,Connection connection, boolean firstCall) {
    board = new Board();
    this.clientInput = clientInput;
    this.clientOutput = clientOutput;
    this.connection = connection;
    this.firstCall = firstCall;
  }
  
  public void setTURN_WAIT_MINUTES(double TURN_WAIT_MINUTES){
    this.TURN_WAIT_MINUTES = TURN_WAIT_MINUTES;
  }
  public void setSTART_WAIT_MINUTES(double START_WAIT_MINUTES){
    this.START_WAIT_MINUTES = START_WAIT_MINUTES;
  }
  public void setLOGIN_WAIT_MINUTES(double LOGIN_WAIT_MINUTES){
    this.LOGIN_WAIT_MINUTES = LOGIN_WAIT_MINUTES;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }

  public Connection getConnection() {
    return connection;
  }

  public ClientInputInterface getClientInput() {
    return clientInput;
  }

  public void setClientInput(ClientInputInterface clientInput) {
    this.clientInput.close();
    this.clientInput = clientInput;
  }

  public ClientOutputInterface getClientOutput() {
    return clientOutput;
  }

  public void setPlayer(HumanPlayer player) {
    this.player = player;
  }

  public HumanPlayer getPlayer() {
    return this.player;
  }
  
  public void setSocketTimeout(int timeout) throws SocketException {
    connection.getSocket().setSoTimeout(timeout);
  }
  
  public boolean timeOut(long startTime, long maxTime){
    // If too long --> kill player (prevent trying to write to closed pipe)
    //System.out.println(System.currentTimeMillis() + " - " + startTime + " > " + maxTime);
    if (System.currentTimeMillis() - startTime > maxTime) {
      clientOutput.displayString("Player took too long, killing connection");
      connection.closeAll();
      clientInput.close();
      return true;
    }
    return false;
  }

  public void updateClientBoard() {
    Board masterBoard = null;
    try {
      // masterBoard = (Board) fromServer.readObject();
      masterBoard = (Board) connection.getInputStream().readObject();
      this.setBoard(masterBoard);
      this.board.setRegions(masterBoard.getRegions());
    } catch (IOException e) {
      System.out.println("IOException is caught");
    } catch (ClassNotFoundException e) {
      System.out.println("ClassNotFoundException is caught");
    }
  }

  public boolean chooseRegions() {
    
    //Initial to -1 for timers, don't set until turn actually starts
    long startTime = -1;
    long maxTime = -1;
      
    try {
      // Set timeout to constant, wait this long for game start
      // This will block on FIRST board = ...
      connection.getSocket().setSoTimeout((int) (START_WAIT_MINUTES * 60 * 1000));
      while (true) {
        // Game starts with board message
        board = (Board) (connection.receiveObject());
        // Return timeout to smaller value
        connection.getSocket().setSoTimeout((int) (TURN_WAIT_MINUTES * 60 * 1000));

        //Set max/start first time board received (start of turn)
        if(maxTime == -1){
          maxTime = (long) (connection.getSocket().getSoTimeout());
          //Catch case for issues in testing, should never really happen
          if (maxTime == 0) {
            maxTime = (long) (TURN_WAIT_MINUTES * 60 * 1000);
          }
        }
        if(startTime == -1){
          startTime = System.currentTimeMillis();
        }
        
        // Output board
        clientOutput.displayBoard(board);
        // Print prompt and get group name
        clientOutput.displayString("Please select a starting group by typing in a group name (i.e. 'Group A')");
        String groupName = clientInput.readInput();
        if(timeOut(startTime, maxTime)) { return false; }
        connection.sendObject(new StringMessage(groupName));

        // Wait for response
        StringMessage responseMessage = (StringMessage) (connection.receiveObject());
        String response = responseMessage.unpacker();
        clientOutput.displayString(response);
        if (response.matches("^Fail:.*$")) {
          continue;
        }
        if (response.matches("^Success:.*$")) {
          break;
        }
      }
      while (true) {
        // Server then sends board again
        board = (Board) (connection.receiveObject());

        // Display and move into placements
        clientOutput.displayBoard(board);
        OrderCreator placement = OrderFactoryProducer.getOrderCreator("P", this);
        if (placement == null) {
          continue;
        }
        List<OrderInterface> placementOrders = new ArrayList<OrderInterface>();
        placement.addToOrderList(placementOrders);
        if(timeOut(startTime, maxTime)) { return false; }
        connection.sendObject(placementOrders);

        // Wait for response
        StringMessage responseMessage = (StringMessage) (connection.receiveObject());
        String response = responseMessage.unpacker();
        clientOutput.displayString(response);
        if (response.matches("^Fail:.*$")) {
          continue;
        }
        if (response.matches("^Success:.*$")) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      connection.closeAll();
      return false;
    }

    return true;
  }

  public String receiveAndDisplayString() throws IOException, ClassNotFoundException{
    StringMessage message = (StringMessage) (connection.receiveObject());
    String str = message.unpacker();
    clientOutput.displayString(str);
    return str;
  }

  //Helper method to ask YN and send back ConfirmationMessage
  public boolean queryYNAndRespond(String query) throws IOException{
    while(true){
      // Request input
      clientOutput.displayString(query);
      String spectateResponse = clientInput.readInput();

      spectateResponse = spectateResponse.toUpperCase();
      // If valid then do work
      if (spectateResponse.length() == 1) {
        if (spectateResponse.charAt(0) == 'Y') {
          connection.sendObject(new ConfirmationMessage(true));
          return true;
        } else if (spectateResponse.charAt(0) == 'N') {
          connection.sendObject(new ConfirmationMessage(false));
          return false;
        }
      }
      // Otherwise repeat
      clientOutput.displayString("Invalid input.");
    }
  }
  
 
  public void playGame() {
    try {
      //Set timeout to START_WAIT plus a little buffer
      setSocketTimeout((int)(60*START_WAIT_MINUTES*1000));
      player = (HumanPlayer) (connection.receiveObject());
      clientOutput.displayString("Successfully connected, you are named: " + player.getName());
      clientOutput.displayString("Please wait for more players to connect");
      //If notStarted
      if(firstCall){
        if(!chooseRegions()) {return; }
      }
      while (true) {

        String turn = receiveAndDisplayString();
        
        long startTime = System.currentTimeMillis();
        long maxTime = (long) (connection.getSocket().getSoTimeout());
        //Catch case for issues in testing, should never really happen
        if (maxTime == 0) {
          maxTime = (long) (TURN_WAIT_MINUTES * 60 * 1000);
        }

        // Start of each turn will have continue message if game still going
        // Otherwise is winner message
        StringMessage startMessage = (StringMessage) (connection.receiveObject());
        String start = startMessage.unpacker();
        if (!start.equals("Continue")) {
          // If not continue then someone won --> print and exit
          clientOutput.displayString(start);
          connection.closeAll();
          clientInput.close();
          return;
        }

        // Next is alive status for player
        ConfirmationMessage isAlive = (ConfirmationMessage) (connection.receiveObject());
        // If null then something wrong
        if (isAlive == null) {
          return;
        }
        // Get primitive
        boolean alive = isAlive.getMessage();
        // If not same then player died on previous turn --> get spectate message
        if (alive != isPlaying) {
          isPlaying = alive;
          //Query for spectating
          //If no then kill connection
          if(!queryYNAndRespond("Would you like to keep spectating? [Y/N]")){
            connection.closeAll();
            clientInput.close();
            return;
          }
        }

        while (true) {
          // Next server sends board
          board = (Board) (connection.receiveObject());
          // Display board
          clientOutput.displayBoard(board);
          // Client generates orders --> sends
          if (alive) {
            //new OrderCreator
            OrderHelper orderhelper = new OrderHelper(this);
            List<OrderInterface> orders = orderhelper.createOrders();
            //If too long --> kill player
            if(timeOut(startTime, maxTime)){ return;}
            connection.sendObject(orders);
          }

          String response = receiveAndDisplayString();
          if (response.matches("^Fail:.*$")) {
            continue;
          }
          if (response.matches("^Success:.*$")) {
            break;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      connection.closeAll();
      clientInput.close();
      return;
    }
  }

  @Override
  public void run(){
    playGame();
  }
}
