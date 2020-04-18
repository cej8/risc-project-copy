package edu.duke.ece651.risc.shared;

public class AttackMove extends SourceDestinationUnitOrder {
  //this class handles the initial moving of units in preparation for an attack of a region
  //Repurposed code from doSourceaAction of AttackOrder
  private static final long serialVersionUID= 20L;
   public AttackMove(Region attacker, Region defender, Unit attackingUnits){
      this.source = attacker;
      this.destination = defender;
      this.units = attackingUnits;
    }
    @Override
  public int getPriority(){
      return Constants.ATTACK_MOVE_PRIORITY;
  }
  public String doAction(){
  // remove units from source (source location)
    source.getUnits().subtractUnits(this.units);
    //source.getOwner().useFood(Constants.ATTACK_COST);
    int cost = units.getTotalUnits() * Constants.ATTACK_COST;
    source.getOwner().getResources().getFuelResource().useFuel(cost);
    return (source.getOwner().getName() + " is attacking " + destination.getOwner().getName() + "'s "
        + destination.getName() + " region with " + units.getUnits() + " units!\n");

  }

}
