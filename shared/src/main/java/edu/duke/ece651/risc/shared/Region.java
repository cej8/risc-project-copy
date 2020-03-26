package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

// Class to handle keeping track of region owner, unit numbers, and adjacent regions
public class Region implements Serializable {
  private static final long serialVersionUID = 1L; //is there a more intuitive numbering we could use?
  private AbstractPlayer owner;
  private String name;
  private Unit units;
  private List<Region> adjRegions;
  private int regionSize;
  private int foodProduction;
  private int technologyProduction;
  
  public Region(){
  }

  //Constructor for assigning name to region (before Players are assigned)
  public Region (String n){
    setName(n);
    setOwner(null);
    setUnits(null);
    this.adjRegions = new ArrayList<Region>();
  }
  
  public Region(AbstractPlayer p, Unit u){// will need to be modified
    setOwner(p);
    setUnits(u);
    this.adjRegions = new ArrayList<Region>();
  }

  public void assignRegion(AbstractPlayer p, Unit u) {
    //assigns this region to Player p with Unit .. is this not just setOwner + setUnits? Do we need this?
    setOwner(p);
    setUnits(u);
  }
  //Setters
  public void setOwner(AbstractPlayer p){
    this.owner = p;
  }
  public void setUnits(Unit u){
    this.units = u;
  }
  public void setAdjRegions(List<Region> adj){
    this.adjRegions = adj;
  }
  public void setName(String n){
    this.name = n;
  }
  public void setSize(int s){
    this.regionSize = s;
  }
  public void setFoodProduction(int f){
    this.foodProduction = f;
  }
  public void setTechProduction(int t){
    this.technologyProduction = t;
  }
  //Getters
  public int getTechProduction(){
    return technologyProduction;
  }
  public int getFoodProduction(){
    return foodProduction;
  }
  public int getSize(){
    return regionSize;
  }
  public AbstractPlayer getOwner(){
    return owner;
  }
  public Unit getUnits(){
    return units;
  }
  public List<Region> getAdjRegions(){
    return adjRegions;
  }

  public String getName(){
    return name;
  }
}
