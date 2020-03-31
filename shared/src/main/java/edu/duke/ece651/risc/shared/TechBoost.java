package edu.duke.ece651.risc.shared;

public class TechBoost implements PlayerOrder {
  private static final long serialVersionUID = 15L;

  Unit unit;
  AbstractPlayer player;
  public TechBoost(AbstractPlayer p){
    this.player = p;
  }
	@Override
	public String doAction() {
    //increase level
    player.getMaxTechLevel().upgradeLevel();
    //remove technology resources
    

    
		return null;
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

}
