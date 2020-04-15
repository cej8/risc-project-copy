package edu.duke.ece651.risc.shared;

import java.util.List;
// Validation class for teleport move
public class TeleportValidator implements ValidatorInterface<TeleportOrder> {
  private Board tempBoard;
  private AbstractPlayer player;

  public TeleportValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;

  }

  public boolean validTechLevel() {
    // check that requirement of a tech level of at least 4 is met
    if (player.getMaxTechLevel().getMaxTechLevel() < 4) {
      return false;
    }
    return true;
  }

  public boolean validOwnership(List<TeleportOrder> tList) {
    // check that a player owns both teh source and destination
    for (TeleportOrder t : tList) {
      if (!t.getSource().getOwner().getName().equals(player.getName())
          || !t.getDestination().getOwner().getName().equals(player.getName())) {
        System.out.println(player.getName() + " does not own source or destination");
        return false;
      }
    }
    return true;// all orders have been successfully checked
  }

  public boolean validUnits(List<TeleportOrder> t) {
    // ensure that units are valid cumulatively for all teleport orders
    for (TeleportOrder teleport : t) {
      Region tempSource = teleport.getSource().getRegionByName(tempBoard, teleport.getSource().getName());
      Region tempDest = teleport.getDestination().getRegionByName(tempBoard, teleport.getDestination().getName());
      Unit sourceUnits = tempSource.getUnits();
      Unit teleportUnits = new Unit(teleport.getUnits().getUnits());
      TeleportOrder teleportCopy = new TeleportOrder(tempSource, tempDest, teleportUnits);
      boolean validMove = true;
      for (int i = 0; i < sourceUnits.getUnits().size(); i++) { // for each index of the source units
        if (sourceUnits.getUnits().get(i).equals(0) && teleportUnits.getUnits().get(i).equals(0)) {
          continue;
        } else if (((sourceUnits.getUnits().get(i) - 1) < teleportUnits.getUnits().get(i))
            || (teleportUnits.getUnits().get(i) < 0)) {
          validMove = false;
        }
      }
      if (validMove) {
        teleportCopy.doAction();
      }
      else {
        System.out.println("Teleport failed: sourceUnits are " + sourceUnits.getUnits() + " but teleportUnits are "
            + teleportUnits.getUnits());
        return false;

      }

    }
    return true;
  }

  public boolean validResources(List<TeleportOrder> tList) {
    int sum = 0;
    // add up total cost of all teleports
    for (TeleportOrder t : tList) {
     int cost=t.getUnits().getTotalUnits() * Constants.TELEPORT_COST;
     System.out.println("Cost is " + cost);
     sum += cost;
    }
      System.out.println("Player has " + player.getResources().getTechResource().getTech()
        + " tech resources. Total cost of teleports is " + sum);
    // if total cost is lower than the players tech resources, return true
    return player.getResources().getTechResource().getTech() >= sum;
  }

  @Override
  public boolean validateOrders(List<TeleportOrder> orders) {
 
    return validTechLevel() && validOwnership(orders) && validUnits(orders) && validResources(orders);
  }

}
