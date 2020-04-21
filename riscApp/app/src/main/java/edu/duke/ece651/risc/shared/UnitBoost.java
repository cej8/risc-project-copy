package edu.duke.ece651.risc.shared;

import java.util.*;

public class UnitBoost extends DestinationUnitOrder {
  private static final long serialVersionUID = 16L;

  // This class takes in an Unit object representing the number of each type of
  // unit from the region the player wants to boost 1 tech level
  public UnitBoost(Region d, Unit u) {
    this.destination = d;
    this.units = u;

  }

  @Override
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> playersDestination = new HashSet<String>();
    //Get destination + regions adjacent
    playersDestination.add(destination.getOwner().getName());
    for(Region adj : destination.getAdjRegions()){
      playersDestination.add(adj.getOwner().getName());
    }
    //Get destination and all adjacent
    return Arrays.asList(playersDestination);
  }

  @Override
  public List<String> doAction() {
    StringBuilder sb = new StringBuilder(
        destination.getOwner().getName() + " upgraded units from " + destination.getUnits().getUnits());
    Unit upgradedUnits = getUpgradedUnits();
    int cost = generateCost();
    // remove total cost
    destination.getOwner().getResources().getTechResource().useTech(cost);
    // add a new upgraded unit to list
    destination.getUnits().addUnits(upgradedUnits);
    // remove old unit from list
    destination.getUnits().subtractUnits(units);
    sb.append(" to " + destination.getUnits().getUnits() + "\n");
    System.out.println(sb.toString());
    return Arrays.asList(sb.toString());
  }

  private Unit getUpgradedUnits() {
    // create list of new units player will have after boost
    List<Integer> upgradedUnitList = new ArrayList<Integer>(); // e.g. [0,3,0,0,0,0,0] means after boost, player will
                                                               // have 3 new level 1 units
    upgradedUnitList.add(0);// no units can't be upgraded to level 0
    // add [3,0,0,0,0,0] to [0] to get [0,3,0,0,0,0,0]
    for (int i = 0; i < units.getUnits().size() - 1; i++) { // can't upgrade 6
      upgradedUnitList.add(units.getUnits().get(i));
    }
    Unit upgradedUnits = new Unit(upgradedUnitList);
    return upgradedUnits;
  }

  //generates the cost of any one boost
  public int generateCost() {
    int cost = 0;
    Unit upgradedUnits = getUpgradedUnits(); // get list of new upgraded units
    // add cost of each upgrade to cost
    for (int i = 0; i < upgradedUnits.getUnits().size(); i++) {
      Integer numUnitsofType = upgradedUnits.getUnits().get(i);
      cost += (upgradedUnits.getCostFromTech(i) * numUnitsofType); // multiply cost to upgrade * num of units of that
                                                                   // type you're upgrading
    }
    System.out.println("Cost of upgrade = " + cost);
    return cost;
  }

  @Override
  public int getPriority() {
    return Constants.UPGRADE_UNITS_PRIORITY;
  }

}
