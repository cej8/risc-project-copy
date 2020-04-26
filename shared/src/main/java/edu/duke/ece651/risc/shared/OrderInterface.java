package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

// Highest level order object
// Orders put into list of this type for sending multiple across socket in bundle
public interface OrderInterface extends Serializable{
  //interface outline the behvaior of a (user defined) order
    //precondition: regions and orders have been validated before calling this method
  //postcondition: board will be in a valid state upon return from the method 

  // Accessor for the order's priority in Constants.java
  public int getPriority();
  // Method to find regions on server board after socket
  public void findValuesInBoard(Board board);
  // Method to get which players can see for turn message
  public List<Set<String>> getPlayersVisibleTo();
  // Method to perform move action, returns message for action
  public List<String> doAction();


}
