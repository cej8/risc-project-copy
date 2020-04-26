package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.Connection;
import java.util.*;
import java.io.*;
import java.net.*;


// Helper class used in ParentServer
// Tries to read from stream for client's socket assuming nothing will actually be sent
// (before game starts or after successful)
// If socket died then exception OTHER THAN timeout --> close socket and remove connection from ChildServer
public class ConnectionTester extends Thread{
  private List<ChildServer> children;
  private MasterServer masterServer;
  private int gameID;
  //Variables for continuing/stopping loop
  //Only way to ensure stops with how java handles threads...
  private boolean continueRunning = true;
  private boolean hasStopped = false;

  public ConnectionTester(List<ChildServer> children, MasterServer masterServer, int gameID){
    this.children = children;
    this.masterServer = masterServer;
    this.gameID = gameID;
  }

  public boolean hasStopped(){
    return hasStopped;
  }

  public void peekConnections(){
    for(ChildServer child : children){
      if(!continueRunning) { return;}
      //Get connection
      Connection playerConnection = child.getPlayerConnection();
      if(child.getFinishedTurn() && playerConnection != null){
        try{
          //Try to listen for .5s
          playerConnection.getSocket().setSoTimeout(500);
          playerConnection.receiveObject();
        }
        catch(Exception e){
          if(!(e instanceof SocketTimeoutException)){
            System.out.println(gameID + " : Removing " + child.getPlayer().getName() + " connection lost after turn complete");
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
    while(continueRunning){
      peekConnections();
    }
    System.out.println(gameID + " : ConnectionTester stopping");
    this.hasStopped = true;
  }

  public void stopLoop(){
    this.continueRunning = false;
  }
}
