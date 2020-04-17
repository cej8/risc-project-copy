package edu.duke.ece651.risc.shared;

public class CloakOrder extends DestinationOrder {
  private static final long serialVersionUID = 41L;

  public CloakOrder(Region destination){
    this.destination = destination;
  }

  @Override
  public int getPriority(){
    return Constants.CLOAK_PRIORITY;
  }

  @Override
  public String doAction(){
    destination.setCloakTurns(destination.getCloakTurns() + 3);
    destination.getOwner().getResources().getTechResource().useTech(Constants.CLOAK_COST);
    return (destination.getOwner().getName() + " cloaked " + destination.getName() + " for three more turns.\n");
  }
}
