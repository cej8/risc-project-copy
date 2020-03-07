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
  public void createStartingGroupsHelper(String groupName, int iStart, int iEnd, List<Region> regionList){
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
    switch (numPlayers){
      case 2:
        createStartingGroupsHelper("A",0,6,regionList);
        createStartingGroupsHelper("B",6,12,regionList);
        break;
      case 3:
        createStartingGroupsHelper("A",0,4,regionList);
        createStartingGroupsHelper("B",4,8,regionList);
        createStartingGroupsHelper("c",8,12,regionList);
        break;
      case 4:
        createStartingGroupsHelper("A",0,3,regionList);
        createStartingGroupsHelper("B",3,6,regionList);
        createStartingGroupsHelper("C",6,9,regionList);
        createStartingGroupsHelper("D",9,12,regionList);
        break;
      case 5:
        createStartingGroupsHelper("A",0,2,regionList);
        createStartingGroupsHelper("B",2,4,regionList);
        createStartingGroupsHelper("C",4,6,regionList);
        createStartingGroupsHelper("No Placement",6,8,regionList);
        createStartingGroupsHelper("D",8,10,regionList);
        createStartingGroupsHelper("E",10,12,regionList);
        break;
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
