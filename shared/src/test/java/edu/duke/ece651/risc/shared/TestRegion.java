package edu.duke.ece651.risc.shared;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

public class TestRegion {
  @Test
  public void test_Region() {
    Region region = new Region();
    
    AbstractPlayer p = new HumanPlayer();
    p.setName("test");
    region.setOwner(p);
    Unit u = new Unit(3);
    region.setUnits(u);
    region.setSize(5);
    region.setFoodProduction(7);
    region.setTechProduction(2);
    // region 2
    Region region2 = new Region();
    AbstractPlayer p2 = new HumanPlayer();
    p2.setName("test");
    region2.setOwner(p2);
    Unit u2 = new Unit(2);
    region2.setUnits(u2);
    region2.setSize(2);
    region2.setFoodProduction(3);
    region2.setTechProduction(4);
    ArrayList<Region> regionList = new ArrayList<Region>();
    regionList.add(region);
    regionList.add(region2);
    region.setAdjRegions(regionList);
    assertEquals(p,region.getOwner());
    assertEquals(u,region.getUnits());
    assertEquals(regionList, region.getAdjRegions());

    // resources check
    assertEquals(5, regionList.get(0).getSize());
    assertEquals(7, regionList.get(0).getFoodProduction());
    assertEquals(2, regionList.get(0).getTechProduction());
    
    regionList.get(0).setFoodProduction(1222222);
    assertEquals(1222222, regionList.get(0).getFoodProduction()); 
    regionList.get(0).setTechProduction(9506);
    assertEquals(9506, regionList.get(0).getTechProduction());

  }

}
