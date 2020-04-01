package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class FoodResources implements Serializable {
  private static final long serialVersionUID = 18L;
  private int foodStash;

  public FoodResources(int startingFood){
    this.foodStash = startingFood;
  }
  public int getFood(){
    return this.foodStash;
  }
  public void addFood(int food){
    this.foodStash = foodStash + food;
  }
  public void useFood(int food){
    this.foodStash = foodStash - food;
  }

}
