package edu.duke.ece651.risc.shared;

import java.io.Serializable;

// Class to set number of units for players
public class Unit implements Serializable {
  private Integer units;
  private static final long serialVersionUID = 2L; //more intuitive numbering?

  public Unit(Integer u){
    setUnits(u);
  }

  public Integer getUnits(){
    return units;
  }

  public void setUnits(Integer u){
    this.units = u;
  }
  
}
