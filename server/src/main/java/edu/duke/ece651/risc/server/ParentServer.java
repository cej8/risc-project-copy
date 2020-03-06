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
  // Generate static board - generate tree to represent board
  public void createBoard(){
    List<Region> adjRegion = new ArrayList<Region>();
    List<Region> regionList = new ArrayList<Region>();
    Region regionA = new Region();
    Region regionB = new Region();
    Region regionC = new Region();
    Region regionD = new Region();
    Region regionE = new Region();
    Region regionF = new Region();
    Region regionG = new Region();
    Region regionH = new Region();
    Region regionI = new Region();
    Region regionJ = new Region();
    Region regionK = new Region();
    Region regionL = new Region();
    Region regionM = new Region();
    Region regionN = new Region();
    Region regionO = new Region();
    // A
    adjRegion.add(regionB);
    regionA.setAdjRegions(adjRegion);
    regionList.add(regionA);
    // B
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionA);
    adjRegion.add(regionC);
    adjRegion.add(regionE);
    regionB.setAdjRegions(adjRegion);
    regionList.add(regionB);
    // C
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionB);
    adjRegion.add(regionD);
    adjRegion.add(regionI);
    regionC.setAdjRegions(adjRegion);
    regionList.add(regionC);
    // D
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionC);
    adjRegion.add(regionF);
    regionD.setAdjRegions(adjRegion);
    regionList.add(regionD);
    // E
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionB);
    adjRegion.add(regionI);
    adjRegion.add(regionJ);
    regionE.setAdjRegions(adjRegion);
    regionList.add(regionE);
    // F
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionD);
    adjRegion.add(regionG);
    adjRegion.add(regionI);
    adjRegion.add(regionK);
    regionF.setAdjRegions(adjRegion);
    regionList.add(regionF);
    // G
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionH);
    adjRegion.add(regionF);
    regionG.setAdjRegions(adjRegion);
    regionList.add(regionG);
    // H
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionG);
    adjRegion.add(regionK);
    regionH.setAdjRegions(adjRegion);
    regionList.add(regionH);
    // I
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionF);
    adjRegion.add(regionE);
    adjRegion.add(regionC);
    adjRegion.add(regionJ);
    regionI.setAdjRegions(adjRegion);
    regionList.add(regionI);
    // J
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionI);
    adjRegion.add(regionE);
    adjRegion.add(regionL);
    regionJ.setAdjRegions(adjRegion);
    regionList.add(regionJ);
    // K
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionF);
    adjRegion.add(regionH);
    adjRegion.add(regionO);
    adjRegion.add(regionL);
    regionK.setAdjRegions(adjRegion);
    regionList.add(regionK);
    // L
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionM);
    adjRegion.add(regionN);
    adjRegion.add(regionJ);
    adjRegion.add(regionK);
    regionL.setAdjRegions(adjRegion);
    regionList.add(regionL);
    // M
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionL);
    regionM.setAdjRegions(adjRegion);
    regionList.add(regionM);
    // N
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionO);
    adjRegion.add(regionL);
    regionN.setAdjRegions(adjRegion);
    regionList.add(regionN);
    // O
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionN);
    adjRegion.add(regionK);
    regionO.setAdjRegions(adjRegion);
    regionList.add(regionO);
    // Add to board
    this.board = new Board(regionList);
    /*
               A
              /
             B   D
             |\ /|
             | C |
             E | F - G
             |\|/|   |
             | I |   H
             |/   \ /  
             J     K
              \   / \
               \ /   \
                L     O
               / \   /
              M   \ /
                   N
     */
  }
  public void regionAdjHelper(List<Region> adjRegion) {
    Region temp = new Region();
    adjRegion.add(temp);
  }
  // Generate initial board, set region groups based on number of players 
  public void createStartingGroups(){
    //int numPlayers = children.size();
    int numPlayers = 3;
    int remainder = MAX_REGIONS % numPlayers;
    int placementRegions = MAX_REGIONS - remainder;
    int groupSize = placementRegions / numPlayers;
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
