package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.*;

// Class to validate if a move is allowed based on game rules
public class MoveValidator implements ValidatorInterface<MoveOrder> {
  private Board tempBoard;
  private AbstractPlayer player;

  public MoveValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;
  }

  // method to check if there is a valid path from one region to another (player
  // must own all regions they move through / to)
  private boolean hasValidRegionPath(Region start, Region end) {

    // Maintains set of visited nodes (owned by player)
    Set<Region> visited = new HashSet<Region>();
    // Queue of regions to search
    Queue<Region> pq = new ArrayDeque<Region>();
    pq.add(start);

    Region currentRegion;

    // While there is still a region to search
    while ((currentRegion = pq.poll()) != null) {
      // Add to visited set
      visited.add(currentRegion);
      // For adjacent
      for (Region adj : currentRegion.getAdjRegions()) {
        // If not owner then ignore
        if (!adj.getOwner().getName().equals(player.getName())) {
          continue;
        }
        // If adjacent is endpoint
        if (adj == end) {
          return true;
        }
        // Otherwise if not already visited add to queue
        if (!visited.contains(adj)) {
          pq.add(adj);
        }
      }
    }
    // If nothing left in queue all have been searched --> can't be done
    return false;
  }

  // helper method
  public boolean isValidMove(MoveOrder m) {
    if (!m.getSource().getOwner().getName().equals(player.getName())
        || !m.getDestination().getOwner().getName().equals(player.getName())) {
      return false;
    }
    // can we get there through regions owned?

    if (hasValidRegionPath(m.getSource(), m.getDestination())) {

      //if (player.getFood() >= m.getSource().findShortestPath(m.getDestination()).getTotalCost()) {
      if (player.getResources().getFoodResource().getFood() >= m.getSource().findShortestPath(m.getDestination()).getTotalCost()) {
        // do we have enough food resources to travel shortest path?
        return true;
      }
      return false;
    }
    return false;
  }

  // Validate the order is acceptable
  @Override
  public boolean validateOrders(List<MoveOrder> moveList) {
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
      // move.doAction();
    }
    // if all moves are valid
    return true;
  }

  // Ensure at least one unit is left behind in region moving from (based on game
  // rules)
  @Override
  public boolean validateUnits(List<MoveOrder> m) {
    for (MoveOrder move : m) {
      Region tempSource = tempBoard.getRegionByName(move.getSource().getName());
      Region tempDest = tempBoard.getRegionByName(move.getDestination().getName());
      Unit sourceUnits = tempSource.getUnits();
      // TODO: WARNING the following line will clear all bonues
      // Unit moveUnits = new Unit(move.getUnits().getUnits());

      // TODO: commented out the rest of the code for compliation
      // MoveOrder moveCopy = new MoveOrder(tempSource, tempDest, moveUnits);
      // // make sure at least 1 sourceUnit, 1 moveUnit, and sourceUnits > moveUnits
      // if ((sourceUnits.getUnits() > moveUnits.getUnits()) &&
      // (sourceUnits.getUnits() > 0) && (moveUnits.getUnits() > 0)) {
      // moveCopy.doSourceAction();
      // moveCopy.doDestinationAction();
      // } else {
      // System.out.println("Move failed: sourceUnits are " + sourceUnits.getUnits() +
      // " but moveUnits are " + moveUnits.getUnits()); //this is just for testing
      // return false;
      // }
    }
    return true;

  }

}