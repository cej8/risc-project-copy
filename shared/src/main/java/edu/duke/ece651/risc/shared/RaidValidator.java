package edu.duke.ece651.risc.shared;

import java.util.*;

// Class to validate if a Raid is allowed based on game rules
// Must ensure regions are adjacent, raider owns source, raider doesn't own destination, source NOT plagued, and that only 1 RaidOrder occurs
public class RaidValidator implements ValidatorInterface<RaidOrder> {
  private Board tempBoard;
  private AbstractPlayer player;
  // Value for if hasRaided during turn
  private boolean hasRaided = false;

  public RaidValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;
  }

  @Override
  public boolean validateOrders(List<RaidOrder> orders){
    //Check valid and hasn't happened already
    for(RaidOrder order : orders){
      if(hasRaided || !isValidOrder(order)){
        return false;
      }
      hasRaided = true;
    }
    return true;
  }
  
  public boolean isValidOrder(RaidOrder order){
    //Simply check a s->d and d->s path exists
    Region destination = order.getDestination().getRegionByName(tempBoard, order.getDestination().getName());
    Region source = order.getSource().getRegionByName(tempBoard, order.getSource().getName());
    
    //Ensure player owns starting region
    if(!player.getName().equals(source.getOwner().getName())){
      return false;
    }
    //Ensure player does not own ending region
    if(player.getName().equals(destination.getOwner().getName())){
      return false;
    }

    //Do not allow if plague
    if(source.getPlague()){
      return false;
    }

    boolean destSource = false;
    boolean sourceDest = false;

    // Ensure dest-->source connection
    for(Region adj : destination.getAdjRegions()){
      if(adj.getName().equals(source.getName())){
        destSource = true;
        break;
      }
    }
    
    // Ensure source-->dest connection
    for(Region adj : source.getAdjRegions()){
      if(adj.getName().equals(destination.getName())){
        sourceDest = true;
        break;
      }
    }
    
    
    return destSource && sourceDest;
  }

}
