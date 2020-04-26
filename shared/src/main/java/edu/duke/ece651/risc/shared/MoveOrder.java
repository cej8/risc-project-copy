package edu.duke.ece651.risc.shared;

import java.util.*;
import com.google.common.collect.Sets; 

// Class for MoveOrders (move units between your regions)
// Simple removes units from source and adds them to destination
// Must also remove fuel cost for moving units (smallest path sum of sizes)*(number of units)
public class MoveOrder extends SourceDestinationUnitOrder {
  private static final long serialVersionUID = 9L; // is there a more intuitive numbering we could use?

  public MoveOrder(Region s, Region d, Unit u) {
    this.source = s;
    this.destination = d;
    this.units = u;
  }

  @Override
  public int getPriority() {
    return Constants.MOVE_PRIORITY;
  }

  // Move only visible to endpoints
  @Override
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> playersSource = new HashSet<String>();
    //Get source + regions adjacent
    playersSource.add(source.getOwner().getName());
    for(Region adj : source.getAdjRegions()){
      playersSource.add(adj.getOwner().getName());
    }
    Set<String> playersDestination = new HashSet<String>();
    //Get destination + regions adjacent
    playersDestination.add(destination.getOwner().getName());
    for(Region adj : destination.getAdjRegions()){
      playersDestination.add(adj.getOwner().getName());
    }
    //Source can only see moveing somewhere, destination can only see move from somewhere
    return Arrays.asList(Sets.union(playersSource, playersDestination),
                         playersSource,
                         Sets.union(playersSource, playersDestination),
                         playersDestination);
  }

  // Removes units from source, adds them to destination, removes fuel cost from source player
  @Override
  public List<String> doAction() {
    int cost = source.findShortestPath(destination).getTotalCost() * units.getTotalUnits();
    source.getOwner().getResources().getFuelResource().useFuel(cost);
    source.getUnits().subtractUnits(this.units);
    destination.getUnits().addUnits(this.units);
    return Arrays.asList(destination.getOwner().getName() + " moved " + units.getTotalUnits() + " units from ",
                         source.getName(),
                         " to ",
                         destination.getName());
  }

}



