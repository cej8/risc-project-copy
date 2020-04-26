package edu.duke.ece651.risc.shared;

import java.util.*;

// Class for order to increase player's technology level
// Costs scaling amount of tech depending on level (internal to TechnologyLevel) 
public class TechBoost implements PlayerOrder {
  private static final long serialVersionUID = 15L;

  private AbstractPlayer player;
  public TechBoost(AbstractPlayer p){
    setPlayer(p);
  }

  /* BEGIN ACCESSORS */
  public AbstractPlayer getPlayer() {
    return player;
  }
  public void setPlayer(AbstractPlayer player) {
    this.player = player;
  }
  /* END ACCESSORS */

  // Priority accessor
  @Override
  public int getPriority() {
    return Constants.UPGRADE_TECH_PRIORITY;
  }
  
  @Override
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> players = new HashSet<String>();
    players.add(player.getName());
    //Only player can see
    return Arrays.asList(players);
  }

  @Override
  public List<String> doAction() {
    //remove technology resources
    player.getResources().getTechResource().useTech(player.getMaxTechLevel().getCostToUpgrade());
    //increase level
    player.getMaxTechLevel().upgradeLevel();
    return Arrays.asList( (player.getName() + " upgraded to tech level " + player.getMaxTechLevel().getMaxTechLevel() + ".") );
  }

  @Override
  public void findValuesInBoard(Board board) {
    for(AbstractPlayer p : board.getPlayerSet()){
      if(p.getName().equals(this.player.getName())){
        this.setPlayer(p);
      }  
    }
  }
  
}
