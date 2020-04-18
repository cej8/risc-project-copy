package edu.duke.ece651.risc.shared;

public abstract class DestinationOrder implements RegionOrder {
  private static final long serialVersionUID = 40L;
  protected Region destination;

  public void setDestination(Region destination){
    this.destination = destination;
  }
  public Region getDestination(){
    return destination;
  }

  @Override
  abstract public int getPriority();

  @Override
  public void findValuesInBoard(Board board){
    for(Region r : board.getRegions()){
        if(r.getName().equals(this.destination.getName())){
          this.destination = r;
        }
    }     
  }

}
