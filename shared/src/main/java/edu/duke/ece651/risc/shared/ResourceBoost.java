package edu.duke.ece651.risc.shared;
//this class represents an order issued bty a playter that will allow them to upgrade the planet to another level whihc will cause the resource production to go up

public class ResourceBoost extends DestinationOrder {
  private static final long serialVersionUID = 24L;

  public ResourceBoost(Region d) {
    this.destination = d;
  }

  @Override
  public String doAction() {
    // cost of a resource boost is size * total units * multiplier ****** should we multiply the cost by the multiplier as well?
     int cost = destination.getSize() * destination.getUnits().getTotalUnits();
     // remove cost of a resource boost from a player
     destination.getOwner().getResources().getTechResource().useTech(cost);
    // upgrade region level (increment level and update multiplier)
    destination.getRegionLevel().upgradeLevel();

    StringBuilder sb = new StringBuilder(destination.getOwner().getName() + " upgraded planet " + destination.getName()
        + " to region level " + destination.getRegionLevel().getRegionLevel() + " \n");
    return sb.toString();
  }

  @Override
  public int getPriority() {
    return Constants.UPGRADE_RESOURCE_PRIORITY;
  }
}
