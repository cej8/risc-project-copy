package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
  Board board;
  boolean isPlaying = true;
  private ClientInputInterface clientInput;
  private ClientOutputInterface clientOutput;
  private AbstractPlayer player;
  private Connection connection;

  public Client(Board b,AbstractPlayer p, InputStream i, OutputStream out) throws IOException{
    clientInput = new ConsoleInput(i);
    clientOutput = new TextDisplay();
    this.board = b;//added for testing purposes
    this.player = p;
    this.connection = new Connection();
    connection.setOutputStream(new ObjectOutputStream(out));
  }
  public Client(ClientInputInterface clientInput, ClientOutputInterface clientOutput){
    this.clientInput = clientInput;
    this.clientOutput = clientOutput;
  }

  public Client() {
    this.board = new Board(null);
    this.isPlaying = true;
    this.connection = new Connection();
  };

  // create connection from already created socket
  public void makeConnection(Socket socket) {
    try {
      connection.setInputStream(new ObjectInputStream(socket.getInputStream()));
      connection.setOutputStream(new ObjectOutputStream(socket.getOutputStream()));

      System.out.println("Connected to " + socket.getLocalAddress() + ":" + socket.getLocalPort());
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  // creating connection by address and port TODO -- do we need this constructor?
  public void makeConnection(String address, int port) {
    try {
      connection.setSocket(new Socket(address, port));
      connection.setInputStream(new ObjectInputStream(connection.getSocket().getInputStream()));
      connection.setOutputStream(new ObjectOutputStream(connection.getSocket().getOutputStream()));

      System.out.println("Connected to " + address + ":" + Integer.toString(port));
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
  public Object receiveObjectOrClose(){
    try{
      return connection.getInputStream().readObject();
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return null;
    }
  }
  void closeAll() {
    try {
      connection.getSocket().close();
      connection.closeAll();
    } catch (IOException e) {
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
  // recieve board
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
  connection.sendObject(placementList);
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
      source = orderHelper(clientInput.readInput());
    }
    while (destination == null) {
      clientOutput.displayString("What region do you want to attack?");
      destination = orderHelper(clientInput.readInput());
    }
    clientOutput.displayString("How many units do you want to move?");
    while (true) {
      try {
        Unit units = new Unit(Integer.parseInt(clientInput.readInput()));
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
        clientOutput.displayString("You are " + player + ", what would you like to do?\n (M)ove\n (A)ttack\n (D)one");
        response = clientInput.readInput();
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
      closeAll();
      return;
    }
  }
public Connection getConnection() {
	return connection;
}
}
