package edu.duke.ece651.risc.shared;

import java.util.*;

// Abstract class for orders on single region with units
public abstract class DestinationUnitOrder implements RegionUnitOrder {

  private static final long serialVersionUID = 10L;
  protected Region destination;
  protected Unit units;

  /* BEGIN ACCESSORS */
  public void setDestination(Region destination){
    this.destination = destination;
  }
  public Region getDestination(){
    return destination;
  }

  public Unit getUnits(){
    return units;
  }
  /* END ACCESSORS */

  @Override
  abstract public int getPriority();


  @Override
  public void findValuesInBoard(Board board){
    for(Region r : board.getRegions()){
      if(r.getName().equals(this.getDestination().getName())){
        this.setDestination(r);
      }
    }
  }

  @Override
  abstract public List<Set<String>> getPlayersVisibleTo();

  @Override
  abstract public List<String> doAction();

}
