package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client extends Thread{
  private Connection connection;
  private Board board;
  private boolean isPlaying = true;
  private ClientInputInterface clientInput;
  private ClientOutputInterface clientOutput;
  private HumanPlayer player;
  
  private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
  private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES;

  public Client() {
    clientInput = new ConsoleInput();
    clientOutput = new TextDisplay();
    board = new Board();
    connection = new Connection();
  }

  public Client(ClientInputInterface clientInput, ClientOutputInterface clientOutput) {
    this();
    this.clientInput = clientInput;
    this.clientOutput = clientOutput;
  }

  
  public void setTURN_WAIT_MINUTES(double TURN_WAIT_MINUTES){
    this.TURN_WAIT_MINUTES = TURN_WAIT_MINUTES;
  }
  public void setSTART_WAIT_MINUTES(double START_WAIT_MINUTES){
    this.START_WAIT_MINUTES = START_WAIT_MINUTES;
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

  public void makeConnection(String address, int port) {
    Socket socket;
    try {
      socket = new Socket(address, port);
      makeConnection(socket);
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public void makeConnection(Socket socket) {
    try {
      connection.setSocket(socket);
      connection.getStreamsFromSocket();
      socket.setSoTimeout((int) (Constants.START_WAIT_MINUTES * 60 * 1000));
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public boolean timeOut(long startTime, long maxTime){
    // If too long --> kill player (prevent trying to write to closed pipe)
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
      connection.getSocket().setSoTimeout((int) (Constants.START_WAIT_MINUTES * 60 * 1000));
      while (true) {
        // Game starts with board message
        board = (Board) (connection.receiveObject());
        // Return timeout to smaller value
        connection.getSocket().setSoTimeout((int) (Constants.TURN_WAIT_MINUTES * 60 * 1000));

        //Set max/start first time board received (start of turn)
        if(maxTime == -1){
          maxTime = (long) (connection.getSocket().getSoTimeout());
          //Catch case for issues in testing, should never really happen
          if (maxTime == 0) {
            maxTime = (long) (Constants.TURN_WAIT_MINUTES * 60 * 1000);
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
        String response = responseMessage.getMessage();
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
        List<PlacementOrder> placementOrders = createPlacements();
        if(timeOut(startTime, maxTime)) { return false; }
        connection.sendObject(placementOrders);

        // Wait for response
        StringMessage responseMessage = (StringMessage) (connection.receiveObject());
        String response = responseMessage.getMessage();
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

  public List<PlacementOrder> placementOrderHelper(List<PlacementOrder> placementList, String regionName,
      Region placement) {
    while (true) {
      try {
        clientOutput.displayString("How many units would you like to place in " + regionName + "? (please enter a number)");
        Unit units = new Unit(Integer.parseInt(clientInput.readInput()));
        PlacementOrder placementOrder = new PlacementOrder(placement, units);
        System.out.println(units.getUnits());
        placementList.add(placementOrder);
        break;
      } catch (NumberFormatException ne) {
        // ne.printStackTrace();
        clientOutput.displayString("That was not an integer, please try again.");
      }
    }
    return placementList;
  }

  public List<PlacementOrder> createPlacements() {
    // Prompt user for placements, create list of placementOrders, send to server
    int startUnits = Constants.UNIT_START_MULTIPLIER * board.getNumRegionsOwned(player);
    clientOutput.displayString("You are " + player.getName() + ", prepare to place " + startUnits + " units.");
    List<PlacementOrder> placementList = new ArrayList<PlacementOrder>();
    List<Region> regionList = board.getRegions();
    Region placement;
    String regionName;
    for (int i = 0; i < regionList.size(); i++) {
      if (player.getName().equals(regionList.get(i).getOwner().getName())) {
        placement = regionList.get(i);
        regionName = regionList.get(i).getName();
        placementList = placementOrderHelper(placementList, regionName, placement);
      }
    }
    return placementList;
  }

 
  public void playGame() {
    try {
      // Make initial connection, waits for server to send back player's player object
      // Get initial player object (for name)
      player = (HumanPlayer) (connection.receiveObject());
      clientOutput.displayString("Successfully connected, you are named: " + player.getName());
      clientOutput.displayString("Please wait for more players to connect");
      // After which choose regions
      if(!chooseRegions()) {return; }
      while (true) {
        long startTime = System.currentTimeMillis();
        long maxTime = (long) (connection.getSocket().getSoTimeout());
        //Catch case for issues in testing, should never really happen
        if (maxTime == 0) {
          maxTime = (long) (Constants.TURN_WAIT_MINUTES * 60 * 1000);
        }

        StringMessage turnMessage = (StringMessage) (connection.receiveObject());
        String turn = turnMessage.getMessage();
        clientOutput.displayString(turn);

        // Start of each turn will have continue message if game still going
        // Otherwise is winner message
        StringMessage startMessage = (StringMessage) (connection.receiveObject());
        String start = startMessage.getMessage();
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
          // Continue prompting until valid input (server closes after 60s)
          while (true) {
            // Request input
            clientOutput.displayString("Would you like to keep spectating? [Y/N]");
            String spectateResponse = clientInput.readInput();
            
            // If too long --> kill player
            if(timeOut(startTime, maxTime)){ return; }

            spectateResponse = spectateResponse.toUpperCase();
            // If valid then do work
            if (spectateResponse.length() == 1) {
              if (spectateResponse.charAt(0) == 'Y') {
                connection.sendObject(new ConfirmationMessage(true));
                break;
              } else if (spectateResponse.charAt(0) == 'N') {
                connection.sendObject(new ConfirmationMessage(false));
                connection.closeAll();
                clientInput.close();
                return;
              }
            }
            // Otherwise repeat
            clientOutput.displayString("Invalid input.");
          }
        }

        while (true) {
          // Next server sends board
          board = (Board) (connection.receiveObject());
          // Display board
          clientOutput.displayBoard(board);
          // Client generates orders --> sends
          if (alive) {
            SDOrderCreator createOrders = new SDOrderCreator(this);
            List<OrderInterface> orders = createOrders.createOrders();
            //If too long --> kill player
            if(timeOut(startTime, maxTime)){ return;}
            connection.sendObject(orders);
          }

          StringMessage responseMessage = (StringMessage) (connection.receiveObject());
          String response = responseMessage.getMessage();
          clientOutput.displayString(response);
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
