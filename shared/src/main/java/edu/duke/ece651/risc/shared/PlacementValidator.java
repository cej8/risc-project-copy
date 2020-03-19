package edu.duke.ece651.risc.shared;

import java.util.List;

// Class to validate if initial unit placements are allowed compared to game rules
public class PlacementValidator implements ValidatorInterface<PlacementOrder> {
  private AbstractPlayer player;
  private Unit playerUnits;
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
	public boolean validateRegions(List<PlacementOrder> placementList) {
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
    public boolean validateUnits(List<PlacementOrder> orders) {
    int totalUnits = this.playerUnits.getUnits();
    for (PlacementOrder p : orders) {
      int placementUnits = p.getUnits().getUnits();
      // make sure at least 1 placementUnit and totalUnits > 0 and placementUnits < totalUnits
      if ((placementUnits <= totalUnits) && (placementUnits > 0) && (totalUnits > 0)) {
        p.doAction();
        totalUnits -= placementUnits;
      } else {
        System.out
            .println("Placement failed: placementUnits are " + placementUnits + " but totalUnits are " + totalUnits); //this is just for testing
        return false;
      }
    }
    //make sure all units have been placed
    if (totalUnits == 0) {
      return true;
    }
    else{
      return false;
    }
	}

}
