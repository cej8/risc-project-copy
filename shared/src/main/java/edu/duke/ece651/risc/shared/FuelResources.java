package edu.duke.ece651.risc.shared;

import java.io.Serializable;

// Fuel resource for players
public class FuelResources implements Serializable {
  private static final long serialVersionUID = 18L;
  private int fuelStash;

  public FuelResources(int startingFuel){
    this.fuelStash = startingFuel;
  }

  /* BEGIN ACCESSORS */
  public void setFuel(int startingFuel){
    this.fuelStash = startingFuel;
  }
  public int getFuel(){
    return this.fuelStash;
  }
  /* END ACCESSORS*/

  //Helpers to add/remove from current amount
  public void addFuel(int fuel){
    this.fuelStash = fuelStash + fuel;
  }
  public void useFuel(int fuel){
    this.fuelStash = fuelStash - fuel;
  }

}
