package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
  private Socket socket;
  private ObjectInputStream fromServer;
  private ObjectOutputStream toServer;
  private Board board;
  private boolean isPlaying;

  public Client() {
    this.board = new Board(null);
    this.isPlaying = true;
  };

  //create connection from already created socket
public void makeConnection(Socket socket){
    try{
      fromServer = new ObjectInputStream(socket.getInputStream());
      toServer = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("Connected to " + socket.getLocalAddress() + ":" + socket.getLocalPort());
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  //creating connection by address and port  TODO -- do we need this constructor? 
 public void makeConnection(String address, int port){
    try{
      socket = new Socket(address, port);
      fromServer = new ObjectInputStream(socket.getInputStream());
      toServer = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("Connected to " + address + ":" + Integer.toString(port));
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

  void closeAll(){
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

}
