package edu.duke.ece651.risc.shared;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

import org.junit.jupiter.api.Test;

public class MoveValidatorTest {

   @Test
  public void test_Owner() {
    trueIfOwner(true);
    trueIfOwner(false);
  }
  //this method return true (valid move) if the regions are owner by the same player and false if not 
  private void trueIfOwner(boolean singleOwner) {
    List<Region> regions = getRegions(singleOwner); 
    Board board = new Board(regions);
    int totalUnits = board.getRegions().get(0).getUnits().getTotalUnits();
    board.getRegions().get(0).getOwner().getResources().getFuelResource().addFuel(3760); //amount of fuel needed for following moves 
    Board boardCopy = (Board) DeepCopy.deepCopy(board);
    List<Region> regionCopy = boardCopy.getRegions();
    MoveValidator mv = new MoveValidator(regionCopy.get(0).getOwner(), boardCopy);
    // Moves using all but one of sourceUnits all with same owner
    List<Unit> units = get6UnitList(4, 9, 14, 19, 24, 29); // true: moving all but one unit   
    List<MoveOrder> moveUnits = getMovesDependent(regionCopy, units);
    
    assertEquals(singleOwner, mv.validateRegions(moveUnits)); //true if one owner for regions, false if multiple
    assertEquals(singleOwner, mv.validateOrders(moveUnits)); //true if one owner for regions, false if multiple
  
    assertEquals(totalUnits, board.getRegions().get(0).getUnits().getTotalUnits()); //number of units on actual cost should not have changed after validation
  }

  @Test
    public void dependentUnitsMoveTest() {
    List<Region> regions = getRegions(true); 
    Board board = new Board(regions);
    int totalUnits = board.getRegions().get(0).getUnits().getTotalUnits();
    board.getRegions().get(0).getOwner().getResources().getFuelResource().addFuel(3760); //amount of fuel needed for following moves 
    Board boardCopy = (Board) DeepCopy.deepCopy(board);
    List<Region> regionCopy = boardCopy.getRegions();
    MoveValidator mv = new MoveValidator(regionCopy.get(0).getOwner(), boardCopy);
    // Moves using all but one of sourceUnits all with same owner
    List<Unit> allButOneUnit = get6UnitList(4, 13, 27, 46, 70, 99); // true: moving all but one unit   
    List<MoveOrder> moveAllButOneUnit = getMovesDependent(regionCopy, allButOneUnit);
    System.out.println("Board before dependent moves: " + createBoard(boardCopy));
    assertEquals(true, mv.validateOrders(moveAllButOneUnit)); //true if one owner for regions, false if multiple
    System.out.println("Board after dependent moves: " + createBoard(boardCopy));
    assertEquals(totalUnits, board.getRegions().get(0).getUnits().getTotalUnits()); //number of units on actual cost should not have changed after validation
  }

  
  @Test
  public void test_UnitMoves() {
    List<Region> regions = getRegions(true); 
    Board board = new Board(regions);
    int totalUnits = board.getRegions().get(0).getUnits().getTotalUnits();
    board.getRegions().get(0).getOwner().getResources().getFuelResource().addFuel(3760); //amount of fuel needed for following moves 
    Board boardCopy = (Board) DeepCopy.deepCopy(board);
    List<Region> regionCopy = boardCopy.getRegions();
    MoveValidator mv = new MoveValidator(regionCopy.get(0).getOwner(), boardCopy);

    // Orders using all units
    List<Unit> regionUnits = get6UnitList(5, 10, 15, 20, 25, 30);
    List<MoveOrder> moveAllUnits = getMovesIndependent(regionCopy, regionUnits); // false: moving all units in region to
                                                                              // another
    assertEquals(false, mv.validateOrders(moveAllUnits));

    // Orders using 0 units
    List<Unit> invalidUnits = get6UnitList(0, 9, 14, 19, 24, 29); // false: moving 0 units
    List<MoveOrder> moveInvalidUnits = getMovesDependent(regionCopy, invalidUnits);
    assertEquals(false, mv.validateOrders(moveInvalidUnits));

    // Orders for which sourceUnits < order Units
    List<Unit> tooManyUnits = get6UnitList(100, 9, 14, 19, 24, 29);
    List<MoveOrder> moveTooManyUnits = getMovesDependent(regionCopy, tooManyUnits);
    assertEquals(false, mv.validateOrders(moveTooManyUnits)); // false: move too many units
  }

    //returns a String of all of the board info
  public String createBoard(Board b){
    StringBuilder boardText = new StringBuilder();
    Map<AbstractPlayer, List<Region>> playerRegionMap = b.getPlayerToRegionMap();
    List<AbstractPlayer> players = new ArrayList<AbstractPlayer>(playerRegionMap.keySet());
    Collections.sort(players);
    for(AbstractPlayer player : players) { //for each entry in the map
      boardText.append(player.getName() + ": \n---------\n"); //append player name
      for (Region r : playerRegionMap.get(player)){ //for each region the player has
        boardText.append(this.printRegionInfo(r)); //add its info to board
      }
      boardText.append("\n");
    }
    return boardText.toString();
  }

 //returns String w board info for a given region
  private String printRegionInfo(Region r){
    int numUnits = r.getUnits().getTotalUnits();
    String name = r.getName();
    StringBuilder sb = new StringBuilder(numUnits + " units in " + name); //add info on num units in region
    sb.append(this.printRegionAdjacencies(r)); //add adj info
    return sb.toString();
  }
  
