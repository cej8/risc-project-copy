package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.io.*;
import java.net.*;


public class SpyTest {
  @Test
  public void test_Spies() {
    Board board = new Board();
    AbstractPlayer p1 = new HumanPlayer("p1");
    AbstractPlayer p2 = new HumanPlayer("p2");
    Unit u1 = new Unit(10);
    Unit u2 = new Unit(10);
    Unit u3 = new Unit(10);
    Region r1 = new Region(p1, u1);
    Region r2 = new Region(p2, u2);
    Region r3 = new Region(p2, u3);
    r1.setName("r1");
    r2.setName("r2");
    r3.setName("r3");
    r1.setAdjRegions(Arrays.asList(r2));
    r2.setAdjRegions(Arrays.asList(r1, r3));
    r3.setAdjRegions(Arrays.asList(r2));
    board.setRegions(Arrays.asList(r1, r2, r3));
    board.initializeSpies(Arrays.asList("p1", "p2"));
    //Board is r1-r2-r3 where p1 owns r1, p2 owns r2, r3
    Set<String> p1Vis = board.getVisibleRegions("p1");
    Set<String> p2Vis = board.getVisibleRegions("p2");
    //p1 can see r1,r2
    assert(p1Vis.contains(r1.getName()));
    assert(p1Vis.contains(r2.getName()));
    assert(!p1Vis.contains(r3.getName()));
    //p2 can see all
    assert(p2Vis.contains(r1.getName()));
    assert(p2Vis.contains(r2.getName()));
    assert(p2Vis.contains(r3.getName()));
    //Set cloaking on r2
    CloakOrder co = new CloakOrder(r2);
    assert(co.getDestination() == r2);
    co.setDestination(r2);
    assert(co.getDestination() == r2);
    assert(p2.getResources().getTechResource().getTech() == Constants.STARTING_TECH_PRODUCTION);
    co.doAction();
    assert(p2.getResources().getTechResource().getTech() == Constants.STARTING_TECH_PRODUCTION - Constants.CLOAK_COST);
    assert(r2.getCloakTurns() == 3);
    p1Vis = board.getVisibleRegions("p1");
    p2Vis = board.getVisibleRegions("p2");
    //p1 can see r1 (r2 is cloaked)
    assert(p1Vis.contains(r1.getName()));
    assert(p1Vis.contains(r2.getName()));
    assert(!p1Vis.contains(r3.getName()));
    //p2 can see all
    assert(p2Vis.contains(r1.getName()));
    assert(p2Vis.contains(r2.getName()));
    assert(p2Vis.contains(r3.getName()));

    //p1 upgrades unit to spy
    SpyUpgradeOrder spo = new SpyUpgradeOrder(r1);
    assert(r1.getUnits().getUnits().get(0) == 10);
    assert(r1.getSpies("p1").size() == 0);
    assert(p1.getResources().getTechResource().getTech() == Constants.STARTING_TECH_PRODUCTION);
    spo.doAction();
    assert(r1.getUnits().getUnits().get(0) == 9);
    assert(r1.getSpies("p1").size() == 1);
    assert(p1.getResources().getTechResource().getTech() == Constants.STARTING_TECH_PRODUCTION - Constants.SPYUPGRADE_COST);

    //p1 moves spy to r2
    SpyMoveOrder smo = new SpyMoveOrder(r1, r2, p1);
    assert(r1.getSpies("p1").size() == 1);
    assert(r2.getSpies("p1").size() == 0);
    smo.doAction();
    assert(r1.getSpies("p1").size() == 0);
    assert(r2.getSpies("p1").size() == 1);

    
    p1Vis = board.getVisibleRegions("p1");
    p2Vis = board.getVisibleRegions("p2");
    //p1 can see r1,r2 (r2 has spy)
    assert(p1Vis.contains(r1.getName()));
    assert(p1Vis.contains(r2.getName()));
    assert(!p1Vis.contains(r3.getName()));
    //p2 can see all
    assert(p2Vis.contains(r1.getName()));
    assert(p2Vis.contains(r2.getName()));
    assert(p2Vis.contains(r3.getName()));

    
    //p1 moves spy to r2
    smo = new SpyMoveOrder(r2, r3, p1);
    assert(r2.getSpies("p1").size() == 1);
    assert(r3.getSpies("p1").size() == 0);
    smo.doAction();
    assert(r2.getSpies("p1").size() == 0);
    assert(r3.getSpies("p1").size() == 1);

    
    p1Vis = board.getVisibleRegions("p1");
    p2Vis = board.getVisibleRegions("p2");
    //p1 can see r1,r3 (r3 has spy)
    assert(p1Vis.contains(r1.getName()));
    assert(p1Vis.contains(r2.getName()));
    assert(p1Vis.contains(r3.getName()));
    //p2 can see all
    assert(p2Vis.contains(r1.getName()));
    assert(p2Vis.contains(r2.getName()));
    assert(p2Vis.contains(r3.getName()));
  }

