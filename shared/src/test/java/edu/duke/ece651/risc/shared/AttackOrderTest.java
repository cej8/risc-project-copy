package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AttackOrderTest {
  @Test
  public void test_attack() {
    // create two regions
    AbstractPlayer ps = new HumanPlayer("Player 1");
    Unit us = new Unit(10);
    AbstractPlayer pd = new HumanPlayer("Player 2");
    Unit ud = new Unit(5);
    Region s = new Region(ps, us);
    Region d = new Region(pd, ud);
    Unit u = new Unit(5);
    // attack
    AttackOrder ao = new AttackOrder(s,d,u);
    ao.doAction(null);
  }
  @Test
  public void test_attack2() {
    // create two regions
    AbstractPlayer ps = new HumanPlayer("Player 1");
    Unit us = new Unit(10);
    AbstractPlayer pd = new HumanPlayer("Player 2");
    Unit ud = new Unit(1);
    Region s = new Region(ps, us);
    Region d = new Region(pd, ud);
    Unit u = new Unit(5);
    // attack
    AttackOrder ao = new AttackOrder(s,d,u);
    ao.doAction(null);
  }
}
