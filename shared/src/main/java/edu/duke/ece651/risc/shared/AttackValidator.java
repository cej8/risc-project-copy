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
	public boolean validateRegions(List<AttackOrder> attackList) {
	 for (AttackOrder attack : attackList) {
      if (!isValidAttack(attack)) {
        return false;
      }
      attack.doAction();
    }
    // if all attacks are valid
    return true;
	}

	@Override
	public boolean validateUnits(List<AttackOrder> a) {
	 // check to make sure numUnits in source < attackOrder units
    for (AttackOrder attack : a) {
      int sourceUnits = attack.getSource().getUnits().getUnits();
      int attackUnits = attack.getUnits().getUnits();
      // make sure at least 1 sourceUnit, 1 attackUnit, and sourceUnits > attackUnits
      if ((sourceUnits > attackUnits) && (sourceUnits > 0) && (attackUnits > 0)) {
        attack.doAction();
      } else {  return false;
      }
    }
    return true;
  }
}
