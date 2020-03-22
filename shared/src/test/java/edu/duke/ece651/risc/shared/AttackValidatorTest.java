package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AttackValidatorTest {
  @Test
  public void test_AttackUnits() {
   List<Region> regions = getRegions();
    Board board = new Board(regions);
    ValidatorInterface<AttackOrder> av = new AttackValidator(board);
    //Orders using all units
    List<Unit> regionUnits = get6UnitList(5, 10, 15, 20, 25, 30);
    List<AttackOrder> attackAllUnits = getAttacksIndependent(regions, regionUnits); //false: attacking w  all units 
    assertEquals(false, av.validateUnits(attackAllUnits));

    //Orders using 0 units
    List<Unit> invalidUnits = get6UnitList(0, 9, 14, 19, 24, 29); //false: moving 0 units
    List<AttackOrder> attackInvalidUnits = getAttacksDependent(regions, invalidUnits); //false: attacking w 0 units
    assertEquals(false, av.validateUnits(attackInvalidUnits));

    //Orders for which sourceUnits < order Units
    List<Unit> tooManyUnits = get6UnitList(100, 9, 14, 19, 24, 29);
    List<AttackOrder> attackWithTooManyUnits = getAttacksDependent(regions, tooManyUnits); //false: attacking w too many units
    assertEquals(false, av.validateUnits(attackWithTooManyUnits));
  }


  @Test
  public void Attack_UnitTest() {
    List<Region> regions = getRegions();
    Board board = new Board(regions);
    ValidatorInterface<AttackOrder> av = new AttackValidator(board);

    //TODO -- mock randomness of attack to predetermine winner to test more attacks
    //would AttackOrder.rollHelper have to be public?
    List<Unit> smallAttacks = get6UnitList(1, 1, 1, 1, 1, 1); //probably true: attacking with 1 unit
    for (Region r : regions) {
      System.out.println(r.getName() + " has " + r.getUnits().getUnits());
    }
    List<AttackOrder> attackOneUnit = getAttacksDependent(regions, smallAttacks);
    assertEquals(true, av.validateUnits(attackOneUnit));
    for (Region r : regions) {
      System.out.println(r.getName() + " now has " + r.getUnits().getUnits());
    }
  }
  
  @Test
  public void test_UnitsandRegions() {
    List<Region> regions = getRegions();
    Board board = new Board(regions);
    ValidatorInterface<AttackOrder> av = new AttackValidator(board);
    AttackOrder mercuryAttackSaturn = new AttackOrder(regions.get(3), regions.get(4), new Unit(5));
    List<AttackOrder> attacks = new ArrayList<AttackOrder>();
    attacks.add(mercuryAttackSaturn);
    assertEquals(true, av.validateOrders(attacks));
  }

  private List<Region> getRegions(){
    AbstractPlayer p1 = new HumanPlayer("Player 1");
    AbstractPlayer p2 = new HumanPlayer("Player 2");
    List<Unit> regionUnits = get6UnitList(5, 10, 15, 20, 25, 30);
    List<Region> regions = getRegionHelper(p1, p2, regionUnits);
    return regions;
  }

  private List<Unit> get6UnitList(int u0, int u1, int u2, int u3, int u4, int u5){
    List<Unit> units = new ArrayList<Unit>();
    Unit unit0 = new Unit(u0);
    units.add(unit0);
    Unit unit1 = new Unit(u1);
    units.add(unit1);
    Unit unit2 = new Unit(u2);
    units.add(unit2);
    Unit unit3 = new Unit(u3);
    units.add(unit3);
    Unit unit4 = new Unit(u4);
    units.add(unit4);
    Unit unit5 = new Unit(u5);
    units.add(unit5);
    return units;
  }

  private List<Region> getRegionHelper(AbstractPlayer p1, AbstractPlayer p2, List<Unit> units){
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

    List<Region> regions = new ArrayList<Region>();
    regions.add(r0);
    regions.add(r1);
    regions.add(r2);
    regions.add(r3);
    regions.add(r4);
    regions.add(r5);

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
    return regions;
  }

  private List<AttackOrder> getAttacksIndependent(List<Region> regions, List<Unit> units) {
    AttackOrder attack01 = new AttackOrder(regions.get(0), regions.get(1), units.get(0));
    AttackOrder attack23 = new AttackOrder(regions.get(2), regions.get(3), units.get(2));
    AttackOrder attack45 = new AttackOrder(regions.get(4), regions.get(5), units.get(4));
    List<AttackOrder> attacks = new ArrayList<AttackOrder>();
    attacks.add(attack01);
    attacks.add(attack23);
    attacks.add(attack45);
    return attacks;
  }

  private List<AttackOrder> getAttacksDependent(List<Region> regions, List<Unit> units) {
    AttackOrder attack01 = new AttackOrder(regions.get(0), regions.get(1), units.get(0));
    AttackOrder attack12 = new AttackOrder(regions.get(1), regions.get(2), units.get(1));
    AttackOrder attack23 = new AttackOrder(regions.get(2), regions.get(3), units.get(2));
    AttackOrder attack34 = new AttackOrder(regions.get(3), regions.get(4), units.get(3));
    AttackOrder attack45 = new AttackOrder(regions.get(4), regions.get(5), units.get(4));
    AttackOrder attack50 = new AttackOrder(regions.get(5), regions.get(0), units.get(5));
    List<AttackOrder> attacks = new ArrayList<AttackOrder>();
    attacks.add(attack01);
    attacks.add(attack12);
    attacks.add(attack23);
    attacks.add(attack34);
    attacks.add(attack45);
    attacks.add(attack50);
    return attacks;
  }

}

