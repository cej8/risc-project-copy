package edu.duke.ece651.risc.shared;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import java.io.*;
import java.net.*;

// Class to keep track of game board, including associated regions
// Simply a list of regions with methods to access information from them
public class Board implements Serializable {
  private static final long serialVersionUID = 7L;
  // Region in board
  List<Region> regions;

  public Board(){
    this.regions = new ArrayList<Region>();
  }

  public Board(List<Region> regionList) {
    this.regions = new ArrayList<Region>();
    this.setRegions(regionList);
  }

  /* BEGIN ACCESSORS */
  public List<Region> getRegions(){
    return this.regions;
  }

 public void setRegions(List<Region> regionList){
    this.regions = regionList;
  }
  /* END ACCESSORS */

  //Get number of regions owned by a player
  public int getNumRegionsOwned(AbstractPlayer player){
    int total = 0;
    for(Region r : regions){
      if(player.getName().equals(r.getOwner().getName())){
          total++;
        }
    }
    return total;
  }

 //Creates a Map of Players to a List of all their owned regions
  public Map<AbstractPlayer, List<Region>> getPlayerToRegionMap(){
    Map<AbstractPlayer, List<Region>> playerRegionMap = new HashMap<AbstractPlayer, List<Region>>();
    List<Region> allRegions = this.getRegions(); //get all the regions
    Set<AbstractPlayer> allPlayers = this.getPlayerSet(); //get all the players
    for (AbstractPlayer p : allPlayers){ //for each player p
      List<Region> playerRegions = new ArrayList<Region>(); //create a list of their regions
      for (Region r : allRegions){ //for each regions r on the board
        if (r.getOwner() == p){ //if the owner is player p
          playerRegions.add(r); //add region r to their list of regions
        }
      }
      playerRegionMap.put(p, playerRegions); //add this <p, list<r>> to the map
      }
    return playerRegionMap;
  }
  
  //Creates a Set of all regions a player owns on the Board
  public Set<Region> getPlayerRegionSet(AbstractPlayer p){
    List<Region> allRegions = this.getRegions();
    Set<Region> playerRegions = new HashSet<Region>();
    for (Region r : allRegions) { //for each region on the board
      if (r.getOwner() != null) {
        if (r.getOwner().getName() == p.getName()) { //if player owns it
          playerRegions.add(r); //add that region to the set
        }
      }
    }
    return playerRegions;
  } 
  
  //Creates a Set of all Players on the Board
  public Set<AbstractPlayer> getPlayerSet(){
    List<Region> allRegions = this.getRegions();
    Set<AbstractPlayer> allPlayers = new HashSet<AbstractPlayer>();
    for (Region r : allRegions){ //for each region on the board
      allPlayers.add(r.getOwner()); //add that player's owner to the set
    }
    return allPlayers;
  }

  //Helper to get all visible regions (does not include adjacent cloaked)
  public Set<String> getVisibleRegions(String playerName){
    return getRegionSet(playerName, false);
  }

  //Helper to get visible regions including cloaked (to update cloak counter)
  public Set<String> getVisibleRegionsIncludingCloaked(String playerName){
    return getRegionSet(playerName, true);
  }

  //Gets visible regions for player, with boolean to force include adjacent cloaked
  public Set<String> getRegionSet(String playerName, boolean includeCloakedAdj){
    Set<String> visible = new HashSet<String>();
    //Add all visible regions to player to set
    for(Region r : regions){
      //If you own region --> visible no matter what
      if(r.getOwner().getName().equals(playerName)){
        visible.add(r.getName());
        //Check all adjacent to owned region, if not cloaked (turns == 0) then add to visible (unless flag true)
        for(Region adj : r.getAdjRegions()){
          if(includeCloakedAdj || adj.getCloakTurns() == 0){
            visible.add(adj.getName());
          }
        }
      }
      //Finally if a spy is in region then visible (no matter what)
      if(r.getSpies(playerName).size() > 0){
        visible.add(r.getName());
      }
    }
    return visible;
  }

  //Helper method to initialize all maps inside of regions with keys for players intended to be on board
  public void initializeSpies(List<String> players){
    for(Region r : regions){
      r.initializeSpies(players);
    }
  }

  //Method to update all region information visible to the player given new board
  public void updateVisible(String playerName, Board newBoard){
    //Get newest version of players
    List<AbstractPlayer> players = new ArrayList<AbstractPlayer>(newBoard.getPlayerSet());
    //Get all regions to update (visible + cloaked)
    Set<String> allUpdate = newBoard.getVisibleRegionsIncludingCloaked(playerName);
    //Get all regions to update everything for (visible - cloaked)
    Set<String> visible = newBoard.getVisibleRegions(playerName);

    //Iterate through board, for each value in allUpdate --> update
    List<Region> newRegions = newBoard.getRegions();
    for(int i = 0; i < regions.size(); i++){
      //Always update spy information
      regions.get(i).copySpies(newRegions.get(i));
      //If in allUpdate (visible + cloaked) then need to update at minimum cloak information
      if(allUpdate.contains(regions.get(i).getName())){
        //Always update cloak
        regions.get(i).setCloakTurns(newRegions.get(i).getCloakTurns());
        //If in visible (not cloaked) then update all
        if(visible.contains(regions.get(i).getName())){
          regions.get(i).copyInformation(newRegions.get(i));
        }
      }
      //Replace region owner with equivalent player from newBoard (equals based on name)
      AbstractPlayer tempOwner = regions.get(i).getOwner();
      int actualOwner = players.indexOf(tempOwner);
      //if -1 then tempOwner DNE in new board (for example Group _ owners)
      //if case then don't update (player object no longer exists so nothing to update to)
      if(actualOwner != -1){
        regions.get(i).setOwner(players.get(actualOwner));
      }
    }
  }

  //Helper to get region by string for name
  public Region getRegionByName(String name){
    Map<String, Region> nameToRegionMap = new HashMap<String, Region>();
    for (Region r : this.getRegions()){
        nameToRegionMap.put(r.getName(), r);
    }
    return nameToRegionMap.get(name);
  }

  //Creates a list (preserve order) of players on board.
  public List<AbstractPlayer> getPlayerList(){
    List<Region> allRegions = this.getRegions();
    Set<AbstractPlayer> addedPlayers = new HashSet<AbstractPlayer>();
    List<AbstractPlayer> allPlayers = new ArrayList<AbstractPlayer>();
    for (Region r : allRegions) { //for each region on the board
      if (r.getOwner() != null) {
        if (!(addedPlayers.contains(r.getOwner()))) { // if that player has not already been to list
          allPlayers.add(r.getOwner()); //add that region's owner to the set
          addedPlayers.add(r.getOwner());//add player to list of added players
        }
      }
    }
    return allPlayers;
  }

  //Helper to get player object by name from regions
  public AbstractPlayer getPlayerByName(String playerName){
    List<AbstractPlayer> players = getPlayerList();
    for(AbstractPlayer player : players){
      if(player.getName().equals(playerName)){
        return player;
      }
    }
    return null;
  }

  //Helper to replace all player objects with name within board with another object
  public void replacePlayerByName(String replaceName, AbstractPlayer playerToReplaceWith){
    for(Region r : regions){
      if(r.getOwner().getName().equals(replaceName)){
        r.setOwner(playerToReplaceWith);
      }
    }
  }

}



