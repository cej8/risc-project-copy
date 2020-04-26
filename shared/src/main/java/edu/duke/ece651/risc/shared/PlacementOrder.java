package edu.duke.ece651.risc.shared;

import java.util.*;

// Class defines how to execute a placement order operation on the board
// Effectively just puts units into destination
// Only used for initial placement of tier 0 units at start of game
public class PlacementOrder extends DestinationUnitOrder {
  private static final long serialVersionUID = 11L;
  public PlacementOrder(Region d, Unit u){
    this.destination = d;
    this.units = u;
  }

  // Priority accessor
  @Override
  public int getPriority() {
    return Constants.PLACEMENT_PRIORITY;
  }

  // All adjacent to region can see
  @Override
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> playersDestination = new HashSet<String>();
    //Get destination + regions adjacent
    playersDestination.add(destination.getOwner().getName());
    for(Region adj : destination.getAdjRegions()){
      playersDestination.add(adj.getOwner().getName());
    }
    //Get destination and all adjacent
    return Arrays.asList(playersDestination);
  }

  // Put units
  @Override
  public List<String> doAction() {
    destination.setUnits(this.units);
    // Message "(Player) placed (units) at (destination)"
    return Arrays.asList(destination.getOwner().getName() + " placed " + units.getTotalUnits() + " units at " + destination.getName() + ".");
  }
}
