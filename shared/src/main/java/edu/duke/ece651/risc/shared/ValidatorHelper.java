package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

public class ValidatorHelper {
  private ValidatorInterface<AttackOrder> attackValidator;
  private ValidatorInterface<MoveOrder> moveValidator;
  private ValidatorInterface<PlacementOrder> placementValidator;
  private Board tempBoard; //added tempBoard field -CJ

  public ValidatorHelper(Board currentBoard) { //changed constructor parameters -CJ
    this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard); //TODO: you will need the DeepCopy.java for this to work on your branch -CJ
    this.moveValidator = new MoveValidator(tempBoard); //TODO: you will need the updated Move/AttackValidator.java with new constructors
    this.attackValidator = new AttackValidator(tempBoard);
  }

  public ValidatorHelper(AbstractPlayer p, Unit u, Board currentBoard) { //TODO: changed constructor parameters -CJ
    this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard); //TODO: you will need the DeepCopy.java for this to work on your branch -CJ
    this.placementValidator = new PlacementValidator(p, u, currentBoard);
  }

    //TODO: order of what goes on in this method needs to be changed as discussed -CJ
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
