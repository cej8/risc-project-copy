package edu.duke.ece651.risc.shared;

import java.util.*;

public abstract class SourceDestinationUnitOrder implements RegionUnitOrder {
  //this class contains relevant fields for order operations between two regions (source and destination)
  private static final long serialVersionUID = 8L; 
  protected Region source;
  protected Region destination;
  protected Unit units;

  public void setDestination(Region destination){
    this.destination = destination;
  }
  public Region getDestination(){
    return destination;
  }
  public void setSource(Region source){
    this.source = source;
  }
  public Region getSource(){
    return source;
  }

  public Unit getUnits(){
    return units;
  }

  public void setUnits(Unit units){
    this.units = units;
  }

  @Override
  abstract public int getPriority();

  @Override
  public void findValuesInBoard(Board board){
    for(Region r : board.getRegions()){
      if(r.getName().equals(this.getDestination().getName())){
        this.setDestination(r);
      }
      if(r.getName().equals(this.getSource().getName())){
        this.setSource(r);
      }
    }
  }

  @Override
  abstract public List<Set<String>> getPlayersVisibleTo();

  @Override
  abstract public List<String> doAction();


}
