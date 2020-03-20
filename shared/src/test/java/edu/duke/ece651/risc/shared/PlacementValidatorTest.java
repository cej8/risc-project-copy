package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PlacementValidatorTest {
  @Test
  public void test_PlacementsUnits() {
    //set up player/region/ units
    AbstractPlayer player = new HumanPlayer("Player 1");
    int totalUnits = 18; //validUnits.size() * 3;
    Unit playerUnits = new Unit(totalUnits);
    ValidatorInterface pv = new PlacementValidator(player, playerUnits);
    List<Region> regions = getRegions(player);

    //true: valid units
    List<Unit> validUnits = get6UnitList(3, 3, 3, 3, 3, 3);
    List<PlacementOrder> validPlacements = getPlacements(regions, validUnits);
    assertEquals(true, pv.validateUnits(validPlacements));

    //false: too few units 
    List<Unit> tooFewUnits = get6UnitList(1, 2, 3, 1, 2, 3);
    List<PlacementOrder> tooFewPlacements = getPlacements(regions, tooFewUnits);
    assertEquals(false, pv.validateUnits(tooFewPlacements));

    //false: too many units 
    List<Unit> tooManyUnits = get6UnitList(1, 2, 3, 4, 5, 6);
    List<PlacementOrder> tooManyPlacements = getPlacements(regions, tooManyUnits);
    assertEquals(false, pv.validateUnits(tooManyPlacements));

   //false: placing zero units 
    List<Unit> zeroUnits = get6UnitList(1, 2, 3, 0, 5, 6);
    List<PlacementOrder> zeroPlacements = getPlacements(regions, zeroUnits);
    assertEquals(false, pv.validateUnits(zeroPlacements));

    //true: regions for placement are valid
    assertEquals(true, pv.validateRegions(validPlacements));
    
  }

  private List<PlacementOrder> getPlacements(List<Region> regions, List<Unit> units) {
    assertEquals(regions.size(), units.size());
    List<PlacementOrder> placements = new ArrayList<PlacementOrder>();
    for (int i = 0; i < regions.size(); i++){
      placements.add(new PlacementOrder(regions.get(i), units.get(i)));
    }
    return placements;
  }

 private List<Unit> get6UnitList(int u0, int u1, int u2, int u3, int u4, int u5){
    List<Unit> units = new ArrayList<Unit>();
    Unit unit0 = new Unit(u0);
    units.add(unit0);
    Unit unit1 = new Unit(u1);
    units.add(unit1);
    Unit unit2 = new Unit(u2);
    units.add(unit2);
    Unit unit3 = new Unit(u3);
    units.add(unit3);
    Unit unit4 = new Unit(u4);
    units.add(unit4);
    Unit unit5 = new Unit(u5);
    units.add(unit5);
    return units;
  }
  
   private List<Region> getRegions(AbstractPlayer player) {
    Region r0 = new Region("Earth");
    Region r1 = new Region("Mars");
    Region r2 = new Region("Venus");
    Region r3 = new Region("Mercury");
    Region r4 = new Region("Saturn");
    Region r5 = new Region("Uranus");

    List<Region> regions = new ArrayList<Region>();
    regions.add(r0);
    regions.add(r1);
    regions.add(r2);
    regions.add(r3);
    regions.add(r4);
    regions.add(r5);

    List<Region> adj0 = new ArrayList<Region>();
    adj0.add(r5);
    adj0.add(r0);
    r0.setAdjRegions(adj0);

    List<Region> adj1 = new ArrayList<Region>();
    adj1.add(r0);
    adj1.add(r2);
    r1.setAdjRegions(adj1);

    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r3);
    r2.setAdjRegions(adj2);

    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r2);
    adj3.add(r4);
    r3.setAdjRegions(adj3);

    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r3);
    adj4.add(r5);
    r4.setAdjRegions(adj4);

    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r4);
    adj5.add(r0);
    r5.setAdjRegions(adj5);

    for (Region r : regions) {
      r.setOwner(player);
    }
    
    return regions;
  }
}

