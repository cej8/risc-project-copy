package edu.duke.ece651.risc.shared;

public class TechnologyLevel {
  private int maxTechLevel;
  public TechnologyLevel(){
    this.maxTechLevel = 1;
  }
public int getMaxTechLevel() {
	return maxTechLevel;
}
public void setMaxTechLevel(int maxTechLevel) {
	this.maxTechLevel = maxTechLevel;
}
  public void upgradeLevel(){
    maxTechLevel++;
  }
  

}
