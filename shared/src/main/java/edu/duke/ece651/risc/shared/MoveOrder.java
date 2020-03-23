package edu.duke.ece651.risc.shared;

public class MoveOrder extends SourceDestinationOrder {
  //this class defines how to execute a move order operation on the board
  private static final long serialVersionUID = 9L; //is there a more intuitive numbering we could use?

  public MoveOrder(Region s, Region d, Unit u){
    this.source = s;
    this.destination = d;
    this.units = u;
  }
  @Override
  public int getPriority(){
    return Constants.MOVE_PRIORITY;
  }
	@Override
  public String doSourceAction() {
    //remove units from source region
    source.setUnits(new Unit(source.getUnits().getUnits() - this.units.getUnits()));
    return "";
  }

  @Override
  public String doDestinationAction(){
    //add units to destination region
    destination.setUnits(new Unit(destination.getUnits().getUnits()+this.units.getUnits()));
    return (destination.getOwner().getName() + " moved " + units.getUnits() + " units from " + source.getName() + " to " + destination.getName() + "\n");
	}


}
