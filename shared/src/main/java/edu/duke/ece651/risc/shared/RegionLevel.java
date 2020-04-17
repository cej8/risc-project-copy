package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//class responsible for holding all information regarding a region's level to be used for upgrading resources in a region
public class RegionLevel implements Serializable {
  private static final long serialVersionUID = 23L;
  private int regionLevel;
  private double multiplier;
  private List<Double> multiplierList;

  public RegionLevel() {
    this.regionLevel = Constants.STARTING_REGION_LEVEL;
    this.multiplierList = setMultipliers();
    this.multiplier = multiplierList.get(regionLevel);

  }

  // inittialize multiplier values to be used by resource growth
  private List<Double> setMultipliers() {
    List<Double> list = new ArrayList<Double>();
    list.add(1.0);
    list.add(1.25);
    list.add(1.5);
    list.add(2.0);
    return list;
  }

  public int getRegionLevel() {
    return regionLevel;
  }

  public double getMultiplier() {
    return multiplier;
  }

  public void upgradeLevel() {
    regionLevel++;//incremtent regionLevel and set new respurce multiplier value
    multiplier = multiplierList.get(regionLevel);
  }
}
