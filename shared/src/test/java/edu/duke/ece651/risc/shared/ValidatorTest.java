package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
  // Test Board Setup
  // r1 - r2
  // | |
  // r3 r4
  // | |
  // r5 - r6
  // P1 owns r1,r2,r4.r5
  // P2 owns r3,r6
  @Test
  public void test_RegionValidator() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    AbstractPlayer p2 = new HumanPlayer("player 2");
    List<Region> regions = getRegionList(p1, p2);
    Board b = new Board(regions);

    RegionValidator rv = new RegionValidator();
    ValidatorHelper vh = new ValidatorHelper();
    
    List<MoveOrder> moves = getMoveOrders(regions);
    List<AttackOrder> attacks = getAttackList(regions);

    List<Region> pRegions = getRegionsForPlacement(p1, p2);
    List<PlacementOrder> placements = getPlacementList(p1, p2);
    Board b1 = new Board(pRegions);
    
    assertEquals(true, rv.isValidMove(moves.get(0)));
    assertEquals(false, rv.isValidMove(moves.get(1)));
    assertEquals(true, rv.isValidMove(moves.get(2)));
    assertEquals(false, rv.isValidMove(moves.get(3)));
    assertEquals(false, vh.getRegionValidator(). movesAreValid(moves,b));
   
    assertEquals(true, rv.isValidAttack(attacks.get(0)));
    assertEquals(false, rv.isValidAttack(attacks.get(1)));
    assertEquals(false, rv.isValidAttack(attacks.get(2)));
    assertEquals(false, vh.getRegionValidator(). attacksAreValid(attacks,b));
     
    assertEquals(true, rv.isValidPlacement(placements.get(0),p1));//valid
    assertEquals(false, rv.isValidPlacement(placements.get(1),p1));//invalid (does not own)
    assertEquals(false, vh.getRegionValidator().placementsAreValid(placements, p2,b1));

  }

  private List<Region> getRegionList(AbstractPlayer p1, AbstractPlayer p2){
    Region r1 = new Region(p1, new Unit(1));
    Region r2 = new Region(p1, new Unit(2));
    Region r4 = new Region(p1, new Unit(4));
    Region r5 = new Region(p1, new Unit(5));
    Region r3 = new Region(p2, new Unit(3));
    Region r6 = new Region(p2, new Unit(6));

    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r4);
    regions.add(r3);
    regions.add(r5);
    regions.add(r6);

    List<Region> adj1 = new ArrayList<Region>();
    adj1.add(r2);
    adj1.add(r3);
    r1.setAdjRegions(adj1);

    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r4);
    r2.setAdjRegions(adj2);

    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r1);
    adj3.add(r5);
    r3.setAdjRegions(adj3);

    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r2);
    adj4.add(r6);
    r4.setAdjRegions(adj4);

    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r3);
    adj5.add(r6);
    r5.setAdjRegions(adj5);

    List<Region> adj6 = new ArrayList<Region>();
    adj6.add(r4);
    adj6.add(r5);
    r6.setAdjRegions(adj6);
    return regions;
  }

  private List<MoveOrder> getMoveOrders(List<Region> regions) {
    MoveOrder move12 = new MoveOrder(regions.get(0), regions.get(1), new Unit(5));// valid adjacent
    MoveOrder move23 = new MoveOrder(regions.get(1), regions.get(3), new Unit(2));// invalid (diff owner)
    MoveOrder move14 = new MoveOrder(regions.get(0), regions.get(2), new Unit(3));// valid not adjacent
    MoveOrder move15 = new MoveOrder(regions.get(0), regions.get(4), new Unit(1));// invalid no path
    List<MoveOrder> moves = new ArrayList<MoveOrder>();
    moves.add(move12);
    moves.add(move23);
    moves.add(move14);
    moves.add(move15);
    return moves;
  }

  private List<AttackOrder> getAttackList(List<Region> regions) {
    AttackOrder attack13 = new AttackOrder(regions.get(0), regions.get(3), new Unit(4));// valid adjacent
    AttackOrder attack23 = new AttackOrder(regions.get(1), regions.get(3), new Unit(3));// invalid not adjacent
    AttackOrder attack36 = new AttackOrder(regions.get(3), regions.get(5), new Unit(2));// invalid same owner
    List<AttackOrder> attacks = new ArrayList<AttackOrder>();
    attacks.add(attack13);
    attacks.add(attack23);
    attacks.add(attack36);
    return attacks;
  }

  private List<PlacementOrder> getPlacementList(AbstractPlayer p1, AbstractPlayer p2) {
    List<Region> pRegions = getRegionsForPlacement(p1, p2);
    PlacementOrder po1 = new PlacementOrder(pRegions.get(0), new Unit(2));
    PlacementOrder po2 = new PlacementOrder(pRegions.get(1), new Unit(2));
    List<PlacementOrder> placements = new ArrayList<PlacementOrder>();
    placements.add(po1);
    placements.add(po2);
    return placements;
  }

  private List<Region> getRegionsForPlacement(AbstractPlayer p1, AbstractPlayer p2) {
    Region region1 = new Region("region1");
    region1.setOwner(p1);
    Region region2 = new Region("region2");
    region2.setOwner(p2);
    List<Region> pRegions = new ArrayList<Region>();
    pRegions.add(region1);
    pRegions.add(region2);
    return pRegions;
  }

  
}
