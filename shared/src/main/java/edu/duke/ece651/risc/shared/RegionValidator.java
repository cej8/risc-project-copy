package edu.duke.ece651.risc.shared;

import java.util.*;

public class RegionValidator implements ValidatorInterface {
  private boolean isConnected(Region start, Region end){
    //find a path of connected nodes from start to end
    for(Region neighbor: start.getAdjRegions()){
      if(neighbor == end){
        return true;
      }
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
    return false;
	}

	@Override
	public boolean isValidPlacement(PlacementOrder p, AbstractPlayer player, Board b) {
		//check that player ownd the regions they are placing units in
  //   List<Region> ownedRegions=b.getPlayerToRegionMap().get(p.getDestination().getOwner()player);
  //   for(Region region: ownedRegions){
  //     if(region == p.getDestination()){
  //       return true;
  //     }
  //   }
  	return false;
	 }

}
