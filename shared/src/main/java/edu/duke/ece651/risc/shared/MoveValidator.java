package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoveValidator implements ValidatorInterface<MoveOrder> {
  private boolean hasValidPath(Region start, Region end, Set<Region> visited) {
    // helper method
    // find a path of connected nodes from start to end
    // Set<Region> visited = new HashSet<Region>();
    visited.add(start);
    for (Region neighbor : start.getAdjRegions()) {
      if (visited.contains(neighbor)) {
        continue;// check if already visited
      }
      visited.add(neighbor);
      if (start.getOwner().getName().equals(neighbor.getOwner().getName())) {// owned by the same player
        if (neighbor == end) {
          return true;
        }
        return hasValidPath(neighbor, end, visited);
      }
    }
    return false;
  }
  // helper method
  public boolean isValidMove(MoveOrder m) {
    // owned by the same person
    if (hasValidPath(m.getSource(), m.getDestination(), new HashSet<Region>())) {
      // and have path to get there via adjacent regions
     return true;
    }
    return false;
  }
	@Override
	public boolean regionsAreValid(List<MoveOrder> moveList) {
	  for (MoveOrder move : moveList) {
      if (!isValidMove(move)) {
        return false;
      }
      move.doAction(); 
    }
    // if all moves are valid
    return true;
	}
 private boolean sourceDestinationOrderIsValid(List<SourceDestinationOrder> o) {
    for (SourceDestinationOrder order : o) {
      int sourceUnits = order.getSource().getUnits().getUnits();
      int orderUnits = order.getUnits().getUnits();
      // make sure at least 1 sourceUnit, 1 orderUnit, and sourceUnits > orderUnits
      if ((sourceUnits > orderUnits) && (sourceUnits > 0) && (orderUnits > 0)) {
        order.doAction();
      } else {
        System.out.println("Order failed: sourceUnits are " + sourceUnits + " but orderUnits are " + orderUnits); //this is just for testing
        return false;
      }
    }
    return true;
  }

	@Override
	public boolean unitsAreValid(List<MoveOrder> m) {
	    // check to make sure numUnits in source < MoveOrder units
    List<SourceDestinationOrder> orders = new ArrayList<SourceDestinationOrder>();
    for (MoveOrder move : m) {
      orders.add(move);
    }
    System.out.print("Move order: ");
    return sourceDestinationOrderIsValid(orders);

	}

}
