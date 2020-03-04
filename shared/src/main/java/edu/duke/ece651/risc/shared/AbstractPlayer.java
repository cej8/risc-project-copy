package edu.duke.ece651.risc.shared;

public abstract class AbstractPlayer {
  private String name;
  private boolean isPlaying;
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

  
}
