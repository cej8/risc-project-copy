package edu.duke.ece651.risc.shared;

import java.util.List;

// Class to validate if initial unit placements are allowed compared to game rules
public class PlacementValidator implements ValidatorInterface<PlacementOrder> {
 private AbstractPlayer player;
  private Unit playerUnits;
  private Board tempBoard;
  public PlacementValidator(AbstractPlayer p, Unit u, Board boardCopy){
    this.player=p;
    this.playerUnits = u;
    this.tempBoard = boardCopy;
  }

    // helper method
  public boolean isValidPlacement(PlacementOrder p, AbstractPlayer ap) {
    // check that player owns the regions they are placing units in
    if(p.getDestination().getOwner().getName().equals(ap.getName())){
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
        //place.doAction();
    }
    // if all placements are valid
    return true;
  }
	 @Override
   public boolean validateOrders(List<PlacementOrder> orders) {
     boolean validRegions = validateRegions(orders);
      boolean validUnits = validateUnits(orders);
      return validRegions && validUnits;
    }

  	@Override
    public boolean validateUnits(List<PlacementOrder> orders) {
    int totalUnits = this.playerUnits.getTotalUnits();
    for (PlacementOrder p : orders) {
      Region tempDest = tempBoard.getRegionByName(p.getDestination().getName());

      Unit placementUnits =  new Unit(p.getUnits().getTotalUnits()); //TODO this is fine? bc placing new units?
      PlacementOrder placementCopy = new PlacementOrder(tempDest, placementUnits);
      // make sure at least 1 placementUnit and totalUnits > 0 and placementUnits < totalUnits
      if ((placementUnits.getTotalUnits() <= totalUnits) && (placementUnits.getTotalUnits() > 0) && (totalUnits > 0)) {
        p.doAction();
        totalUnits -= placementUnits.getTotalUnits();
      } else {
        System.out
            .println("Placement failed: placementUnits are " + placementUnits.getTotalUnits() + " but totalUnits are " + totalUnits); //this is just for testing
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
