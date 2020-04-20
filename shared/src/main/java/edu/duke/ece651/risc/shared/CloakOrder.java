package edu.duke.ece651.risc.shared;

import java.util.*;
// Class allows for cloaking of region for 3 turns, preventing adjacent viewing

// Can add cloaking if already cloaked, effectlively increasing turn number passed 3
// Cost 100 tech and must have Level 3
public class CloakOrder extends DestinationOrder {
  private static final long serialVersionUID = 41L;

  public CloakOrder(Region destination){
    this.destination = destination;
  }

  @Override
  public int getPriority(){
    return Constants.CLOAK_PRIORITY;
  }

  @Override
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> players = new HashSet<String>();
    players.add(destination.getOwner().getName());
    for(Region adj : destination.getAdjRegions()){
      players.add(adj.getOwner().getName());
    }
    //All adjacent can see cloaking happened
    return Arrays.asList(players);
  }

  @Override
  public List<String> doAction(){
    destination.setCloakTurns(destination.getCloakTurns() + 3);
    destination.getOwner().getResources().getTechResource().useTech(Constants.CLOAK_COST);
    return Arrays.asList((destination.getOwner().getName() + " cloaked " + destination.getName() + " for three more turns."));
  }
}
