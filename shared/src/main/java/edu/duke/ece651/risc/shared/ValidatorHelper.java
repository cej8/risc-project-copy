package edu.duke.ece651.risc.shared;

import java.util.*;

// Helper class to validate orders and placements (game play) 
// This is the validator called in server/client validation
// Will call correct validator for orders DEPENDING ON LIST'S ORDER
public class ValidatorHelper {
  // Validators for all order types
  private ValidatorInterface<PlacementOrder> placementValidator;

  private ValidatorInterface<AttackMove> attackValidator;
  private ValidatorInterface<MoveOrder> moveValidator;
  private ValidatorInterface<TechBoost> techBoostValidator;
  private ValidatorInterface<UnitBoost> unitBoostValidator;
  private ValidatorInterface<TeleportOrder> teleportValidator;
  private ValidatorInterface<ResourceBoost> resourceBoostValidator;
  private ValidatorInterface<CloakOrder> cloakValidator;
  private ValidatorInterface<SpyUpgradeOrder> spyUpgradeValidator;
  private ValidatorInterface<SpyMoveOrder> spyMoveValidator;
  private ValidatorInterface<RaidOrder> raidValidator;

  // Internal copy of board/player
  private Board tempBoard;
  private AbstractPlayer player;

  public ValidatorHelper(AbstractPlayer player, Board currentBoard) {
    this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard);
    this.player = tempBoard.getPlayerByName(player.getName());

    this.attackValidator = new AttackValidator(this.player, tempBoard);
    this.moveValidator = new MoveValidator(this.player, tempBoard);
    this.techBoostValidator = new TechBoostValidator(this.player, tempBoard);
    this.unitBoostValidator = new UnitBoostValidator(this.player, tempBoard);
    this.teleportValidator = new TeleportValidator(this.player, tempBoard);
    this.resourceBoostValidator = new ResourceBoostValidator(this.player, tempBoard);
    this.cloakValidator = new CloakValidator(this.player, tempBoard);
    this.spyUpgradeValidator = new SpyUpgradeValidator(this.player, tempBoard);
    this.spyMoveValidator = new SpyMoveValidator(this.player, tempBoard);
    this.raidValidator = new RaidValidator(this.player, tempBoard);

  }

  public ValidatorHelper(AbstractPlayer player, Unit u, Board currentBoard) {
    this.tempBoard = (Board) DeepCopy.deepCopy(currentBoard);
    this.player = tempBoard.getPlayerByName(player.getName());

    this.placementValidator = new PlacementValidator(this.player,u,tempBoard);
  }

  // check all orders are valid for round per player
  public boolean allOrdersValid(List<OrderInterface> orders) {
    //Ensure all orders converted to internal board
    for (int i = 0; i < orders.size(); i++) {
      orders.get(i).findValuesInBoard(tempBoard);
    }

    // booleans for each order type
    boolean validMove = true;
    boolean validAttackMove = true;
    boolean validUnitBoost = true;
    boolean validTechBoost = true;
    boolean validTeleport = true;
    boolean validResourceBoost = true;
    boolean validCloak = true;
    boolean validSpyUpgrade = true;
    boolean validSpyMove = true;
    boolean validRaid = true;

    // Lists for each order type
    List<AttackMove> attackMoveList = new ArrayList<AttackMove>();
    List<MoveOrder> moveList = new ArrayList<MoveOrder>();
    List<UnitBoost> unitBoostList = new ArrayList<UnitBoost>();
    List<TechBoost> techBoostList = new ArrayList<TechBoost>();
    List<ResourceBoost> resourceBoostList = new ArrayList<ResourceBoost>();
    List<TeleportOrder> teleportList = new ArrayList<TeleportOrder>();
    List<CloakOrder> cloakList = new ArrayList<CloakOrder>();
    List<SpyUpgradeOrder> spyUpgradeList = new ArrayList<SpyUpgradeOrder>();
    List<SpyMoveOrder> spyMoveList = new ArrayList<SpyMoveOrder>();
    List<RaidOrder> raidList = new ArrayList<RaidOrder>();

    // For order in list if priority then call validator for that order type
    for (OrderInterface order : orders) {
      if (order.getPriority() == Constants.ATTACK_MOVE_PRIORITY) {
        attackMoveList.clear();
        attackMoveList.add((AttackMove) order);
        validAttackMove = validAttackMove && attackValidator.validateOrders(attackMoveList);
      } else if (order.getPriority() == Constants.MOVE_PRIORITY) {
        moveList.clear();
        moveList.add((MoveOrder) order);
        validMove = validMove && moveValidator.validateOrders(moveList);
      } else if (order.getPriority() == Constants.UPGRADE_TECH_PRIORITY) {
        techBoostList.clear();
        techBoostList.add((TechBoost) order);
        validTechBoost = validTechBoost && techBoostValidator.validateOrders(techBoostList);
      } else if (order.getPriority() == Constants.UPGRADE_UNITS_PRIORITY) {
        unitBoostList.clear();
        unitBoostList.add((UnitBoost) order);
        validUnitBoost = validUnitBoost && unitBoostValidator.validateOrders(unitBoostList);
      }
      else if (order.getPriority() == Constants.UPGRADE_RESOURCE_PRIORITY) {
        resourceBoostList.clear();
        resourceBoostList.add((ResourceBoost) order);
        validResourceBoost = validResourceBoost && resourceBoostValidator.validateOrders(resourceBoostList);
      }
      else if (order.getPriority() == Constants.TELEPORT_ORDER_PRIORITY) {
        teleportList.clear();
        teleportList.add((TeleportOrder) order);
        validTeleport = validTeleport && teleportValidator.validateOrders(teleportList);
      } else if (order.getPriority() == Constants.CLOAK_PRIORITY) {
        cloakList.clear();
        cloakList.add((CloakOrder) order);
        validCloak = validCloak && cloakValidator.validateOrders(cloakList);
      } else if (order.getPriority() == Constants.SPYUPGRADE_PRIORITY) {
        spyUpgradeList.clear();
        spyUpgradeList.add((SpyUpgradeOrder) order);
        validSpyUpgrade = validSpyUpgrade && spyUpgradeValidator.validateOrders(spyUpgradeList);
      } else if (order.getPriority() == Constants.SPYMOVE_PRIORITY) {
        spyMoveList.clear();
        spyMoveList.add((SpyMoveOrder) order);
        validSpyMove = validSpyMove && spyMoveValidator.validateOrders(spyMoveList);
      } else if (order.getPriority() == Constants.RAID_PRIORITY){
        raidList.clear();
        raidList.add((RaidOrder) order);
        validRaid = validRaid && raidValidator.validateOrders(raidList);
      }
    }

    //Check if all are valid
    return validMove && validAttackMove && validTechBoost && validUnitBoost && validTeleport && 
      validCloak && validSpyUpgrade && validSpyMove && validResourceBoost && validRaid;
  }

  // checks all placement are valid per player, special case
  public boolean allPlacementsValid(List<OrderInterface> placements) {
    for (int i = 0; i < placements.size(); i++) {
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
