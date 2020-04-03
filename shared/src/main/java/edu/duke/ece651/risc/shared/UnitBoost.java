package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

public class UnitBoost extends DestinationOrder {
  private static final long serialVersionUID = 16L;
  //This class takes in an Unit object representing the number of each type of unit from the region the player wants to boost 1 tech level 
  public UnitBoost(Region d, Unit u) {
    this.destination = d;
    this.units = u;

  }

  @Override
  public String doAction() {
    StringBuilder sb = new StringBuilder(
        destination.getOwner() + " upgraded units from " + destination.getUnits().getUnits());
    //create list of new units player will have after boost
    List<Integer> upgradedUnitList = new ArrayList<Integer>(); //e.g. [0,3,0,0,0,0,0] means after boost, player will have 3 new level 1 units
    upgradedUnitList.add(0);//no units can't be upgraded to level 0
    // add [3,0,0,0,0,0] to [0] to get [0,3,0,0,0,0,0]
    for (int i = 0; i < units.getUnits().size()-1; i++){ //can't upgrade 6
      upgradedUnitList.add(units.getUnits().get(i));
    }
    Unit upgradedUnits = new Unit(upgradedUnitList);
    //add a new upgraded unit to list
    destination.getUnits().addUnits(upgradedUnits);
    //remove old unit from list
    destination.getUnits().subtractUnits(units);
    sb.append(" to " + destination.getUnits().getUnits() + "\n");
    return sb.toString();
  }

  @Override
  public int getPriority() {
    return Constants.UPGRADE_UNITS_PRIORITY;
  }

}
