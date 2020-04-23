package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.io.*;
import java.net.*;


public class RaidTest {
  @Test
  public void test_Raid() {
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

    p1.getResources().getFuelResource().setFuel(100);
    p1.getResources().getTechResource().setTech(1000);
    p2.getResources().getFuelResource().setFuel(100);
    p2.getResources().getTechResource().setTech(1000);


    assert(p1.getResources().getFuelResource().getFuel() == 100);
    assert(p1.getResources().getTechResource().getTech() == 1000);
    assert(p2.getResources().getFuelResource().getFuel() == 100);
    assert(p2.getResources().getTechResource().getTech() == 1000);  

    //P1 steals 20% of p2
    RaidOrder ro = new RaidOrder(r1, r2);
    ro.doAction();
    
    assert(p1.getResources().getFuelResource().getFuel() == 120);
    assert(p1.getResources().getTechResource().getTech() == 1200);
    assert(p2.getResources().getFuelResource().getFuel() == 80);
    assert(p2.getResources().getTechResource().getTech() == 800);

    p1.getResources().getFuelResource().setFuel(100);
    p1.getResources().getTechResource().setTech(1000);
    p2.getResources().getFuelResource().setFuel(100);
    p2.getResources().getTechResource().setTech(1000);

    p1.setMaxTechLevel(6);

    assert(p1.getResources().getFuelResource().getFuel() == 100);
    assert(p1.getResources().getTechResource().getTech() == 1000);
    assert(p2.getResources().getFuelResource().getFuel() == 100);
    assert(p2.getResources().getTechResource().getTech() == 1000);  

    //P1 steals 100% of p2
    ro = new RaidOrder(r1, r2);
    ro.doAction();
    
    assert(p1.getResources().getFuelResource().getFuel() == 200);
    assert(p1.getResources().getTechResource().getTech() == 2000);
    assert(p2.getResources().getFuelResource().getFuel() == 0);
    assert(p2.getResources().getTechResource().getTech() == 0);

    p1.getResources().getFuelResource().setFuel(100);
    p1.getResources().getTechResource().setTech(1000);
    p2.getResources().getFuelResource().setFuel(100);
    p2.getResources().getTechResource().setTech(1000);

    p1.setMaxTechLevel(3);

    assert(p1.getResources().getFuelResource().getFuel() == 100);
    assert(p1.getResources().getTechResource().getTech() == 1000);
    assert(p2.getResources().getFuelResource().getFuel() == 100);
    assert(p2.getResources().getTechResource().getTech() == 1000);  

    //P1 steals 40% of p2
    ro = new RaidOrder(r1, r2);
    ro.doAction();
    
    assert(p1.getResources().getFuelResource().getFuel() == 140);
    assert(p1.getResources().getTechResource().getTech() == 1400);
    assert(p2.getResources().getFuelResource().getFuel() == 60);
    assert(p2.getResources().getTechResource().getTech() == 600);

    p1.getResources().getFuelResource().setFuel(100);
    p1.getResources().getTechResource().setTech(1000);
    p2.getResources().getFuelResource().setFuel(100);
    p2.getResources().getTechResource().setTech(1000);

    p1.setMaxTechLevel(3);
    p2.setMaxTechLevel(4);

    assert(p1.getResources().getFuelResource().getFuel() == 100);
    assert(p1.getResources().getTechResource().getTech() == 1000);
    assert(p2.getResources().getFuelResource().getFuel() == 100);
    assert(p2.getResources().getTechResource().getTech() == 1000);  

    //P1 steals 20% of p2
    ro = new RaidOrder(r1, r2);
    ro.doAction();
    
    assert(p1.getResources().getFuelResource().getFuel() == 120);
    assert(p1.getResources().getTechResource().getTech() == 1200);
    assert(p2.getResources().getFuelResource().getFuel() == 80);
    assert(p2.getResources().getTechResource().getTech() == 800);
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
    r3.setAdjRegions(Arrays.asList(r1, r2));
    board.setRegions(Arrays.asList(r1, r2, r3));
    board.initializeSpies(Arrays.asList("p1", "p2"));
    //Has 1 directional from r3 to r1    

    ValidatorHelper vh1;
    ValidatorHelper vh2;
    List<OrderInterface> p1Orders;
    List<OrderInterface> p2Orders;

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Try raiding from
    p1Orders.add(new RaidOrder(r2, r3));
    assert(!vh1.allOrdersValid(p1Orders));
    //Try raiding self
    p2Orders.add(new RaidOrder(r3, r2));
    assert(!vh2.allOrdersValid(p2Orders));


    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Try raiding twice
    p1Orders.add(new RaidOrder(r1, r2));
    p1Orders.add(new RaidOrder(r1, r2));
    assert(!vh1.allOrdersValid(p1Orders));
    //Try raiding when no return path
    p2Orders.add(new RaidOrder(r3, r1));
    assert(!vh2.allOrdersValid(p2Orders));

    
    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Try without path there
    p1Orders.add(new RaidOrder(r1, r3));
    assert(!vh1.allOrdersValid(p1Orders));
    //Raid correctly
    p2Orders.add(new RaidOrder(r2, r1));
    assert(!vh2.allOrdersValid(p2Orders));


  }

}
