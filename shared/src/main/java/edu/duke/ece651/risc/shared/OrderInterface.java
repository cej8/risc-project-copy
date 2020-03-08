package edu.duke.ece651.risc.shared;

public interface OrderInterface {
  //interface outline the behvaior of a (user defined) order
    //precondition: regions and orders have been validated before calling this method
  //postcondition: board will be in a valid state upon retunr from the method 
  public void doAction(Board b);
}
