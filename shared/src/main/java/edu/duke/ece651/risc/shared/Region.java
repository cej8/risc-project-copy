package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

// Class to handle keeping track of region owner, unit numbers, and adjacent regions
public class Region implements Serializable {
  private static final long serialVersionUID = 1L; // is there a more intuitive numbering we could use?
  private AbstractPlayer owner;
  private String name;
  private Unit units;
  private List<Region> adjRegions;
  private int regionSize;
  private int fuelProduction;
  private int technologyProduction;
  private boolean hasPlague;
  private int cloakTurns;
  private Map<String, List<Spy>> spies;
  // public Region() {
  // }

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
    this.cloakTurns = 0;
    this.spies = new HashMap<String, List<Spy>>();
  }

  public Region(AbstractPlayer p, Unit u) {// will need to be modified
    setName("");
    setOwner(p);
    setUnits(u);
    this.adjRegions = new ArrayList<Region>();
    setFuelProduction(Constants.STARTING_FUEL_PRODUCTION);
    setTechProduction(Constants.STARTING_TECH_PRODUCTION);
    setSize(Constants.REGION_SIZE);
    this.hasPlague = false;
    this.cloakTurns = 0;
    this.spies = new HashMap<String, List<Spy>>();
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

  public void setAllSpiesFalse(){
    for(List<Spy> list : spies.values()){
      for(Spy spy : list){
        spy.setHasMoved(false);
      }
    }
  }

  public void initializeSpies(List<String> players){
    for(String p : players){
      spies.put(p, new ArrayList<Spy>());
    }
  }

  
  public boolean getPlague(){
    return this.hasPlague;
  }
  public void setPlague(boolean p){
    this.hasPlague = p;
  }

  public Path findShortestPath(Region end) {
    Queue<Path> pq = new PriorityQueue<Path>(new PathComparator());
    // pq.add(path);

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
  private boolean alreadyInPath(Path p, Region adj){
 for (Region r : p.getPath()) {
        if (adj.getName().equals(r.getName())) {
          return true;// already in path, skip
        }
      }
      return false;
  }

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

  // Setters
  public void setOwner(AbstractPlayer p) {
    this.owner = p;
  }

  public void setUnits(Unit u) {
    this.units = u;
  }

  public void setAdjRegions(List<Region> adj) {
    this.adjRegions = adj;
  }

  public void setName(String n) {
    this.name = n;
  }

  public void setSize(int s) {
    this.regionSize = s;
  }

  public void setFuelProduction(int f) {
    this.fuelProduction = f;
  }

  public void setTechProduction(int t) {
    this.technologyProduction = t;
  }

  // Getters
  public int getTechProduction() {
    return technologyProduction;
  }

  public int getFuelProduction() {
    return fuelProduction;
  }

  public int getSize() {
    return regionSize;
  }

  public AbstractPlayer getOwner() {
    return owner;
  }

  public Unit getUnits() {
    return units;
  }

  public List<Region> getAdjRegions() {
    return adjRegions;
  }

  public String getName() {
    return name;
  }  


  //Method to copy spies from another region (assumes same name/adjacent/etc.)
  public void copySpies(Region regionCopy){
    this.spies = (Map<String, List<Spy>>)DeepCopy.deepCopy(regionCopy.getSpies());
  }

  //Method to copy information from another region (assumes same name/adjacent/etc.) that may change between turns
  //This includes the owner, units, hasPlague
  public void copyInformation(Region regionCopy){
    this.owner = (AbstractPlayer)DeepCopy.deepCopy(regionCopy.getOwner());
    this.units = (Unit)DeepCopy.deepCopy(regionCopy.getUnits());
    this.hasPlague = regionCopy.getPlague();
  }

}
