package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;

import com.google.common.reflect.TypeToken.TypeSet;

// Class to set number of units for players
public class Unit implements Serializable {
  private List<Integer> units;
  private static Map<Integer, Integer> techToBonusMap;
  private static Map<Integer, String> techToTypeMap;
  private static final long serialVersionUID = 2L;
  static {
    //maps tech level with associated bonus
    techToBonusMap = new HashMap<Integer, Integer>(); 
    techToBonusMap.put(0, 0);
    techToBonusMap.put(1, 1);
    techToBonusMap.put(2, 3);
    techToBonusMap.put(3, 5);
    techToBonusMap.put(4, 8);
    techToBonusMap.put(5, 11);
    techToBonusMap.put(6, 15);
    //maps tech level with name of unit type
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
    this.setEvenDistribution(7, 0);
  }

  // Initialize numUnits units all with bonus 0
  public Unit(Integer numUnits) {
    this.setEvenDistribution(7, 0);
    this.addUnits(numUnits, 0);
  }

  //creates list of numOfTypes indices all with value numOfEach, representing an even distriubtion of bonuses
  private void setEvenDistribution(Integer numOfTypes, Integer numOfEach) {
    units = new ArrayList<Integer>();
    for (int i = 0; i < numOfTypes; i++) {
      units.add(numOfEach);
    }
  }

  //adds numUnits all of technology level tech to the list
  public void addUnits(int numUnits, int tech) {
    units.set(tech, units.get(tech) + numUnits);
  }

  //returns ranked from lowest to highest bonus list of unit types currently in units list 
  public List<String> getListOfUnitTypes() {
    List<String> types = new ArrayList<String>();
    for (int i = 0; i < units.size(); i++) {
      if (units.get(i) != 0) {
        types.add(this.getTypeFromTech(i));
      }
    }
    return types;
  }

  // return total number of units 
  public Integer getTotalUnits() {
    Integer total = 0;
    for (Integer i : units) {
      total += i;
    }
    return total;
  }

  public List<Integer> getUnits(){
    return this.units;
  }

  public void setUnits(List<Integer> u) {
    this.units = u;
  }
  //returns a list of the actual units (e.g. if 1 unit of each type [0, 1, 2, 3, 4, 5, 6] vs [1, 1, 1, 1, 1, 1]
  public List<Integer> getUnitList() {
    List<Integer> allUnits = new ArrayList<Integer>();
    for (int i = 0; i < this.units.size(); i++) {
      for (int j = 0; j < this.units.get(i); j++) {
        allUnits.add(i);
      }
    }
    return allUnits;
  }

  public Integer getBonusFromTech(Integer tech) {
    return techToBonusMap.get(tech);
  }

  public String getTypeFromTech(Integer tech) {
    return techToTypeMap.get(tech);
  }



  // sets the first unit of tech level 'tech' to increase by 'increase'
  public void upgradeUnit(Integer tech, int increase) {
    units.set(tech, units.get(tech) - 1); // subtract one from numOfType you're upgrading from
    units.set(tech + increase, units.get(tech + increase) + 1); // add one to numOfType you're upgrading
  }

   //  //returns set of unit types in this list of units
  //   public Set<String> getSetOfUnitTypes() { 
  //   Set<String> set = new HashSet<String>();
  // for (int i = 0; i < units.size(); i++){
  //     set.add(this.getUnitTypeFromTech(i));
  //   }
  //   return set;
  // }
  //  //returns map of unit types to num of unit types 
  //   public Map<String, Integer> getTypetoNumMap()  {
  //   Map<String, Integer> unitNumTypeMap = new HashMap<String, Integer>();
  //     for (int i = 0; i < units.size(); i++){
  //     String key = techToTypeMap.get(i);
  //       unitNumTypeMap.put(key, units.get(i));
  //   }
  //   return unitNumTypeMap;
  // } 

  
  
}
