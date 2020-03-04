package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.List;

// Class to handle keeping track of region owner, unit numbers, and adjacent regions
public class Region implements Serializable {
  private static final long serialVersionUID = 2L; //is there a more intuitive numbering we could use?
  private AbstractPlayer owner;
  private Unit units;
  private List<Region> adjRegions;

  public void assignRegion(AbstractPlayer p, Unit u) {
    //assigns this region to Player p with Unit .. is this not just setOwner + setUnits?
  }
  //Setters
  public void setOwner(AbstractPlayer p){
    this.owner = p;
  }
  public void setUnits(Unit u){
    this.units = u;
  }
  void setAdjRegions(List<Region> adj){
    this.adjRegions = adj;
  }
  //Getters
  public AbstractPlayer getOwner(){
    return owner;
  }
  public Unit getUnits(){
    return units;
  }
  public List<Region> getAdjRegions(){
    return adjRegions;
  }
}
