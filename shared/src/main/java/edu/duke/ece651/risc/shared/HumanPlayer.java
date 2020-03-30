package edu.duke.ece651.risc.shared;

import java.net.*;
import java.io.*;
// Class to create a human player (client)
public class HumanPlayer extends AbstractPlayer {
  private static final long serialVersionUID = 6L;
  public HumanPlayer(){
    
  }

  public HumanPlayer(String name) {//testing construcotr for tests that do no dpend on socket connection
    this.isPlaying = true;
    this.name = name;
    this.foodStash = Constants.STARTING_FOOD;
    this.techStash = Constants.STARTING_TECH;
  }
  
}
