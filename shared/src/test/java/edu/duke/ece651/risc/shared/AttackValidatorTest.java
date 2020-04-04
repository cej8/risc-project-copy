package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import edu.duke.ece651.risc.shared.*;

import org.junit.jupiter.api.Test;

public class AttackValidatorTest {
  //  @Test
  public void test_AttackUnits() {
    List<Region> regions = getRegions();
    Board board = new Board(regions);
    // ValidatorInterface<AttackMove> av1 = new AttackValidator(new
    // HumanPlayer("Player 1"),(Board)DeepCopy.deepCopy(board));
    AttackValidator av1 = new AttackValidator(new HumanPlayer("Player 1"), (Board) DeepCopy.deepCopy(board));
    // ValidatorInterface<AttackMove> av2 = new AttackValidator(new
    // HumanPlayer("Player 2"),(Board)DeepCopy.deepCopy(board));
    AttackValidator av2 = new AttackValidator(new HumanPlayer("Player 2"), (Board) DeepCopy.deepCopy(board));

    // Orders using all units
    List<Unit> regionUnits = get6UnitList(5, 10, 15, 20, 25, 30);
    List<AttackMove> attackAllUnits = getAttacksIndependent(regions, regionUnits); // false: attacking w all units
    assertEquals(false, av1.validateUnits(attackAllUnits.subList(0, 2)));
    assertEquals(false, av2.validateUnits(attackAllUnits.subList(2, 3)));

    // Need new to refresh map...
    av1 = new AttackValidator(new HumanPlayer("Player 1"), (Board) DeepCopy.deepCopy(board));
    av2 = new AttackValidator(new HumanPlayer("Player 2"), (Board) DeepCopy.deepCopy(board));
    // Orders using 0 units
    List<Unit> invalidUnits = get6UnitList(0, 9, 14, 19, 24, 29); // false: moving 0 units
    List<AttackMove> attackInvalidUnits = getAttacksDependent(regions, invalidUnits); // false: attacking w 0 units
    assertEquals(false, av1.validateUnits(attackInvalidUnits.subList(0, 4)));
    assertEquals(true, av2.validateUnits(attackInvalidUnits.subList(4, 6)));

    // Need new to refresh map...
    av1 = new AttackValidator(new HumanPlayer("Player 1"), (Board) DeepCopy.deepCopy(board));
    av2 = new AttackValidator(new HumanPlayer("Player 2"), (Board) DeepCopy.deepCopy(board));
    // Orders for which sourceUnits < order Units
    List<Unit> tooManyUnits = get6UnitList(100, 9, 14, 19, 24, 29);
    List<AttackMove> attackWithTooManyUnits = getAttacksDependent(regions, tooManyUnits); // false: attacking w too many
                                                                                          // units
    assertEquals(false, av1.validateUnits(attackWithTooManyUnits.subList(0, 4)));
    assertEquals(true, av2.validateUnits(attackWithTooManyUnits.subList(4, 6)));
  }

  //  @Test
  public void Attack_UnitTest() {
    List<Region> regions = getRegions();
    Board board = new Board(regions);
    // ValidatorInterface<AttackMove> av1 = new AttackValidator(new
    // HumanPlayer("Player 1"), board);
    // ValidatorInterface<AttackMove> av2 = new AttackValidator(new
    // HumanPlayer("Player 2"), board);
    AttackValidator av1 = new AttackValidator(new HumanPlayer("Player 1"), board);
    AttackValidator av2 = new AttackValidator(new HumanPlayer("Player 2"), board);

    // TODO -- mock randomness of attack to predetermine winner to test more attacks
    // would AttackOrder.rollHelper have to be public?
    List<Unit> smallAttacks = get6UnitList(1, 1, 1, 1, 1, 1); // probably true: attacking with 1 unit
    for (Region r : regions) {
      System.out.println(r.getName() + " has " + r.getUnits().getUnits());
    }
    List<AttackMove> attackOneUnit = getAttacksDependent(regions, smallAttacks);
    assertEquals(true, av1.validateUnits(attackOneUnit.subList(0, 4)));
    assertEquals(true, av2.validateUnits(attackOneUnit.subList(4, 6)));

    for (Region r : regions) {
      System.out.println(r.getName() + " now has " + r.getUnits().getUnits());
    }
  }