  @Test
  public void test_SpyValidators(){
    Board board = new Board();
    AbstractPlayer p1 = new HumanPlayer("p1");
    AbstractPlayer p2 = new HumanPlayer("p2");
    Unit u1 = new Unit(5);
    Unit u2 = new Unit(2);
    Unit u3 = new Unit(1);
    Region r1 = new Region(p1, u1);
    Region r2 = new Region(p2, u2);
    Region r3 = new Region(p2, u3);
    r1.setName("r1");
    r2.setName("r2");
    r3.setName("r3");
    r1.setAdjRegions(Arrays.asList(r2));
    r2.setAdjRegions(Arrays.asList(r1, r3));
    r3.setAdjRegions(Arrays.asList(r2));
    board.setRegions(Arrays.asList(r1, r2, r3));
    board.initializeSpies(Arrays.asList("p1", "p2"));
    
    ValidatorHelper vh1;
    ValidatorHelper vh2;
    List<OrderInterface> p1Orders;
    List<OrderInterface> p2Orders;

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Try cloaking region don't own
    p1Orders.add(new CloakOrder(r2));
    assert(!vh1.allOrdersValid(p1Orders));
    //Try cloaking when not high enough level
    p2Orders.add(new CloakOrder(r2));
    assert(!vh2.allOrdersValid(p2Orders));

    //Set both to 3 for cloaking, give both 2 times cloak cost
    p1.setMaxTechLevel(new TechnologyLevel(3));
    p2.setMaxTechLevel(new TechnologyLevel(3));
    p1.setPlayerResource(new PlayerResources(100, Constants.CLOAK_COST*2));
    p2.setPlayerResource(new PlayerResources(100, Constants.CLOAK_COST*2));

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Normal cloak
    p1Orders.add(new CloakOrder(r1));
    assert(vh1.allOrdersValid(p1Orders));
    //Try cloaking 1 more than current tech
    p2Orders.add(new CloakOrder(r2));
    p2Orders.add(new CloakOrder(r2));
    p2Orders.add(new CloakOrder(r2));
    assert(!vh2.allOrdersValid(p2Orders));

    
    //Give both 2 times upgrade cost
    p1.setPlayerResource(new PlayerResources(100, Constants.SPYUPGRADE_COST*2));
    p2.setPlayerResource(new PlayerResources(100, Constants.SPYUPGRADE_COST*2));

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Try to upgrade region not owned
    p1Orders.add(new SpyUpgradeOrder(r2));
    assert(!vh1.allOrdersValid(p1Orders));
    //Try upgrading only unit present
    p2Orders.add(new SpyUpgradeOrder(r3));
    assert(!vh2.allOrdersValid(p2Orders));

    
    //Give both 2 times upgrade cost
    p1.setPlayerResource(new PlayerResources(100, Constants.SPYUPGRADE_COST*2));
    p2.setPlayerResource(new PlayerResources(100, Constants.SPYUPGRADE_COST*2));

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Try to upgrade more than resources
    p1Orders.add(new SpyUpgradeOrder(r1));
    p1Orders.add(new SpyUpgradeOrder(r1));
    p1Orders.add(new SpyUpgradeOrder(r1));
    assert(!vh1.allOrdersValid(p1Orders));
    //Legal upgrade
    p2Orders.add(new SpyUpgradeOrder(r2));
    assert(vh2.allOrdersValid(p2Orders));

    //P1 actually create spy
    (new SpyUpgradeOrder(r1)).doAction();

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //p1 try to move r1 to r3 (illegal two enemy)
    p1Orders.add(new SpyMoveOrder(r1, r3, p1));
    assert(!vh1.allOrdersValid(p1Orders));
    //p2 try to move spy that doesn't exist from r3 to r1
    p2Orders.add(new SpyMoveOrder(r3, r1, p2));
    assert(!vh2.allOrdersValid(p2Orders));

    
    //p2 creates spy in r2
    (new SpyUpgradeOrder(r2)).doAction();
    
    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //p1 try to move r1 to r2 then r2 to r3 (illegal cannot move twice in enemy)
    p1Orders.add(new SpyMoveOrder(r1, r2, p1));
    p1Orders.add(new SpyMoveOrder(r2, r3, p1));
    assert(!vh1.allOrdersValid(p1Orders));
    //p2 try to move r2 to r3 then r3 to r1 (legal since internal does not count)
    p2Orders.add(new SpyMoveOrder(r2, r3, p2));
    p2Orders.add(new SpyMoveOrder(r3, r1, p2));
    assert(vh2.allOrdersValid(p2Orders));



  }

}
