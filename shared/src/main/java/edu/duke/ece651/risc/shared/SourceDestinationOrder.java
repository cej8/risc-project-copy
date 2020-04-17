package edu.duke.ece651.risc.shared;

public abstract class SourceDestinationOrder implements RegionOrder {
  private static final long serialVersionUID = 45L;
  protected Region source;
  protected Region destination;

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

  @Override
  abstract public int getPriority();

  @Override
  public void findValuesInBoard(Board board){
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
