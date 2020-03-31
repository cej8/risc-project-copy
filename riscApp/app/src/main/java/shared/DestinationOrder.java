package shared;

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

  public Unit getUnits(){
    return units;
  }
  
  @Override
  public String doSourceAction() {return "";};

  @Override
  abstract public String doDestinationAction();

@Override
abstract public int getPriority();


  @Override
  public void convertOrderRegions(Board board){
    for(Region r : board.getRegions()){
      if(r.getName().equals(this.getDestination().getName())){
        this.setDestination(r);
      }
    }
  }

}
