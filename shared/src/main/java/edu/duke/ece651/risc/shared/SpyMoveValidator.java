package edu.duke.ece651.risc.shared;

import java.util.*;

public class SpyMoveValidator implements ValidatorInterface<SpyMoveOrder> {

  private Board tempBoard;
  private AbstractPlayer player;

  public SpyMoveValidator(AbstractPlayer player, Board boardCopy){
    this.tempBoard = boardCopy;
    this.player = player;
  }


  private boolean hasSpyPath(Region start, Region end){
    //Allows movement into enemy territory
    //Essentially checks all connected player owned regions and adjacents
    //If desired region adjacent to player owned (or starting region) then can move
    //Otherwise cannot find (would need to move 2 into enemy)
    Set<Region> visited = new HashSet<Region>();
    Queue<Region> pq = new ArrayDeque<Region>();
    pq.add(start);

    Region currentRegion;
    while((currentRegion = pq.poll()) != null){
      visited.add(currentRegion);
      for(Region adj : currentRegion.getAdjRegions()){
        if(adj == end){
          return true;
        }
        if(adj.getOwner().getName().equals(this.player.getName()) && !visited.contains(adj)){
          pq.add(adj);
        }
      }
    }

    return false;    
  }

  private boolean isValidMove(SpyMoveOrder move){
    Region start = move.getSource().getRegionByName(tempBoard, move.getSource().getName());
    Region end = move.getDestination().getRegionByName(tempBoard, move.getDestination().getName());
    AbstractPlayer player = move.getPlayer();
    //There are no spies in region
    if(start.getSpies(player.getName()).size() == 0){
      return false;
    }
    //Otherwise get first, since moved always added to end this should be the least recently moved
    Spy spy = start.getSpies(player.getName()).get(0);
    //If it has moved then all behind have...
    if(spy.getHasMoved()){
      return false;
    }

    //Check if path exists
    boolean hasPath = hasSpyPath(start, end);
    //No path
    if(!hasPath){
      return false;
    }
    //Otherwise path exists and is legal, doAction()
    move.doAction();
    return true;
  }

  @Override
  public boolean validateOrders(List<SpyMoveOrder> orders){
    for(SpyMoveOrder move : orders){
      if(!isValidMove(move)){
        return false;
      }
    }
    return true;
  }
  
}
