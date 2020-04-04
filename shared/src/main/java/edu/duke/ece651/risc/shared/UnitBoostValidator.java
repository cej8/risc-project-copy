package edu.duke.ece651.risc.shared;

import java.util.List;

public class UnitBoostValidator implements ValidatorInterface {
private Board tempBoard;
  private AbstractPlayer player;

  public UnitBoostValidator(AbstractPlayer player, Board boardCopy) {
    this.tempBoard = boardCopy;
    this.player = player;
  }

	@Override
	public boolean validateOrders(List orders) {
		// TODO Auto-generated method stub
		return false;
	}

}
