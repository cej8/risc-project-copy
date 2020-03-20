package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ValidatorHelper {
  private ValidatorInterface<AttackOrder> attackValidator;
  private ValidatorInterface<MoveOrder> moveValidator;
  private ValidatorInterface<PlacementOrder> placementValidator;
  private Board tempBoard; // added tempBoard field -CJ

  // public ValidatorHelper(Board currentBoard) { //changed constructor parameters
  // -CJ
  // this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard); //TODO: you will
  // need the DeepCopy.java for this to work on your branch -CJ
  // this.moveValidator = new MoveValidator(tempBoard); //TODO: you will need the
  // updated Move/AttackValidator.java with new constructors
  // this.attackValidator = new AttackValidator(tempBoard);
  // }

  // public ValidatorHelper(AbstractPlayer p, Unit u, Board currentBoard) {
  // //TODO: changed constructor parameters -CJ
  // this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard); //TODO: you will
  // need the DeepCopy.java for this to work on your branch -CJ
  // this.placementValidator = new PlacementValidator(p, u, currentBoard);
  // }

  // TODO: order of what goes on in this method needs to be changed as discussed
  
  // -CJ
  public boolean allOrdersValid(List<OrderInterface> orders) {
    List<AttackOrder> attackList = new ArrayList<AttackOrder>();
    List<MoveOrder> moveList = new ArrayList<MoveOrder>();
    for (OrderInterface order : orders) {
      if (order.getPriority() == 5000) {
        System.out.println("Found attack");
        attackList.add((AttackOrder) order);
      } else if (order.getPriority() == 1000) {
        moveList.add((MoveOrder) order);
           System.out.println("Found move");
    
      }
    }
    //  System.out.println(/*moveValidator.validateUnits(moveList)); //&& */moveValidator.validateRegions(moveList));
    boolean validMoves =/* moveValidator.validateUnits(moveList) && */moveValidator.validateRegions(moveList);
    
    boolean validAttacks = attackValidator.validateRegions(attackList) && attackValidator.validateUnits(attackList);

    return validMoves && validAttacks; 

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