  // @Test
  public void test_UnitsandRegions() {
    List<Region> regions = getRegions();
    Board board = new Board(regions);
    // ValidatorInterface<AttackMove> av = new AttackValidator(new
    // HumanPlayer("Player 1"), board);
    AttackValidator av = new AttackValidator(new HumanPlayer("Player 1"), board);

    AttackMove mercuryAttackSaturn = new AttackMove(regions.get(3), regions.get(4), new Unit(5));
    List<AttackMove> attacks = new ArrayList<AttackMove>();
    attacks.add(mercuryAttackSaturn);
    assertEquals(true, av.validateOrders(attacks));
  }

  private List<Region> getRegions() {
    AbstractPlayer p1 = new HumanPlayer("Player 1");
    AbstractPlayer p2 = new HumanPlayer("Player 2");
    List<Unit> regionUnits = get6UnitList(5, 10, 15, 20, 25, 30);
    List<Region> regions = getRegionHelper(p1, p2, regionUnits);
    return regions;
  }

  private List<Unit> get6UnitList(int u0, int u1, int u2, int u3, int u4, int u5) {
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

  private Unit getStartUnits() {
    List<Integer> sourceUnits = new ArrayList<Integer>();
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    Unit u = new Unit();
    u.setUnits(sourceUnits);
    return u;
  }

  private Unit getAttackUnitsValid() {
    List<Integer> attackUnits = new ArrayList<Integer>();
    attackUnits.add(1);
    attackUnits.add(1);
    attackUnits.add(0);
    attackUnits.add(0);
    attackUnits.add(1);
    attackUnits.add(0);
    attackUnits.add(1);
    Unit u = new Unit();
    u.setUnits(attackUnits);
    return u;
  }

  private Unit getAttackUnitsInvalid() {
    List<Integer> attackUnits = new ArrayList<Integer>();
    attackUnits.add(1);
    attackUnits.add(1);
    attackUnits.add(0);
    attackUnits.add(0);
    attackUnits.add(2);
    attackUnits.add(0);
    attackUnits.add(1);
    Unit u = new Unit();
    u.setUnits(attackUnits);
    return u;
  }

  private List<Region> getRegionHelper(AbstractPlayer p1, AbstractPlayer p2, List<Unit> units) {
    Region r0 = new Region(p1, getStartUnits());
    r0.setName("Earth");
    Region r1 = new Region(p1, getStartUnits());
    r1.setName("Mars");
    Region r2 = new Region(p1, getStartUnits());
    r2.setName("Venus");
    Region r3 = new Region(p1, getStartUnits());
    r3.setName("Mercury");
    Region r4 = new Region(p2, getStartUnits());
    r4.setName("Saturn");
    Region r5 = new Region(p2, getStartUnits());
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

  private List<AttackMove> getAttacksIndependent(List<Region> regions, List<Unit> units) {
    AttackMove attack01 = new AttackMove(regions.get(0), regions.get(1), getAttackUnitsValid());
    AttackMove attack23 = new AttackMove(regions.get(2), regions.get(5),getAttackUnitsValid());
    AttackMove attack45 = new AttackMove(regions.get(4), regions.get(5), getAttackUnitsValid());
    List<AttackMove> attacks = new ArrayList<AttackMove>();
    attacks.add(attack01);
    attacks.add(attack23);
    attacks.add(attack45);
    return attacks;
  }

  private List<AttackMove> getAttacksDependent(List<Region> regions, List<Unit> units) {
    AttackMove attack01 = new AttackMove(regions.get(0), regions.get(1), units.get(0));
    AttackMove attack12 = new AttackMove(regions.get(1), regions.get(2), units.get(1));
    AttackMove attack23 = new AttackMove(regions.get(2), regions.get(3), units.get(2));
    AttackMove attack34 = new AttackMove(regions.get(3), regions.get(4), units.get(3));
    AttackMove attack45 = new AttackMove(regions.get(4), regions.get(5), units.get(4));
    AttackMove attack50 = new AttackMove(regions.get(5), regions.get(0), units.get(5));
    List<AttackMove> attacks = new ArrayList<AttackMove>();
    attacks.add(attack01);
    attacks.add(attack12);
    attacks.add(attack23);
    attacks.add(attack34);
    attacks.add(attack45);
    attacks.add(attack50);
    return attacks;
  }

}
