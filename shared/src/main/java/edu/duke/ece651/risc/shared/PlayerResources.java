package edu.duke.ece651.risc.shared;

import java.io.*;

// Class that holds all resource types the player has
public class PlayerResources implements Serializable {
  private static final long serialVersionUID = 17L;
  private FuelResources foodResource;
  private TechResources techResource;

  public PlayerResources(int startingFood, int startingTech){
    this.foodResource = new FuelResources(startingFood);
    this.techResource = new TechResources(startingTech);
  }
  public FuelResources getFuelResource(){
    return this.foodResource;
  }
  public TechResources getTechResource(){
    return this.techResource;
  }

}
