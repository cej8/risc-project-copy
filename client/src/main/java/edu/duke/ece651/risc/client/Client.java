package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
  /*
  private final double START_WAIT_MINUTES = 2.5;
  private final double TURN_WAIT_MINUTES = 1;
  private final int MAX_UNITS = 15;*/
  
  private Connection connection;
  Board board;
  boolean isPlaying = true;
  private ClientInputInterface clientInput;
  private ClientOutputInterface clientOutput;
  private HumanPlayer player;

  public Client(){
    clientInput = new ConsoleInput();
    clientOutput = new TextDisplay();
    board = new Board();
    connection = new Connection();
  }

  public Client(ClientInputInterface clientInput, ClientOutputInterface clientOutput){
    this();
    this.clientInput = clientInput;
    this.clientOutput = clientOutput;
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

  public ClientInputInterface getClientInput(){
    return clientInput;
  }
  public ClientOutputInterface getClientOutput(){
    return clientOutput;
  }
  public void setPlayer(HumanPlayer player){
    this.player = player;
  }
  public void setSocketTimeout(int timeout) throws SocketException{
    connection.getSocket().setSoTimeout(timeout);
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
      connection.setSocket(socket);
      //clientOutput.displayString("Connected to " + socket.getLocalAddress().getHostName() + ":" + socket.getLocalPort());
      connection.getStreamsFromSocket();
      socket.setSoTimeout((int)(Constants.START_WAIT_MINUTES*60*1000));
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

  public void chooseRegions(){    
    try{
      //Set timeout to constant, wait this long for game start
      //This will block on FIRST board = ...
      connection.getSocket().setSoTimeout((int)(Constants.START_WAIT_MINUTES*60*1000));
      while(true){
        //Game starts with board message
        board = (Board)(connection.receiveObject());
        //Return timeout to smaller value
        connection.getSocket().setSoTimeout((int)(Constants.TURN_WAIT_MINUTES*60*1000));

        //Output board
        clientOutput.displayBoard(board);
        //Print prompt and get group name
        clientOutput.displayString("Please select a starting region");
        String groupName = clientInput.readInput();
        connection.sendObject(new StringMessage(groupName));

        //Wait for response
        StringMessage responseMessage = (StringMessage)(connection.receiveObject());
        String response = responseMessage.getMessage();
        clientOutput.displayString(response);
        if(response.matches("^Fail:.*$")){ continue;}
        if(response.matches("^Success:.*$")){ break;}
      }
      
      while(true){
        //Server then sends board again
        board = (Board)(connection.receiveObject());

        //Display and move into placements
        clientOutput.displayBoard(board);
        connection.sendObject(createPlacements());

        //Wait for response
        StringMessage responseMessage = (StringMessage)(connection.receiveObject());
        String response = responseMessage.getMessage();
        clientOutput.displayString(response);
        if(response.matches("^Fail:.*$")){ continue;}
        if(response.matches("^Success:.*$")){ break;}
      }
    }
    catch(Exception e){
      e.printStackTrace();
      connection.closeAll();
      return;
    }
  }

  
  public List<PlacementOrder> placementOrderHelper(List<PlacementOrder> placementList,String regionName, Region placement){
    while (true) {
      try {
        clientOutput.displayString("How many units would you like to place in " + regionName + "?");
        Unit units = new Unit(Integer.parseInt(clientInput.readInput()));
        PlacementOrder placementOrder = new PlacementOrder(placement, units);
        placementList.add(placementOrder);
        break;
      } catch (NumberFormatException ne) {
        //ne.printStackTrace();
        clientOutput.displayString("That was not an integer, please try again.");
      }
    }
    return placementList;
  }
  
  public List<PlacementOrder> createPlacements(){
    // Prompt user for placements, create list of placementOrders, send to server
     clientOutput.displayString("You are " + player.getName() + ", prepare to place " + Constants.MAX_UNITS + " units.");
    List<PlacementOrder> placementList = new ArrayList<PlacementOrder>();
    List<Region> regionList = board.getRegions();
    Region placement;
    String regionName;
    for (int i = 0; i < regionList.size(); i++){
      if (player.getName() == regionList.get(i).getOwner().getName()){
        placement = regionList.get(i);
        regionName = regionList.get(i).getName();
        placementList = placementOrderHelper(placementList,regionName,placement);
      }
    }
    return placementList;
  }
  
  public Region orderHelper(String response){
    List<Region> regionList = board.getRegions();
    for (int i = 0; i < regionList.size(); i++){
      if (response.equals(regionList.get(i).getName())){
        return regionList.get(i);
      }
    }
    clientOutput.displayString("Region does not exist.");
    return null;
  }
  
  public List<OrderInterface> attackOrderHelper(List<OrderInterface> orderList){
    Region source = null;
    Region destination = null;
    while (source == null) {
      clientOutput.displayString("What region do you want to attack from?");
      source = orderHelper(clientInput.readInput());
    }
    while (destination == null) {
      clientOutput.displayString("What region do you want to attack?");
      destination = orderHelper(clientInput.readInput());
    }
    while (true) {
      try {
        clientOutput.displayString("How many units do you want to attack?");
        Unit units = new Unit(Integer.parseInt(clientInput.readInput()));
        AttackOrder attackOrder = new AttackOrder(source, destination, units);
        orderList.add(attackOrder);
        break;
      } catch (NumberFormatException ne) {
        //ne.printStackTrace();
         clientOutput.displayString("That was not an integer, please try again.");
      }
    }
   return orderList;
  }
  
  public List<OrderInterface> moveOrderHelper(List<OrderInterface> orderList){
    Region source = null;
    Region destination = null;
    while (source == null) {
      clientOutput.displayString("What region do you want to move units from?");
      source = orderHelper(clientInput.readInput());
    }
    while (destination == null) {
      clientOutput.displayString("What region do you want to move units to?");
      destination = orderHelper(clientInput.readInput());
    }
    while (true) {
      try {
        clientOutput.displayString("How many units do you want to move?");
        Unit units = new Unit(Integer.parseInt(clientInput.readInput()));
        MoveOrder moveOrder = new MoveOrder(source, destination, units);
        orderList.add(moveOrder);
        break;
      } catch (NumberFormatException ne) {
        //ne.printStackTrace();
         clientOutput.displayString("That was not an integer, please try again.");
      }
    }
   return orderList;
  }
  
  public List<OrderInterface> createOrders(){
    //prompt user for orders --> create list of OrderInterface --> send to server
    List<OrderInterface> orderList = new ArrayList<OrderInterface>();
    
   String response = null;
   boolean orderSelect = true;
   while (orderSelect) {
     // prompt user
     clientOutput.displayString("You are " + player.getName() + ", what would you like to do?\n (M)ove\n (A)ttack\n (D)one");
     response = clientInput.readInput();
     if (response.equals("D")){
       orderSelect = false;
       break;
     } else if (response.equals("M")){
       orderList = moveOrderHelper(orderList);
       clientOutput.displayString("You made a Move order, what else would you like to do?");
     } else if (response.equals("A")){
       orderList = attackOrderHelper(orderList);
       clientOutput.displayString("You made an Attack order, what else would you like to do?");
     } else {
       clientOutput.displayString("Please select either M, A, or D");
     }
   }
   return orderList;
  }

  public void playGame(){    
    try{
      //Make initial connection, waits for server to send back player's player object
      long maxTime = (long)(connection.getSocket().getSoTimeout());
      if(maxTime == 0){
        maxTime = (long)(Constants.TURN_WAIT_MINUTES*60*1000);
      }
      
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
          clientInput.close();
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
            String spectateResponse = clientInput.readInput();
            //If too long --> kill player
            if(System.currentTimeMillis() - startTime > maxTime){
              clientOutput.displayString("Player took too long, killing");
              connection.closeAll();
              return;
            }
            
            spectateResponse = spectateResponse.toUpperCase();
            //If valid then do work
            if(spectateResponse.length() == 1){
              if(spectateResponse.charAt(0) == 'Y'){
                connection.sendObject(new ConfirmationMessage(true));
                break;
              }
              else if(spectateResponse.charAt(0) == 'N'){
                connection.sendObject(new ConfirmationMessage(false));
                connection.closeAll();
                clientInput.close();
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
            connection.sendObject(createOrders());
          }

          //If too long --> kill player
          if(System.currentTimeMillis() - startTime > maxTime){
            clientOutput.displayString("Player took too long, killing");
            connection.closeAll();
            clientInput.close();
            return;
          }

          StringMessage responseMessage = (StringMessage)(connection.receiveObject());
          String response = responseMessage.getMessage();
          clientOutput.displayString(response);
          if(response.matches("^Fail:.*$")){ continue;}
          if(response.matches("^Success:.*$")){ break;}
        }
      }
    }
    catch(Exception e){
      e.printStackTrace();
      connection.closeAll();
      clientInput.close();
      return;
    }
        
  }
}
