package edu.duke.ece651.risc.shared;

// Class defines how to execute a placement order operation on the board
public class PlacementOrder extends DestinationOrder {
  private static final long serialVersionUID = 11L;
  public PlacementOrder(Region d, Unit u){
    this.destination = d;
    this.units = u;
  }
  @Override
  public void doAction(Board b){
    destination.setUnits(this.units);
  }

}
