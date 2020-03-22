package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

// Class to check / validate that an attack order is allowed based on game conditions
public class AttackValidator implements ValidatorInterface<AttackOrder> {
 // helper method
private Board tempBoard;
  private AbstractPlayer player;
  
  public AttackValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;
  }
  // Checks if attack is valid given conditions
  public boolean isValidAttack(AttackOrder a) {
    // regions must be owned by different players
    //starting must be owned by player
    if(!a.getSource().getOwner().getName().equals(player.getName())){
      System.out.println("player does not own source");
      return false;
    }
    
    if (a.getDestination().getOwner().getName().equals(player.getName())) {
       System.out.println("player cannot own destionation");
     return false;
     }
    // regions must be adjacent
    for (Region neighbor : a.getSource().getAdjRegions()) {
      if (neighbor.getName().equals(a.getDestination().getName())) {
        return true;
      }
    }
  System.out.println("regions are not adjdacent");
    return false;
  }
   @Override
   public boolean validateRegions(List<AttackOrder> attackList) {
	 for (AttackOrder attack : attackList) {
     if (!isValidAttack(attack)) {
        System.out.println("Attack not valid");
        return false;
      }
      //attack.doAction();
    }

  // if all attacks are valid
    return true;
	}
@Override
public boolean validateOrders(List<AttackOrder> attackList) {
  boolean validRegions = validateRegions(attackList);
    boolean validUnits = validateUnits(attackList);
    return validRegions && validUnits;
  }
  // Method to validate corrent units in each region
  	@Override
	public boolean validateUnits(List<AttackOrder> a) {
	 // check to make sure numUnits in source < attackOrder units
     for (AttackOrder attack : a) {
      Region tempSource = tempBoard.getRegionByName(attack.getSource().getName());
      Region tempDest = tempBoard.getRegionByName(attack.getDestination().getName());
      Unit sourceUnits = tempSource.getUnits();
      Unit attackUnits = new Unit(attack.getUnits().getUnits());
      // make sure at least 1 sourceUnit, 1 attackUnit, and sourceUnits > attackUnits
      if ((sourceUnits.getUnits() > attackUnits.getUnits()) && (sourceUnits.getUnits() > 0) && (attackUnits.getUnits() > 0)) {
        attack.doAction(tempSource, tempDest, attackUnits);
      } else {
        System.out.println("Attack failed: sourceUnits are " + sourceUnits.getUnits() + " but attackUnits are " + attackUnits.getUnits()); //this is just for testing
        return false;
      }
    }
    return true;
  }

	
}
