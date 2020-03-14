package edu.duke.ece651.risc.shared;

import java.util.Random;

public class AttackOrder extends SourceDestinationOrder {
  // this class defines how to execute an attack order operation on the board
  private static final long serialVersionUID = 12L;
  public AttackOrder(Region s, Region d, Unit u){
    this.source = s;
    this.destination = d;
    this.units = u;
  }
  @Override
  public void doAction(Board b){
    // remove units from source (attack location)
    source.setUnits(new Unit(source.getUnits().getUnits() - this.units.getUnits()));
    while(!isWinner()){
      Region loseUnits = rollHelper(source, destination);
      if (loseUnits == destination){
        destination.setUnits(new Unit(destination.getUnits().getUnits() - 1));
      } else {
        units = new Unit(this.units.getUnits() - 1);
      }
      System.out.println("Destination: " + destination.getUnits().getUnits() + " ; Attack Units: " + this.units.getUnits());
    }
  }
  private boolean isWinner(){
    if (this.units.getUnits() == 0){
      System.out.println("Destination wins (def)");
      return true;
    } else if (destination.getUnits().getUnits() == 0) {
      System.out.println("Source wins (attack)");
      destination.assignRegion(source.getOwner(),this.units);
      System.out.println("Updated destination region: " + destination.getOwner().getName() + ": " + destination.getUnits().getUnits());
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
