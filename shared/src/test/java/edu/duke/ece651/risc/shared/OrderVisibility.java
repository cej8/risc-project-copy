package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.io.*;
import java.net.*;


public class OrderVisibility {
  @Test
  public void test_vis() {
    Board board = new Board();
    AbstractPlayer p1 = new HumanPlayer("p1");
    AbstractPlayer p2 = new HumanPlayer("p2");
    AbstractPlayer p3 = new HumanPlayer("p3");
    Unit u1 = new Unit(10);
    Unit u2 = new Unit(10);
    Unit u3 = new Unit(10);
    Unit u4 = new Unit(10);
    Region r1 = new Region(p1, u1);
    Region r2 = new Region(p2, u2);
    Region r3 = new Region(p2, u3);
    Region r4 = new Region(p3, u4);
    r1.setName("r1");
    r2.setName("r2");
    r3.setName("r3");
    r4.setName("r4");
    //Square
    r1.setAdjRegions(Arrays.asList(r4, r2));
    r2.setAdjRegions(Arrays.asList(r1, r3));
    r3.setAdjRegions(Arrays.asList(r2, r4));
    r4.setAdjRegions(Arrays.asList(r3, r1));
    board.setRegions(Arrays.asList(r1, r2, r3, r4));
    board.initializeSpies(Arrays.asList("p1", "p2", "p3"));

    MoveOrder mo = new MoveOrder(r2, r3, new Unit(1));
    AttackMove am = new AttackMove(r2, r1, new Unit(1));
    AttackCombat ac = new AttackCombat(r2, r1, new Unit(1));
    PlacementOrder po = new PlacementOrder(r3, new Unit(1));
    UnitBoost ub = new UnitBoost(r3, new Unit(1));
    SpyUpgradeOrder su = new SpyUpgradeOrder(r2);
    SpyMoveOrder sm = new SpyMoveOrder(r2, r3, p2);
    CloakOrder co = new CloakOrder(r2);
    TechBoost tb = new TechBoost(p2);
    RaidOrder ro = new RaidOrder(r2, r1);

    List<Set<String>> vismo = mo.getPlayersVisibleTo();
    List<Set<String>> visam = am.getPlayersVisibleTo();
    List<Set<String>> visac = ac.getPlayersVisibleTo();
    List<Set<String>> vispo = po.getPlayersVisibleTo();
    List<Set<String>> visub = ub.getPlayersVisibleTo();
    List<Set<String>> vissu = su.getPlayersVisibleTo();
    List<Set<String>> vissm = sm.getPlayersVisibleTo();
    List<Set<String>> visco = co.getPlayersVisibleTo();
    List<Set<String>> vistb = tb.getPlayersVisibleTo();
    List<Set<String>> visro = ro.getPlayersVisibleTo();


  }

}
