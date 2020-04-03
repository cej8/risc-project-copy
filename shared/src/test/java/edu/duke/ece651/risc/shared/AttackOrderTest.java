package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AttackOrderTest {
  @Test
  public void test_attackFood(){
    AbstractPlayer p1 = new HumanPlayer("player 1");
    int food = p1.getResources().getFuelResource().getFuel();
    int tech = p1.getResources().getTechResource().getTech();
    assertEquals(20,food);
    assertEquals(15, tech);
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
    assertEquals(18,p1.getResources().getFuelResource().getFuel());
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

}
