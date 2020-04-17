package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

public class Spy implements Serializable {

  private static final long serialVersionUID = 43L;
  private boolean hasMoved;

  public Spy(){
    hasMoved = false;
  }
  
  public boolean getHasMoved(){
    return hasMoved;
  }

  public void setHasMoved(boolean hasMoved){
    this.hasMoved = hasMoved;
  }
}
