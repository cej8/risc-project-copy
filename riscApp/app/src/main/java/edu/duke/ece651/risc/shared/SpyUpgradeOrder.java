package edu.duke.ece651.risc.shared;

import java.util.*;
// Class to allow level 0 unit to upgrade to a Spy unit type

// New spy started in region called in, spy can be moved on same turn as creation

public class SpyUpgradeOrder extends DestinationOrder{
  private static final long serialVersionUID = 50L;

  public SpyUpgradeOrder(Region destination){
    this.destination = destination;
  }

  @Override
  public int getPriority(){
    return Constants.SPYUPGRADE_PRIORITY;
  }

  @Override
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> players = new HashSet<String>();
    players.add(destination.getOwner().getName());
    //Only player invoking can see
    return Arrays.asList(players);
  }

  @Override
  public List<String> doAction(){
    //Remove single level 0 unit
    destination.getUnits().subtractUnits(new Unit(1));
    //Add new spy to region
    destination.getSpies(destination.getOwner().getName()).add(new Spy());
    //Remove tech cost
    destination.getOwner().getResources().getTechResource().useTech(Constants.SPYUPGRADE_COST);
    return Arrays.asList( (destination.getOwner().getName() + " upgraded to a spy in region " + destination.getName() + ".") );
  }

}
