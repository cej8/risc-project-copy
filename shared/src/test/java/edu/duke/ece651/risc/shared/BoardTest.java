package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;
import java.io.*;
import java.net.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

//@RunWith(MockitoJUnitrunner.class) 

public class BoardTest {
  @Test
  public void test_Board() throws IOException {
    //set up mock server
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    Board board = getBoard(player1);
    // ArrayList<Object> objs = new ArrayList<Object>();
    // objs.add(board);
    // Socket mockClientSocket = MockTests.setupMockSocket(objs);    
    List<Region> regions2 = new ArrayList<Region>();
    board.setRegions(regions2);
    assertEquals(regions2, board.getRegions());
    // }
  }

  @Test void test_BoardMethods(){
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    AbstractPlayer player2 = new HumanPlayer("Player 1");
    Board board = getBoard(player1);
    Map<AbstractPlayer, List<Region>> map = board.getPlayerToRegionMap();
    assertEquals(map.get(player1), board.getRegions());
    Set<AbstractPlayer> set = board.getPlayerSet();
    assertEquals(true, set.contains(player1));
    assertEquals(false, set.contains(player2));
    
  }
   public Board getBoard(AbstractPlayer player1) {
    //set up units/regions/adjancies
    Unit unit = new Unit(10);
    Unit adjUnit = new Unit(15);
    Unit adjUnit2 = new Unit(20);
    Region region = new Region(player1, unit);
    region.setName("Earth");
    Region adjRegion = new Region(player1, adjUnit);
    adjRegion.setName("Wind");
    Region adjRegion2 = new Region(player1, adjUnit2);
    adjRegion2.setName("Fire");
    List<Region> regions = new ArrayList<Region>();
    regions.add(adjRegion);
    regions.add(adjRegion2);
    region.setAdjRegions(regions); //set region's adjacentRegions
    regions.add(region); //add all regions to regionlist for board
    Board board = new Board(regions);
    assertEquals(regions, board.getRegions());
    assertEquals(region, adjRegion2.getRegionByName(board, "Earth"));
    return board;
  }

  @Test
  public void test_BoardAssorted(){
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
    r3.setCloakTurns(3);
    r1.setAdjRegions(Arrays.asList(r2));
    r2.setAdjRegions(Arrays.asList(r1, r3));
    r3.setAdjRegions(Arrays.asList(r2));
    board.setRegions(Arrays.asList(r1, r2, r3));
    board.initializeSpies(Arrays.asList("p1", "p2"));

    assert(board.getNumRegionsOwned(p1) == 1);
    assert(board.getNumRegionsOwned(p2) == 2);
    Map<AbstractPlayer, List<Region>> ptrm = board.getPlayerToRegionMap();
    assert(ptrm.get(p1).equals(Arrays.asList(r1)));
    assert(ptrm.get(p2).equals(Arrays.asList(r2,r3)));

    Set<Region> p1r = board.getPlayerRegionSet(p1);
    Set<Region> p2r = board.getPlayerRegionSet(p2);
    assert(p1r.containsAll(Arrays.asList(r1)));
    assert(p2r.containsAll(Arrays.asList(r2,r3)));
    

    Board b2 = new Board();
    Unit u4 = new Unit(5);
    Unit u5 = new Unit(5);
    Unit u6 = new Unit(5);
    Region r4 = new Region(p1, u4);
    Region r5 = new Region(p2, u5);
    Region r6 = new Region(p2, u6);
    r4.setName("r1");
    r5.setName("r2");
    r6.setName("r3");
    r6.setCloakTurns(2);
    r4.setAdjRegions(Arrays.asList(r5));
    r5.setAdjRegions(Arrays.asList(r4, r6));
    r6.setAdjRegions(Arrays.asList(r5));
    b2.setRegions(Arrays.asList(r4, r5, r6));
    b2.initializeSpies(Arrays.asList("p1", "p2"));
    
    board.updateVisible("p1", b2);
    assert(r1.getUnits().getUnits().get(0) == 5);
    assert(r2.getUnits().getUnits().get(0) == 5);
    assert(r3.getUnits().getUnits().get(0) == 10);
    assert(r3.getCloakTurns() == 3);

    Board b3 = new Board();
    Unit u7 = new Unit(0);
    Unit u8 = new Unit(0);
    Unit u9 = new Unit(0);
    Region r7 = new Region(p1, u7);
    Region r8 = new Region(p2, u8);
    Region r9 = new Region(p2, u9);
    r7.setName("r1");
    r8.setName("r2");
    r9.setName("r3");
    r9.setCloakTurns(1);
    r8.setCloakTurns(3);
    r7.setAdjRegions(Arrays.asList(r8));
    r8.setAdjRegions(Arrays.asList(r7, r9));
    r9.setAdjRegions(Arrays.asList(r8));
    b3.setRegions(Arrays.asList(r7, r8, r9));
    b3.initializeSpies(Arrays.asList("p1", "p2"));


    board.updateVisible("p1", b3);
    assert(r1.getUnits().getUnits().get(0) == 0);
    assert(r2.getUnits().getUnits().get(0) == 5);
    assert(r3.getUnits().getUnits().get(0) == 10);
    assert(r3.getCloakTurns() == 3);
    assert(r2.getCloakTurns() == 3);


    Board b4 = new Board();
    Unit u10 = new Unit(3);
    Unit u11 = new Unit(3);
    Unit u12 = new Unit(3);
    Region r10 = new Region(p1, u10);
    Region r11 = new Region(p2, u11);
    Region r12 = new Region(p2, u12);
    r10.setName("r1");
    r11.setName("r2");
    r12.setName("r3");
    r12.setCloakTurns(1);
    r11.setCloakTurns(2);
    r10.setAdjRegions(Arrays.asList(r11));
    r11.setAdjRegions(Arrays.asList(r10, r12));
    r12.setAdjRegions(Arrays.asList(r11));
    b4.setRegions(Arrays.asList(r10, r11, r12));
    b4.initializeSpies(Arrays.asList("p1", "p2"));
    List<Spy> r12Spy = r12.getSpies("p1");
    r12Spy.add(new Spy());

    board.updateVisible("p1", b4);
    assert(r1.getUnits().getUnits().get(0) == 3);
    assert(r2.getUnits().getUnits().get(0) == 5);
    assert(r3.getUnits().getUnits().get(0) == 3);
    assert(r3.getCloakTurns() == 1);
    assert(r2.getCloakTurns() == 2);

    Region r1C = board.getRegionByName("r1");
    assert(r1C == r1);

    List<AbstractPlayer> plist = board.getPlayerList();
    assert(plist.containsAll(Arrays.asList(p1,p2)));

  }

