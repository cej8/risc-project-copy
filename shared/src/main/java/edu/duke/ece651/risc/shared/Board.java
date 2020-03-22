package edu.duke.ece651.risc.shared;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import java.io.*;
import java.net.*;

public class Board implements Serializable {
  private static final long serialVersionUID = 7L;
  List<Region> regions;

  public Board(){
    this.regions = new ArrayList<Region>();
  }
  
  public Board(List<Region> regionList){
    this.setRegions(regionList);
  }
  
  //get and set List of Regions
  public List<Region> getRegions(){
    return this.regions;
  }

 public void setRegions(List<Region> regionList){
    this.regions = regionList;
  }

  public Region getRegionByName(String name){
    Map<String, Region> nameToRegionMap = new HashMap<String, Region>();
    for (Region r : this.getRegions()){
      nameToRegionMap.put(r.getName(), r);
    }
    return nameToRegionMap.get(name);
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



  //Creates a Set of all Players on the Board 
  public Set<AbstractPlayer> getPlayerSet(){
    List<Region> allRegions = this.getRegions();
    Set<AbstractPlayer> allPlayers = new HashSet<AbstractPlayer>();
    for (Region r : allRegions){ //for each region on the board
      allPlayers.add(r.getOwner()); //add that player's owner to the set
    }
    return allPlayers;
  }
  
  
  
}

