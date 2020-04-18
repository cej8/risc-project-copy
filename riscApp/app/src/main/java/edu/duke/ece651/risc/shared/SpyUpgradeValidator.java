package edu.duke.ece651.risc.shared;

import java.util.List;

public class SpyUpgradeValidator implements ValidatorInterface<SpyUpgradeOrder>{
  private Board tempBoard;
  private AbstractPlayer player;

  public SpyUpgradeValidator(AbstractPlayer player, Board boardCopy){
    this.tempBoard = boardCopy;
    this.player = player;
  }

  @Override
  public boolean validateOrders(List<SpyUpgradeOrder> orders){
    for(SpyUpgradeOrder order : orders){
      if(!isValidOrder(order)){
        return false;
      }
    }
    return true;
  }

  public boolean isValidOrder(SpyUpgradeOrder order){
    Region destination = order.getDestination().getRegionByName(tempBoard, order.getDestination().getName());
    Unit destUnits = destination.getUnits();
    //Ensure player owns destination
    if(!destination.getOwner().getName().equals(player.getName())){
      return false;
    }
    //Ensure at least 1 level 0 unit
    if(destination.getUnits().getUnits().get(0) <= 0){
      return false;
    }
    //Ensure won't use only unit present
    if(destination.getUnits().getTotalUnits() == 1){
      return false;
    }
    //Ensure player has enough tech
    if(destination.getOwner().getResources().getTechResource().getTech() < Constants.SPYUPGRADE_COST){
      return false;
    }
    //If above true then commit
    order.doAction();
    return true;
  }
}
