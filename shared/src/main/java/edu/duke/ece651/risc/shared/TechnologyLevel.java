package edu.duke.ece651.risc.shared;

import java.io.Serializable;

// Object that maintains technology level of player
// Also maintains region levels and costs of upgrades
// Also used to generate cost of TechBoost given current level
public class TechnologyLevel implements Serializable {
  private static final long serialVersionUID = 14L;
  private int maxTechLevel;
  private int costToUpgrade;
  private int maxRegionLevel;
  
  public TechnologyLevel(){
    this.maxTechLevel = Constants.STARTING_TECH_LEVEL;
    this.costToUpgrade = Constants.STARTING_UPGRADE_COST;
    this.maxRegionLevel = Constants.STARTING_REGION_LEVEL;
  }
  
  public TechnologyLevel(int maxTechLevel){
    this();
    for(int i = this.maxTechLevel; i < maxTechLevel; i++){
      upgradeLevel();
    }
  }

  /* BEGIN ACCESSORS */
  public int getMaxTechLevel() {
    return maxTechLevel;
  }

  public int getCostToUpgrade() {
    return costToUpgrade;
  }

  public int getMaxRegionLevel() {
    return maxRegionLevel;
  }
  /* END ACCESSORS */

  // Helper to increase tech level/cost
  public void upgradeLevel(){
    maxTechLevel++;
    costToUpgrade += 25 * (maxTechLevel-1);
    setRegionUnlock();
  }

  // Helper to increase regionlevel/gate unlocks
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
