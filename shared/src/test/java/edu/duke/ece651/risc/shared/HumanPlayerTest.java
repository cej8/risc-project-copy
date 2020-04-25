package edu.duke.ece651.risc.shared;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HumanPlayerTest {
  @Test
  public void test_PlayerCreation() {
      AbstractPlayer h = new HumanPlayer("Human1");
      ArrayList<Object> someObjects = new ArrayList<Object>();
      someObjects.add(new ArrayList<Region>());
      //  AbstractPlayer player1 = new HumanPlayer("Player 1", MockTests.setupMockSocket(someObjects));
      AbstractPlayer player2 = new HumanPlayer("Player 2");
      h.setPlaying(true);
      assertEquals("Human1",h.getName());
      assertEquals(true, h.isPlaying());
  }

  @Test
  public void test_abstractplayer(){
    AbstractPlayer p = new HumanPlayer("pname");
    assert(p.isWatching() == null);
    p.setWatching(true);
    assert(p.isWatching() == true);
    p.setWatchingNull();
    assert(p.isWatching() == null);
    AbstractPlayer p2 = new HumanPlayer("qname");
    assert(p.compareTo(p2) < 0);
    assert(p2.compareTo(p) > 0);
    assert(p.compareTo(p) == 0);
    assert(p.equals(null) == false);
    assert(p.equals(2) == false);
    assert(p.equals(p) == true);
    assert(p.equals(p2) == false);
  }

}
