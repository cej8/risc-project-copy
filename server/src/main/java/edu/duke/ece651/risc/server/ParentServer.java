package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class ParentServer {
  private final int PORT = 12345;
  private final int MAX_PLAYERS = 1;
  private final int MAX_REGIONS = 15;
  private ServerSocket serverSocket;
  private List<ChildServer> children;
  private Board board;
  //Map<OrderInterface, List> orderList;

  public List<ChildServer> getChildren(){
    return children;
  }
  
  public void waitingForConnections() throws IOException {
    children = new ArrayList<ChildServer>();
    serverSocket = new ServerSocket(PORT);

    while (children.size() < MAX_PLAYERS) {
      HumanPlayer newPlayer;
      try {
        newPlayer = new HumanPlayer("Player " + Integer.toString(children.size() + 1), serverSocket.accept());
      } catch (Exception e) {
        e.printStackTrace(System.out);
        continue;
      }
      System.out.println(newPlayer.getName() + " joined.");
      children.add(new ChildServer(newPlayer, this));
    }

  }
  public Board getBoard(){
    return this.board;
  }
  // Helper method to add player to global player list - children
  public void addPlayer(ChildServer c){
    children.add(c);
  }
  // Generate initial board, set region groups based on number of players 
  public void createStartingGroups(){
    //int numPlayers = children.size();
    int numPlayers = 3;
    int remainder = MAX_REGIONS % numPlayers;
    int placementRegions = MAX_REGIONS - remainder;
    int groupSize = placementRegions / numPlayers;
    // Board board = new
    List<Region> regionList = new ArrayList<Region>();
    // assign no units
    Unit u = new Unit();
    u.setUnits(0);
    HumanPlayer player;
    for (int i = 0; i < numPlayers; i++){
        player = new HumanPlayer();
        // Setting inital group names 
        player.setName("Group " + Integer.toString('A' - i));
      for (int j = 0; j < groupSize; j++){
        Region r = new Region(player,u);
        regionList.add(r);
      }
    }
    while (remainder > 0){
      // assign remainder as NoPlacement
      player = new HumanPlayer();
      player.setName("No Placement");
      Region r = new Region(player,u);
      regionList.add(r);
      remainder--;
    }
    this.board = new Board(regionList);
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
