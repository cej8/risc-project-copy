package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


public class MoveOrderTest {
   @Test
  public void test_moveFood(){
    AbstractPlayer p1 = new HumanPlayer("player 1");
    int food = p1.getFood();
    int tech = p1.getTech();
    assertEquals(10,food);
    assertEquals(10, tech);
    Region r1 = new Region(p1, new Unit(5));
    r1.setSize(1);
    Region r2 = new Region(p1, new Unit (2));
    r2.setSize(1);
    assertEquals(5,r1.getUnits().getTotalUnits());
    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    Board b = new Board(regions);
    OrderInterface move1 = new MoveOrder(r1,r2,new Unit(2));
    move1.doDestinationAction();
    int r1Size = r1.getSize();
    int r2Size = r2.getSize();
    assertEquals(8,p1.getFood());
  }
  // @Test
  // public void test_moveOrder() {
  //   AbstractPlayer p1 = new HumanPlayer("player 1");
  //   Region r1= new Region(p1, new Unit(10));
  //   Region r2 = new Region(p1, new Unit(5));
  //   Region r3 = new Region(p1, new Unit(8));
  //   List<Region> regions = new ArrayList<Region>();
  //   regions.add(r1);
  //   regions.add(r2);
  //   regions.add(r3);
  //   Board b = new Board(regions);
  //   OrderInterface move1 = new MoveOrder(r1, r2, new Unit(5));
  //   OrderInterface move2 = new MoveOrder(r2, r3, new Unit(2));
  //   OrderInterface move3 = new MoveOrder(r3, r1, new Unit(3));
  //   List<OrderInterface> moves = new ArrayList<OrderInterface>();
  //   moves.add(move1);
  //   moves.add(move2);
  //   moves.add(move3);
  //   assertEquals(10, r1.getUnits().getUnits());
  //   assertEquals(5, r2.getUnits().getUnits());
  //   assertEquals(8, r3.getUnits().getUnits());
    
  //   for(OrderInterface order: moves){
  //     order.doSourceAction();
  //     order.doDestinationAction();
  //   }
  //   assertEquals(8, r1.getUnits().getUnits());
  //   assertEquals(8, r2.getUnits().getUnits());
  //   assertEquals(7, r3.getUnits().getUnits());
   
  //   assertEquals(Constants.MOVE_PRIORITY, move1.getPriority());
    

  // }

}
