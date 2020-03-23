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
  public void doSourceAction() {
    // remove units from source (source location)
    System.out.println(source.getOwner().getName() + " is attacking " + destination.getOwner().getName() + "'s " + destination.getName() + " region with " + units.getUnits() + " units!"); //I just added this in for clarity but if these messages aren't going to the user then obivously not necessary
    source.setUnits(new Unit(source.getUnits().getUnits() - units.getUnits()));
  }
  @Override
  public void doDestinationAction(){
    // Continue executing attack until one player has no more units left in region or attack group
    while (!isWinner(source, destination, units)) {
      // loseUnits represents which player will lose a unit (aka they lost the roll)
      Region loseUnits = rollHelper(source, destination);
      if (loseUnits == destination) {
        destination.setUnits(new Unit(destination.getUnits().getUnits() - 1));
      } else {
        units = new Unit(units.getUnits() - 1);
      }
      System.out.println("Defending units remaining: " + destination.getUnits().getUnits() + "; Attacking units remaining: " + units.getUnits());
    }
  }
  
  // method to check if a player has won attack round
  private boolean isWinner(Region s, Region d, Unit u) {
    if (u.getUnits() == 0) {
      System.out
          .println(d.getOwner().getName() + " (defender) retains their region " + d.getName());
      return true;
    } else if (d.getUnits().getUnits() == 0) {
      System.out.println(s.getOwner().getName() + " (attacker) takes over the region " + d.getName());
      d.assignRegion(s.getOwner(), u);
      System.out.println("Updated " + d.getName() + " region: " + d.getOwner().getName()
          + " now owns " + d.getName() + " with " + d.getUnits().getUnits() + " unit(s)");
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
