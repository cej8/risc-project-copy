package edu.duke.ece651.risc.shared;

public class SpyUpgradeOrder extends DestinationOrder{
  private static final long serialVersionUID = 50L;

  public SpyUpgradeOrder(Region destination){
    this.destination = destination;
  }

  @Override
  public int getPriority(){
    return Constants.SPYUPGRADE_PRIORITY;
  }

  @Override
  public String doAction(){
    //Remove single level 0 unit
    destination.getUnits().subtractUnits(new Unit(1));
    //Add new spy to region
    destination.getSpies(destination.getOwner().getName()).add(new Spy());
    //Remove tech cost
    destination.getOwner().getResources().getTechResource().useTech(Constants.SPYUPGRADE_COST);
    return destination.getOwner().getName() + " upgraded to a spy in region " + destination.getName() + "\n";
  }

}
