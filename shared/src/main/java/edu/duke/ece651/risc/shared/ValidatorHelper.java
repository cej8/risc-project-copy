package edu.duke.ece651.risc.shared;

public class ValidatorHelper {
  // this class will create a validator helper interface for any arbitrary
  // validator that is needed
  private ValidatorInterface<MoveOrder> moveValidator;
  private ValidatorInterface<AttackOrder> attackValidator;
  private ValidatorInterface<PlacementOrder> placementValidator;

  ValidatorHelper(AbstractPlayer p, Unit u) {
    moveValidator = new MoveValidator();
    attackValidator = new AttackValidator();
    placementValidator = new PlacementValidator(p, u);

  }

  public ValidatorInterface<MoveOrder> getMoveValidator() {
    return moveValidator;
  }

  public ValidatorInterface<AttackOrder> getAttackValidator() {
    return attackValidator;
  }

  public ValidatorInterface<PlacementOrder> getPlacementValidator() {
    return placementValidator;
  }

}
