package edu.duke.ece651.risc.shared;

import java.util.*;

// Class to validate if a Raid is allowed based on game rules
public class RaidValidator implements ValidatorInterface<RaidOrder> {
  private Board tempBoard;
  private AbstractPlayer player;
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

    for(Region adj : destination.getAdjRegions()){
      if(adj.getName().equals(source.getName())){
        destSource = true;
        break;
      }
    }
    
    for(Region adj : source.getAdjRegions()){
      if(adj.getName().equals(destination.getName())){
        sourceDest = true;
        break;
      }
    }
    
    
    return destSource && sourceDest;
  }

}
