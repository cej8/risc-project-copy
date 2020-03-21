package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public interface OrderInterface extends Serializable{
  //interface outline the behvaior of a (user defined) order
    //precondition: regions and orders have been validated before calling this method
  //postcondition: board will be in a valid state upon return from the method 
  public int getPriority();
  public void doAction();

  public void convertOrderRegions(Board board);

}
