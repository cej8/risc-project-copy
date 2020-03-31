package edu.duke.ece651.risc.shared;

import java.io.*;

public class PlayerResources implements Serializable {
  private static final long serialVersionUID = 17L;
  private FoodResources foodResource;
  private TechResources techResource;

  public PlayerResources(int startingFood, int startingTech){
    this.foodResource = new FoodResources(startingFood);
    this.techResource = new TechResources(startingTech);
  }
  public FoodResources getFoodResource(){
    return this.foodResource;
  }
  public TechResources getTechResource(){
    return this.techResource;
  }

}
