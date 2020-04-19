package edu.duke.ece651.risc.shared;

import java.util.*;
import com.google.common.collect.Sets; 

public class TeleportOrder extends SourceDestinationUnitOrder {
  //this class defines how to execute a teleport order of units from one region to another
  private static final long serialVersionUID = 22L;
  public TeleportOrder(Region s, Region d, Unit u){
    this.source = s;
    this.destination = d;
    this.units = u;
  }

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
                         playersDestination,
                         Sets.union(playersSource, playersDestination));
  }

  @Override
  public List<String> doAction() {
    //remove cost of tech resources from a player (50*number of units)
    int cost = units.getTotalUnits() * Constants.TELEPORT_COST;
    source.getOwner().getResources().getTechResource().useTech(cost);
    source.getUnits().subtractUnits(this.units);
    destination.getUnits().addUnits(this.units);
    return Arrays.asList((destination.getOwner().getName() + " teleported " + units.getTotalUnits() + " units from "),
                        source.getName(),
                        " to ",
                        destination.getName(),
                        ".");
  }

  @Override
  public int getPriority() {
    return Constants.TELEPORT_ORDER_PRIORITY;
  }
}
