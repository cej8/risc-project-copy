package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.*;


public class TestMovePathFinder {
  @Test
  public void test_pathFinder(){
    AbstractPlayer p1 = new HumanPlayer("player 1");
    AbstractPlayer p2 = new HumanPlayer("player 2");
    List<Region> regions = getRegionList(p1, p2);
    Path sp14 = regions.get(0).findShortestPath(regions.get(2));//valid not adjacent
    System.out.println("R1 to R4");
    printPath(sp14);
    Path sp12 = regions.get(0).findShortestPath(regions.get(1));//valid adjacent
      System.out.println("R1 to R2");
     printPath(sp12);
      Path sp26 = regions.get(1).findShortestPath(regions.get(5));//valid adjacent
      System.out.println("R2 to R6");
     printPath(sp26);
  
    Path sp16 = regions.get(0).findShortestPath(regions.get(5));
     System.out.println("R1 to R6");
      printPath(sp16);
      Path sp18 = regions.get(0).findShortestPath(regions.get(7));//invalid
     System.out.println("R1 to R8");
      printPath(sp18);
 
   
  }
  private void printPath(Path shortestPath){
    if(shortestPath==null){
      System.out.println("No path exists");
      return;
    }
    for(Region r: shortestPath.getPath()){
      System.out.println(r.getName());
    }
    
  }
   @Test
  public void test_moveValidator() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    
    AbstractPlayer p2 = new HumanPlayer("player 2");
    List<Region> regions = getRegionList(p1, p2);
    Board b = new Board(regions);

    MoveValidator mv = new MoveValidator(p1, b);
    MoveOrder move1 = new MoveOrder(regions.get(6), regions.get(1), new Unit(2));//valid
    assertEquals(true,mv.isValidMove(move1));
    MoveOrder move2 = new MoveOrder(regions.get(6), regions.get(4),new Unit(2));//invalid, not enough resources
    assertEquals(false, mv.isValidMove(move2));
    MoveOrder move3 = new MoveOrder(regions.get(3), regions.get(7), new Unit(2));//no valid path of adjacent regions
    assertEquals(false, mv.isValidMove(move3));
    
    

  }
  private List<Region> getRegionList(AbstractPlayer p1, AbstractPlayer p2) {
    Region r1 = new Region(p1, new Unit(1));
    r1.setName("r1");
    r1.setSize(1);
    r1.setFoodProduction(100);
    
    Region r2 = new Region(p1, new Unit(2));
    r2.setName("r2");
    r2.setSize(2);
    r2.setFoodProduction(100);
  
    Region r4 = new Region(p1, new Unit(4));
    r4.setName("r4");
    r4.setSize(4);
    r4.setFoodProduction(100);
    
    
    Region r5 = new Region(p1, new Unit(5));
    r5.setName("r5");
    r5.setSize(1);
    r5.setFoodProduction(100);
  
    Region r3 = new Region(p2, new Unit(3));
    r3.setName("r3");
    r3.setSize(1);
    r3.setFoodProduction(100);
  
    Region r6 = new Region(p1, new Unit(6));
    r6.setName("r6");
    r6.setSize(6);
    r1.setFoodProduction(100);
    
     Region r7 = new Region(p1, new Unit(7));
    r7.setName("r7");
    r7.setSize(5);
    r7.setFoodProduction(100);
    
     Region r8 = new Region(p2, new Unit(8));
    r8.setName("r8");
    r8.setSize(5);
    r8.setFoodProduction(100);
   
  

    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r4);
    regions.add(r3);
    regions.add(r5);
    regions.add(r6);
    regions.add(r7);
    regions.add(r8);

    List<Region> adj1 = new ArrayList<Region>();

    adj1.add(r2);
    adj1.add(r3);
     adj1.add(r7);

    r1.setAdjRegions(adj1);

    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r4);
    r2.setAdjRegions(adj2);

    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r1);
    adj3.add(r5);
    adj3.add(r4);
    r3.setAdjRegions(adj3);

    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r2);
    adj4.add(r6);
    adj4.add(r3);
    adj4.add(r7);

    r4.setAdjRegions(adj4);

    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r3);
    adj5.add(r6);
    r5.setAdjRegions(adj5);

    List<Region> adj6 = new ArrayList<Region>();
    adj6.add(r4);
    adj6.add(r5);
    adj6.add(r8);

    r6.setAdjRegions(adj6);

     List<Region> adj7 = new ArrayList<Region>();
    adj7.add(r4);
    adj7.add(r1);
    adj7.add(r8);
    r7.setAdjRegions(adj7);

     List<Region> adj8 = new ArrayList<Region>();
    adj8.add(r7);
    adj8.add(r6);
    r8.setAdjRegions(adj8);
    return regions;
  }

}
