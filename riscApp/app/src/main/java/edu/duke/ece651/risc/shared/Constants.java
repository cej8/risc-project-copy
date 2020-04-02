package edu.duke.ece651.risc.shared;

// Class to handle program constants
public final class Constants {
  // time to wait for initial connection of players (must be > 0)
  public static final double START_WAIT_MINUTES = 2.5;
  // time to wait between turns (must be > 0)
  public static final double TURN_WAIT_MINUTES = 3.0;
  // max number of units per player (must be > 0)
  public static final int UNIT_START_MULTIPLIER = 3;
  // max number of players at a given time (must be > 0)
  public static final int MAX_PLAYERS = 5;
  // max number of regions in board (must be equal to board in ParentServer)
  public static final int MAX_REGIONS = 12;
  // default port number (port for server connection default)
  public static final int DEFAULT_PORT = 12345;
 
  //priorities for ordering moves 
  public static final int ATTACK_COMBAT_PRIORITY=5000;
  public static final int ATTACK_MOVE_PRIORITY = 3000;
  public static final int MOVE_PRIORITY = 1000;
  public static final int PLACEMENT_PRIORITY = 1;
  public static final int UPGRADE_UNITS_PRIORITY = 500;
  public static final int UPGRADE_TECH_PRIORITY = 9000;
  // cost to attack - food 
  public static final int ATTACK_COST = 1;
  public static final int STARTING_FOOD = 20;
  public static final int STARTING_TECH = 15;
  //board values
  public static final int REGION_SIZE = 10;
  // Technology Defaults
  public static final int STARTING_TECH_LEVEL = 1;
  public static final int STARTING_UPGRADE_COST = 50;
}