package edu.duke.ece651.risc.shared;

import java.util.*;
import com.google.common.collect.Sets;

// Class for RaidOrder type
// Allows a player to "raid" an adjacent enemy region
// Will steal (remove from other and add to self) Max((SourceTechLevel - DestTechLevel),1)/5 percent of total
// With max tech of 6 and starting at 1 this means level 6 vs level 1 will take all
// Guaranteed minimum 20%
public class RaidOrder extends SourceDestinationOrder{
  private static final long serialVersionUID = 56L;

  public RaidOrder(Region source, Region destination){
    this.source = source;
    this.destination = destination;
  }

  // Priority accessor
  @Override
  public int getPriority(){
    return Constants.RAID_PRIORITY;
  }

  // Visibility
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

  // Removes Max((SourceTechLevel - DestTechLevel),1)/5 percent of total resources from destination and gives to source
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
    // Message of form "(Player) raided (Region) from (Region)"
    // Raider can see "You gained X fuel and Y tech!"
    // Raidee can see "You lost X fuel and Y tech!"
    return Arrays.asList(source.getOwner().getName() + " raided ",
                         destination.getName(),
                         " from ",
                         source.getName(),
                         "\nYou gained " + fuelCost + " fuel and " + techCost + " tech!",
                         "\nYou lost " + fuelCost + " fuel and " + techCost + " tech!");
  }

}
