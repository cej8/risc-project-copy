package edu.duke.ece651.risc.shared;

public class MoveOrder extends SourceDestinationUnitOrder {
  // this class defines how to execute a move order operation on the board
  private static final long serialVersionUID = 9L; // is there a more intuitive numbering we could use?

  public MoveOrder(Region s, Region d, Unit u) {
    this.source = s;
    this.destination = d;
    this.units = u;
  }

  @Override
  public int getPriority() {
    return Constants.MOVE_PRIORITY;
  }

  @Override
  public String doAction() {
    int cost = source.findShortestPath(destination).getTotalCost() * units.getTotalUnits();
    source.getOwner().getResources().getFuelResource().useFuel(cost);
    source.getUnits().subtractUnits(this.units);
    destination.getUnits().addUnits(this.units);
    StringBuilder sb = new StringBuilder(destination.getOwner().getName() + " moved " + units.getTotalUnits() + " units from " + source.getName() + " to " + destination.getName() + "\n");
    return sb.toString();

  }

}



