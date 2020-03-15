package edu.duke.ece651.risc.shared;

import java.util.*;

public class RegionValidator implements ValidatorInterface {
  private boolean hasValidPath(Region start, Region end,Set<Region> visited) {
    // find a path of connected nodes from start to end
    // Set<Region> visited = new HashSet<Region>();
    visited.add(start);
    for (Region neighbor : start.getAdjRegions()) {
      if (visited.contains(neighbor)) {
        continue;// check if already visited
      }
<<<<<<< HEAD
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

  @Override
  public boolean isValidMove(MoveOrder m, Board b) {
    // owned by the same person
    // List<Region>
    // ownedRegions=b.getPlayerToRegionMap().get(m.getSource().getOwner());
    // for(Region region: ownedRegions){
    // if(region==m.getDestination()){//destination is in source owner's list of
    // regions
    if (hasValidPath(m.getSource(), m.getDestination(), new HashSet<Region>())) {
      // and have path to get there via adjacent regions
      return true;

    }
    // }
    // }
=======
      return isConnected(neighbor, end);
    }
    
    return false;
  }
	@Override
	public boolean isValidMove(MoveOrder m, Board b) {
    //owned by the same person
  //  List<Region> ownedRegions=b.getPlayerToRegionMap().get(m.getSource().getOwner());
//    for(Region region: ownedRegions){
  //    if(region==m.getDestination()){//destination is in source owner's list of regions
    //    if(isConnected(m.getSource(), m.getDestination())){
           //and have path to get there via adjacent regions
      //    return true;
        //}
      //}
    //}
>>>>>>> a9e6f79e776aca868e75a57510ba951a0b9eaaba
    return false;
  }

  @Override
  public boolean isValidPlacement(PlacementOrder p, AbstractPlayer player, Board b) {
    // check that player ownd the regions they are placing units in
    // List<Region>
    // ownedRegions=b.getPlayerToRegionMap().get(p.getDestination().getOwner()player);
    // for(Region region: ownedRegions){
    // if(region == p.getDestination()){
    // return true;
    // }
    // }
    return false;
  }

}
