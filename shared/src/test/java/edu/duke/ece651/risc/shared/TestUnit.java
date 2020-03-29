package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.lang.*;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TestUnit {
  @Test
  public void test_unit() {
    // test empty list
    Unit empty = new Unit();
    assertEquals(0, empty.getTotalUnits()); //0 zeros
    for (Integer i : empty.getUnits()) {
      assertEquals(0, i); //all indices representing frequences of bonuses have value 0
    }
    assertEquals(0, empty.getUnitList().size()); //list of actual units is empty

    List<Integer> unit = new ArrayList<Integer>();
    unit.add(3);
    unit.add(0);
    unit.add(0);
    empty.setUnits(unit);
    assertEquals(3, empty.getTotalUnits()); //3 total units
    assertEquals(3, empty.getUnits().get(0)); //3 units of bonus 0
    assertEquals("Civilian", empty.getTypeFromTech(0));
    assertEquals(unit, empty.getUnits()); //set units == getUnits

    List<Integer> unitList = new ArrayList<Integer>();
    unitList.add(0);
    unitList.add(0);
    unitList.add(0);
    assertEquals(unitList, empty.getUnitList());  //Unit.unit List<Integer> of [3, 0, 0] returns an actual unit list of List<Integer> of [0, 0, 0]
  }
@Test
    public void test_upgrade(){
    // // test 5 units
    Unit unit = new Unit(3);
    assertEquals(3, unit.getTotalUnits()); //3 total units
    assertEquals(3, unit.getUnits().get(0)); //3 units of bonus 0
    assertEquals("Civilian", unit.getTypeFromTech(0)); 
    unit.upgradeUnit(0, 2); // upgrade one basic unit by 2 (Citizen > Student Trainee > Junior Scientist)
    assertEquals(1, unit.getUnits().get(2)); //1 unit of bonus 1
    assertEquals(2, unit.getUnits().get(0)); //2 units remaining of bonus 0
    assertEquals("Junior Technician", unit.getTypeFromTech(2));
    unit.upgradeUnit(0, 2); // change a 0 tech to 2
    assertEquals(2, unit.getUnits().get(2)); //2 units of 2
    assertEquals(1, unit.getUnits().get(0)); //1 unit of 0
    unit.upgradeUnit(0, 2); // change a 0 tech 0 to 2
    assertEquals(3, unit.getUnits().get(2)); //3 units of 2
    assertEquals(0, unit.getUnits().get(0)); //0 units of 0
    unit.upgradeUnit(2, 2); // change a  2 to 4
    assertEquals(1, unit.getUnits().get(4)); 
    assertEquals(2, unit.getUnits().get(2));
    unit.upgradeUnit(2, 3); // change 2 to 5
    assertEquals(1, unit.getUnits().get(5));
    assertEquals(1, unit.getUnits().get(2));
    unit.upgradeUnit(2, 4); // change  2 to 6
    assertEquals(1, unit.getUnits().get(6)); 
    assertEquals(0, unit.getUnits().get(2));
    this.printList(unit);
  }

  @Test
  void test_addUnits() {
    Unit unit = new Unit(5);
    unit.addUnits(1, 3); // add unit of tech 3
    assertEquals(1, unit.getUnits().get(3));
    assertEquals(5, unit.getBonusFromTech(3));
    unit.addUnits(1, 3); // add unit of tech 3
    assertEquals(2, unit.getUnits().get(3));
    assertEquals(7, unit.getTotalUnits());
  }

  @Test
  public void test_bonus() {
    Unit unit = this.getUnit();
    for (Integer i : unit.getUnits()) {
      assertEquals(2, i); //assert all types have frequency of 2
    }
    unit.addUnits(1, 0); //add one 0
    assertEquals(3, unit.getUnits().get(0));
    unit.addUnits(2, 1); //add two 1s
    assertEquals(4, unit.getUnits().get(1));
    
  }

  @Test
  public void test_set() {
    Unit unit = this.getUnit();
    Set<String> addedUnitTypes = new HashSet<String>(); // set of unit types that have been addeded (i.e. starts off empty as none have been)
    StringBuffer sbSet = new StringBuffer();
    for (int i = 0; i < unit.getUnits().size(); i++) {
      if (!(addedUnitTypes.contains(unit.getTypeFromTech(i)))) { // if it hasn't been added
        sbSet.append(unit.getTypeFromTech(i) + ": " + unit.getUnits().get(i) + "\n");
        addedUnitTypes.add(unit.getTypeFromTech(i)); // add to list of added types
      }
    }
    assertEquals(7, addedUnitTypes.size()); //assert each type was added once
    for (String s : unit.getListOfUnitTypes()) {
      assertTrue(addedUnitTypes.contains(s)); //assert each of this unit's types is in addedUnitTypes
    }
    StringBuffer sbTypeList = new StringBuffer(); 
    for (int i = 0; i < unit.getUnits().size(); i++) {
      if (unit.getUnits().get(i) != 0) {
        sbTypeList.append(unit.getTypeFromTech(i) + ": " + unit.getUnits().get(i) + "\n");
      }
    }
    assertEquals(0, sbSet.compareTo(sbTypeList));
      //    assert both those methods produce same mapping of unit types to their count
  }

  @Test
  public void test_unvalidated(){
    Unit unit = getUnit();
    assertThrows(IndexOutOfBoundsException.class, () -> unit.addUnits(1, 7)); //TODO: currently this throws, can't add invalid unit tech
    assertThrows(IndexOutOfBoundsException.class, () -> unit.upgradeUnit(6, 10)); //TODO: currently this throws, can't add invalid unit tech
    this.printList(unit);
    
  }
  
 // create list with 2 of each {0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6}
  public Unit getUnit() {
    Unit unit = new Unit(14);
    unit.upgradeUnit(0, 6);
    unit.upgradeUnit(0, 6);
    unit.upgradeUnit(0, 5);
    unit.upgradeUnit(0, 4);
    unit.upgradeUnit(0, 3);
    unit.upgradeUnit(0, 2);
    unit.upgradeUnit(0, 1);
    unit.upgradeUnit(0, 5);
    unit.upgradeUnit(0, 4);
    unit.upgradeUnit(0, 3);
    unit.upgradeUnit(0, 2);
    unit.upgradeUnit(0, 1);
    return unit;
  }

  public void printList(Unit u) {
    for (int i = 0; i < u.getUnits().size(); i++) {
      System.out.println("Tech: " + i + " (" + u.getTypeFromTech(i) + "), Bonus: "
          + u.getBonusFromTech(i));
    }
    System.out.println("Num units of each type: " + u.getUnits());
  }

}
