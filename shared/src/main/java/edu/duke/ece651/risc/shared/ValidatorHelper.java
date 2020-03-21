package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ValidatorHelper {
  private ValidatorInterface<AttackOrder> attackValidator;
  private ValidatorInterface<MoveOrder> moveValidator;
  private ValidatorInterface<PlacementOrder> placementValidator;
  private Board tempBoard; // added tempBoard field -CJ

  public ValidatorHelper(Board currentBoard) { //changed constructor parameters
    this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard); 
  this.moveValidator = new MoveValidator(tempBoard); 
  this.attackValidator = new AttackValidator(tempBoard);
  }

  public ValidatorHelper(AbstractPlayer p, Unit u, Board currentBoard) {
  //TODO: changed constructor parameters -CJ
  this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard); 
  this.placementValidator = new PlacementValidator(p, u, currentBoard);
  }

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
    boolean validMoves = moveValidator.validateOrders(moveList);
    System.out.println(validMoves);
    
    boolean validAttacks = attackValidator.validateOrders(attackList);
    System.out.println(validAttacks);
    
    return validMoves && validAttacks; 

  }

  public boolean allPlacementsValid(List<OrderInterface> placements) {
    List<PlacementOrder> pList = new ArrayList<PlacementOrder>();
    for (OrderInterface order : placements) {
      if (order.getPriority()==1) {
        pList.add((PlacementOrder) order);
      }

    }

    return placementValidator.validateOrders(pList);
  }

}
