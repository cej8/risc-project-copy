package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PairTest {
  @Test
  public void test_Pair() {
    Pair p1 = new Pair<String, String>("first", "second");
    Pair p2 = new Pair<String, String>("first", "second");
    assert(p1.getFirst().equals("first"));
    assert(p1.getSecond().equals("second"));
    assert(p1.equals(p2));
    assert(!p1.equals(null));
    Integer i = new Integer(0);
    assert(!p1.equals(i));
  }

}
