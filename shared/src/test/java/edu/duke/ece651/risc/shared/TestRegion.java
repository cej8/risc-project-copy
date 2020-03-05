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
    //  u.setUnits(3);
    region.setUnits(u);
    // region 2
    Region region2 = new Region();
    AbstractPlayer p2 = new HumanPlayer();
    p2.setName("test");
    region2.setOwner(p2);
    Unit u2 = new Unit(2);
    // u2.setUnits(3);
    region2.setUnits(u2);
    ArrayList<Region> regionList = new ArrayList<Region>();
    regionList.add(region);
    regionList.add(region2);
    region.setAdjRegions(regionList);
    assertEquals(p,region.getOwner());
    assertEquals(u,region.getUnits());
    assertEquals(regionList, region.getAdjRegions());

    Region region3= new Region(p,u);
  }

}