   //returns String w adjacency info info for a given region  
  private String printRegionAdjacencies(Region r){
    StringBuilder sb = new StringBuilder(" (next to:");
    List<Region> adjList = r.getAdjRegions();
    for (int i = 0; i < adjList.size(); i++){
      sb.append(" " + adjList.get(i).getName()); //add name of adjacent region to sb
      if (i <  adjList.size() -1){
        sb.append(","); //add a comma if not the last in list
      }
    }
    sb.append(")\n"); //close parentheses
    return sb.toString();
  }
      

  
  
  private List<Region> getRegions(boolean singleOwner) {
    AbstractPlayer p1 = new HumanPlayer("Player 1");
    AbstractPlayer p2 = new HumanPlayer("Player 2");
    List<Region> regions = null;
    List<Unit> regionUnits = get6UnitList(5, 10, 15, 20, 25, 30);
    if (!singleOwner) {
      regions = getRegionHelper(p1, p2, regionUnits);
    } else {
      regions = getRegionHelper(p1, p1, regionUnits);
    }
    return regions;
  }

  private List<Unit> get6UnitList(int u0, int u1, int u2, int u3, int u4, int u5) {
    List<Unit> units = new ArrayList<Unit>();
    List<Integer> un0 = listOfUnitInts(u0, u0, u0, u0, u0, u0, u0);
    List<Integer> un1 = listOfUnitInts(u1, u1, u1, u1, u1, u1, u1);
    List<Integer> un2 = listOfUnitInts(u2, u2, u2, u2, u2, u2, u2);
    List<Integer> un3 = listOfUnitInts(u3, u3, u3, u3, u3, u3, u3);
    List<Integer> un4 = listOfUnitInts(u4, u4, u4, u4, u4, u4, u4);
    List<Integer> un5 = listOfUnitInts(u5, u5, u5, u5, u5, u5, u5);
    Unit unit0 = new Unit(un0);
    units.add(unit0);
    Unit unit1 = new Unit(un1);
    units.add(unit1);
    Unit unit2 = new Unit(un2);
    units.add(unit2);
    Unit unit3 = new Unit(un3);
    units.add(unit3);
    Unit unit4 = new Unit(un4);
    units.add(unit4);
    Unit unit5 = new Unit(un5);
    units.add(unit5);
    return units;
  }

  private List<Integer> listOfUnitInts(int u0, int u1, int u2, int u3, int u4, int u5, int u6) {
    List<Integer> unit = new ArrayList<Integer>();
    unit.add(u0);
    unit.add(u1);
    unit.add(u2);
    unit.add(u3);
    unit.add(u4);
    unit.add(u5);
    unit.add(u6);
    return unit;
  }

  private List<Region> getRegionHelper(AbstractPlayer p1, AbstractPlayer p2, List<Unit> units) {

    Region r0 = new Region(p1, units.get(0));
    r0.setName("Earth");
    Region r1 = new Region(p1, units.get(1));
    r1.setName("Mars");
    Region r2 = new Region(p1, units.get(2));
    r2.setName("Venus");
    Region r3 = new Region(p1, units.get(3));
    r3.setName("Mercury");
    Region r4 = new Region(p2, units.get(4));
    r4.setName("Saturn");
    Region r5 = new Region(p2, units.get(5));
    r5.setName("Uranus");

    List<Region> adj0 = new ArrayList<Region>();
    adj0.add(r5);
    adj0.add(r1);
    r0.setAdjRegions(adj0);

    List<Region> adj1 = new ArrayList<Region>();
    adj1.add(r0);
    adj1.add(r2);
    r1.setAdjRegions(adj1);

    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r3);
    r2.setAdjRegions(adj2);

    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r2);
    adj3.add(r4);
    r3.setAdjRegions(adj3);

    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r3);
    adj4.add(r5);
    r4.setAdjRegions(adj4);

    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r4);
    adj5.add(r0);
    r5.setAdjRegions(adj5);

    List<Region> regions = new ArrayList<Region>();
    regions.add(r0);
    regions.add(r1);
    regions.add(r2);
    regions.add(r3);
    regions.add(r4);
    regions.add(r5);

    return regions;
  }

  private List<MoveOrder> getMovesIndependent(List<Region> regions, List<Unit> units) {
    MoveOrder move01 = new MoveOrder(regions.get(0), regions.get(1), units.get(0));
    MoveOrder move23 = new MoveOrder(regions.get(2), regions.get(3), units.get(2));
    MoveOrder move45 = new MoveOrder(regions.get(4), regions.get(5), units.get(4));
    List<MoveOrder> moves = new ArrayList<MoveOrder>();
    moves.add(move01);
    moves.add(move23);
    moves.add(move45);
    return moves;
  }

  // private void printCostPerMove(List<MoveOrder> moves){
  //   for (MoveOrder m : moves){
  //     System.out.println(
  //   }
  // }
  private List<MoveOrder> getMovesDependent(List<Region> regions, List<Unit> units) {
    MoveOrder move01 = new MoveOrder(regions.get(0), regions.get(1), units.get(0));

    MoveOrder move12 = new MoveOrder(regions.get(1), regions.get(2), units.get(1));
    MoveOrder move23 = new MoveOrder(regions.get(2), regions.get(3), units.get(2));
    MoveOrder move34 = new MoveOrder(regions.get(3), regions.get(4), units.get(3));
    MoveOrder move45 = new MoveOrder(regions.get(4), regions.get(5), units.get(4));
    MoveOrder move50 = new MoveOrder(regions.get(5), regions.get(0), units.get(5));
    List<MoveOrder> moves = new ArrayList<MoveOrder>();
    moves.add(move01);
    moves.add(move12);
    moves.add(move23);
    moves.add(move34);
    moves.add(move45);
    moves.add(move50);
    return moves;
  }

}
