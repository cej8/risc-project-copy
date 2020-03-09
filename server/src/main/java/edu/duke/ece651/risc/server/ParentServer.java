package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class ParentServer {
  private final int PORT = 12345;
  private final int MAX_PLAYERS = 1;
  private final int MAX_REGIONS = 12;
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
  public void createStartingGroupsHelper(char groupName, int iStart, int iEnd, List<Region> regionList){
    HumanPlayer player;
    Unit u = new Unit();
    u.setUnits(0);
    player = new HumanPlayer();
    player.setName("Group " + groupName);
    for (int i = iStart; i < iEnd; i++){
      Region r = regionList.get(i); 
      r.assignRegion(player,u);
    }
  }
  public void createStartingGroups(){
    //int numPlayers = children.size();
    BoardGenerator genBoard = new BoardGenerator();
    genBoard.createBoard();
    board = genBoard.getBoard();
    int numPlayers = 5;
    List<Region> regionList = board.getRegions();
    char groupName = 'A';
    int remainder = MAX_REGIONS % numPlayers;
    int placementRegions = MAX_REGIONS - remainder;
    int groupSize = placementRegions / numPlayers;
    int numGroups;
    if (numPlayers == 5){
      numGroups = numPlayers + 1;
    } else {
      numGroups = numPlayers;
    }
    for (int i = 0; i < numGroups; i++){
      createStartingGroupsHelper(groupName,(groupSize * i), (groupSize + (groupSize * i)), regionList);
      groupName++;
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
