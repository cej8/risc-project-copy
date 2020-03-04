package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.List;

// Class to handle keeping track of region owner, unit numbers, and adjacent regions
public class Region implements Serializable {
  private static final long serialVersionUID = 1L; // what does this mean? Different number?
  private AbstractPlayer owner;
  private Unit units;
  private List<Region> adjRegions;

  public void assignRegion(AbstractPlayer o, Unit u){
    
  }
  //Setters
  public void setOwner(AbstractPlayer o){
    this.owner = o;
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
