package edu.duke.ece651.risc.shared;

import java.util.*;

// Class defines how to execute a placement order operation on the board
public class PlacementOrder extends DestinationUnitOrder {
  private static final long serialVersionUID = 11L;
  public PlacementOrder(Region d, Unit u){
    this.destination = d;
    this.units = u;
  }

  @Override
  public int getPriority() {
    return Constants.PLACEMENT_PRIORITY;
  }

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

  @Override
  public List<String> doAction() {
    destination.setUnits(this.units);
    return Arrays.asList(destination.getOwner().getName() + " placed " + units.getTotalUnits() + " units at " + destination.getName() + ".");
  }
}
