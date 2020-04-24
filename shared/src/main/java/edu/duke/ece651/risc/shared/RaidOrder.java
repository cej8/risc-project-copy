package edu.duke.ece651.risc.shared;

import java.util.*;
import com.google.common.collect.Sets;

public class RaidOrder extends SourceDestinationOrder{
  private static final long serialVersionUID = 56L;

  public RaidOrder(Region source, Region destination){
    this.source = source;
    this.destination = destination;
  }

  @Override
  public int getPriority(){
    return Constants.RAID_PRIORITY;
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
    return Arrays.asList(playersSource,
                         playersDestination,
                         Sets.union(playersSource, playersDestination),
                         playersSource,
                         new HashSet<String>(Arrays.asList(source.getOwner().getName())),
                         new HashSet<String>(Arrays.asList(destination.getOwner().getName())));
  }

  @Override
  public List<String> doAction() {
    //Remove fuel and tech times max((sourceTechLevel - destTechLevel),1)/5
    int fuelCost = destination.getOwner().getResources().getFuelResource().getFuel();
    int techCost = destination.getOwner().getResources().getTechResource().getTech();
    double multiplier = Math.max((source.getOwner().getMaxTechLevel().getMaxTechLevel() - destination.getOwner().getMaxTechLevel().getMaxTechLevel()), 1)*1.0/5.0;

    fuelCost *= multiplier;
    techCost *= multiplier;
    
    source.getOwner().getResources().getFuelResource().addFuel(fuelCost);
    source.getOwner().getResources().getTechResource().addTech(techCost);
    destination.getOwner().getResources().getFuelResource().useFuel(fuelCost);
    destination.getOwner().getResources().getTechResource().useTech(techCost);
    return Arrays.asList(source.getOwner().getName() + " raided ",
                         destination.getName(),
                         " from ",
                         source.getName(),
                         "\nYou gained " + fuelCost + " fuel and " + techCost + " tech!",
                         "\nYou lost " + fuelCost + " fuel and " + techCost + " tech!");
  }

}
