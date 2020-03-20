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
      return moveValidator.regionsAreValid(moveList);// && moveValidator.unitsAreValid(moveList);
 
    }
    else if(moveList.size()==0){
      return attackValidator.regionsAreValid(attackList);//&& attackValidator.unitsAreValid(attackList);
          
    }
    else{
      return attackValidator.regionsAreValid(attackList) 
        && moveValidator.regionsAreValid(moveList);// && moveValidator.unitsAreValid(moveList) && attackValidator.unitsAreValid(attackList);
    }
    
   
    
  }

  public boolean allPlacementsValid(List<OrderInterface> placements) {
    List<PlacementOrder> pList = new ArrayList<PlacementOrder>();
    for (OrderInterface order : placements) {
      if (order instanceof PlacementOrder) {
        pList.add((PlacementOrder) order);
      }

    }

    return placementValidator.regionsAreValid(pList) && placementValidator.unitsAreValid(pList);
  }

}
