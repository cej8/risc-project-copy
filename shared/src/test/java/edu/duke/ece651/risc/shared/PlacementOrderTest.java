package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PlacementOrderTest {
  @Test
  public void test_placementOrder() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    Region r1= new Region(p1, new Unit(10));
    Region r2 = new Region(p1, new Unit(5));
    Region r3 = new Region(p1, new Unit(8));
    Board board = new Board();
    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r3);
    board.setRegions(regions);
    for(int i = 0; i < regions.size(); i++) {
      OrderInterface placement = new PlacementOrder(regions.get(i),new Unit(i + 5));
      placement.doAction();
      //  placement.doDestinationAction();
      assertEquals(Constants.PLACEMENT_PRIORITY, placement.getPriority());
      System.out.println("Placement number: " + (i + 5));
    }
    assertEquals(5,regions.get(0).getUnits().getTotalUnits());
    assertEquals(6,regions.get(1).getUnits().getTotalUnits());
    assertEquals(7, regions.get(2).getUnits().getTotalUnits());


  }

}
