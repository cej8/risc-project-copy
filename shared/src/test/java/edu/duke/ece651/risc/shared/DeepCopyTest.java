package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.io.*;
import java.net.*;
import org.junit.jupiter.api.Test;

public class DeepCopyTest {
  @Test
  public void test_DeepCopyPrimitive() throws IOException, ClassNotFoundException{
    String string = "test string";
    String stringCopied = (String)DeepCopy.deepCopy(string);
    assertEquals(0, stringCopied.compareToIgnoreCase(string));

    int num = 27;
    int numCopied = (int) DeepCopy.deepCopy(num);
    assertEquals(true, num == numCopied);
  }

 @Test
  public void test_DeepCopy() throws IOException, ClassNotFoundException{

    AbstractPlayer player1 = new HumanPlayer("Player 1");
    Unit unit = new Unit(10);
    Unit adjUnit = new Unit(15);
    Unit adjUnit2 = new Unit(20);
    Unit copyUnit = (Unit) DeepCopy.deepCopy(unit);
    assertEquals(unit.getUnits(), copyUnit.getUnits());
    
    Region region = new Region(player1, unit);
    Region adjRegion = new Region(player1, adjUnit);
    Region adjRegion2 = new Region(player1, adjUnit2);
    List<Region> regions = new ArrayList<Region>();
    regions.add(adjRegion);
    regions.add(adjRegion2);
    region.setAdjRegions(regions); //set region's adjacentRegions
    Region copyRegion = (Region) DeepCopy.deepCopy(region);
    assertEquals(region.getOwner().getName(), copyRegion.getOwner().getName());
    assertEquals(region.getAdjRegions().size(), copyRegion.getAdjRegions().size());
    regions.add(region); //add all regions to regionlist for board

    Board board = new Board(regions);
    Board copyBoard = (Board) DeepCopy.deepCopy(board);
    assertEquals(board.getRegions().size(), copyBoard.getRegions().size());
   

    
  }
}
