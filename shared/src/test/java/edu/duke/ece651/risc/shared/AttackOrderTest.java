package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AttackOrderTest {
  @Test
  public void test_attackFood(){
    AbstractPlayer p1 = new HumanPlayer("player 1");
    int food = p1.getResources().getFoodResource().getFood();
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
    AttackOrder attack1 = new AttackOrder(r1,r2,new Unit(2));
    attack1.doSourceAction();
    int r1Size = r1.getSize();
    int r2Size = r2.getSize();
    assertEquals(19,p1.getResources().getFoodResource().getFood());
  }
  // @Test
  // public void test_attack() {
  //   setupTestAttack(10, 5, 5);
  // }
  // @Test
  // public void test_attack2() {
  //   setupTestAttack(10, 1, 5);
  // }
  //  @Test
  // public void test_attack3() {
  //   setupTestAttack(10, 10, 5);
  // }
  // @Test
  // public void test_attack4(){
  //   setupTestAttack(2,10,1);
  // }

  // //method for creating attacks to test
  // private void setupTestAttack(int numSourceUnits, int numDestUnits, int numAttackUnits) {
  //   // create two regions
  //   AbstractPlayer ps = new HumanPlayer("Player 1");
  //   Unit us = new Unit(numSourceUnits);
  //   AbstractPlayer pd = new HumanPlayer("Player 2");
  //   Unit ud = new Unit(numDestUnits);
  //   Region s = new Region(ps, us);
  //   s.setName("Mars");
  //   s.setOwner(ps);
  //   Region d = new Region(pd, ud);
  //   d.setName("Venus");
  //   d.setOwner(pd);
  //   Unit u = new Unit(numAttackUnits);
  //   // attack
  //   AttackOrder ao = new AttackOrder(s, d, u);
  //   assertEquals(Constants.ATTACK_PRIORITY, ao.getPriority());
  //   ao.doSourceAction();
  //   ao.doDestinationAction();
  // }

}
