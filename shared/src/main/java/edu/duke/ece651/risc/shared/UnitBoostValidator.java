package edu.duke.ece651.risc.shared;

import java.util.List;

public class UnitBoostValidator implements ValidatorInterface<UnitBoost> {
  private Board tempBoard;
  private AbstractPlayer player;

  public UnitBoostValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;
  }

  @Override
  public boolean validateOrders(List<UnitBoost> orders) {
    return validateRegions(orders) && validateCost(orders) && validateUnits(orders);
  }

  // make sure player owns all regions in which units are to be upgraded
  @Override
  public boolean validateRegions(List<UnitBoost> orders) {
    for (UnitBoost boost : orders) {
      if (!(validateRegion(boost))) {
        return false;
      }
    }
    return true;
  }

  // Player must own region
  public boolean validateRegion(UnitBoost order) {
    if (order.getDestination().getOwner().equals(player)) {
      return true;
    } else {
      return false;
    }
  }

  // Make sure have enough units of each type to upgrade
  // Cannot upgrade max bonus units
  @Override
  public boolean validateUnits(List<UnitBoost> orders) {
    for (UnitBoost boost : orders) {
      if (!(validateUnit(boost))) {
        return false;
      }
    }
    return true;
  }

  // return true if have enough of each type of bonus
  private boolean validateUnit(UnitBoost order) {
    Region tempDest = order.getDestination().getRegionByName(tempBoard, order.getDestination().getName());
    Unit remainingUnits = (Unit) DeepCopy.deepCopy(tempDest.getUnits());
    remainingUnits.subtractUnits(order.getUnits());
    // making sure all indices >= 0 after substracting from total
    for (int i = 0; i < remainingUnits.getUnits().size(); i++) {
      if (remainingUnits.getUnits().get(i) < 0) { // if any value @ index of units is now <0
        return false;
      }
    }
    if (!(preventBoostingMaxedLevel(order))) {// if try to boost a maxed level, false
      return false;
    }
    UnitBoost boostCopy = new UnitBoost(tempDest, order.getUnits());
    boostCopy.doAction();
    return true;
  }

  // returns false if player tries to upgrade units that already have max bonus
  private boolean preventBoostingMaxedLevel(UnitBoost order) {
    // if value of last index is anything other than 0, return false
    if ((order.getUnits().getUnits().get((order.getUnits().getUnits().size() - 1))).equals(0)) {
      return true;
    } else {
      return false;
    }
  }

  // returns true if player has enough tech to afford upgrade
  public boolean validateCost(List<UnitBoost> orders) {
    Integer totalCost = 0;
    for (UnitBoost order : orders) {
      totalCost += order.generateCost(); // count up total cost of all orders
    }
    if (player.getResources().getTechResource().getTech() >= totalCost) {
      return true;
    } else {
      return false;
    }
  }
}
