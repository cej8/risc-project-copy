package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class ParentServer {
  private final int PORT = 12345;
  private final int MAX_PLAYERS = 1;

  private ServerSocket serverSocket;
  private List<ChildServer> children;
  //private Board board;
  //Map<OrderInterface, List> orderList;

  public List<ChildServer> getChildren(){
    return children;
  }
  
  public void waitingForConnections() throws IOException{
    children = new ArrayList<ChildServer>();
    serverSocket = new ServerSocket(PORT);

    while(children.size() < MAX_PLAYERS){
      HumanPlayer newPlayer;
      try{
        newPlayer = new HumanPlayer("Player " + Integer.toString(children.size()+1), serverSocket.accept());
      }
      catch(Exception e){
        e.printStackTrace(System.out);
        continue;
      }
      System.out.println(newPlayer.getName() + " joined.");
      children.add(new ChildServer(newPlayer, this));
    }
    
  }

  public void closeAll(){
    for(ChildServer child : children){
      child.getPlayer().closeAll();
    }
    try{
      serverSocket.close();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }
}
