package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoveValidator implements ValidatorInterface<MoveOrder> {
 private Board tempBoard;

  public MoveValidator(Board boardCopy) {
    this.tempBoard = boardCopy;
  }

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
        if (neighbor.getName().equals(end.getName())) {
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
  public boolean validateOrders(List<MoveOrder> moveList){
    boolean validRegions = validateRegions(moveList);
    boolean validUnits = validateUnits(moveList);
    return validRegions && validUnits;
  }

  @Override
	public boolean validateRegions(List<MoveOrder> moveList) {
	  for (MoveOrder move : moveList) {
      if (!isValidMove(move)) {
        System.out.println("Move not valid");
        return false;
      }
      //move.doAction(); 
    }
    // if all moves are valid
    return true;
	}

  	@Override
	public boolean validateUnits(List<MoveOrder> m) {
    for (MoveOrder move : m) {
      Region tempSource = tempBoard.getRegionByName(move.getSource().getName());
      Region tempDest = tempBoard.getRegionByName(move.getDestination().getName());
      Unit sourceUnits = tempSource.getUnits();
      Unit moveUnits = new Unit(move.getUnits().getUnits());
      // make sure at least 1 sourceUnit, 1 moveUnit, and sourceUnits > moveUnits
      if ((sourceUnits.getUnits() > moveUnits.getUnits()) && (sourceUnits.getUnits() > 0) && (moveUnits.getUnits() > 0)) {
        move.doAction(tempSource, tempDest, moveUnits);
      } else {
        System.out.println("Move failed: sourceUnits are " + sourceUnits.getUnits() + " but moveUnits are " + moveUnits.getUnits()); //this is just for testing
        return false;
      }
    }
    return true;

	}


}
