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
  //boolean isPlaying

  void makeConnection(String address, int port){
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

}
