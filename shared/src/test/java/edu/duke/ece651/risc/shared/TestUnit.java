package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestUnit {
  @Test
  public void test_unit() {
    Unit unit = new Unit();
    Integer num = 12;
    unit.setUnits(num);
    Integer response = unit.getUnits();
    assertEquals(12,response);
  }

}
