package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

import com.google.common.reflect.TypeToken.TypeSet;

// Class to set number of units for players
public class Unit implements Serializable {
  private List<Integer> units;
  private List<Integer> numOfEachType;
  private static Map<Integer, Integer> techToBonusMap;
  private static Map<Integer, String> techToTypeMap;
  private static final long serialVersionUID = 2L;
  static {
    techToBonusMap = new HashMap<Integer, Integer>();
    techToBonusMap.put(0, 0);
    techToBonusMap.put(1, 1);
    techToBonusMap.put(2, 3);
    techToBonusMap.put(3, 5);
    techToBonusMap.put(4, 8);
    techToBonusMap.put(5, 11);
    techToBonusMap.put(6, 15);
    techToTypeMap = new HashMap<Integer, String>();
    techToTypeMap.put(0, "Civilian");
    techToTypeMap.put(1, "Student Trainee");
    techToTypeMap.put(2, "Junior Technician");
    techToTypeMap.put(3, "Aerospace Engineer");
    techToTypeMap.put(4, "Space Cadet");
    techToTypeMap.put(5, "Astronaut");
    techToTypeMap.put(6, "Space Captain");
  }

  public Unit() {
    units = new ArrayList<Integer>();
    this.setNumOfEachType(7, 0);
  }

  // Initialize numUnits units all with bonus 0
  public Unit(Integer numUnits) {
    units = new ArrayList<Integer>();
    this.setNumOfEachType(7, 0);
    this.addUnits(numUnits, 0);
  }

  private void setNumOfEachType(Integer numOfTypes, Integer numOfEach) {
    numOfEachType = new ArrayList<Integer>();
    for (int i = 0; i < numOfTypes; i++) {
      numOfEachType.add(numOfEach);
    }
  }

  public void addUnits(int numUnits, int tech) {
    for (Integer i = 0; i < numUnits; i++) {
      units.add(tech); // add numUnits of tech level tech to units list
      numOfEachType.set(tech, numOfEachType.get(tech) + 1);// add numUnits to the curr value in int tech index of
                                                           // numOfEachType list
    }
    units.sort(Comparator.naturalOrder());
  }

  public List<String> getSortedListOfUnitTypes() {
    List<String> types = new ArrayList<String>();
    Set<String> addedUnitTypes = new HashSet<String>(); // set of unit types that have been added (i.e. starts off empty
                                                        // bc none added
    for (Integer i : this.units) {
      String currentType = this.getUnitTypeFromTech(i);
      if (!(addedUnitTypes.contains(currentType))) { // if it hasn't been added
        types.add(currentType);
        addedUnitTypes.add(currentType); // add to list of printed types

      }

    }
    return types;
  }

  // returns sorted list of units
  public List<Integer> getUnitList() {
    units.sort(Comparator.naturalOrder());
    return this.units;
  }

  public List<Integer> getNumOfEachType() {
    return this.numOfEachType;
  }

  public Set<String> getUnitTypeSet() {
    Set<String> set = new HashSet<String>();
    for (Integer i : this.units) {
      set.add(this.getUnitTypeFromTech(i));
    }
    return set;
  }

  public Map<String, Integer> getUnitNumTypeMap() {
    Map<String, Integer> unitNumTypeMap = new HashMap<String, Integer>();
    for (Integer i : this.units) {
      String key = techToTypeMap.get(i);
      unitNumTypeMap.put(key, this.getUnitTypeCount(key));
    }
    return unitNumTypeMap;
  }

  public Integer getUnitTypeCount(String type) {
    Integer count = 0;
    for (Integer i : this.units) {
      String key = techToTypeMap.get(i);
      if (key.equalsIgnoreCase(type)) {
        count++;
      }
    }
    return count;
  }

  // returns the bonus of a unit by index
  public String getUnitTypeFromIndex(int index) {
    Integer currUnit = units.get(index);
    return techToTypeMap.get(currUnit);
  }

  // returns the bonus of a unit by index
  public Integer getUnitBonusFromIndex(int index) {
    Integer currUnit = units.get(index);
    return techToBonusMap.get(currUnit);
  }

  // returns the tech level of a unit by index
  public Integer getUnitTechFromIndex(int index) {
    return units.get(index);
  }

  // return total number of units
  public Integer getUnits() {
    return units.size();
  }

  private void upgradeUnitsHelper(int index, int increase) {
    Integer tech = units.get(index);
    numOfEachType.set(tech, numOfEachType.get(tech) - 1); // subtract one from numOfType you're upgrading from
    numOfEachType.set(tech + increase, numOfEachType.get(tech + increase) + 1); // add one to numOfType you're upgrading
    units.set(index, tech + increase);

  }

  // sets the first unit of tech level 'tech' to increase by 'increase'
  public void upgradeUnits(Integer tech, int increase) {
    for (int i = 0; i < this.getUnits(); i++) {
      if (units.get(i) == tech) {
        upgradeUnitsHelper(i, increase);
        // TODO -- take away cost here?
        units.sort(Comparator.naturalOrder());
        break;
      }
    }
  }

  public Integer getBonusFromTech(Integer tech) {
    return techToBonusMap.get(tech);
  }

  public String getUnitTypeFromTech(Integer tech) {
    return techToTypeMap.get(tech);
  }

}
