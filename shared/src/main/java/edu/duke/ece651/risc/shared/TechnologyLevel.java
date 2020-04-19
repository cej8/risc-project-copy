package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public class TechnologyLevel implements Serializable {
  //this class reprensents the maximum technolgy level of a player
  private static final long serialVersionUID = 14L;
  private int maxTechLevel;
  private int costToUpgrade;
  private int maxRegionLevel;
  
  public TechnologyLevel(){
    this.maxTechLevel = Constants.STARTING_TECH_LEVEL;
    this.costToUpgrade = Constants.STARTING_UPGRADE_COST;
    this.maxRegionLevel = Constants.STARTING_REGION_LEVEL;
  }

  public int getMaxTechLevel() {
    return maxTechLevel;
  }
public int getMaxRegionLevel() {
	return maxRegionLevel;
}


  
  public TechnologyLevel(int maxTechLevel){
    this();
    for(int i = this.maxTechLevel; i < maxTechLevel; i++){
      upgradeLevel();
    }
  }

  
  public void upgradeLevel(){
    maxTechLevel++;
    costToUpgrade += 25 * (maxTechLevel-1);
    setRegionUnlock();
  }

public int getCostToUpgrade() {
	return costToUpgrade;
}
  private void setRegionUnlock(){
    //upgrade region level on even tech levels
    if(maxRegionLevel==4){
      return;//cannot co past region level 4
    }
    if(maxTechLevel%2==0){
      this.maxRegionLevel++;
    }
   
  }

}
