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
  private String createBoard(Board b){
    StringBuilder boardText = new StringBuilder();
    Map<AbstractPlayer, List<Region>> playerRegionMap = b.getPlayerToRegionMap();
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
      

  
 



}
