package edu.duke.ece651.risc.shared;

// Class contains relevant fields for order operations with only a destination
public abstract class DestinationOrder implements OrderInterface {

  private static final long serialVersionUID = 10L;
  protected Region destination;
  protected Unit units;
  @Override
  abstract public void doAction();
}
