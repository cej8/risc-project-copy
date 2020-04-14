package edu.duke.ece651.risc.shared;

public class TeleportOrder extends SourceDestinationOrder {
  //this class defines how to execute a teleport order of units from one region to another
   private static final long serialVersionUID = 22L;
  public TeleportOrder(Region s, Region d, Unit u){
    this.source = s;
    this.destination = d;
    this.units = u;
  }
	@Override
    public String doAction() {
    //remove cost of tech resources from a player (50*number of units)
    int cost = units.getTotalUnits() * Constants.TELEPORT_COST;
    source.getOwner().getResources().getTechResource().useTech(cost);
  
    source.getUnits().subtractUnits(this.units);
    destination.getUnits().addUnits(this.units);
      StringBuilder sb = new StringBuilder(destination.getOwner().getName() + " teleported " + units.getTotalUnits() + " units from " + source.getName() + " to " + destination.getName() + "\n");
    return sb.toString();

  
	}

	@Override
	public int getPriority() {
		return Constants.TELEPORT_ORDER_PRIORITY;
	}

}
