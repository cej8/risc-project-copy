package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceBoostTest {
  @Test
  public void test_resourceBoostOrder() {

    AbstractPlayer p = new HumanPlayer("p1");
    List<Integer> u1 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);
    p.getResources().getTechResource().addTech(800);// set resources to 630

    Region r1 = new Region(p, new Unit(u1));
    r1.setName("A");
    r1.setSize(2);

    ResourceBoost rb = new ResourceBoost(r1);
    assertEquals(70, r1.getUnits().getTotalUnits());
    assertEquals(1, r1.getRegionLevel().getRegionLevel());
    assertEquals(1.0, r1.getRegionLevel().getMultiplier());
    assertEquals(830, r1.getOwner().getResources().getTechResource().getTech());

    rb.doAction();// upgrade to level 2
    assertEquals(2, r1.getRegionLevel().getRegionLevel());
    assertEquals(1.25, r1.getRegionLevel().getMultiplier());
    assertEquals(690, r1.getOwner().getResources().getTechResource().getTech());
    rb.doAction();// upgrade to level 3
    assertEquals(3, r1.getRegionLevel().getRegionLevel());
    assertEquals(1.5, r1.getRegionLevel().getMultiplier());
    assertEquals(515, r1.getOwner().getResources().getTechResource().getTech());
    rb.doAction();// upgrade to level 4
    assertEquals(4, r1.getRegionLevel().getRegionLevel());
    assertEquals(2.0, r1.getRegionLevel().getMultiplier());
    assertEquals(305, r1.getOwner().getResources().getTechResource().getTech());
  }

  public List<Integer> listOfUnitInts(int u0, int u1, int u2, int u3, int u4, int u5, int u6) {
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

  @Test
  public void test_ResourceBoostValidator() {
    Board b = setBoard();
    Region r1 = b.getRegions().get(0);
    AbstractPlayer p1 = r1.getOwner();
    Region r2 = b.getRegions().get(1);
    Region r3 = b.getRegions().get(2);
    AbstractPlayer p3 = r3.getOwner();
    Region r4 = b.getRegions().get(3);
    AbstractPlayer p2 = r4.getOwner();
    // Region r5 = b.getRegions().get(4);
    // Region r6 = b.getRegions().get(5);
    ResourceBoostValidator rbv4p1 = new ResourceBoostValidator(p1, b);
    ResourceBoostValidator rbv4p2 = new ResourceBoostValidator(p2, b);
    ResourceBoostValidator rbv4p3 = new ResourceBoostValidator(p3, b);

    // p1 has max region level of 4
    assertEquals(4, p1.getMaxTechLevel().getMaxRegionLevel());
    // p2 has max region level of 3
    assertEquals(3, p2.getMaxTechLevel().getMaxRegionLevel());
    // p3 hs max region level of 2
    assertEquals(2, p3.getMaxTechLevel().getMaxRegionLevel());
    // invalid resource boost of r4 player does not have enough tech resources to
    // make move
    ResourceBoost invalidResources = new ResourceBoost(r4);
    List<ResourceBoost> invalidP2Resources = new ArrayList<>();
    invalidP2Resources.add(invalidResources);
    assertEquals(false, rbv4p2.validResources(invalidP2Resources));
    assertEquals(true, rbv4p2.validTechLevel(invalidP2Resources));
    assertEquals(true, rbv4p2.validOwnership(invalidP2Resources));
    // invalid resource boost(of r3 player does not have the correct tech level fro
    // two boosts back to back
    ResourceBoost invalidTechLevel = new ResourceBoost(r3);
    ResourceBoost invalidTechLevel2 = new ResourceBoost(r3);
    List<ResourceBoost> invalidP3Level = new ArrayList<>();
    invalidP3Level.add(invalidTechLevel);
    assertEquals(true, rbv4p3.validResources(invalidP3Level));
    assertEquals(true, rbv4p3.validTechLevel(invalidP3Level));

    invalidP3Level.add(invalidTechLevel2);

    assertEquals(false, rbv4p3.validTechLevel(invalidP3Level));
    assertEquals(true, rbv4p3.validOwnership(invalidP3Level));

    // invalid resource boost (region 1 cannot boost past level 4)
    ResourceBoost invalidBoostLimit1 = new ResourceBoost(r1);
    ResourceBoost invalidBoostLimit2 = new ResourceBoost(r1);
    ResourceBoost invalidBoostLimit3 = new ResourceBoost(r1);
    ResourceBoost invalidBoostLimit4 = new ResourceBoost(r1);

    List<ResourceBoost> invalidP1Limit = new ArrayList<>();
    invalidP1Limit.add(invalidBoostLimit1);
    invalidP1Limit.add(invalidBoostLimit2);
    invalidP1Limit.add(invalidBoostLimit3);

    assertEquals(true, rbv4p1.validResources(invalidP1Limit));

    assertEquals(true, rbv4p1.validTechLevel(invalidP1Limit));
    // valid until additional boost is attempted
    invalidP1Limit.add(invalidBoostLimit4);

    assertEquals(false, rbv4p1.validTechLevel(invalidP1Limit));
    assertEquals(true, rbv4p1.validOwnership(invalidP1Limit));
    // invalid ressource boostr(r4- player does not own
    // destination
    ResourceBoost invalidOwnership = new ResourceBoost(r4);
    List<ResourceBoost> invalidP1Owner = new ArrayList<>();
    invalidP1Owner.add(invalidOwnership);
    assertEquals(true, rbv4p1.validResources(invalidP1Owner));
    assertEquals(true, rbv4p1.validTechLevel(invalidP1Owner));
    assertEquals(false, rbv4p1.validOwnership(invalidP1Owner));

  }

  public Board setBoard() {
    List<Integer> u1 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);
    List<Integer> u2 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);
    List<Integer> u3 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);
    List<Integer> u4 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);
    List<Integer> u5 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);
    List<Integer> u6 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);

    AbstractPlayer p1 = new HumanPlayer("player 1");// valid player
    p1.getResources().getTechResource().addTech(800);// set resources to 630
    p1.getMaxTechLevel().upgradeLevel();// upgrade to 2
    p1.getMaxTechLevel().upgradeLevel();// upgrade to 3
    p1.getMaxTechLevel().upgradeLevel();// upgrade to 4
    p1.getMaxTechLevel().upgradeLevel();// upgrade to 5
    p1.getMaxTechLevel().upgradeLevel();// upgrade to 6

    AbstractPlayer p2 = new HumanPlayer("player 2");
    p2.getMaxTechLevel().upgradeLevel();// upgrade to 2
    p2.getMaxTechLevel().upgradeLevel();// upgrade to 3
    p2.getMaxTechLevel().upgradeLevel();// upgrade to 4

    AbstractPlayer p3 = new HumanPlayer("player 3");
    p3.getResources().getTechResource().addTech(800);// set resorces to 630
    p3.getMaxTechLevel().upgradeLevel();// upgrade to 2

    Region r1 = new Region(p1, new Unit(u1));
    r1.setName("Earth");
    r1.setSize(2);
    Region r2 = new Region(p1, new Unit(u2));
    r2.setName("Wind");
    r2.setSize(2);
    Region r3 = new Region(p3, new Unit(u3));
    r3.setName("Fire");
    r3.setSize(2);
    Region r4 = new Region(p2, new Unit(u4));
    r4.setName("Rain");
    r4.setSize(2);
    Region r5 = new Region(p2, new Unit(u5));
    r5.setName("Grass");
    r5.setSize(2);
    Region r6 = new Region(p3, new Unit(u6));
    r6.setName("Moon");
    r6.setSize(2);

    List<Region> adj1 = new ArrayList<Region>();
    adj1.add(r2);
    adj1.add(r3);
    adj1.add(r4);
    r1.setAdjRegions(adj1);
    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r3);
    adj2.add(r6);
    r2.setAdjRegions(adj2);
    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r1);
    adj3.add(r2);
    adj3.add(r5);
    r3.setAdjRegions(adj3);
    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r1);
    r4.setAdjRegions(adj4);
    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r3);
    r5.setAdjRegions(adj5);
    List<Region> adj6 = new ArrayList<Region>();
    adj6.add(r2);
    r6.setAdjRegions(adj6);

    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r3);
    regions.add(r4);
    regions.add(r5);
    regions.add(r6);
    Board b = new Board(regions);
    return b;
  }

}
