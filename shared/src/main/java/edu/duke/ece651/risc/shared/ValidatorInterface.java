package edu.duke.ece651.risc.shared;

import java.util.List;

public interface ValidatorInterface {
  //this interface will be used to validate moves on regions and units are valid
  public boolean movesAreValid(List <MoveOrder> m);
  public boolean attacksAreValid(List <AttackOrder> a);
  public boolean placementsAreValid(List <PlacementOrder> p, AbstractPlayer player);
  
}
