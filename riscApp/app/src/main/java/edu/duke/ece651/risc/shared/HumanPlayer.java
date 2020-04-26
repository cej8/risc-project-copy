package edu.duke.ece651.risc.shared;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Class to create a human player (client)
public class HumanPlayer extends AbstractPlayer {
  private static final long serialVersionUID = 6L;


  public HumanPlayer(){
    this.isPlaying = true;
    this.playerResource = new PlayerResources(Constants.STARTING_FUEL_PRODUCTION, Constants.STARTING_TECH_PRODUCTION);
    maxTechLevel = new TechnologyLevel();
  }

  public HumanPlayer(String name) {//testing construcotr for tests that do no dpend on socket connection
    this.isPlaying = true;
    this.name = name;
    maxTechLevel = new TechnologyLevel();
    this.playerResource = new PlayerResources(Constants.STARTING_FUEL_PRODUCTION, Constants.STARTING_TECH_PRODUCTION);

  }



}
