package edu.duke.ece651.risc.shared;

public class UnitBoost extends DestinationOrder {
  private static final long serialVersionUID = 16L;
  public UnitBoost (Region d, Unit u){
    this.destination = d;
    this.units = u;

  }
  @Override
public String doAction() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public int getPriority() {
  return Constants.UPGRADE_UNITS_PRIORITY;
}

}
