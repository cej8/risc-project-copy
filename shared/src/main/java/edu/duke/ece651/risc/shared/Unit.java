package edu.duke.ece651.risc.shared;
import java.io.Serializable;
import java.util.*;

// Class to set number of units for players
public class Unit implements Serializable {
  private List<Integer> units;
  private static Map<Integer, Integer> techToBonusMap;  
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
  }
  
  public Unit() {
  }

  //Initialize numUnits units all with bonus 0
  public Unit(Integer numUnits){
    units = new ArrayList<Integer>();
    for (Integer i = 0; i < numUnits; i++){
      units.add(0);
    }
  }

  public List<Integer> getUnitList() {
    return this.units;
  }

  //returns the bonus of a unit by index
  public Integer getUnitBonus(int index) {
    Integer currUnit = units.get(index);
    return techToBonusMap.get(currUnit);
  }

  //returns the tech level of a unit by index
  public Integer getUnitTech(int index) {
    return units.get(index);
  }
  
  //return total number of units
  public Integer getUnits(){
    return units.size();
  }

  //TODO -- will need to validate that units.get(index) + increase <= 6... which class should this occur under?
  private void upgradeUnitsHelper(int index, int increase) {
    units.set(index, units.get(index) + increase);
  }

  //sets the first unit of tech level 'tech' to increase by 'increase'
  public void upgradeUnits(Integer tech, int increase){
     for (int i = 0; i < this.getUnits(); i++) {
       if (units.get(i) == tech){
        upgradeUnitsHelper(i, increase);
        //TODO -- take away cost here?
        break;
      }
    }
  }

  public Integer getBonusFromTech(Integer tech){
    return techToBonusMap.get(tech);
  }
  
}
