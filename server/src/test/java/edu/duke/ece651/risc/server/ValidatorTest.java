package edu.duke.ece651.risc.server;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.MoveOrder;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.RegionValidator;
import edu.duke.ece651.risc.shared.Unit;


public class ValidatorTest {
  @Test
  public void test_moveRegionValidator() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    Region r1= new Region(p1, new Unit(1));
    Region r2 = new Region(p1, new Unit(2));
    Region r4 = new Region(p1, new Unit(4));

    AbstractPlayer p2 = new HumanPlayer("player 2");
    Region r3= new Region(p2, new Unit(3));
    Region r5 = new Region(p1, new Unit(5));
    Region r6 = new Region(p2, new Unit(6));
  
    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r4);
    regions.add(r3);
    regions.add(r5);
    regions.add(r6);
  
    List<Region> adj1= new ArrayList<Region>();
    adj1.add(r2);
    adj1.add(r3);
    r1.setAdjRegions(adj1);

     List<Region> adj2= new ArrayList<Region>();
     adj2.add(r1);
     adj2.add(r4);
     r2.setAdjRegions(adj2);

    List<Region> adj3= new ArrayList<Region>();
    adj3.add(r1);
    adj3.add(r5);
    r3.setAdjRegions(adj3);
    
    List<Region> adj4= new ArrayList<Region>();
    adj4.add(r2);
    adj4.add(r6);
    r4.setAdjRegions(adj4);
    
    List<Region> adj5= new ArrayList<Region>();
    adj5.add(r3);
    adj5.add(r6);
    r5.setAdjRegions(adj5);

    List<Region> adj6= new ArrayList<Region>();
    adj6.add(r4);
    adj6.add(r5);
    r6.setAdjRegions(adj6);
    
    
    Board b = new Board(regions);
    ValidatorInterface rv = new RegionValidator();
    MoveOrder move12 = new MoveOrder(r1, r2, new Unit(5));//valid adjacent
    MoveOrder move23 = new MoveOrder(r2, r3, new Unit(2));//invalid (diff owner)
    MoveOrder move14 = new MoveOrder(r1, r4, new Unit(3));//valid not adjacent
    MoveOrder move15 = new MoveOrder(r1, r5, new Unit(1));//invalid no path
     assertEquals(true, rv.isValidMove(move12, b));
    assertEquals(false, rv.isValidMove(move23, b));
     assertEquals(true, rv.isValidMove(move14, b));
    assertEquals(false, rv.isValidMove(move15, b));
                  








    
 

  }

}