  @Test
  void test_rest(){
    Board board = new Board();
    AbstractPlayer p1 = new HumanPlayer("p1");
    AbstractPlayer p2 = new HumanPlayer("p2");
    AbstractPlayer p3 = new HumanPlayer("p3");
    AbstractPlayer p4 = new HumanPlayer("p4");
    Unit u1 = new Unit(10);
    Unit u2 = new Unit(10);
    Unit u3 = new Unit(10);
    Unit u4 = new Unit(10);
    Region r1 = new Region(p1, u1);
    Region r2 = new Region(p2, u2);
    Region r3 = new Region(p3, u3);
    Region r4 = new Region(null, u4);
    r1.setName("r1");
    r2.setName("r2");
    r3.setName("r3");
    r4.setName("r4");
    r1.setAdjRegions(Arrays.asList(r2));
    r2.setAdjRegions(Arrays.asList(r1, r3));
    r3.setAdjRegions(Arrays.asList(r2, r4));
    r4.setAdjRegions(Arrays.asList(r3));
    board.setRegions(Arrays.asList(r1, r2, r3, r4));
    board.initializeSpies(Arrays.asList("p1", "p2", "p3", "p4"));
    //Check null player, should never happen
    List<AbstractPlayer> pList = board.getPlayerList();
    assert(pList.containsAll(Arrays.asList(p1,p2,p3)));
    assert(board.getPlayerRegionSet(p1).contains(r1));
    r4.setOwner(p4);
   
    Board b2 = new Board();
    Unit u5 = new Unit(5);
    Unit u6 = new Unit(5);
    Unit u7 = new Unit(5);
    Unit u8 = new Unit(5);
    AbstractPlayer p1c = new HumanPlayer("p1");
    AbstractPlayer p2c = new HumanPlayer("p2");
    AbstractPlayer p3c = new HumanPlayer("p3");
    AbstractPlayer p4c = new HumanPlayer("p4");
    Region r5 = new Region(p1c, u5);
    Region r6 = new Region(p2c, u6);
    Region r7 = new Region(p3c, u7);
    Region r8 = new Region(p3c, u8);
    r5.setName("r1");
    r6.setName("r2");
    r7.setName("r3");
    r8.setName("r4");
    r5.setAdjRegions(Arrays.asList(r6));
    r6.setAdjRegions(Arrays.asList(r5, r7));
    r7.setAdjRegions(Arrays.asList(r6, r8));
    r8.setAdjRegions(Arrays.asList(r7));
    b2.setRegions(Arrays.asList(r5, r6, r7, r8));
    b2.initializeSpies(Arrays.asList("p1", "p2", "p3", "p4"));

    board.updateVisible("p1", b2);
    assert(r3.getOwner() == p3c);
    assert(r4.getOwner() == p4);

    assert(board.getPlayerByName("p4") == p4);
    assert(board.getPlayerByName("jeff") == null);
    
  }
}
