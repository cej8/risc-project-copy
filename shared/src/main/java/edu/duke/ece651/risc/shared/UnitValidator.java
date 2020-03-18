package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

//Class verifies that the number of units used in a order were valid
//operates under the assumption that the Regions are valid
public class UnitValidator implements ValidatorInterface {

  @Override
  public boolean attacksAreValid(List<AttackOrder> a) {
    // check to make sure numUnits in source < attackOrder units
    List<SourceDestinationOrder> orders = new ArrayList<SourceDestinationOrder>();
    for (AttackOrder attack : a) {
      orders.add(attack);
    }
    System.out.print("Attack order: ");
    return sourceDestinationOrderIsValid(orders);  
  }

  @Override
  public boolean movesAreValid(List<MoveOrder> m) {
    // check to make sure numUnits in source < MoveOrder units
    List<SourceDestinationOrder> orders = new ArrayList<SourceDestinationOrder>();
    for (MoveOrder move : m) {
      orders.add(move);
    }
    System.out.print("Move order: ");
    return sourceDestinationOrderIsValid(orders);
  }

  private boolean sourceDestinationOrderIsValid(List<SourceDestinationOrder> o) {
    for (SourceDestinationOrder order : o) {
      int sourceUnits = order.getSource().getUnits().getUnits();
      int orderUnits = order.getUnits().getUnits();
      // make sure at least 1 sourceUnit, 1 orderUnit, and sourceUnits > orderUnits
      if ((sourceUnits > orderUnits) && (sourceUnits > 0) && (orderUnits > 0)) {
        order.doAction();
      } else {
        System.out.println("Order failed: sourceUnits are " + sourceUnits + " but orderUnits are " + orderUnits); //this is just for testing
        return false;
      }
    }
    return true;
  }

 

  @Override
  public boolean placementsAreValid(List<PlacementOrder> p, AbstractPlayer player) {
    //TODO -- complete this method once we've restructured validation classes to be based on the type of order instead of region vs unit
      return false;
    }

}
