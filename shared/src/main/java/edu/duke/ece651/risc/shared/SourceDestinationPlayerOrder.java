package edu.duke.ece651.risc.shared;

import java.util.*;

// Abstract class for orders with source/destination and internal player
public abstract class SourceDestinationPlayerOrder implements RegionPlayerOrder {
  private static final long serialVersionUID = 48L;
  protected Region source;
  protected Region destination;
  protected AbstractPlayer player;

  /* BEGIN ACCESSORS */
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

  public void setPlayer(AbstractPlayer player){
    this.player = player;
  }
  public AbstractPlayer getPlayer(){
    return player;
  }
  /* END ACCESSORS*/


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
