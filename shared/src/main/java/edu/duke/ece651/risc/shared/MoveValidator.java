package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.*;

// Class to validate if a move is allowed based on game rules
// Must own both endpoints, have a path between them, have enough source units, not remove all source units,
// have enough fuel, and source is not plagued
// Also cannot move to the same region started from
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
    if(m.getSource().getPlague()){
      return false;
    }
    if(m.getSource()==m.getDestination()){
      System.out.println("Source cannot also be destination");
      return false;
    }
    if (!m.getSource().getOwner().getName().equals(player.getName())
        || !m.getDestination().getOwner().getName().equals(player.getName())) {
      System.out.println(player.getName()+" does not own source or destination");
      return false;
    }
    if (hasValidRegionPath(m.getSource(), m.getDestination())) {
      int shortPathLen = m.getSource().findShortestPath(m.getDestination()).getTotalCost();
      if (player.getResources().getFuelResource().getFuel() >=  shortPathLen *  m.getUnits().getTotalUnits()) {
         System.out.println(player.getName()+" move order good.");
         return true;
       }
       System.out.println(player.getName()+" move order failed. Player did not have enough fuel for a path cost of "+ shortPathLen * m.getUnits().getTotalUnits());
       return false;
    }
    System.out.println(player.getName()+" move order no path.");
    return false;
  }

  // Validate the order is acceptable
  @Override
  public boolean validateOrders(List<MoveOrder> moveList) {
    for(MoveOrder move : moveList){
       if(!isValidMove(move) || !validateUnits(move)){
         return false;
       }
    }
    return true;
  }


  // rules)
  // @Override
  // Ensure at least one unit is left behind in region moving from (based on game
  private boolean hasEnoughUnits(MoveOrder m){
    int totalUnits = m.getSource().getUnits().getTotalUnits();
    int moveUnits = m.getUnits().getTotalUnits();
    if (totalUnits > moveUnits) {
      return true;
    }
    else {
      return false;
    }
  }

  public boolean validateUnits(MoveOrder move) {
    Region tempSource = move.getSource().getRegionByName(tempBoard, move.getSource().getName());
    Region tempDest = move.getDestination().getRegionByName(tempBoard, move.getDestination().getName());
    Unit sourceUnits = tempSource.getUnits();
    Unit moveUnits = new Unit(move.getUnits().getUnits());

    MoveOrder moveCopy = new MoveOrder(tempSource, tempDest, moveUnits);
    boolean validMove = hasEnoughUnits(moveCopy); //true if have enough total units for move
      
    // set validMove to false if any of these are false: at least 1 sourceUnit, 1
    // moveUnit, and sourceUnits > moveUnits in each index of source
    for (int i = 0; i < sourceUnits.getUnits().size(); i++) { // for each index of the source units
      if (sourceUnits.getUnits().get(i).equals(0) && moveUnits.getUnits().get(i).equals(0)){
          continue;   
      }
      else if ((sourceUnits.getUnits().get(i) < moveUnits.getUnits().get(i)) || (moveUnits.getUnits().get(i) < 0)) { //if don't have enough sourceUnits or try to move negative
        validMove = false;
      }
    }
    if (validMove && this.hasValidRegionPath(tempSource, tempDest)) {
      moveCopy.doAction();
    }
    else {
      if (!validMove) {
        System.out.println("Move failed: sourceUnits are " + sourceUnits.getUnits() + " but moveUnits are " + moveUnits.getUnits()); // this is just for testing
      }
      else{
        System.out.println("Move failed: there is no valid path between Region " + tempSource.getName()
              + " and Region " + tempDest.getName()); // this is just for testing
          System.out.println("Cost to traverse this path = "/* + tempSource.findShortestPath(tempDest).getTotalCost() */
              + "; " + tempSource.getOwner().getName() + " has "
              + tempSource.getOwner().getResources().getFuelResource().getFuel() + " fuel remaining");
      }
      return false;
    }
    return true;
  }

}
