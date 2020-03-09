package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import org.junit.jupiter.api.Test;

public class MoveOrderTest {
  @Test
  public void test_moveOrder() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    Region r1= new Region(p1, new Unit(10));
    Region r2 = new Region(p1, new Unit(5));
    Region r3 = new Region(p1, new Unit(8));
    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r3);
    Board b = new Board(regions);
    OrderInterface move1 = new MoveOrder(r1, r2, new Unit(5));
    OrderInterface move2 = new MoveOrder(r2, r3, new Unit(2));
    OrderInterface move3 = new MoveOrder(r3, r1, new Unit(3));
 List<OrderInterface> moves = new ArrayList<OrderInterface>();
    moves.add(move1);
    moves.add(move2);
    moves.add(move3);
    assertEquals(10, r1.getUnits().getUnits());
    assertEquals(5, r2.getUnits().getUnits());
    assertEquals(8, r3.getUnits().getUnits());
    
    for(OrderInterface order: moves){
      order.doAction(b);
    }
    assertEquals(8, r1.getUnits().getUnits());
    assertEquals(8, r2.getUnits().getUnits());
    assertEquals(7, r3.getUnits().getUnits());
   
    
    
    

  }

}
