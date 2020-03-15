package edu.duke.ece651.risc.shared;

public abstract class SourceDestinationOrder implements OrderInterface {
  //this class contains relevant fields for order operations between two regions (source and destination)
  private static final long serialVersionUID = 8L; 
  protected Region source;
  protected Region destination;
  protected Unit units;
	@Override
<<<<<<< HEAD
  abstract public void doAction(Board b);
	public Region getSource() {
		return source;
	}
	public Region getDestination() {
		return destination;
	}
	public Unit getUnits() {
		return units;
	}
=======
  abstract public void doAction();
>>>>>>> a9e6f79e776aca868e75a57510ba951a0b9eaaba

}
