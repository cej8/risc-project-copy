package edu.duke.ece651.risc.shared;

import java.io.*;
// Abstract class to handle Player specifc methods 
public abstract class AbstractPlayer implements Serializable, Comparable<AbstractPlayer>{
  protected String name;
  protected boolean isPlaying;
  protected Boolean isWatching;

  protected TechnologyLevel maxTechLevel;
  

  protected PlayerResources playerResource;

  private static final long serialVersionUID = 5L;

  public PlayerResources getResources(){
    return this.playerResource;
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
  public void setWatchingNull(){
    this.isWatching = null;
  }
  @Override
  public int compareTo(AbstractPlayer p){
    return this.name.compareTo(p.getName());
  }
public TechnologyLevel getMaxTechLevel() {
	return maxTechLevel;
}

}

 
  



    
       
  

    
     
