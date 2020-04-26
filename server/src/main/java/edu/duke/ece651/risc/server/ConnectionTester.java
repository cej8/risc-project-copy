package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.Connection;
import java.util.*;
import java.io.*;
import java.net.*;

public class ConnectionTester extends Thread{
  private List<ChildServer> children;
  private MasterServer masterServer;
  private int gameID;
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
