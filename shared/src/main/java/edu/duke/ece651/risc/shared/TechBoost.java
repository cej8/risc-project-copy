package edu.duke.ece651.risc.shared;

public class TechBoost implements PlayerOrder {
  private static final long serialVersionUID = 15L;
  
  AbstractPlayer player;
  public TechBoost(AbstractPlayer p){
    this.player = p;
  }
	@Override
	public String doAction() {
    //remove technology resources
    player.getResources().getTechResource().useTech(player.getMaxTechLevel().getCostToUpgrade());
      //increase level
    player.getMaxTechLevel().upgradeLevel();
    
		return null;
	}

	@Override
	public int getPriority() {
    return Constants.UPGRADE_TECH_PRIORITY;
	}

}
