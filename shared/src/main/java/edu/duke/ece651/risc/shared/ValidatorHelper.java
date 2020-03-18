package edu.duke.ece651.risc.shared;

public class ValidatorHelper {
  // this class will create a validator helper interface for any arbitrary
  // validator that is needed
  private ValidatorInterface regionValidator;
  private ValidatorInterface unitValidator;

  ValidatorHelper() {
    regionValidator = new RegionValidator();
    unitValidator = new UnitValidator();
  }

  public ValidatorInterface getRegionValidator() {
    return regionValidator;
  }

  public ValidatorInterface getUnitValidator() {
    return unitValidator;
  }

}
