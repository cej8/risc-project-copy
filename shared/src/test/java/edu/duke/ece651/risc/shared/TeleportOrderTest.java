package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeleportOrderTest {
  @Test
  public void test_Teleport_Behavior() {
    AbstractPlayer p= new HumanPlayer("p1");
    List<Integer> u1 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);
    List<Integer> u2 = listOfUnitInts(10, 10, 10, 10, 10, 10, 10);
    p.getResources().getTechResource().addTech(600);// set resources to 630
  
     Region ra = new Region(p, new Unit(u1));
    ra.setName("A");
    ra.setSize(2);
    Region rb = new Region(p, new Unit(u2));
    rb.setName("B");
    rb.setSize(2);
   
    OrderInterface t1 = new TeleportOrder(ra, rb, new Unit(5));// teleport 5 units from r1 to r2
    assertEquals(70, ra.getUnits().getTotalUnits());
    assertEquals(70, rb.getUnits().getTotalUnits());

    assertEquals(600 + Constants.STARTING_TECH_PRODUCTION, ra.getOwner().getResources().getTechResource().getTech());
    t1.doAction();
    assertEquals(65, ra.getUnits().getTotalUnits());// verify units were removed from the sourde
    assertEquals(75, rb.getUnits().getTotalUnits());// verify units were added to teh source
    // cost of a teleport is 5 * 50 tech resources
    assertEquals(350 + Constants.STARTING_TECH_PRODUCTION, rb.getOwner().getResources().getTechResource().getTech());

  }

   @Test
  public void test_Teleport_Validation() {
    Board b = setBoard();

    Region r1 = b.getRegions().get(0);
    AbstractPlayer p1 = r1.getOwner();
    Region r2 = b.getRegions().get(1);
    Region r3 = b.getRegions().get(2);
    AbstractPlayer p3 = r3.getOwner();
    Region r4 = b.getRegions().get(3);
    AbstractPlayer p2 = r4.getOwner();
    Region r5 = b.getRegions().get(4);
    Region r6 = b.getRegions().get(5);
    TeleportValidator tv4p1 = new TeleportValidator(p1, b);
    TeleportValidator tv4p2 = new TeleportValidator(p2, b);
    TeleportValidator tv4p3 = new TeleportValidator(p3, b);
    TeleportValidator tv4p4 = new TeleportValidator(p3, b);

    // valid teleport order (r1 to r2- both owned by p1 who has sufficient resources
    TeleportOrder valid1 = new TeleportOrder(r1, r2, new Unit(4));
    List<TeleportOrder> validP1Moves = new ArrayList<>();
    validP1Moves.add(valid1);
    assertEquals(true, tv4p1.validUnits(validP1Moves));
    assertEquals(true, tv4p1.validResources(validP1Moves));
    assertEquals(true, tv4p1.validTechLevel());
    assertEquals(true, tv4p1.validOwnership(validP1Moves));
    assertEquals(true, tv4p1.validateOrders(validP1Moves));
    p1.getResources().getTechResource().addTech(200);//reset tech resources for p1;
    
    // invalid teleportOrder (r1 to r1 - player does not have enough units to make
    // move)
    TeleportOrder invalidUnits = new TeleportOrder(r1, r2, new Unit(12));
    List<TeleportOrder> invalidP1Units = new ArrayList<>();
    invalidP1Units.add(invalidUnits);
    assertEquals(false, tv4p1.validUnits(invalidP1Units));
    assertEquals(true, tv4p1.validResources(invalidP1Units));
    assertEquals(true, tv4p1.validTechLevel());
    assertEquals(true, tv4p1.validOwnership(invalidP1Units));

    // invalid teleportOrder(r1 to r4- player does not own both source and
    // destination
    TeleportOrder invalidOwnership = new TeleportOrder(r1, r4, new Unit(1));
    List<TeleportOrder> invalidP1Owner = new ArrayList<>();
    invalidP1Owner.add(invalidOwnership);
    assertEquals(true, tv4p1.validUnits(invalidP1Owner));
    assertEquals(true, tv4p1.validResources(invalidP1Owner));
    assertEquals(true, tv4p1.validTechLevel());
    assertEquals(false, tv4p1.validOwnership(invalidP1Owner));

    // invalid teleportOrder(r4 to r5- player does not have enough tech resources to
    // make move
    TeleportOrder invalidResources = new TeleportOrder(r4, r5, new Unit(5));
    List<TeleportOrder> invalidP2Resources = new ArrayList<>();
    invalidP2Resources.add(invalidResources);
    assertEquals(true, tv4p2.validUnits(invalidP2Resources));
    assertEquals(false, tv4p2.validResources(invalidP2Resources));
    assertEquals(true, tv4p2.validTechLevel());
    assertEquals(true, tv4p2.validOwnership(invalidP2Resources));

    // invalid teleportOrder(r1 to r2- player does not have the correct tech level
    TeleportOrder invalidTechLevel = new TeleportOrder(r3, r6, new Unit(4));
    List<TeleportOrder> invalidP3Level = new ArrayList<>();
    invalidP3Level.add(invalidTechLevel);
    assertEquals(true, tv4p3.validUnits( invalidP3Level));
    assertEquals(true, tv4p3.validResources( invalidP3Level));
    assertEquals(false, tv4p3.validTechLevel());
    assertEquals(true, tv4p3.validOwnership( invalidP3Level));


    // invalid teleportOrder(r1 to r2- player does not have enough units
    TeleportOrder invalidUnitNum = new TeleportOrder(r3, r6, new Unit(1000));
    List<TeleportOrder> invalidP4Level = new ArrayList<>();
    invalidP4Level.add(invalidUnitNum);
    assertEquals(false, tv4p4.validUnits( invalidP4Level));

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

    AbstractPlayer p2 = new HumanPlayer("player 2");
    p2.getMaxTechLevel().upgradeLevel();// upgrade to 2
    p2.getMaxTechLevel().upgradeLevel();// upgrade to 3
    p2.getMaxTechLevel().upgradeLevel();// upgrade to 4

    AbstractPlayer p3 = new HumanPlayer("player 3");
    p3.getResources().getTechResource().addTech(800);// set resorces to 630

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
