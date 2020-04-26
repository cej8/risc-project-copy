package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

// Class that holds data for Spy Unit, this is seperate from Unit class because of special functionality
// Keeps information on if a Spy has moved into enemy region this turn (boolean, false if can move, set false at end of turn)
public class Spy implements Serializable {

  private static final long serialVersionUID = 43L;
  private boolean hasMoved;

  public Spy(){
    hasMoved = false;
  }
  
  /* BEGIN ACCESSORS */
  public boolean getHasMoved(){
    return hasMoved;
  }
  public void setHasMoved(boolean hasMoved){
    this.hasMoved = hasMoved;
  }
  /* END ACCESSORS */
}
