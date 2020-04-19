package edu.duke.ece651.risc.shared;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import java.io.*;
import java.net.*;

// Class to keep track of game board, including associated regions
public class Board implements Serializable {
    private static final long serialVersionUID = 7L;
    List<Region> regions;

    public Board(){
        this.regions = new ArrayList<Region>();
    }

    public Board(List<Region> regionList) {
        this.regions = new ArrayList<Region>();
        this.setRegions(regionList);
    }

    //get and set List of Regions
    public List<Region> getRegions(){
        return this.regions;
    }

    public void setRegions(List<Region> regionList){
        this.regions = regionList;
    }


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
    //Creates a Set of all Players on the Board
    public Set<AbstractPlayer> getPlayerSet(){
        List<Region> allRegions = this.getRegions();
        Set<AbstractPlayer> allPlayers = new HashSet<AbstractPlayer>();
        for (Region r : allRegions) { //for each region on the board
            if (r.getOwner() != null) {
                allPlayers.add(r.getOwner()); //add that region's owner to the set
            }
        }
        return allPlayers;
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

}
