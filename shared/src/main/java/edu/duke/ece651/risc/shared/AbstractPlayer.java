package edu.duke.ece651.risc.shared;

public abstract class AbstractPlayer {
  protected String name;
  protected boolean isPlaying;
  // setters
  public void setName(String n){
    this.name = n;
  }
  public void setisPlaying(boolean b){
    this.isPlaying = b;
  }
  // getters
  public boolean getisPlaying(){
    return isPlaying; 
  }
  public String getName(){
    return name;
  }
}
