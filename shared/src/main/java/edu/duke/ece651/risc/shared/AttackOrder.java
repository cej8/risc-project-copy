package edu.duke.ece651.risc.shared;

import java.util.Random;

public class AttackOrder extends SourceDestinationOrder {
  // this class defines how to execute an attack order operation on the board
  private static final long serialVersionUID = 12L;
  

  public AttackOrder(Region attacker, Region defender, Unit attackingUnits){
      this.source = attacker;
      this.destination = defender;
      this.units = attackingUnits;
    }    
  @Override
  public int getPriority(){
    return Constants.ATTACK_PRIORITY;
  }

  @Override
  public String doSourceAction() {
    // remove units from source (source location)
    //TODO --WARNING: this line will erase all bonuses
    // source.setUnits(new Unit(source.getUnits().getUnits() - units.getTotalUnits()));
    return (source.getOwner().getName() + " is attacking " + destination.getOwner().getName() + "'s " + destination.getName() + " region with " + units.getTotalUnits() + " units!\n"); 
  }
  @Override
  public String doDestinationAction(){
    // Continue executing attack until one player has no more units left in region or attack group
    while (!isWinner(source, destination, units)) {
      // loseUnits represents which player will lose a unit (aka they lost the roll)
      Region loseUnits = rollHelper(source, destination);
      if (loseUnits == destination) {
            //TODO --WARNING: this line will erase all bonuses
        //    destination.setUnits(new Unit(destination.getUnits().getTotalUnits() - 1));
          } else {
         //TODO --WARNING: this line will erase all bonuses
        //units = new Unit(units.getTotalUnits() - 1);
      }
      System.out.println("Defending units remaining: " + destination.getUnits().getTotalUnits() + "; Attacking units remaining: " + units.getTotalUnits());
    }

    String returnString;
    
    if (units.getTotalUnits() == 0) {
      returnString = (destination.getOwner().getName() + " (defender) retains their region ");
    }
    else{
     returnString = (source.getOwner().getName() + " (attacker) takes over the region ");
    }
    returnString += destination.getName() + ", " + destination.getUnits().getTotalUnits() + " units survived!\n";
    return returnString;
    
  }
  
  // method to check if a player has won attack round
  private boolean isWinner(Region s, Region d, Unit u) {
    if (u.getTotalUnits() == 0) {
      System.out
          .println(d.getOwner().getName() + " (defender) retains their region " + d.getName());
      return true;
    } else if (d.getUnits().getTotalUnits() == 0) {
      System.out.println(s.getOwner().getName() + " (attacker) takes over the region " + d.getName());
      d.assignRegion(s.getOwner(), u);
      System.out.println("Updated " + d.getName() + " region: " + d.getOwner().getName()
          + " now owns " + d.getName() + " with " + d.getUnits().getTotalUnits() + " unit(s)");
      return true;
    } else {
      return false;
    }
  }
  
  // method to randomly select number on 20 sided dice
  private Region rollHelper(Region defRegion, Region attackRegion){
    Random diceTwenty = new Random();
    int attackResult = diceTwenty.nextInt(20);
    int defResult = diceTwenty.nextInt(20);
    if (defResult >= attackResult){
      return attackRegion;
    }
    return defRegion;
    }


}
