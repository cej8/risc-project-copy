package edu.duke.ece651.risc.shared;

// Class to handle program constants
public final class Constants {
  // time to wait for initial connection of players
  public static final double START_WAIT_MINUTES = 2.5;
  // time to wait between turns
  public static final double TURN_WAIT_MINUTES = 1;
  // max number of units per player
  public static final int MAX_UNITS = 15;
  // max number of players at a given time
  public static final int MAX_PLAYERS = 1;
  // max number of regions in board
  public static final int MAX_REGIONS = 12;
  // default port number
  public static final int DEFAULT_PORT = 12345;
  //priorities for oder handling
  public static final int MOVE_PRIORITY = 1000;
  public static final int ATTACK_PRIORITY = 5000;
  public static final int PLACEMENT_PRIORITY = 1;

  //priorities for ordering moves 
  public static final int ATTACK_PRIORITY = 5000;
  public static final int MOVE_PRIORITY = 1000;
  public static final int PLACEMENT_PRIORITY = 1;
}
