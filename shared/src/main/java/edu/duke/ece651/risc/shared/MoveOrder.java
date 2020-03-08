package edu.duke.ece651.risc.shared;

public class MoveOrder extends SourceDestinationOrder {
  //this class defines how to execute a move order operation on the board
  public MoveOrder(Region s, Region d, Unit u){
    this.source = s;
    this.destination = d;
    this.units = u;
  }
	@Override
  public void doAction(Board b) {
    //remove units from source region and add them to the destination region
    source.setUnits(new Unit(source.getUnits().getUnits() - this.units.getUnits()));
    destination.setUnits(new Unit(destination.getUnits().getUnits()+this.units.getUnits()));
	}

}
