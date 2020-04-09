package edu.duke.ece651.risc.shared;

import java.util.*;
// Helper class to validate orders and placements (game play) 
public class ValidatorHelper {
  private ValidatorInterface<AttackMove> attackValidator;
  private ValidatorInterface<MoveOrder> moveValidator;
  private ValidatorInterface<PlacementOrder> placementValidator;
  private ValidatorInterface<TechBoost> techBoostValidator;
  private ValidatorInterface<UnitBoost>unitBoostValidator;
  private Board tempBoard;
  private AbstractPlayer player;

  public ValidatorHelper(AbstractPlayer player, Board currentBoard) {
    this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard);
    this.player = (AbstractPlayer) DeepCopy.deepCopy(player);
    this.moveValidator = new MoveValidator(this.player, tempBoard);
    this.attackValidator = new AttackValidator(this.player, tempBoard);
    this.techBoostValidator= new TechBoostValidator(this.player, tempBoard);
    this.unitBoostValidator= new UnitBoostValidator(this.player, tempBoard);
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
    /*
    List<AttackMove> attackMoveList = new ArrayList<AttackMove>();
    List<MoveOrder> moveList = new ArrayList<MoveOrder>();
    List<UnitBoost>unitBoostList= new ArrayList<UnitBoost>();
    List<TechBoost>techBoostList= new ArrayList<TechBoost>();
    for (OrderInterface order : orders) {
      if (order.getPriority() == Constants.ATTACK_MOVE_PRIORITY) {
        //  System.out.println("Found attack");
        attackMoveList.add((AttackMove) order);
      }
      else if (order.getPriority() == Constants.MOVE_PRIORITY) {
        moveList.add((MoveOrder) order);
        //  System.out.println("Found move");
      }
      else if (order.getPriority() == Constants.UPGRADE_TECH_PRIORITY) {
        //  System.out.println("Found attack");
        techBoostList.add((TechBoost) order);
      }
      else if (order.getPriority() == Constants.UPGRADE_UNITS_PRIORITY){
        unitBoostList.add((UnitBoost) order);
        //  System.out.println("Found move");
      }
  
    }
    boolean validMoves = moveValidator.validateOrders(moveList);
    System.out.println(validMoves);

    boolean validAttacks = attackValidator.validateOrders(attackMoveList);
    System.out.println(validAttacks);

    boolean validUnitBoost= unitBoostValidator.validateOrders(unitBoostList);

    boolean validTechBoost = techBoostValidator.validateOrders(techBoostList);
    */

    
    boolean validMove = true;
    boolean validAttackMove = true;
    boolean validUnitBoost= true;
    boolean validTechBoost = true;

    List<AttackMove> attackMoveList = new ArrayList<AttackMove>();
    List<MoveOrder> moveList = new ArrayList<MoveOrder>();
    List<UnitBoost> unitBoostList= new ArrayList<UnitBoost>();
    List<TechBoost> techBoostList= new ArrayList<TechBoost>();

    for(OrderInterface order : orders){
      if (order.getPriority() == Constants.ATTACK_MOVE_PRIORITY) {
        attackMoveList.clear();
        attackMoveList.add((AttackMove)order);
        validAttackMove = validAttackMove && attackValidator.validateOrders(attackMoveList);
      }
      else if (order.getPriority() == Constants.MOVE_PRIORITY) {
        moveList.clear();
        moveList.add((MoveOrder)order);
        validMove = validMove && moveValidator.validateOrders(moveList);
      }
      else if (order.getPriority() == Constants.UPGRADE_TECH_PRIORITY) {
        techBoostList.clear();
        techBoostList.add((TechBoost)order);
        validTechBoost = validTechBoost && techBoostValidator.validateOrders(techBoostList);
      }
      else if (order.getPriority() == Constants.UPGRADE_UNITS_PRIORITY){
        unitBoostList.clear();
        unitBoostList.add((UnitBoost)order);
        validUnitBoost = validUnitBoost && unitBoostValidator.validateOrders(unitBoostList);
      }
    }

    return validMove && validAttackMove && validTechBoost && validUnitBoost;

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
