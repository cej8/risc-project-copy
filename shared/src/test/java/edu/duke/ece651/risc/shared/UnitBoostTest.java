package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UnitBoostTest {
  @Test
  public void test_UnitBoost() {
    List<Region> regions = getRegions(5, 5, 5, 5, 5, 5); // 5 units of each type in each region
    Board board = new Board(regions);
    Region earth = regions.get(0).getRegionByName(board, "Earth");
    Unit boost5zeros = new Unit(5);
    UnitBoost boost = new UnitBoost(earth, boost5zeros);
    int totalUnits = earth.getUnits().getTotalUnits();
    boost.doAction();     //Earth units are now [0,10,5,5,5,5,5]
    assertEquals(0, earth.getUnits().getUnits().get(0)); // 5-5
    assertEquals(10, earth.getUnits().getUnits().get(1));// 5+5
    assertEquals(5, earth.getUnits().getUnits().get(2));// 5, unchanged
    assertEquals(5, earth.getUnits().getUnits().get(3)); //5, unchanged
    assertEquals(5, earth.getUnits().getUnits().get(4)); // 5, unchanged
    assertEquals(5, earth.getUnits().getUnits().get(5)); // 5, unchanged
    assertEquals(5, earth.getUnits().getUnits().get(6)); // 5, unchanged
    assertEquals(totalUnits, earth.getUnits().getTotalUnits()); //total numUnits unchanged w boost
    Unit boostRestUnits = new Unit(listOfUnitInts(0, 5, 5, 5, 1, 1, 0));
    UnitBoost boost2 = new UnitBoost(earth, boostRestUnits);
    boost2.doAction();     //Earth units are now [0,5,5,5,9,5,6]
    assertEquals(0, earth.getUnits().getUnits().get(0)); // 5, unchanged
    assertEquals(5, earth.getUnits().getUnits().get(1));// 10-5
    assertEquals(5, earth.getUnits().getUnits().get(2));// 5+5-5 (now that 5 have been moved in from 1)
    assertEquals(5, earth.getUnits().getUnits().get(3)); // 5+5-5
    assertEquals(9, earth.getUnits().getUnits().get(4));// 5+5-1
    assertEquals(5, earth.getUnits().getUnits().get(5));// 5+1-1
    assertEquals(6, earth.getUnits().getUnits().get(6));// 5+1
    assertEquals(totalUnits, earth.getUnits().getTotalUnits()); //total numUnits unchanged w boost
  }

    @Test
    public void test_IndividualUnitBoost() {
      List<Region> regions = getRegions(1, 1, 1, 1, 1, 1); // 1 unit of each type in each region
      Board board = new Board(regions);
      Region earth = regions.get(0).getRegionByName(board, "Earth");
      Unit earthUnits = earth.getUnits();
      List<Integer> individUnits = listOfUnitInts(0, 1, 2, 3, 4, 5, 6);
      assertEquals(individUnits, earthUnits.getUnitList()); // actual units on earth [0, 1, 2, 3, 4, 5, 6]
      Unit unitBoost = new Unit(listOfUnitInts(1, 1, 1, 1, 1, 1, 0)); //boost all by 1 (except highest)
      UnitBoost boost = new UnitBoost(earth, unitBoost);
      boost.doAction(); //Earth units are now [0,1,1,1,1,1,2]
      List<Integer> actualUnits = listOfUnitInts(1, 2, 3, 4, 5, 6, 6);  // actual units on earth [1, 2, 3, 4, 5, 6, 6]
      assertEquals(actualUnits, earthUnits.getUnitList()); 
    }
  
  private List<Region> getRegions(int u0, int u1, int u2, int u3, int u4, int u5) {
    AbstractPlayer p1 = new HumanPlayer("Player 1");
    AbstractPlayer p2 = new HumanPlayer("Player 2");
    List<Region> regions = null;
    List<Unit> regionUnits = get6UnitList(u0, u1, u2, u3, u4, u5);
    regions = getRegionHelper(p1, p2, regionUnits);
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
    adj0.add(r0);
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
}
