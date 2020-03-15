package edu.duke.ece651.risc.shared;

public interface ValidatorInterface {
  public boolean isValidMove(MoveOrder m, Board b);
  // public boolean isValidAttack(AttackOrder  a);
  public boolean isValidPlacement(PlacementOrder p,AbstractPlayer player, Board b);
  
}
