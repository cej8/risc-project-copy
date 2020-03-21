package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AttackOrderTest {
  @Test
  public void test_attack() {
    setupTestAttack(10, 5, 5);
  }
  @Test
  public void test_attack2() {
    setupTestAttack(10, 1, 5);
  }
   @Test
  public void test_attack3() {
    setupTestAttack(10, 10, 5);
  }
  @Test
  public void test_attack4(){
    setupTestAttack(2,10,1);
  }

  //method for creating attacks to test
  private void setupTestAttack(int numSourceUnits, int numDestUnits, int numAttackUnits) {
    // create two regions
    AbstractPlayer ps = new HumanPlayer("Player 1");
    Unit us = new Unit(numSourceUnits);
    AbstractPlayer pd = new HumanPlayer("Player 2");
    Unit ud = new Unit(numDestUnits);
    Region s = new Region(ps, us);
    s.setName("Mars");
    s.setOwner(ps);
    Region d = new Region(pd, ud);
    d.setName("Venus");
    d.setOwner(pd);
    Unit u = new Unit(numAttackUnits);
    // attack
    AttackOrder ao = new AttackOrder(s, d, u);
    assertEquals(Constants.ATTACK_PRIORITY, ao.getPriority());
    ao.doAction();
  }

}
