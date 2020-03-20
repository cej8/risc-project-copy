package edu.duke.ece651.risc.shared;

public abstract class SourceDestinationOrder implements OrderInterface {
  //this class contains relevant fields for order operations between two regions (source and destination)
  private static final long serialVersionUID = 8L; 
  protected Region source;
  protected Region destination;
  protected Unit units;
	@Override
  abstract public void doAction();

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
    public void setSource(Region s){
     this.source =s;   
    }
     public void setDestination(Region d){
     this.destination =d;   
    }

	public Region getSource() {
		return source;
	}
	public Region getDestination() {
		return destination;
	}
	public Unit getUnits() {
		return units;
	}

}
