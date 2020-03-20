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
    Region adjRegion = new Region(player1, adjUnit);
    Region adjRegion2 = new Region(player1, adjUnit2);
    List<Region> regions = new ArrayList<Region>();
    regions.add(adjRegion);
    regions.add(adjRegion2);
    region.setAdjRegions(regions); //set region's adjacentRegions
    regions.add(region); //add all regions to regionlist for board
    Board board = new Board(regions);
    assertEquals(regions, board.getRegions());
    return board;
  }
}
