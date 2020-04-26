package edu.duke.ece651.risc.shared;

import java.io.*;
// Abstract class to handle Player specifc methods 
// Current only HumanPlayer inherets from this method
// Essentially contains all player-specific information for the board (resources, name, etc.)
public abstract class AbstractPlayer implements Serializable, Comparable<AbstractPlayer>{
	// String of player's name, will match user's login
  protected String name;
  // boolean of if the player has lost yet (set to false on losing last region)
  protected boolean isPlaying;
  // Boolean for if user is spectating, left as null until prompted
  // If set to false (not spectating) then user removed from match
  // If spectaing then set to true, defaults back to null if user DC's
  protected Boolean isWatching;
  // User's max tech level
  protected TechnologyLevel maxTechLevel;
  // User's resources
  protected PlayerResources playerResource;

  private static final long serialVersionUID = 5L;

  /* BEGIN ACCESSORS */
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
    this.isWatching = Boolean.valueOf(isWatching);
  }
  public void setWatchingNull(){
    this.isWatching = null;
  }

  public TechnologyLevel getMaxTechLevel() {
    return maxTechLevel;
  }
  public void setMaxTechLevel(TechnologyLevel maxTechLevel){
    this.maxTechLevel = maxTechLevel;
  }
  // Helper to convert int into TechnologyLevel
  public void setMaxTechLevel(int maxTechLevel){ 
    this.maxTechLevel = new TechnologyLevel(maxTechLevel);
  }

  public PlayerResources getResources(){
    return this.playerResource;
  }
  public void setPlayerResource(PlayerResources playerResource){
    this.playerResource = playerResource;
  }

  // Comparator for object, uses name as metric
  @Override
  public int compareTo(AbstractPlayer p){
    return this.name.compareTo(p.getName());
  }

  // Equality for object, uses name as metric
  @Override
  public boolean equals(Object obj){
    if(obj == null){
      return false;
    }

    if(!AbstractPlayer.class.isAssignableFrom(obj.getClass())){
      return false;
    }

    final AbstractPlayer comp = (AbstractPlayer) obj;
    return comp.getName().equals(name);

  }

}

 
  



    
       
  

    
     
