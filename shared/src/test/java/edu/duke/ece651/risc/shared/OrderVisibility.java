package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.io.*;
import java.net.*;


public class OrderVisibility {
  @Test
  public void test_vis() {
    Board board = new Board();
    AbstractPlayer p1 = new HumanPlayer("p1");
    AbstractPlayer p2 = new HumanPlayer("p2");
    AbstractPlayer p3 = new HumanPlayer("p3");
    Unit u1 = new Unit(10);
    Unit u2 = new Unit(10);
    Unit u3 = new Unit(10);
    Unit u4 = new Unit(10);
    Region r1 = new Region(p1, u1);
    Region r2 = new Region(p2, u2);
    Region r3 = new Region(p2, u3);
    Region r4 = new Region(p3, u4);
    r1.setName("r1");
    r2.setName("r2");
    r3.setName("r3");
    r4.setName("r4");
    r1.setAdjRegions(Arrays.asList(r2));
    r2.setAdjRegions(Arrays.asList(r1, r3));
    r3.setAdjRegions(Arrays.asList(r2, r4));
    r4.setAdjRegions(Arrays.asList(r3));
    board.setRegions(Arrays.asList(r1, r2, r3, r4));
    board.initializeSpies(Arrays.asList("p1", "p2", "p3"));

  }

}
