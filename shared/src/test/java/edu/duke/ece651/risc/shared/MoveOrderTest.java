package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


public class MoveOrderTest {
   @Test
  public void test_moveFood(){
    AbstractPlayer p1 = new HumanPlayer("player 1");
    int food = p1.getResources().getFoodResource().getFood();
    int tech = p1.getResources().getTechResource().getTech();
    assertEquals(20,food);
    assertEquals(15, tech);
    Region r1 = new Region(p1, new Unit(5));
    r1.setSize(1);
    Region r2 = new Region(p1, new Unit (2));
    r2.setSize(1);
    assertEquals(5,r1.getUnits().getTotalUnits());
    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    List<Region> adj1= new ArrayList<Region>();
    adj1.add(r2);
    r1.setAdjRegions(adj1);
    regions.add(r2);
     List<Region> adj2= new ArrayList<Region>();
    adj2.add(r1);
    r2.setAdjRegions(adj2);
  
    Board b = new Board(regions);
    OrderInterface move1 = new MoveOrder(r1,r2,new Unit(2));
    move1.doAction();
    int r1Size = r1.getSize();
    int r2Size = r2.getSize();
    assertEquals(16,p1.getResources().getFoodResource().getFood());
  }

  public List<Integer> listOfUnitInts(int u0, int u1, int u2, int u3, int u4, int u5, int u6) {
    List<Integer> unit = new ArrayList<Integer>();
    unit.add(u0);
    unit.add(u1);
    unit.add(u2);
    unit.add(u3);
    unit.add(u4);
    unit.add(u5);
    unit.add(u6);
    return unit;
  }
  
  @Test
  public void test_moveOrder() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    Region r1 = new Region(p1, new Unit(10));
    r1.setSize(2);
    Region r2 = new Region(p1, new Unit(5));
    r2.setSize(2);
    Region r3 = new Region(p1, new Unit(8));
    r3.setSize(2);
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
    assertEquals(10, r1.getUnits().getTotalUnits());
    assertEquals(5, r2.getUnits().getTotalUnits());
    assertEquals(8, r3.getUnits().getTotalUnits());
    
    for (OrderInterface order : moves) {
      order.doAction();
    }
    
    assertEquals(8, r1.getUnits().getTotalUnits());
    assertEquals(8, r2.getUnits().getTotalUnits());
    assertEquals(7, r3.getUnits().getTotalUnits());
   
    assertEquals(Constants.MOVE_PRIORITY, move1.getPriority());  
  }

  @Test
  public void test_UnitMoves() {
    Board board = setBoard();
    List<Integer> u2 = listOfUnitInts(2, 2, 2, 2, 2, 2, 2);
    List<Integer> u3 = listOfUnitInts(3, 3, 3, 3, 3, 3, 3);
    List<Integer> u5 = listOfUnitInts(5, 5, 5, 5, 5, 5, 5);
    List<Integer> u0 = listOfUnitInts(0, 0, 0, 0, 0, 0, 0);
    OrderInterface move1 = new MoveOrder(board.getRegions().get(0), board.getRegions().get(1), new Unit(5));
    OrderInterface move2 = new MoveOrder(board.getRegions().get(1), board.getRegions().get(2), new Unit(2));
    OrderInterface move3 = new MoveOrder(board.getRegions().get(2), board.getRegions().get(0), new Unit(3));
    List<OrderInterface> moves = new ArrayList<OrderInterface>();
    moves.add(move1);
    moves.add(move2);
    moves.add(move3);

    //TODO add asserts
  }

  public Board setBoard(){
    AbstractPlayer p1 = new HumanPlayer("player 1");
    Region r1 = new Region(p1, new Unit(10));
    r1.setSize(2);
    Region r2 = new Region(p1, new Unit(10));
    r2.setSize(2);
    Region r3 = new Region(p1, new Unit(10));
    r3.setSize(2);
    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r3);
    Board b = new Board(regions);
    return b;
  }
  
}
