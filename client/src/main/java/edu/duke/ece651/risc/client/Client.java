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
  private AbstractPlayer player;
  private InputStream userInput;

  public Client(Board b, AbstractPlayer p, InputStream i, OutputStream out) throws IOException{
    clientInput = new ConsoleInput(i);
    clientOutput = new TextDisplay();
    this.board = b;//added for testing purposes
    this.player = p;
    userInput = i;
  
    toServer = new ObjectOutputStream(out);
  }
  // public Client(){
  //   clientInput = new ConsoleInput();
  //   clientOutput = new TextDisplay();
  //    }

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
  public List<PlacementOrder> placementOrderHelper(List<PlacementOrder> placementList,String regionName, Region placement){
      clientOutput.displayString("How many units would you like to place in " + regionName);
      while (true) {
        try {
          Unit units = new Unit(Integer.parseInt(clientInput.readInput()));
          PlacementOrder placementOrder = new PlacementOrder(placement, units);
          placementList.add(placementOrder);
          break;
        } catch (NumberFormatException ne) {
          ne.printStackTrace();
          clientOutput.displayString("That was not an integer, please try again. How many units do you want to place?");
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
      if (player.getName().equals(regionList.get(i).getOwner().getName())){
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
      if (response.equals(regionList.get(i).getName())){
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
      source = orderHelper(clientInput.readInput());
    }
    while (destination == null) {
      clientOutput.displayString("What region do you want to attack?");
      destination = orderHelper(clientInput.readInput());
    }
    clientOutput.displayString("How many units do you want to attack with?");
    while (true) {
      try {
        Unit units = new Unit(Integer.parseInt(clientInput.readInput()));
        AttackOrder attackOrder = new AttackOrder(source, destination, units);
        orderList.add(attackOrder);
        break;
      } catch (NumberFormatException ne) {
         ne.printStackTrace();
         clientOutput.displayString("That was not an integer, please try again. How many units do you want to attack with?");
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
    clientOutput.displayString("How many units do you want to move?");
    while (true) {
      try {
        Unit units = new Unit(Integer.parseInt(clientInput.readInput()));
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
        clientOutput.displayString("You are " + player.getName() + ", what would you like to do?\n (M)ove\n (A)ttack\n (D)one");
        response = clientInput.readInput();
        if (response.toUpperCase().equals("D")){
          orderSelect = false;
          break;
        } else if (response.toUpperCase().equals("M")){
          orderList = moveOrderHelper(orderList);
          clientOutput.displayString("You made a Move order, what else would you like to do?\n");
        } else if (response.toUpperCase().equals("A")){
          orderList = attackOrderHelper(orderList);
          clientOutput.displayString("You made an Attack order, what else would you like to do?\n");
        } else {
          clientOutput.displayString("Please select either M, A, or D\n");
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

}
