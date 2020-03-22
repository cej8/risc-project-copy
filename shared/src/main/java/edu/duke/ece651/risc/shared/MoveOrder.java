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
  public void doAction() {
    //remove units from source region and add them to the destination region
    doAction(source, destination, units); 
	}
 public void doAction(Region s, Region d, Unit u) {
    s.setUnits(new Unit(s.getUnits().getUnits() - u.getUnits()));
    d.setUnits(new Unit(d.getUnits().getUnits()+u.getUnits()));
    System.out.println(u.getUnits() + ":" + s.getName() +"->" + d.getName());
  }


  public void doAction(Region s, Region d, Unit u) {
    s.setUnits(new Unit(s.getUnits().getUnits() - u.getUnits()));
    d.setUnits(new Unit(d.getUnits().getUnits()+u.getUnits()));
  }
  
  // @Override //pass in copy of board 
  // public void validateAction(Board temp) {
  //   Region tSource = temp.getRegionByName(source.getName());
  //   Region tDestination = temp.getRegionByName(destination.getName());
  //    tSource.setUnits(new Unit(tSource.getUnits().getUnits() - this.units.getUnits()));
  //   tDestination.setUnits(new Unit(tDestination.getUnits().getUnits()+this.units.getUnits()));
  // }
}
//TODO -- between orders, how make sure all get same copy of board?
