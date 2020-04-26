package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

// Class to handle keeping track of region owner, unit numbers, and adjacent regions
// This is the heart of the board
public class Region implements Serializable {
  private static final long serialVersionUID = 1L; 
  // Player that owns region
  private AbstractPlayer owner;
  // Name of region (must be unique within Board)
  private String name;
  // Units internal to region
  private Unit units;
  // List of regions adjacent to this one (must also reciprocate)
  private List<Region> adjRegions;
  // Size of region
  private int regionSize;
  // Base fuel production per turn
  private int fuelProduction;
  // Base technology production per turn
  private int technologyProduction;
  // If region has plague (no production, order limited)
  private boolean hasPlague;
  // "Tier" of region (increases resource outptu)
  private RegionLevel regionLevel;
  // Turns region is cloaked for
  private int cloakTurns;
  // Map of player name to spies in region
  private Map<String, List<Spy>> spies;

  // Constructor for assigning name to region (before Players are assigned)
  public Region(String n) {
    setName(n);
    setOwner(null);
    setUnits(null);
    this.adjRegions = new ArrayList<Region>();
    setFuelProduction(Constants.STARTING_FUEL_PRODUCTION);
    setTechProduction(Constants.STARTING_TECH_PRODUCTION);
    setSize(Constants.REGION_SIZE);
    this.hasPlague = false;

    this.regionLevel=new RegionLevel();
    this.cloakTurns = 0;
    this.spies = new HashMap<String, List<Spy>>();

  }

  //Constructor to create region with player/units
  public Region(AbstractPlayer p, Unit u) {
    setName("");
    setOwner(p);
    setUnits(u);
    this.adjRegions = new ArrayList<Region>();
    setFuelProduction(Constants.STARTING_FUEL_PRODUCTION);
    setTechProduction(Constants.STARTING_TECH_PRODUCTION);
    setSize(Constants.REGION_SIZE);
    this.hasPlague = false;

    this.regionLevel=new RegionLevel();


    this.cloakTurns = 0;
    this.spies = new HashMap<String, List<Spy>>();

  }

  /* BEGIN ACCESSORS */
  public AbstractPlayer getOwner() {
    return owner;
  }
  public void setOwner(AbstractPlayer p) {
    this.owner = p;
  }

  public String getName() {
    return name;
  } 
  public void setName(String n) {
    this.name = n;
  }

  public Unit getUnits() {
    return units;
  }
  public void setUnits(Unit u) {
    this.units = u;
  }

  public List<Region> getAdjRegions() {
    return adjRegions;
  }
  public void setAdjRegions(List<Region> adj) {
    this.adjRegions = adj;
  }

  public int getSize() {
    return regionSize;
  }
  public void setSize(int s) {
    this.regionSize = s;
  }

  public int getFuelProduction() {
    return fuelProduction;
  }
  public void setFuelProduction(int f) {
    this.fuelProduction = f;
  }

  public int getTechProduction() {
    return technologyProduction;
  }
  public void setTechProduction(int t) {
    this.technologyProduction = t;
  }

  public boolean getPlague(){
    return this.hasPlague;
  }
  public void setPlague(boolean p){
    this.hasPlague = p;
  }

  public RegionLevel getRegionLevel() {
    return regionLevel;
  }

  public int getCloakTurns(){
    return cloakTurns;
  }
  public void setCloakTurns(int cloakTurns){
    this.cloakTurns = cloakTurns;
  }
  public Map<String, List<Spy>> getSpies(){
    return spies;
  }

  public List<Spy> getSpies(String name){
    return spies.get(name);
  }

  public void addSpyList(String name, List<Spy> list){
    spies.put(name, list);
  }

  public void addSpy(String name, Spy spy){
    if(!spies.containsKey(name)){
      spies.put(name, new ArrayList<Spy>());
    }
    spies.get(name).add(spy);
  }
  /* END ACCESSORS */

  //Helper to set all spies to false state (notMoved)
  public void setAllSpiesFalse(){
    for(List<Spy> list : spies.values()){
      for(Spy spy : list){
        spy.setHasMoved(false);
      }
    }
  }

  //Helper to initialize spy map given player names
  public void initializeSpies(List<String> players){
    for(String p : players){
      spies.put(p, new ArrayList<Spy>());
    }
  }

  //Method to search adjacent/connected to find path for region
  public Path findShortestPath(Region end) {
    Queue<Path> pq = new PriorityQueue<Path>(new PathComparator());
    for (Region adj : this.getAdjRegions()) {
        Path path = new Path();
        path.add(this);
 
      if (!adj.getOwner().getName().equals(owner.getName())) {
        continue;
      }
      if(adj ==end){
        path.add(adj);
        return path;
      }
      adj.findShortestPathHelper(end, path, pq);
    }
    return pq.poll();

  }

  //Method to check if region is already in path object (legacy)
  private boolean alreadyInPath(Path p, Region adj){
    for (Region r : p.getPath()) {
      if (adj.getName().equals(r.getName())) {
       return true;// already in path, skip
      }
    }
    return false;
  }

  //Helper to find shortest path given a partial path
  private void findShortestPathHelper(Region end, Path path, Queue<Path> pq) {
    path.add(this);
    Path currentPath =new Path(path);
     for (Region adj : this.getAdjRegions()) {
      // if not owned by player then ignore
      if (!adj.getOwner().getName().equals(owner.getName())) {
        continue;
      }
      if(alreadyInPath(currentPath,adj)){
        continue;
      }
      if (adj == end) {
        currentPath.add(adj);
        pq.add(currentPath);
        return;//found end
      }
      else {
        adj.findShortestPathHelper(end, currentPath, pq);
         currentPath = path;
      }
    }
  }

  //Helper to find region on board interal to region (legacy)
  public Region getRegionByName(Board board, String name){
    Map<String, Region> nameToRegionMap = new HashMap<String, Region>();
    for (Region r : board.getRegions()){
      nameToRegionMap.put(r.getName(), r);
    }
    return nameToRegionMap.get(name);
  }
  
  public void assignRegion(AbstractPlayer p, Unit u) {
    setOwner(p);
    setUnits(u);
  }

  //Method to copy spies from another region (assumes same name/adjacent/etc.)
  public void copySpies(Region regionCopy){
    this.spies = (Map<String, List<Spy>>)DeepCopy.deepCopy(regionCopy.getSpies());
  }

  //Method to copy information from another region (assumes same name/adjacent/etc.) that may change between turns
  //This includes the owner, units, hasPlague, regionLevel
  //Used in FOW/visibilty method internal to board
  public void copyInformation(Region regionCopy){
    this.owner = regionCopy.getOwner();
    this.units = regionCopy.getUnits();
    this.hasPlague = regionCopy.getPlague();
    this.regionLevel = regionCopy.getRegionLevel();
  }


}
