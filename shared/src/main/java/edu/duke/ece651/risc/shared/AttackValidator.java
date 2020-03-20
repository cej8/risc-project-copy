package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

public class AttackValidator implements ValidatorInterface<AttackOrder> {
 // helper method
  public boolean isValidAttack(AttackOrder a) {
    // regions must be owned by different players
    if (a.getSource().getOwner().getName().equals(a.getDestination().getOwner().getName())) {
      return false;
    }
    // regions must be adjacent
    for (Region neighbor : a.getSource().getAdjRegions()) {
      if (neighbor == a.getDestination()) {
        return true;
      }
    }
    return false;
  }
	@Override
	public boolean regionsAreValid(List<AttackOrder> attackList) {
	 for (AttackOrder attack : attackList) {
      if (!isValidAttack(attack)) {
        return false;
      }
      attack.doAction();
    }
    // if all attacks are valid
    return true;
	}
  private boolean sourceDestinationOrderIsValid(List<SourceDestinationOrder> o) {
    for (SourceDestinationOrder order : o) {
      int sourceUnits = order.getSource().getUnits().getUnits();
      int orderUnits = order.getUnits().getUnits();
      // make sure at least 1 sourceUnit, 1 orderUnit, and sourceUnits > orderUnits
      if ((sourceUnits > orderUnits) && (sourceUnits > 0) && (orderUnits > 0)) {
        order.doAction();
      } else {
        //   System.out.println("Order failed: sourceUnits are " + sourceUnits + " but orderUnits are " + orderUnits); //this is just for testing
        return false;
      }
    }
    return true;
  }
	@Override
	public boolean unitsAreValid(List<AttackOrder> a) {
	 // check to make sure numUnits in source < attackOrder units
    List<SourceDestinationOrder> orders = new ArrayList<SourceDestinationOrder>();
    for (AttackOrder attack : a) {
      orders.add(attack);
    }
    //   System.out.print("Attack order: ");
    return sourceDestinationOrderIsValid(orders);  
	}

}
