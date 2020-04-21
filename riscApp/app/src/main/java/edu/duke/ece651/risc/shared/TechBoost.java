package edu.duke.ece651.risc.shared;

import java.util.*;

public class TechBoost implements PlayerOrder {
  //this class represents an upgrade order that is issued by a player to increase their maximum technology level
  private static final long serialVersionUID = 15L;
  
  private AbstractPlayer player;
  public TechBoost(AbstractPlayer p){
    setPlayer(p);
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
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> players = new HashSet<String>();
    players.add(player.getName());
    //Only player can see
    return Arrays.asList(players);
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
