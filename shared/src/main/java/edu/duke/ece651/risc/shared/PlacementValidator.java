package edu.duke.ece651.risc.shared;

import java.util.List;

public class PlacementValidator implements ValidatorInterface<PlacementOrder> {
  AbstractPlayer player;
  Unit playerUnits;
  public PlacementValidator(AbstractPlayer p, Unit u){
    this.player=p;
    this.playerUnits=u;
  }
    // helper method
  public boolean isValidPlacement(PlacementOrder p, AbstractPlayer ap) {
    // check that player owns the regions they are placing units in
    if(p.getDestination().getOwner()==ap){
      return true;
    }
    return false;
  }
  	@Override
	public boolean regionsAreValid(List<PlacementOrder> placementList) {
	 for (PlacementOrder place : placementList) {
      if (!isValidPlacement(place, this.player)) {
        return false;
      }
      place.doAction();
    }
    // if all placements are valid
    return true;

  }
	

	@Override
	public boolean unitsAreValid(List<PlacementOrder> orders) {
		// TODO Auto-generated method stub
		return false;
	}

}
