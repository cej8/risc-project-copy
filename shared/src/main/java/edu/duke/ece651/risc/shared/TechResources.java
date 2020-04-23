package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class TechResources implements Serializable{
  private static final long serialVersionUID = 19L;
  private int techStash;

  public TechResources(int startingTech){
    this.techStash = startingTech;
  }
  public void setTech(int techStash){
    this.techStash = techStash;
  }
  public int getTech(){
    return this.techStash;
  }
    public void addTech(int tech){
    this.techStash = techStash + tech;
  }
  public void useTech(int tech){
    this.techStash = techStash - tech;
  }
}
