package edu.duke.ece651.risc.shared;

import java.util.*;

// Abstract class for orders on single region without any extra requirements
public abstract class DestinationOrder implements RegionOrder {
  private static final long serialVersionUID = 40L;
  protected Region destination;

  /* BEGIN ACCESSORS */
  public void setDestination(Region destination){
    this.destination = destination;
  }
  public Region getDestination(){
    return destination;
  }
  /* END ACCESSORS */

  @Override
  abstract public int getPriority();

  @Override
  public void findValuesInBoard(Board board){
    for(Region r : board.getRegions()){
        if(r.getName().equals(this.destination.getName())){
          this.destination = r;
        }
    }     
  }

  @Override
  abstract public List<Set<String>> getPlayersVisibleTo();

  @Override
  abstract public List<String> doAction();

}
