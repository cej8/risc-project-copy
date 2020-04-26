package edu.duke.ece651.risc.shared;

import java.util.*;

// Class that maintain resource boost order
// Allows user to increase "tier" of region to increase amount of resources it produces
// Each "tier" requires a individual tech level, actual order requires investment of resources
public class ResourceBoost extends DestinationOrder {
  private static final long serialVersionUID = 24L;

  public ResourceBoost(Region d) {
    this.destination = d;
  }

  // Priority accessor
  @Override
  public int getPriority() {
    return Constants.UPGRADE_RESOURCE_PRIORITY;
  }

  // Visiblity
  @Override
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> playersDestination = new HashSet<String>();
    //Get destination + regions adjacent
    playersDestination.add(destination.getOwner().getName());
    for(Region adj : destination.getAdjRegions()){
      playersDestination.add(adj.getOwner().getName());
    }
    //Adjacent only ones who can see
    return Arrays.asList(playersDestination);
  }

  // reduces cost from region's player and increases tier by one
  @Override
  public List<String> doAction() {
    // cost of a resource boost is size * total units * multiplier
    int cost =(int)(destination.getSize() * destination.getUnits().getTotalUnits()
               * destination.getRegionLevel().getMultiplier());
     // remove cost of a resource boost from a player
     destination.getOwner().getResources().getTechResource().useTech(cost);
    // upgrade region level (increment level and update multiplier)
    destination.getRegionLevel().upgradeLevel();
    // Message "(Player) upgraded planet (region) to region level (level)."
    return Arrays.asList(destination.getOwner().getName() + " upgraded planet " + destination.getName() + " to region level " + destination.getRegionLevel().getRegionLevel() + ".");
  }

}
