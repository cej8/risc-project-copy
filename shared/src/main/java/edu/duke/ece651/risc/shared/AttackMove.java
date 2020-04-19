package edu.duke.ece651.risc.shared;

import java.util.*;
import com.google.common.collect.Sets; 

public class AttackMove extends SourceDestinationUnitOrder {
  //this class handles the initial moving of units in preparation for an attack of a region
  //Repurposed code from doSourceaAction of AttackOrder
  private static final long serialVersionUID= 20L;
  
  public AttackMove(Region attacker, Region defender, Unit attackingUnits){
     this.source = attacker;
     this.destination = defender;
     this.units = attackingUnits;
  }

  @Override
  public int getPriority(){
      return Constants.ATTACK_MOVE_PRIORITY;
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
    //Source can only see attacking somewhere, destination can only see attack with something
    return Arrays.asList(playersSource, 
                         Sets.union(playersSource, playersDestination),
                         playersDestination,
                         Sets.union(playersSource, playersDestination));
  }

  @Override
  public List<String> doAction(){
  // remove units from source (source location)
    source.getUnits().subtractUnits(this.units);
    //source.getOwner().useFood(Constants.ATTACK_COST);
    int cost = units.getTotalUnits() * Constants.ATTACK_COST;
    source.getOwner().getResources().getFuelResource().useFuel(cost);
    //Returns as form "(Blank) is attacking (Blank) with units"
    return Arrays.asList(source.getOwner().getName() + "'s " + source.getName(),
                         " is attacking ",
                         destination.getOwner().getName() + "'s " + destination.getName(), 
                         " region with " + units.getUnits() + " units!");

  }

}
