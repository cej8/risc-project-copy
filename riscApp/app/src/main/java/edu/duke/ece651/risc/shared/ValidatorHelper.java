package edu.duke.ece651.risc.shared;

import java.util.*;
// Helper class to validate orders and placements (game play) 
public class ValidatorHelper {
  private ValidatorInterface<AttackMove> attackValidator;
  private ValidatorInterface<MoveOrder> moveValidator;
  private ValidatorInterface<PlacementOrder> placementValidator;
  private Board tempBoard;
  private AbstractPlayer player;

  public ValidatorHelper(AbstractPlayer player, Board currentBoard) {
    this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard);
    this.player = player;
    this.moveValidator = new MoveValidator(player, tempBoard);
    this.attackValidator = new AttackValidator(player, tempBoard);
  }

  public ValidatorHelper(AbstractPlayer player, Unit u, Board currentBoard) {
    this.player = player;
    this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard);
    this.placementValidator = new PlacementValidator(player, u, currentBoard);
  }
  // check all orders are valid for round per player
  public boolean allOrdersValid(List<OrderInterface> orders) {
    for(int i = 0; i < orders.size(); i++){
       orders.get(i).findValuesInBoard(tempBoard);
    }
    List<AttackMove> attackList = new ArrayList<AttackMove>();
    List<MoveOrder> moveList = new ArrayList<MoveOrder>();
    for (OrderInterface order : orders) {
      if (order.getPriority() == Constants.ATTACK_MOVE_PRIORITY) {
        //  System.out.println("Found attack");
        attackList.add((AttackMove) order);
      } else if (order.getPriority() == Constants.MOVE_PRIORITY) {
        moveList.add((MoveOrder) order);
        //  System.out.println("Found move");
      }
    }
    boolean validMoves = moveValidator.validateOrders(moveList);
    System.out.println(validMoves);

    boolean validAttacks = attackValidator.validateOrders(attackList);
    System.out.println(validAttacks);

    return validMoves && validAttacks;

  }
  // checks all placement are valid per player
  public boolean allPlacementsValid(List<OrderInterface> placements) {
    for(int i = 0; i < placements.size(); i++){
     placements.get(i).findValuesInBoard(tempBoard);
    }
    List<PlacementOrder> pList = new ArrayList<PlacementOrder>();
    for (OrderInterface order : placements) {
      if (order.getPriority() == Constants.PLACEMENT_PRIORITY) {
        pList.add((PlacementOrder) order);
      }

    }

    return placementValidator.validateOrders(pList);

  }

}
