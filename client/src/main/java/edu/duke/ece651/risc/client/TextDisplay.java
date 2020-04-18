package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.util.*;
import java.io.*;


public class TextDisplay implements ClientOutputInterface {

  PrintWriter output;

  public TextDisplay(){
    try{
      this.output = new PrintWriter(System.out);
    }
    //Shouldn't be possible...
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public TextDisplay(PrintWriter output){
    this.output = output;
  }
  
  @Override
  //prints the board info to stdout 
  public void displayBoard(Board b){
    String boardText = createBoard(b);
    output.println(boardText);
    output.flush();
  }

  @Override
  public void displayString(String str){
    output.println(str);
    output.flush();
  }
  
  @Override
  public void displayBoard(Board b, String playerName){
    String boardText = createBoard(b, playerName, b.getVisibleRegions(playerName));
    output.println(boardText);
    output.flush();
  }

  public String createBoard(Board b){
    Set<String> visible = new HashSet<String>();
    for(Region r : b.getRegions()){
      visible.add(r.getName());
    }
    return createBoard(b, "", visible);
  }

  //returns a String of all of the board info
  public String createBoard(Board b, String playerName, Set<String> visible){
System.out.println(visible);
    StringBuilder boardText = new StringBuilder();
    Map<AbstractPlayer, List<Region>> playerRegionMap = b.getPlayerToRegionMap();
    List<AbstractPlayer> players = new ArrayList<AbstractPlayer>(playerRegionMap.keySet());
    Collections.sort(players);
    for(AbstractPlayer player : players) { //for each entry in the map
      boardText.append(player.getName());
      if(playerName.equals(player.getName())){
        boardText.append(": \nFuel: " + player.getResources().getFuelResource().getFuel() + "  Tech: " + player.getResources().getTechResource().getTech() + " Tech Level: " + player.getMaxTechLevel().getMaxTechLevel());
      }
      boardText.append("\n------------------\n"); //append player name
      for (Region r : playerRegionMap.get(player)){ //for each region the player has
        boardText.append(printRegionInfo(r, playerName, visible.contains(r.getName()))); //add its info to board
      }
      boardText.append("\n");
    }
    return boardText.toString();
  }

 //returns String w board info for a given region
  private String printRegionInfo(Region r, String playerName, boolean visible){
    StringBuilder sb = new StringBuilder();
    //Say old if not visible or has cloaking and not owned by player
    if(!visible){
      sb.append("Last known information: ");
    }
    //Add unit list
    sb.append(r.getUnits().getUnits() + " units in " + r.getName());
    sb.append(", ");
    //Add spy info
    sb.append(getSpies(r, playerName));
    sb.append(", ");
    sb.append(printRegionAdjacencies(r)); //add adj info
    sb.append(" (Size " + r.getSize() + ")");


    //Add cloak info
    if(r.getCloakTurns() > 0){
      sb.append(" (Cloaked for " + r.getCloakTurns() + " more turns)");
    }
    //Add plague info
    if(r.getPlague()){
      sb.append(" (PLAGUED)");
    }
    sb.append("\n");
    return sb.toString();
  }
  
  private String getSpies(Region r, String playerName){
    List<Spy> spyList = r.getSpies(playerName);
    if(spyList == null || spyList.size() == 0){
      return "No spies";
    }
    int haveMoved = 0;
    for(Spy spy : spyList){
      if(spy.getHasMoved()){
        haveMoved++;
      }
    }
    return haveMoved + " moved spies, " + (spyList.size() - haveMoved) + " ready spies";
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
    sb.append(")"); //close parentheses
    return sb.toString();
  }

}
