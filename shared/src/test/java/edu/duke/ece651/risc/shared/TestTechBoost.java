package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

public class TestTechBoost {
  @Test
  public void test_TechLevel(){
    TechnologyLevel tl = new TechnologyLevel();
    assertEquals(1, tl.getMaxTechLevel());
    assertEquals(50, tl.getCostToUpgrade());
  tl.upgradeLevel();
   assertEquals(2, tl.getMaxTechLevel());
    assertEquals(75, tl.getCostToUpgrade());
  tl.upgradeLevel();
   assertEquals(3, tl.getMaxTechLevel());
    assertEquals(125, tl.getCostToUpgrade());
  tl.upgradeLevel();
   assertEquals(4, tl.getMaxTechLevel());
    assertEquals(200, tl.getCostToUpgrade());
    
    

  }
  @Test
  public void test_TechBoost() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    p1.getResources().getTechResource().addTech(700);
    OrderInterface techboost = new TechBoost(p1);
    //1->2 (cost 50)
    techboost.doAction();
    assertEquals(2,p1.getMaxTechLevel().getMaxTechLevel());
    assertEquals(680, p1.getResources().getTechResource().getTech());
    //2->3 (cost 75)
    techboost.doAction();
    assertEquals(3,p1.getMaxTechLevel().getMaxTechLevel());
    assertEquals(605, p1.getResources().getTechResource().getTech());
    // 3->4 (cost 125)
    techboost.doAction();
    assertEquals(4,p1.getMaxTechLevel().getMaxTechLevel());
    assertEquals(480, p1.getResources().getTechResource().getTech());
    //4->5 (cost 200)
    techboost.doAction();
    assertEquals(5,p1.getMaxTechLevel().getMaxTechLevel());
    assertEquals(280, p1.getResources().getTechResource().getTech());
    //5->6 (cost 300)
     p1.getResources().getTechResource().addTech(100);
       techboost.doAction();
    assertEquals(6,p1.getMaxTechLevel().getMaxTechLevel());
    assertEquals(80, p1.getResources().getTechResource().getTech());

  }

  @Test
  public void test_techBoostValidator(){
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

    p1.setPlayerResource(new PlayerResources(100, 500));
    p2.setPlayerResource(new PlayerResources(100, 10));
    
    ValidatorHelper vh1;
    ValidatorHelper vh2;
    List<OrderInterface> p1Orders;
    List<OrderInterface> p2Orders;

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Boost twice
    p1Orders.add(new TechBoost(p1));
    p1Orders.add(new TechBoost(p1));
    assert(!vh1.allOrdersValid(p1Orders));
    //Not enough 
    p2Orders.add(new TechBoost(p2));
    assert(!vh2.allOrdersValid(p2Orders));

    p1.setPlayerResource(new PlayerResources(100, 5000));
    p2.setPlayerResource(new PlayerResources(100, 5000));

    p1.setMaxTechLevel(Constants.MAX_TECH_LEVEL);
    p2.setMaxTechLevel(Constants.MAX_TECH_LEVEL-1);

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Boost at max
    p1Orders.add(new TechBoost(p1));
    assert(!vh1.allOrdersValid(p1Orders));
    //Valid
    p2Orders.add(new TechBoost(p2));
    assert(vh2.allOrdersValid(p2Orders));

  }

}
