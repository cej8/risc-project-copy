package edu.duke.ece651.risc.shared;

public abstract class SourceDestinationOrder implements OrderInterface {
  //this class contains relevant fields for order operations between two regions (source and destination
  protected Region source;
  protected Region destination;
  protected Unit units;
	@Override
  abstract public void doAction(Board b);

}
