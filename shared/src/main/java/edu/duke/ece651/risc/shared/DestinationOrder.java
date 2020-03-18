package edu.duke.ece651.risc.shared;

// Class contains relevant fields for order operations with only a destination
public abstract class DestinationOrder implements OrderInterface {

  private static final long serialVersionUID = 10L;
  protected Region destination;
  protected Unit units;

  public void setDestination(Region destination){
    this.destination = destination;
  }
  public Region getDestination(){
    return destination;
  }
  
  @Override
  abstract public void doAction();

  @Override
  public void convertOrderRegions(Board board){
    for(Region r : board.getRegions()){
      if(r.getName().equals(this.getDestination().getName())){
        this.setDestination(r);
      }
    }
  }
}
