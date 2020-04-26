package edu.duke.ece651.risc.shared;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Validator for technology boost
// Must check if player has enough resources and hasn't already hit max
// Also ensures only one can happen per turn
public class TechBoostValidator implements ValidatorInterface<TechBoost> {
  private Board tempBoard;
  private AbstractPlayer player;
  private boolean hasBoosted = false;

  public TechBoostValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;
  }

  @Override
  public boolean validateOrders(List<TechBoost> orders) {
    for(TechBoost order: orders){
      if(hasBoosted || !isValidOrder(order)){
        return false;
      }
      hasBoosted = true;
    }
    return true;
  }

  public boolean isValidOrder(TechBoost order){
    order.findValuesInBoard(tempBoard);
    if(player.getResources().getTechResource().getTech()-player.getMaxTechLevel().getCostToUpgrade()<0){
      //not enough tech resources to perform desired upgrade
      return false;
    }
    //max level of 6 reached: not more tech levels available
    if(player.getMaxTechLevel().getMaxTechLevel()==Constants.MAX_TECH_LEVEL){
      return false;
    }
    return true;
  }

}
