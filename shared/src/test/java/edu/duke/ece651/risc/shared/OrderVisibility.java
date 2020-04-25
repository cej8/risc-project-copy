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
    TeleportOrder to = new TeleportOrder(r2, r3, new Unit(1));

    List<Set<String>> vismo = mo.getPlayersVisibleTo();
    assert(vismo.size() == 4);
    assert(vismo.get(0).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));
    assert(vismo.get(1).containsAll(Arrays.asList(p1.getName(),p2.getName())));
    assert(vismo.get(2).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));
    assert(vismo.get(3).containsAll(Arrays.asList(p2.getName(),p3.getName())));

    List<Set<String>> visam = am.getPlayersVisibleTo();
    assert(visam.size() == 4);
    assert(visam.get(0).containsAll(Arrays.asList(p1.getName(),p2.getName())));
    assert(visam.get(1).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));
    assert(visam.get(2).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));
    assert(visam.get(3).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));

    List<Set<String>> visac = ac.getPlayersVisibleTo();
    assert(visac.size() == 1);
    assert(visac.get(0).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));

    List<Set<String>> vispo = po.getPlayersVisibleTo();
    assert(vispo.size() == 1);
    assert(vispo.get(0).containsAll(Arrays.asList(p2.getName(),p3.getName())));

    List<Set<String>> visub = ub.getPlayersVisibleTo();
    assert(visub.size() == 1);
    assert(visub.get(0).containsAll(Arrays.asList(p2.getName(),p3.getName())));

    List<Set<String>> vissu = su.getPlayersVisibleTo();
    assert(vissu.size() == 1);
    assert(vissu.get(0).containsAll(Arrays.asList(p2.getName())));

    List<Set<String>> vissm = sm.getPlayersVisibleTo();
    assert(vissm.size() == 1);
    assert(vissm.get(0).containsAll(Arrays.asList(p2.getName())));

    List<Set<String>> visco = co.getPlayersVisibleTo();
    assert(visco.size() == 1);
    assert(visco.get(0).containsAll(Arrays.asList(p1.getName(),p2.getName())));

    List<Set<String>> vistb = tb.getPlayersVisibleTo();
    assert(vistb.size() == 1);
    assert(vistb.get(0).containsAll(Arrays.asList(p2.getName())));

    List<Set<String>> visro = ro.getPlayersVisibleTo();
    assert(visro.size() == 6);
    assert(visro.get(0).containsAll(Arrays.asList(p1.getName(),p2.getName())));
    assert(visro.get(1).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));
    assert(visro.get(2).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));
    assert(visro.get(3).containsAll(Arrays.asList(p1.getName(),p2.getName())));
    assert(visro.get(4).containsAll(Arrays.asList(p2.getName())));
    assert(visro.get(5).containsAll(Arrays.asList(p1.getName())));

    List<Set<String>> visto = to.getPlayersVisibleTo();
    assert(visto.size() == 5);
    assert(visto.get(0).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));
    assert(visto.get(1).containsAll(Arrays.asList(p1.getName(),p2.getName())));
    assert(visto.get(2).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));
    assert(visto.get(3).containsAll(Arrays.asList(p2.getName(),p3.getName())));
    assert(visto.get(4).containsAll(Arrays.asList(p1.getName(),p2.getName(),p3.getName())));

    

  }

}
