package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

}
