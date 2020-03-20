package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

public class ValidatorHelper {
  private ValidatorInterface<AttackOrder> attackValidator;
  private ValidatorInterface<MoveOrder> moveValidator;
  private ValidatorInterface<PlacementOrder> placementValidator;

  public ValidatorHelper() {
    this.attackValidator = new AttackValidator();
    this.moveValidator = new MoveValidator();
  }

  public ValidatorHelper(AbstractPlayer p, Unit u) {
    this.placementValidator = new PlacementValidator(p, u);
  }

  public boolean allOrdersValid(List<OrderInterface> orders){
    List<AttackOrder> attackList = new ArrayList<AttackOrder>();
    List<MoveOrder> moveList = new ArrayList<MoveOrder>();
    for(OrderInterface order: orders){
      if(order instanceof AttackOrder){
        attackList.add((AttackOrder)order);
      }
      else if(order instanceof MoveOrder){
        moveList.add((MoveOrder)order);
      }
     
    }
    if (attackList.size()==0 && moveList.size()==0) {
          return true;
    }else if(attackList.size()==0){
      return moveValidator.validateRegions(moveList) && moveValidator.validateUnits(moveList);
 
    }
    else if(moveList.size()==0){
      return attackValidator.validateRegions(attackList)&& attackValidator.validateUnits(attackList);
          
    }
    else{
      return attackValidator.validateRegions(attackList) 
        && moveValidator.validateRegions(moveList) && moveValidator.validateUnits(moveList) && attackValidator.validateUnits(attackList);
    }
    
   
    
  }

  public boolean allPlacementsValid(List<OrderInterface> placements) {
    List<PlacementOrder> pList = new ArrayList<PlacementOrder>();
    for (OrderInterface order : placements) {
      if (order instanceof PlacementOrder) {
        pList.add((PlacementOrder) order);
      }

    }

    return placementValidator.validateRegions(pList) && placementValidator.validateUnits(pList);
  }

}
