package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
  private final double START_WAIT_MINUTES = 2.5;
  private final double TURN_WAIT_MINUTES = 1;
  
  private Connection connection;
  Board board;
  boolean isPlaying = true;
  private ClientInputInterface clientInput;
  private ClientOutputInterface clientOutput;
  private HumanPlayer player;

  public void Client(){
    clientInput = new ConsoleInput();
    clientOutput = new TextDisplay();
  }

  public void Client(ClientInputInterface clientInput, ClientOutputInterface clientOutput){
    this.clientInput = clientInput;
    this.clientOutput = clientOutput;
  }

  
  public void setBoard(Board board) {
    this.board = board;
  }

  public Connection getConnection() {
    return connection;
  }
  

  public void makeConnection(String address, int port){
    Socket socket;
    try{
      socket = new Socket(address, port);
      makeConnection(socket);
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  public void makeConnection(Socket socket){
    try{
      socket.setSoTimeout((int)(START_WAIT_MINUTES*60*1000));
      connection = new Connection(socket);
      clientOutput.displayString("Connected to " + socket.getLocalAddress() + ":" + socket.getLocalPort());
      connection.getStreamsFromSocket();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  public void updateClientBoard() {
    Board masterBoard = null;
    try {
      // masterBoard = (Board) fromServer.readObject();
       masterBoard = (Board)connection.getInputStream().readObject();
       this.setBoard(masterBoard);
      this.board.setRegions(masterBoard.getRegions());
    } catch (IOException e) {
      System.out.println("IOException is caught");
    } catch (ClassNotFoundException e) {
      System.out.println("ClassNotFoundException is caught");
    }
  }

  public Board getBoard() {
    return board;
  }

  public void chooseRegions(){    
    try{
      //Set timeout to constant, wait this long for game start
      //This will block on FIRST board = ...
      connection.getSocket().setSoTimeout((int)(START_WAIT_MINUTES*60*1000));
      while(true){
        //Game starts with board message
        board = (Board)(connection.receiveObject());
        //Return timeout to smaller value
        connection.getSocket().setSoTimeout((int)(TURN_WAIT_MINUTES*60*1000));

        //Output board
        clientOutput.displayBoard(board);
        //Print prompt and get group name
        clientOutput.displayString("Please select a starting region");
        String groupName = clientInput.readInput(System.in);
        connection.sendObject(new StringMessage(groupName));

        //Wait for response
        StringMessage responseMessage = (StringMessage)(connection.receiveObject());
        String response = responseMessage.getMessage();
        if(response.matches("^Fail:")){ continue;}
        if(response.matches("^Success:")){ break;}
      }

      while(true){
        //Server then sends board again
        board = (Board)(connection.receiveObject());

        //Display and move into placements
        clientOutput.displayBoard(board);
        createPlacements();

        //Wait for response
        StringMessage responseMessage = (StringMessage)(connection.receiveObject());
        String response = responseMessage.getMessage();
        if(response.matches("^Fail:")){ continue;}
        if(response.matches("^Success:")){ break;}
      }
    }
    catch(Exception e){
      e.printStackTrace();
      connection.closeAll();
      return;
    }
  }

  
  public List<PlacementOrder> placementOrderHelper(List<PlacementOrder> placementList,String regionName, Region placement){
    clientOutput.displayString("How many units would you like to place in " + regionName);
    while (true) {
      try {
        Unit units = new Unit(Integer.parseInt(clientInput.readInput(System.in)));
        PlacementOrder placementOrder = new PlacementOrder(placement, units);
        placementList.add(placementOrder);
        break;
      } catch (NumberFormatException ne) {
        ne.printStackTrace();
        clientOutput.displayString("That was not an integer, please try again. How many units do you want to move?");
      }
    }
    return placementList;
  }
  
  public void createPlacements(){
    // Prompt user for placements, create list of placementOrders, send to server
    List<PlacementOrder> placementList = new ArrayList<PlacementOrder>();
    List<Region> regionList = board.getRegions();
    Region placement;
    String regionName;
    try{
    for (int i = 0; i < regionList.size(); i++){
      if (player.getName() == regionList.get(i).getOwner().getName()){
        placement = regionList.get(i);
        regionName = regionList.get(i).getName();
        placementList = placementOrderHelper(placementList,regionName,placement);
      }
    }
    connection.sendObject(placementList);
    }
    catch(Exception e){
      e.printStackTrace();
      connection.closeAll();
    }
  }
  
  public Region orderHelper(String response){
    List<Region> regionList = board.getRegions();
    for (int i = 0; i < regionList.size(); i++){
      if (response == regionList.get(i).getName()){
        return regionList.get(i);
      }
    }
    return null;
  }
  
  public List<OrderInterface> attackOrderHelper(List<OrderInterface> orderList){
    Region source = null;
    Region destination = null;
    while (source == null) {
      clientOutput.displayString("What region do you want to attack from?");
      source = orderHelper(clientInput.readInput(System.in));
    }
    while (destination == null) {
      clientOutput.displayString("What region do you want to attack?");
      destination = orderHelper(clientInput.readInput(System.in));
    }
    clientOutput.displayString("How many units do you want to move?");
    while (true) {
      try {
        Unit units = new Unit(Integer.parseInt(clientInput.readInput(System.in)));
        AttackOrder attackOrder = new AttackOrder(source, destination, units);
        orderList.add(attackOrder);
        break;
      } catch (NumberFormatException ne) {
         ne.printStackTrace();
         clientOutput.displayString("That was not an integer, please try again. How many units do you want to move?");
      }
    }
   return orderList;
  }
  
  public List<OrderInterface> moveOrderHelper(List<OrderInterface> orderList){
    Region source = null;
    Region destination = null;
    while (source == null) {
      clientOutput.displayString("What region do you want to move units from?");
      source = orderHelper(clientInput.readInput(System.in));
    }
    while (destination == null) {
      clientOutput.displayString("What region do you want to move units to?");
      destination = orderHelper(clientInput.readInput(System.in));
    }
    clientOutput.displayString("How many units do you want to move?");
    while (true) {
      try {
        Unit units = new Unit(Integer.parseInt(clientInput.readInput(System.in)));
        MoveOrder moveOrder = new MoveOrder(source, destination, units);
        orderList.add(moveOrder);
        break;
      } catch (NumberFormatException ne) {
         ne.printStackTrace();
         clientOutput.displayString("That was not an integer, please try again. How many units do you want to move?");
      }
    }
   return orderList;
  }
  
  public void createOrders(){
    //prompt user for orders --> create list of OrderInterface --> send to server
    List<OrderInterface> orderList = new ArrayList<OrderInterface>();
    try {
      String response = null;
      boolean orderSelect = true;
      while (orderSelect) {
        // prompt user
        clientOutput.displayString("You are " + player + ", what would you like to do?\n (M)ove\n (A)ttack\n (D)one");
        response = clientInput.readInput(System.in);
        if (response == "D"){
          orderSelect = false;
          break;
        } else if (response == "M"){
          orderList = moveOrderHelper(orderList);
          clientOutput.displayString("You made a Move order, what else would you like to do?");
        } else if (response == "A"){
          orderList = attackOrderHelper(orderList);
          clientOutput.displayString("You made an Attack order, what else would you like to do?");
        } else {
          clientOutput.displayString("Please select eithe M, A, or D");
        }
      }
      connection.sendObject(orderList);
    }
    catch(Exception e){
      e.printStackTrace();
      connection.closeAll();
      return;
    }
  }

  public void playGame(String address, int port){    
    try{
      //Make initial connection, waits for server to send back player's player object
      makeConnection(address, port);
      long maxTime = (long)(connection.getSocket().getSoTimeout());
      //Get initial player object (for name)
      player = (HumanPlayer)(connection.receiveObject());
      //After which choose regions
      chooseRegions();
      
      while(true){
        long startTime = System.currentTimeMillis();

        //Start of each turn will have continue message if game still going
        //Otherwise is winner message
        StringMessage startMessage = (StringMessage)(connection.receiveObject());
        String start = startMessage.getMessage();
        if(!start.equals("Continue")){
          //If not continue then someone won --> print and exit
          clientOutput.displayString(start);
          connection.closeAll();
          return;
        }
        
        //Next is alive status for player
        ConfirmationMessage isAlive = (ConfirmationMessage)(connection.receiveObject());
        //If null then something wrong
        if(isAlive == null){ return;}
        //Get primitive
        boolean alive = isAlive.getMessage();
        //If not same then player died on previous turn --> get spectate message
        if(alive != isPlaying){
          //Continue prompting until valid input (server closes after 60s)
          while(true){
            //Request input
            clientOutput.displayString("Would you like to keep spectating? [Y/N]");
            String spectateResponse = clientInput.readInput(System.in);
            //If too long --> kill player
            if(System.currentTimeMillis() - startTime > maxTime){
              clientOutput.displayString("Player took too long, killing");
              connection.closeAll();
              return;
            }
            
            spectateResponse = spectateResponse.toUpperCase();
            //If valid then do work
            if(spectateResponse.length() != 1){            
              if(spectateResponse.charAt(0) == 'Y'){
                connection.sendObject(new ConfirmationMessage(true));
                break;
              }
              else if(spectateResponse.charAt(0) == 'N'){
                connection.sendObject(new ConfirmationMessage(false));
                connection.closeAll();
                return;
              }
            }
            //Otherwise repeat
            clientOutput.displayString("Invalid input.");
          }
        }

        while(true){
          //Next server sends board
          board = (Board)(connection.receiveObject());
          //Display board
          clientOutput.displayBoard(board);
          //Client generates orders --> sends 
          if(alive){
            createOrders();
          }

          //If too long --> kill player
          if(System.currentTimeMillis() - startTime > maxTime){
            clientOutput.displayString("Player took too long, killing");
            connection.closeAll();
            return;
          }

          StringMessage responseMessage = (StringMessage)(connection.receiveObject());
          String response = responseMessage.getMessage();
          if(response.matches("^Fail:")){ continue;}
          if(response.matches("^Success:")){ break;}
        }
      }
    }
    catch(Exception e){
      e.printStackTrace();
      connection.closeAll();
      return;
    }
        
  }
}
