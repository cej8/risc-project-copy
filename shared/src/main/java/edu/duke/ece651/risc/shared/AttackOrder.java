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
  public void doAction() {
    // remove units from source (source location)
    System.out.println(source.getOwner().getName() + " is attacking " + destination.getOwner().getName() + "'s "
                       + destination.getName() + " region with " + this.units.getUnits() + " units!"); //I just added this in for clarity but if these messages aren't going to the user then obivously not necessary
    source.setUnits(new Unit(source.getUnits().getUnits() - this.units.getUnits()));
    // Continue executing attack until one player has no more units left in region or attack group
    while (!isWinner()) {
      // loseUnits represents which player will lose a unit (aka they lost the roll)
      Region loseUnits = rollHelper(source, destination);
      if (loseUnits == destination) {
        destination.setUnits(new Unit(destination.getUnits().getUnits() - 1));
      } else {
        units = new Unit(this.units.getUnits() - 1);
      }
      System.out.println("Defending units remaining: " + destination.getUnits().getUnits() + "; Attacking units remaining: " + this.units.getUnits());
    }
  }
  // method to check if a player has won attack round
  private boolean isWinner(){
    if (this.units.getUnits() == 0){
      System.out.println(this.destination.getOwner().getName() + " (defender) retains their region " + destination.getName());
      return true;
    } else if (destination.getUnits().getUnits() == 0) {
      System.out.println(source.getOwner().getName() + " (attacker) takes over the region " + destination.getName());
      destination.assignRegion(source.getOwner(),this.units);
      System.out.println(
          "Updated "+ destination.getName() + " region: " + destination.getOwner().getName() + " now owns "+ destination.getName() + " with " + destination.getUnits().getUnits() + " unit(s)");
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
