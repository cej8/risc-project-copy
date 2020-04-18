package edu.duke.ece651.risc.shared;
// Class to allow moving of spies between regions
// Unlimited movement within player's own regions, 1 region per turn into enemy regions per turn
public class SpyMoveOrder extends SourceDestinationPlayerOrder {
  private static final long serialVersionUID = 46L; 

  public SpyMoveOrder(Region s, Region d, AbstractPlayer player) {
    this.source = s;
    this.destination = d;
    this.player = player;
  }

  @Override
  public int getPriority() {
    return Constants.SPYMOVE_PRIORITY;
  }

  @Override
  public String doAction() {
    //Get spy, remove from source
    Spy spy = source.getSpies(player.getName()).get(0);
    source.getSpies(player.getName()).remove(0);
    //Place spy in destination
    destination.getSpies(player.getName()).add(spy);
    //If destination not owned by player then hasMoved = true
    if(!player.getName().equals(destination.getOwner().getName())){
      spy.setHasMoved(true);
    }
    return player.getName() + " moved a spy from " + source.getName() + " to " + destination.getName() + "\n";
  }

}



