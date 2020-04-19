package edu.duke.ece651.risc.shared;

import java.util.List;

public class CloakValidator implements ValidatorInterface<CloakOrder>{
  private Board tempBoard;
  private AbstractPlayer player;

  public CloakValidator(AbstractPlayer player, Board boardCopy){
    this.tempBoard = boardCopy;
    this.player = player;
  }

  @Override
  public boolean validateOrders(List<CloakOrder> orders){
    for(CloakOrder order : orders){
      if(!isValidOrder(order)){
        return false;
      }
    }
    return true;
  }

  public boolean isValidOrder(CloakOrder order){
    Region destination = order.getDestination().getRegionByName(tempBoard, order.getDestination().getName());
    //Ensure player owns destination
    if(!destination.getOwner().getName().equals(player.getName())){
      return false;
    }
    //Ensure player has enough tech
    if(destination.getOwner().getResources().getTechResource().getTech() < Constants.CLOAK_COST){
      return false;
    }
    //Ensure player tech level at least 3
    if(destination.getOwner().getMaxTechLevel().getMaxTechLevel() < 3){
      return false;
    }
    //If above true then commit
    order.doAction();
    return true;
  }
}
