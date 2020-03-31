package edu.duke.ece651.risc.shared;

import java.io.*;
// Abstract class to handle Player specifc methods 
public abstract class AbstractPlayer implements Serializable, Comparable<AbstractPlayer>{
  protected String name;
  protected boolean isPlaying;
  protected Boolean isWatching;
  protected int foodStash;
  protected int techStash;
  protected TechnologyLevel maxTechLevel;
  
  private static final long serialVersionUID = 5L;

  public int getFood(){
    return this.foodStash;
  }
  public int getTech(){
    return this.techStash;
  }
  public void addFood(int food){
    this.foodStash = foodStash + food;
  }
  public void useFood(int food){
    this.foodStash = foodStash - food;
  }
  public void addTech(int tech){
    this.techStash = techStash + tech;
  }
  public void useTech(int tech){
    this.techStash = techStash - tech;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public boolean isPlaying() {
    return isPlaying;
  }
  public void setPlaying(boolean isPlaying) {
    this.isPlaying = isPlaying;
  }
  public Boolean isWatching(){
    return isWatching;
  }
  public void setWatching(boolean isWatching){
    this.isWatching = Boolean.valueOf(isWatching); //new Boolean(isWatching);
  }
  @Override
  public int compareTo(AbstractPlayer p){
    return this.name.compareTo(p.getName());
  }
public TechnologyLevel getMaxTechLevel() {
	return maxTechLevel;
}

}

 
  



    
       
  

    
     
