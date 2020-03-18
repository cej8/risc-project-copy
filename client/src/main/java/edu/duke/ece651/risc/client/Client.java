package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
  private Socket socket;
  private ObjectInputStream fromServer;
  private ObjectOutputStream toServer;
  Board board;
  boolean isPlaying = true;
  private ClientInputInterface clientInput;
  private ClientOutputInterface clientOutput;
  private HumanPlayer player;

  public Client(Board b){
    clientInput = new ConsoleInput();
    clientOutput = new TextDisplay();
    this.board = b;
  }

  public Client(ClientInputInterface clientInput, ClientOutputInterface clientOutput){
    this.clientInput = clientInput;
    this.clientOutput = clientOutput;
  }
  
  public void makeConnection(String address, int port){
    try{
      socket = new Socket(address, port);
      socket.setSoTimeout(60*1000);
      fromServer = new ObjectInputStream(socket.getInputStream());
      toServer = new ObjectOutputStream(socket.getOutputStream());
      clientOutput.displayString("Connected to " + address + ":" + Integer.toString(port));
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  public Object receiveObject() throws IOException, ClassNotFoundException{
    return fromServer.readObject();
  }

  public void sendObject(Object object) throws IOException{
    toServer.writeObject(object);
  }

  public Object receiveObjectOrClose(){
    try{
      return fromServer.readObject();
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return null;
    }
  }

  public void closeAll(){
    try{
      socket.close();
      fromServer.close();
      toServer.close();
    }
    catch(IOException e){
      e.printStackTrace(System.out);
    }
  }

  public void updateClientBoard()  {
    Board masterBoard = null;
    try{
      masterBoard = (Board) fromServer.readObject();
      this.setBoard(masterBoard);
      this.board.setRegions(masterBoard.getRegions());
    }
    catch(IOException e) {
      System.out.println("IOException is caught");
    }
    catch(ClassNotFoundException e) {
      System.out.println("ClassNotFoundException is caught");
    }
}

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public ObjectInputStream getFromServer() {
    return fromServer;
  }

  public void setFromServer(ObjectInputStream fromServer) {
    this.fromServer = fromServer;
  }

  public ObjectOutputStream getToServer() {
    return toServer;
  }

  public void setToServer(ObjectOutputStream toServer) {
    this.toServer = toServer;
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }
  /*  public void chooseRegions(){    
    try{
      //Set timeout to 5 minutes, wait this long for game start
      socket.setSoTimeout(5*60*1000);
      while(true){
        //Game starts with board message
        board = (Board)(receiveObject());
        //Return timeout to smaller value
        socket.setSoTimeout(60*1000);

        //Output board
        clientOutput.displayBoard(board);
        //Print prompt and get group name
        clientOutput.displayString("Please select a starting region");
        String groupName = clientInput.readInput(System.in);
        sendObject(new StringMessage(groupName));

        //Wait for response
        StringMessage responseMessage = (StringMessage)(receiveObject());
        String response = responseMessage.getMessage();
        if(response.matches("^Fail:")){ continue;}
        if(response.matches("^Success:")){ break;}
      }

      while(true){
        //Server then sends board again
        board = (Board)(receiveObject());

        //Display and move into placements
        clientOutput.displayBoard(board);
        createPlacements();

        //Wait for response
        StringMessage responseMessage = (StringMessage)(receiveObject());
        String response = responseMessage.getMessage();
        if(response.matches("^Fail:")){ continue;}
        if(response.matches("^Success:")){ break;}
      }
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return;
    }
    }*/
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
  // Prompt user for placements, create list of placementOrders, send to server
  public void createPlacements(){
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
  sendObject(placementList);
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
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
    //TODO: prompt user for orders --> create list of OrderInterface --> send to server
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
      sendObject(orderList);
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return;
    }
  }

  /*
  public void playGame(String address, int port){
    try{
      //Make initial connection, waits for server to send back player's player object
      makeConnection(address, port);
      player = (HumanPlayer)(receiveObject());
      //After which choose regions
      chooseRegions();
      
      while(true){
        //Start of each turn will have continue message if game still going
        //Otherwise is winner message
        StringMessage startMessage = (StringMessage)(receiveObject());
        String start = startMessage.getMessage();
        if(!start.equals("Continue")){
          //If not continue then someone won --> print and exit
          clientOutput.displayString(start);
          return;
        }
        
        //Next is alive status for player
        ConfirmationMessage isAlive = (ConfirmationMessage)(receiveObject());
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
            spectateResponse = spectateResponse.toUpperCase();
            //If valid then do work
            if(spectateResponse.length() != 1){            
              if(spectateResponse.charAt(0) == 'Y'){
                sendObject(new ConfirmationMessage(true));
                break;
              }
              else if(spectateResponse.charAt(0) == 'N'){
                sendObject(new ConfirmationMessage(false));
                closeAll();
                return;
              }
            }
            //Otherwise repeat
            clientOutput.displayString("Invalid input.");
          }
        }

        while(true){
          //Next server sends board
          board = (Board)(receiveObject());
          //Display board
          clientOutput.displayBoard(board);
          //Client generates orders --> sends 
          if(alive){
            createOrders();
          }

          StringMessage responseMessage = (StringMessage)(receiveObject());
          String response = responseMessage.getMessage();
          if(response.matches("^Fail:")){ continue;}
          if(response.matches("^Success:")){ break;}
        }
      }
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return;
    }
        
    }*/
}
