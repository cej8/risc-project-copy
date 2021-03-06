package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class AttackOrderTest {
  @Test
  public void test_attackFood(){
    AbstractPlayer p1 = new HumanPlayer("player 1");
    int food = p1.getResources().getFuelResource().getFuel();
    int tech = p1.getResources().getTechResource().getTech();
    AbstractPlayer p2 = new HumanPlayer("player 2");
    Region r1 = new Region(p1, new Unit(5));
    r1.setSize(1);
    Region r2 = new Region(p2, new Unit (2));
    r2.setSize(1);
    assertEquals(5,r1.getUnits().getTotalUnits());
    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    Board b = new Board(regions);
    // AttackOrder attack1 = new AttackOrder(r1,r2,new Unit(2));
    OrderInterface attackp1 = new AttackMove(r1,r2,new Unit(2));
    //   OrderInterface attackp2 = new AttackCombat(r1,r2,new Unit(2));
    attackp1.doAction();
    // attackp2.doAction();
    int r1Size = r1.getSize();
    int r2Size = r2.getSize();
    assertEquals(48,p1.getResources().getFuelResource().getFuel());
  }
   @Test
   public void test_attack() {
      List<Integer> sourceUnits= new ArrayList<Integer>();
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    sourceUnits.add(1);
    // create two regions
    AbstractPlayer ps = new HumanPlayer("Player 1");
    Unit us = new Unit();
    us.setUnits(sourceUnits);
    
    AbstractPlayer pd = new HumanPlayer("Player 2");
    Unit ud = new Unit();
    List<Integer> destUnits= new ArrayList<Integer>();
    destUnits.add(1);
    destUnits.add(1);
    destUnits.add(0);
    destUnits.add(1);
    destUnits.add(1);
    destUnits.add(2);
    destUnits.add(1);
    ud.setUnits(destUnits);
    
    Region s = new Region(ps, us);
    s.setName("Mars");
    Region d = new Region(pd, ud);
    d.setName("Venus");
   
    setupTestAttack(s,d);
    setupTestAttack(d,s);
   }

  // //method for creating attacks to test
  private void setupTestAttack(Region r1, Region r2) {
    
    List<Integer> attackUnits= new ArrayList<Integer>();
    attackUnits.add(1);
    attackUnits.add(1);
    attackUnits.add(0);
    attackUnits.add(0);
    attackUnits.add(1);
    attackUnits.add(0);
    attackUnits.add(1);

    Unit ua = new Unit();
    ua.setUnits(attackUnits);
    // attack
    AttackMove ap1 = new AttackMove(r1, r2, ua);
    ap1.doAction();
    AttackCombat ap2 = new AttackCombat(r1, r1, ua);
    ap2.doAction();
   
  }

  @Test
  public void test_RandomOnAttackCombat(){
    List<Integer> listOf5 = new ArrayList<Integer>(Collections.nCopies(7, 5));
    Board board = new Board();
    AbstractPlayer p1 = new HumanPlayer("p1");
    AbstractPlayer p2 = new HumanPlayer("p2");
    Unit u1 = new Unit(1);
    Unit u2 = new Unit(listOf5);
    Region r1 = new Region(p1, u1);
    Region r2 = new Region(p2, u2);
    r1.setName("r1");
    r2.setName("r2");
    r1.setAdjRegions(Arrays.asList(r2));
    r2.setAdjRegions(Arrays.asList(r1));
    board.setRegions(Arrays.asList(r1, r2));
    board.initializeSpies(Arrays.asList("p1", "p2"));

    List<Integer> listOf5Fresh = new ArrayList<Integer>(Collections.nCopies(7, 5));
    List<Integer> listOf1Fresh = new ArrayList<Integer>(Collections.nCopies(7, 1));
    AttackCombat ac = new AttackCombat(r1, r2, new Unit(listOf5Fresh));
    r2.setUnits(new Unit(listOf1Fresh));
    r2.setOwner(p2);
    ac.doAction();

    listOf5Fresh = new ArrayList<Integer>(Collections.nCopies(7, 5));
    listOf1Fresh = new ArrayList<Integer>(Collections.nCopies(7, 1));
    ac = new AttackCombat(r1, r2, new Unit(listOf1Fresh));
    r2.setUnits(new Unit(listOf5Fresh));
    r2.setOwner(p2);
    ac.doAction();

    List<Integer> listOf0Fresh = new ArrayList<Integer>(Collections.nCopies(7, 0));
    listOf0Fresh.add(6, new Integer(5));
    List<Integer> listOf0Fresh2 = new ArrayList<Integer>(Collections.nCopies(7, 0));
    listOf0Fresh2.add(6, new Integer(5));
    ac = new AttackCombat(r1, r2, new Unit(listOf1Fresh));
    r2.setUnits(new Unit(listOf0Fresh2));
    r2.setOwner(p2);
    ac.doAction();

    listOf0Fresh = new ArrayList<Integer>(Collections.nCopies(7, 0));
    listOf0Fresh2 = new ArrayList<Integer>(Collections.nCopies(7, 0));
    listOf0Fresh2.add(6, new Integer(5));
    ac = new AttackCombat(r1, r2, new Unit(listOf1Fresh));
    r2.setUnits(new Unit(listOf0Fresh2));
    r2.setOwner(p2);
    ac.doAction();

    listOf0Fresh = new ArrayList<Integer>(Collections.nCopies(7, 0));
    listOf0Fresh.add(6, new Integer(5));
    listOf0Fresh2 = new ArrayList<Integer>(Collections.nCopies(7, 0));
    ac = new AttackCombat(r1, r2, new Unit(listOf1Fresh));
    r2.setUnits(new Unit(listOf0Fresh2));
    r2.setOwner(p2);
    ac.doAction();

  }

}
