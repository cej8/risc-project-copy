package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ValidatorHelper {
  private ValidatorInterface<AttackOrder> attackValidator;
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

  public boolean allOrdersValid(List<OrderInterface> orders) {
    List<AttackOrder> attackList = new ArrayList<AttackOrder>();
    List<MoveOrder> moveList = new ArrayList<MoveOrder>();
    for (OrderInterface order : orders) {
      if (order.getPriority() == Constants.ATTACK_PRIORITY) {
        //  System.out.println("Found attack");
        attackList.add((AttackOrder) order);
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

  public boolean allPlacementsValid(List<OrderInterface> placements) {
    List<PlacementOrder> pList = new ArrayList<PlacementOrder>();
    for (OrderInterface order : placements) {
      if (order.getPriority() == Constants.PLACEMENT_PRIORITY) {
        pList.add((PlacementOrder) order);
      }

    }

    return placementValidator.validateOrders(pList);

  }

}
