package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

// Class to check / validate that an attack order is allowed based on game conditions
public class AttackValidator implements ValidatorInterface<AttackMove> {
private Board tempBoard;
  private AbstractPlayer player;
  
  public AttackValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;
  }
  // Checks if attack is valid given conditions
  public boolean isValidAttack(AttackMove a) {
    if(a.getSource().getPlague()){
      return false;
    }
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
  System.out.println("regions are not adjacent");
    return false;
  }
  // @Override
   public boolean validateRegions(List<AttackMove> attackList) {
	 for (AttackMove attack : attackList) {
     if (!isValidAttack(attack)) {
        System.out.println("Attack not valid");
        return false;
      }
      
    }

  // if all attacks are valid
    return true;
	}
@Override
public boolean validateOrders(List<AttackMove> attackList) {
  boolean validRegions = validateRegions(attackList);
    boolean validUnits = validateUnits(attackList);
    return validRegions && validUnits;
  }
  // Method to validate corrent units in each region
  //  	@Override

      private boolean hasEnoughUnits(AttackMove m){
    int totalUnits = m.getSource().getUnits().getTotalUnits();
    int moveUnits = m.getUnits().getTotalUnits();
    if (totalUnits > moveUnits) {
      return true;
    }
    else{
      return false;
    }
  
	public boolean validateUnits(List<AttackMove> a) {
	 // check to make sure numUnits in source < attackOrder units
     for (AttackMove attack : a) {
      Region tempSource = attack.getSource().getRegionByName(tempBoard, attack.getSource().getName());
      Region tempDest = attack.getDestination().getRegionByName(tempBoard, attack.getDestination().getName());
      Unit sourceUnits = tempSource.getUnits();
      Unit attackUnits = new Unit(attack.getUnits().getUnits());
      AttackMove attackCopyMove = new AttackMove(tempSource, tempDest, attackUnits);
      // make sure at least 1 sourceUnit, 1 attackUnit, and sourceUnits > attackUnits
       boolean validMove = hasEnoughUnits(attackCopyMove);
      // set validMove to false if any of these are false: at least 1 sourceUnit, 1
      // moveUnit, and sourceUnits > moveUnits in each index of source
      
      for (int i = 0; i < sourceUnits.getUnits().size(); i++) { // for each index of the source units
        if (sourceUnits.getUnits().get(i).equals(0) && attackUnits.getUnits().get(i).equals(0)){
            continue;   
        }
        else if ((sourceUnits.getUnits().get(i) < attackUnits.getUnits().get(i)) || (attackUnits.getUnits().get(i) < 0)) {
          validMove = false;
        }
      }
      if(validMove){
        attackCopyMove.doAction();
      
      } else {
        System.out.println("Attack failed: sourceUnits are " + sourceUnits.getTotalUnits() + " but attackUnits are " + attackUnits.getTotalUnits()); //this is just for testing
        return false;
      }
    }
    return true;
  }

	
}
