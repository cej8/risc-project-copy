package edu.duke.ece651.risc.shared;

public class TechBoost implements PlayerOrder {
  //this class represents an upgrade order that is issued by a player to increase their maximum technology level
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
	@Override
	public void findValuesInBoard(Board board) {
    for(AbstractPlayer p : board.getPlayerSet()){
      if(p.getName().equals(this.player.getName())){
        this.setPlayer(p);
      }
     
    }
	
		
	}
	public AbstractPlayer getPlayer() {
		return player;
	}
	public void setPlayer(AbstractPlayer player) {
		this.player = player;
	}

}
