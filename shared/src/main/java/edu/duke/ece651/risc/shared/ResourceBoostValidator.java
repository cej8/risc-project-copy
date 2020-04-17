package edu.duke.ece651.risc.shared;

import java.util.List;

public class ResourceBoostValidator implements ValidatorInterface<ResourceBoost> {
    private Board tempBoard;
  private AbstractPlayer player;

  public ResourceBoostValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;
  }

  public boolean validTechLevel(List<ResourceBoost> orders){
     //compare region level to max region level that player has unlocked
    //region level 2 is availabl at tech level 2
    //region level 3 is availabel at tehc level 4
    //region level is available at tech level 6
    for(ResourceBoost r: orders){
      Region tempDest = r.getDestination().getRegionByName(tempBoard, r.getDestination().getName());
      ResourceBoost rbCopy = new ResourceBoost(tempDest);
      
       //verify that not boosting past level 4
      if(r.getDestination().getRegionLevel().getRegionLevel()==4){
        return false;
      }
      //if current level plus 1 is higher than max = invalid
      if(r.getDestination().getRegionLevel().getRegionLevel()+1>player.getMaxTechLevel().getMaxRegionLevel()){
        return false;
      }
      
      //if order is valid pseudo increase region level
      rbCopy.doAction();
    }

    return true;
  }
  public boolean validOwnership(List<ResourceBoost> orders){
    //verify that player owns region
    for(ResourceBoost rb: orders){
      if(!rb.getDestination().getOwner().getName().equals(player.getName())){
          return false;
        }
    }
    return true;
  }
  public boolean validResources(List<ResourceBoost>orders){
    int sum = 0;
    
    // add up total cost of all resourse boosts
    for (ResourceBoost r : orders) {
         int cost=(int)(r.getDestination().getUnits().getTotalUnits() * r.getDestination().getSize()*r.getDestination().getRegionLevel().getMultiplier());
     sum += cost;
    }
    // if total cost is lower than the players tech resources, return true
    return player.getResources().getTechResource().getTech() >= sum;

  }
	@Override
	public boolean validateOrders(List <ResourceBoost>orders) {
      return validOwnership(orders)&&validResources(orders) &&validTechLevel(orders);
	}

}
