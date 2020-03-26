package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TestUnit {
  @Test
  public void test_unit() {
    Unit unit = new Unit(5);
    assertEquals(5, unit.getUnits());
    assertEquals(0, unit.getUnitBonus(0));
    assertEquals(0, unit.getUnitTech(0));
    assertEquals(0, unit.getBonusFromTech(unit.getUnitTech(0)));
    unit.upgradeUnits(0, 2); //change index 0 tech from 0 to 2
    assertEquals(2, unit.getUnitTech(0));
    assertEquals(3, unit.getUnitBonus(0));
    unit.upgradeUnits(0, 2); //change index 1 tech (aka the next first unit with level of 0) from 0 to 2
    assertEquals(2, unit.getUnitTech(1));
    assertEquals(3, unit.getUnitBonus(1));
    assertEquals(3, unit.getBonusFromTech(unit.getUnitTech(1)));
    unit.upgradeUnits(0, 2); //change index 2 tech from 0 to 2
    assertEquals(2, unit.getUnitTech(2));
    unit.upgradeUnits(2, 2); //change index 0 tech from 2 to 4
    assertEquals(4, unit.getUnitTech(0));
    assertEquals(8, unit.getUnitBonus(0));
    assertEquals(8, unit.getBonusFromTech(unit.getUnitTech(0)));
    unit.upgradeUnits(2, 3); //change index 1 tech from 2 to 5
    assertEquals(5, unit.getUnitTech(1));
    assertEquals(11, unit.getUnitBonus(1));
    unit.upgradeUnits(2, 4); //change index 2 tech from 2 to 6
    assertEquals(6, unit.getUnitTech(2));
    assertEquals(15, unit.getUnitBonus(2));
    this.printList(unit);
  }

  public void printList(Unit u) {
    List<Integer> list = u.getUnitList();
    System.out.print("{");
    for (int i = 0; i < u.getUnits(); i++) {
      System.out.print("(" + u.getUnitTech(i) + ", " + u.getUnitBonus(i) + ")");
      if (i != (list.size() - 1)) {
        System.out.print(", ");
      }
    }
    System.out.println("} ");
  }

 
}
