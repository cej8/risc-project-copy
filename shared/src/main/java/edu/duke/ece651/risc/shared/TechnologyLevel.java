package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class TechnologyLevel implements Serializable {
  //this class reprensents the maximum technolgy level of a player
  private static final long serialVersionUID = 14L;
  private int maxTechLevel;
  private int costToUpgrade;
  public TechnologyLevel(){
    this.maxTechLevel = Constants.STARTING_TECH_LEVEL;
    this.costToUpgrade = Constants.STARTING_UPGRADE_COST;
  }
public int getMaxTechLevel() {
	return maxTechLevel;
}
  public void upgradeLevel(){
    maxTechLevel++;
    costToUpgrade += 25 * (maxTechLevel-1);
  }
public int getCostToUpgrade() {
	return costToUpgrade;
}
  

}