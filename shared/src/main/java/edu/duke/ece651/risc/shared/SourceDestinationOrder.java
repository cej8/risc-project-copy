package edu.duke.ece651.risc.shared;

public abstract class SourceDestinationOrder implements RegionOrder {
  //this class contains relevant fields for order operations between two regions (source and destination)
  private static final long serialVersionUID = 8L; 
  protected Region source;
  protected Region destination;
  protected Unit units;

  public void setDestination(Region destination){
    this.destination = destination;
  }
  public Region getDestination(){
    return destination;
  }
  public void setSource(Region source){
    this.source = source;
  }
  public Region getSource(){
    return source;
  }

  public Unit getUnits(){
    return units;
  }
  //@Override
  //abstract public String doSourceAction();

  //@Override
  //abstract public String doDestinationAction();

  @Override
  abstract public int getPriority();

  @Override
  public void convertOrderRegions(Board board){
    for(Region r : board.getRegions()){
      if(r.getName().equals(this.getDestination().getName())){
        this.setDestination(r);
      }
      if(r.getName().equals(this.getSource().getName())){
        this.setSource(r);
      }
    }
  }
@Override
abstract public String doAction();


}
