package edu.duke.ece651.risc.shared;

import java.util.Random;

public class AttackOrder extends SourceDestinationOrder {
  // this class defines how to execute an attack order operation on the board
  private static final long serialVersionUID = 12L;

  /* public AttackOrder(Region s, Region d, Unit u) {
    this.source = s;
    this.destination = d;
    this.units = u; */
  //suggested renaming of variables so it's clear what they represent 

  public AttackOrder(Region attacker, Region defender, Unit attackingUnits){
      this.source = attacker;
      this.destination = defender;
      this.units = attackingUnits;
    }    
  

  @Override
  //TODO-- This method takes in a Board and doesn't use it which is a little surprising/confusing. Board should probably be used otherwise this method does not really align with the doAction(Board) method from interface 
  public void doAction(Board b) {
    // remove units from source (attack location)
    //TODO-- is "attack location" accurate? thought the destination was thte location of the attack 
    System.out.println(source.getOwner().getName() + " is attacking " + destination.getOwner().getName() + "'s "
                       + destination.getName() + " region with " + this.units.getUnits() + " units!"); //I just added this in for clarity but if these messages aren't going to the user then obivously not necessary
    source.setUnits(new Unit(source.getUnits().getUnits() - this.units.getUnits()));
    while (!isWinner()) {
      //TODO-- can you provide comments about each block of code? I'm not entirely sure what loseUnits represents as a Region just by its name
      Region loseUnits = rollHelper(source, destination);
      if (loseUnits == destination) {
        destination.setUnits(new Unit(destination.getUnits().getUnits() - 1));
      } else {
        units = new Unit(this.units.getUnits() - 1);
      }
      //System.out.println("Destination: " + destination.getUnits().getUnits() + " ; Attack Units: " + this.units.getUnits());
      // I'm assuming this is Destination "Units" vs Attack units. I think switching between source/destination and attacker/defender is a little confusing. From the player's perspective, I think any messages they receive should use attack/defender and/or player names, rather than source and destination. And referring to the regions by their actual names. Suggested change below, sorry if misinterpreted intended results:
 System.out
   .println("Defending units remaining: " + destination.getUnits().getUnits() + "; Attacking units remaining: " + this.units.getUnits());
    }
  }
  
  private boolean isWinner(){
    if (this.units.getUnits() == 0){
      //System.out.println("Destination wins (def)");
      //suggested change below to be more descriptive 
      System.out.println(this.destination.getOwner().getName() + " (defender) retains their region " + destination.getName());
      return true;
    } else if (destination.getUnits().getUnits() == 0) {
      //System.out.println("Source wins (attack)");
      //suggested change below to be more descriptive 
      System.out.println(source.getOwner().getName() + " (attacker) takes over the region " + destination.getName());
      destination.assignRegion(source.getOwner(),this.units);
      // System.out.println(
      //     "Updated destination region: " + destination.getOwner().getName() + ": " + destination.getUnits().getUnits());
      //.suggested change below for clarity
     System.out.println(
          "Updated "+ destination.getName() + " region: " + destination.getOwner().getName() + " now owns "+ destination.getName() + " with " + destination.getUnits().getUnits() + " unit(s)");
      return true; 
    } else {
      return false;
    }
  }
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
