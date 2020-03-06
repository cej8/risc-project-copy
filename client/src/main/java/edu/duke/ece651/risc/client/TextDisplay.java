package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.util.*;


public class TextDisplay implements ClientOutputInterface {
  
  @Override
  //prints the board info to stdout 
  public void displayBoard(Board b){
    String boardText = createBoard(b);
    System.out.println(boardText);
  }

  //returns a String of all of the board info
  public String createBoard(Board b){
    StringBuilder boardText = new StringBuilder();
    Map<AbstractPlayer, List<Region>> playerRegionMap = this.playerToRegionMap(b);
    for(Map.Entry<AbstractPlayer, List<Region>>  entry : playerRegionMap.entrySet()) { //for each entry in the map
      boardText.append(entry.getKey().getName() + ": \n---------\n"); //append player name
      for (Region r : entry.getValue()){ //for each region the player has
        boardText.append(this.printRegionInfo(r)); //add its info to board
      }
      boardText.append("\n");
    }
    return boardText.toString();
  }

 //returns String w board info for a given region
  private String printRegionInfo(Region r){
    int numUnits = r.getUnits().getUnits();
    String name = r.getName();
    StringBuilder sb = new StringBuilder(numUnits + " units in " + name); //add info on num units in region
    sb.append(this.printRegionAdjacencies(r)); //add adj info
    return sb.toString();
  }
  
   //returns String w adjacency info info for a given region  
  private String printRegionAdjacencies(Region r){
    StringBuilder sb = new StringBuilder(" (next to:");
    List<Region> adjList = r.getAdjRegions();
    for (int i = 0; i < adjList.size(); i++){
      sb.append(" " + adjList.get(i).getName()); //add name of adjacent region to sb
      if (i <  adjList.size() -1){
        sb.append(","); //add a comma if not the last in list
      }
    }
    sb.append(")\n"); //close parentheses
    return sb.toString();
  }
      

  
  //Creates a Map of Players to a List of all their owned regions 
  private Map<AbstractPlayer, List<Region>> playerToRegionMap(Board b){
    Map<AbstractPlayer, List<Region>> playerRegionMap = new HashMap<AbstractPlayer, List<Region>>();
    List<Region> allRegions = b.getRegions(); //get all the regions
    Set<AbstractPlayer> allPlayers = playerSet(b); //get all the players
    for (AbstractPlayer p : allPlayers){ //for each player p
      List<Region> playerRegions = new ArrayList<Region>(); //create a list of their regions
      for (Region r : allRegions){ //for each regions r on the board
        if (r.getOwner() == p){ //if the owner is player p
          playerRegions.add(r); //add region r to their list of regions
        }
      }
      playerRegionMap.put(p, playerRegions); //add this <p, r> to the map
      }
    return playerRegionMap;
  }



  //Creates a Set of all Players on the Board -- should I move this into the Board class so it could be used elsewhere?
  private Set<AbstractPlayer> playerSet(Board b){
    List<Region> allRegions = b.getRegions();
    Set<AbstractPlayer> allPlayers = new HashSet<AbstractPlayer>();
    for (Region r : allRegions){ //for each region on the board
      allPlayers.add(r.getOwner()); //add that player's owner to the set
    }
    return allPlayers;
  }



}
