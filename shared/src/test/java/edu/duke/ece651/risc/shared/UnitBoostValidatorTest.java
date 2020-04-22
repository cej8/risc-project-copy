package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class UnitBoostValidatorTest {
  @Test
  public void test_UBVregion() {
    List<Region> regions = getRegions(1, 1, 1, 1, 1, 1); // 1 unit of each type in each region
    Board board = new Board(regions);
    Region earth = regions.get(0).getRegionByName(board, "Earth");
    AbstractPlayer player1 = earth.getOwner(); // set player 1 = current owner of earth
    AbstractPlayer player2 = new HumanPlayer("Player 2");
    Unit earthUnits = new Unit(earth.getUnits().getUnits());
    Unit unitBoost = new Unit(listOfUnitInts(1, 0, 0, 0, 0, 0, 0)); // cost=3, player1 should have enough
    UnitBoost boost = new UnitBoost(earth, unitBoost);
    Board boardCopy = (Board) DeepCopy.deepCopy(board);
    ValidatorInterface<UnitBoost> ubv = new UnitBoostValidator(player1, boardCopy);
    List<UnitBoost> orders = new ArrayList<UnitBoost>();
    orders.add(boost);
    assertEquals(true, ubv.validateOrders(orders)); // true: owns region
    assertEquals(earthUnits.getUnits(), earth.getUnits().getUnits()); // units remained unchanged
    boardCopy = (Board) DeepCopy.deepCopy(board);
    earth.setOwner(player2); // player 2 now owns earth
    assertEquals(false, ubv.validateOrders(orders)); // false: doesn't own region
    assertEquals(earthUnits.getUnits(), earth.getUnits().getUnits()); // units remained unchanged

  }

  @Test
  public void test_UBVunits() {
    List<Region> regions = getRegions(1, 1, 1, 1, 1, 1); // 1 unit of each type in each region
    Board board = new Board(regions);
    regions.get(0).getRegionByName(board, "Earth").getOwner().getResources().getTechResource().addTech(300);
    Board copy = (Board) DeepCopy.deepCopy(board);
    Region earth = regions.get(0).getRegionByName(copy, "Earth");
   
    Unit unitBoost = new Unit(listOfUnitInts(0, 0, 0, 0, 0, 5, 0));
    upgradeTech(earth, 5); //upgrade player tech 5 times

    Region testPlague = new Region("Fire");
    AbstractPlayer plagueOwner= new HumanPlayer("plague owner");
    testPlague.setPlague(true);
    UnitBoost ub = new UnitBoost(testPlague, unitBoost);
    UnitBoostValidator plagueValidator = new UnitBoostValidator(plagueOwner, board);
    List<UnitBoost> plagueBoost= new ArrayList<UnitBoost>();
    plagueBoost.add(ub);
    assertEquals(false, plagueValidator.validateRegions(plagueBoost));

    UnitBoost boost = new UnitBoost(earth, unitBoost);
    ValidatorInterface<UnitBoost> ubv = new UnitBoostValidator(earth.getOwner(), copy);
    List<UnitBoost> orders = new ArrayList<UnitBoost>();
    orders.add(boost); // doesn't have enough units (1 vs 5), fail
    assertEquals(false, ubv.validateOrders(orders)); // false

    // successive orders failing  -- tech
    Board boardCopy = (Board) DeepCopy.deepCopy(board);
    earth = regions.get(0).getRegionByName(boardCopy, "Earth");
        upgradeTech(earth, 2); //upgrade player tech 2 times to a level 3
    System.out.println("Earth currently has units " + earth.getUnits().getUnits());
    Unit unitBoost0 = new Unit(listOfUnitInts(1, 0, 0, 0, 0, 0, 0));
    UnitBoost boost0 = new UnitBoost(earth, unitBoost0); // valid: units will be [0, 2, 1, 1, 1, 1, 1]
    orders.set(0, boost0);
    Unit unitBoost1 = new Unit(listOfUnitInts(0, 2, 0, 0, 0, 0, 0));
    UnitBoost boost1 = new UnitBoost(earth, unitBoost1); // valid: units will be [0, 0, 3, 1, 1, 1, 1]
    orders.add(boost1);
    Unit unitBoost2 = new Unit(listOfUnitInts(0, 0, 3, 0, 0, 0, 0)); // valid: units will be [0, 0, 0, 4, 1, 1, 1]
    UnitBoost boost2 = new UnitBoost(earth, unitBoost2);
    orders.add(boost2);
    Unit unitBoost3 = new Unit(listOfUnitInts(0, 0, 0, 4, 0, 0, 0)); // INvalid: tech level is 3, needs 4 
    UnitBoost boost3 = new UnitBoost(earth, unitBoost3);
    orders.add(boost3);
    ValidatorInterface<UnitBoost> ubvValid = new UnitBoostValidator(earth.getOwner(), boardCopy);
    assertEquals(false, ubvValid.validateOrders(orders)); //invalID

 // successive orders failing suceeding

    boardCopy = (Board) DeepCopy.deepCopy(board);
    earth = regions.get(0).getRegionByName(boardCopy, "Earth");
        upgradeTech(earth, 5); //upgrade player tech 5 times 
    System.out.println("Earth currently has units " + earth.getUnits().getUnits());
    unitBoost0 = new Unit(listOfUnitInts(1, 0, 0, 0, 0, 0, 0));
    boost0 = new UnitBoost(earth, unitBoost0); // valid: units will be [0, 2, 1, 1, 1, 1, 1]
    orders = new ArrayList<UnitBoost>();
    orders.add(boost0);
    unitBoost1 = new Unit(listOfUnitInts(0, 2, 0, 0, 0, 0, 0));
    boost1 = new UnitBoost(earth, unitBoost1); // valid: units will be [0, 0, 3, 1, 1, 1, 1]
    orders.add(boost1);
    unitBoost2 = new Unit(listOfUnitInts(0, 0, 3, 0, 0, 0, 0)); // valid: units will be [0, 0, 0, 4, 1, 1, 1]
    boost2 = new UnitBoost(earth, unitBoost2);
    orders.add(boost2);
    unitBoost3 = new Unit(listOfUnitInts(0, 0, 0, 4, 0, 0, 0)); // valid: units will be [0, 0, 0, 0, 5, 1, 1]
    boost3 = new UnitBoost(earth, unitBoost3);
    orders.add(boost3);
    ubvValid = new UnitBoostValidator(earth.getOwner(), boardCopy);
    assertEquals(true, ubvValid.validateOrders(orders));
    
    // successive orders failing
    List<UnitBoost> ordersS = new ArrayList<UnitBoost>();
    System.out.println("Earth currently has units " + earth.getUnits().getUnits());
    Unit unitBoost0S = new Unit(listOfUnitInts(1, 0, 0, 0, 0, 0, 0));
    boardCopy = (Board) DeepCopy.deepCopy(board);
    earth = regions.get(0).getRegionByName(boardCopy, "Earth");
    upgradeTech(earth, 5); //upgrade player tech 5 times 
    UnitBoost boost0S = new UnitBoost(earth, unitBoost0S); // valid: units will be [0, 2, 1, 1, 1, 1, 1]
    ordersS.add(boost0S);
    Unit unitBoost1S = new Unit(listOfUnitInts(0, 2, 0, 0, 0, 0, 0));
    UnitBoost boost1S = new UnitBoost(earth, unitBoost1S); // valid: units will be [0, 0, 3, 1, 1, 1, 1]
    ordersS.add(boost1S);
    Unit unitBoost2S = new Unit(listOfUnitInts(0, 0, 4, 0, 0, 0, 0)); // invalid: units are [0, 0, *3*, 1, 1, 1, 1]
    UnitBoost boost2S = new UnitBoost(earth, unitBoost2S);
    ordersS.add(boost2S);
    ValidatorInterface<UnitBoost> ubvValidS = new UnitBoostValidator(earth.getOwner(), boardCopy);
    assertEquals(false, ubvValidS.validateOrders(ordersS));

  }

  private void upgradeTech(Region earth, int num) {
    for (int i = 0; i < num; i++) {
      earth.getOwner().getMaxTechLevel().upgradeLevel();
    }
  }

  @Test
  public void testUBV_tech(){


  }

  @Test
  public void test_UBVcorners() {
    List<Region> regions = getRegions(1, 1, 1, 1, 1, 1); // 1 unit of each type in each region
    Board board = new Board(regions);
    regions.get(0).getRegionByName(board, "Earth").getOwner().getResources().getTechResource().addTech(300); // 15 + 125
                                                                                                             // = 140
    Board boardCopy = (Board) DeepCopy.deepCopy(board);
    Region earth = regions.get(0).getRegionByName(boardCopy, "Earth");
    UnitBoostValidator ubv = new UnitBoostValidator(earth.getOwner(), boardCopy);
    List<UnitBoost> orders = new ArrayList<UnitBoost>();
    Unit illegalUnits = new Unit(listOfUnitInts(0, 0, 0, 0, 0, 0, 1));
        upgradeTech(earth, 5); //upgrade player tech 5 times 
    UnitBoost boostUnits = new UnitBoost(earth, illegalUnits);
    orders.add(boostUnits);
    assertEquals(false, ubv.validateOrders(orders)); // false
    boardCopy = (Board) DeepCopy.deepCopy(board);
    earth = regions.get(0).getRegionByName(boardCopy, "Earth");
        upgradeTech(earth, 5); //upgrade player tech 5 times 

    ValidatorInterface<UnitBoost> ubv2 = new UnitBoostValidator(earth.getOwner(), boardCopy);
    List<UnitBoost> orders2 = new ArrayList<UnitBoost>();
    earth.getOwner().getResources().getTechResource().useTech(175); // now player has 140 tech (315-175=140)
    Unit validUnits = new Unit(listOfUnitInts(1, 1, 1, 1, 1, 1, 0)); // cost should be exactly 140
    UnitBoost boost = new UnitBoost(earth, validUnits);
    orders2.add(boost);
    assertEquals(true, ubv2.validateOrders(orders2)); // true
    boardCopy = (Board) DeepCopy.deepCopy(board);
    earth = regions.get(0).getRegionByName(boardCopy, "Earth");
        upgradeTech(earth, 5); //upgrade player tech 5 times 
    ValidatorInterface<UnitBoost> ubv3 = new UnitBoostValidator(earth.getOwner(), boardCopy);
    List<UnitBoost> orders3 = new ArrayList<UnitBoost>();
    earth.getOwner().getResources().getTechResource().useTech(176); // now player only has 139 tech (315-176=139)
    Unit failUnits = new Unit(listOfUnitInts(1, 1, 1, 1, 1, 1, 0)); // same order that just passed, cost 140
    UnitBoost failBoost = new UnitBoost(earth, failUnits);
    orders3.add(failBoost);
    assertEquals(true, ubv3.validateOrders(orders3)); // fail, not enough tech

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
