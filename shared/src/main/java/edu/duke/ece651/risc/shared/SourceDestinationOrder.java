package edu.duke.ece651.risc.shared;

import java.util.*;

// Abstract class for orders that have a source/destination only
public abstract class SourceDestinationOrder implements RegionOrder {
  private static final long serialVersionUID = 55L;
  protected Region source;
  protected Region destination;

  /* BEGIN ACCESSORS */
  public void setSource(Region source){
    this.source = source;
  }
  public Region getSource(){
    return source;
  }
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
        if(r.getName().equals(this.source.getName())){
          this.source = r;
        }
    }     
  }

  @Override
  abstract public List<Set<String>> getPlayersVisibleTo();

  @Override
  abstract public List<String> doAction();

}
