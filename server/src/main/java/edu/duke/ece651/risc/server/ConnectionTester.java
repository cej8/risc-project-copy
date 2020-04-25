package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.Connection;
import java.util.*;
import java.io.*;
import java.net.*;

public class ConnectionTester extends Thread{
  private List<ChildServer> children;
  private MasterServer masterServer;
  private int gameID;

  public ConnectionTester(List<ChildServer> children, MasterServer masterServer, int gameID){
    this.children = children;
    this.masterServer = masterServer;
    this.gameID = gameID;
  }

  public void peekConnections(){
    for(ChildServer child : children){
      //Get connection
      Connection playerConnection = child.getPlayerConnection();
      if(child.isFinished() && playerConnection != null){
        try{
          //Try to listen for .5s
          playerConnection.getSocket().setSoTimeout(500);
          playerConnection.receiveObject();
        }
        catch(Exception e){
          if(!(e instanceof SocketTimeoutException)){
            //If not timeout then not there --> close connection
            playerConnection.closeAll();
            child.setPlayerConnection(null);
            //Only remove player IFF in this game
            masterServer.removePlayer(child.getPlayer().getName(), gameID);
          }
        }
      }
    }
  }

  @Override
  public void run(){
    while(true){
      peekConnections();
    }
  }
}
