package edu.duke.ece651.risc.shared;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
  // Test Board Setup
  // r1 - r2
  // | |
  // r3 r4
  // | |
  // r5 - r6
  // P1 owns r1,r2,r4.r5
  // P2 owns r3,r6
  @Test
  public void test_RegionValidator() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    AbstractPlayer p2 = new HumanPlayer("player 2");
    List<Region> regions = getRegionList(p1, p2);
    Board b = new Board(regions);

    MoveValidator mv = new MoveValidator(p1, b);
    AttackValidator av = new AttackValidator(p1, b);
    PlacementValidator pv = new PlacementValidator(p2, new Unit(15), b);
    TechBoostValidator tbv = new TechBoostValidator(p1, b);
     List<MoveOrder> moves = getMoveOrders(regions);
    List<AttackMove> attacks = getAttackList(regions);
    List<PlacementOrder> placements = getPlacementList(p1, p2);

    List<TechBoost> invalidCostBoost = getInvalidResourceTechBoosts(p2);
    assertEquals(false, tbv.validateOrders(invalidCostBoost));

    List<TechBoost> validBoosts = getValidTechBoosts(p1);
    assertEquals(true, tbv.validateOrders(validBoosts));

    List<TechBoost> invalidCountBoosts = getInvalidCountTechBoosts(p2);
    assertEquals(false, tbv.validateOrders(invalidCountBoosts));
 
    assertEquals(false, mv.validateOrders(moves));
    // TODO uncomment when attacska re changed  
    assertEquals(false, av.validateOrders(attacks));

    assertEquals(false, pv.validateRegions(placements));

  }
  @Test
  public void plagueOrderFailures(){
    Board board = new Board();
      Region testPlague = new Region("Fire");
    AbstractPlayer plagueOwner= new HumanPlayer("plague owner");
    testPlague.setPlague(true);
    ResourceBoost ub = new ResourceBoost(testPlague);
    ResourceBoostValidator plagueValidator = new ResourceBoostValidator(plagueOwner, board);
    List<ResourceBoost> plagueBoost= new ArrayList<ResourceBoost>();
    plagueBoost.add(ub);
    assertEquals(false, plagueValidator.validOwnership(plagueBoost));

    TeleportOrder to= new TeleportOrder(testPlague,testPlague,new Unit(1));
     TeleportValidator plagueValidator2 = new TeleportValidator(plagueOwner, board);
    List<TeleportOrder> plagueTeleport= new ArrayList<TeleportOrder>();
    plagueTeleport.add(to);
    assertEquals(false, plagueValidator2.validOwnership(plagueTeleport));



  }
  private List<TechBoost> getInvalidCountTechBoosts(AbstractPlayer p) {
    List<TechBoost> boosts = new ArrayList<TechBoost>();
    TechBoost tb1 = new TechBoost(p);
    TechBoost tb2 = new TechBoost(p);
    boosts.add(tb1);
    boosts.add(tb2);

    return boosts;

  }

  private List<TechBoost> getInvalidResourceTechBoosts(AbstractPlayer p) {
    List<TechBoost> boosts = new ArrayList<TechBoost>();
    TechBoost tb1 = new TechBoost(p);
    // TechBoost tb2 = new TechBoost(p);
    boosts.add(tb1);
    // boosts.add(tb2);

    return boosts;

  }

  private List<TechBoost> getValidTechBoosts(AbstractPlayer p) {
    p.getResources().getTechResource().addTech(200);
    List<TechBoost> boosts = new ArrayList<TechBoost>();
    TechBoost tb1 = new TechBoost(p);
    // TechBoost tb2 = new TechBoost(p);
    boosts.add(tb1);
    // boosts.add(tb2);

    return boosts;
  }

  private List<Region> getRegionList(AbstractPlayer p1, AbstractPlayer p2) {
    Region r1 = new Region(p1, new Unit(1));
    r1.setName("r1");
    Region r2 = new Region(p1, new Unit(2));
    r2.setName("r2");
    Region r4 = new Region(p1, new Unit(4));
    r4.setName("r4");
    Region r5 = new Region(p1, new Unit(5));
    r5.setName("r5");
    Region r3 = new Region(p2, new Unit(3));
    r3.setName("r6");
    Region r6 = new Region(p2, new Unit(6));
    r6.setName("r6");
    r6.setPlague(true);
    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r4);
    regions.add(r3);
    regions.add(r5);
    regions.add(r6);

    List<Region> adj1 = new ArrayList<Region>();

    adj1.add(r2);
    adj1.add(r3);
    r1.setAdjRegions(adj1);

    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r4);
    r2.setAdjRegions(adj2);

    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r1);
    adj3.add(r5);
    r3.setAdjRegions(adj3);

    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r2);
    adj4.add(r6);
    r4.setAdjRegions(adj4);

    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r3);
    adj5.add(r6);
    r5.setAdjRegions(adj5);

    List<Region> adj6 = new ArrayList<Region>();
    adj6.add(r4);
    adj6.add(r5);
    r6.setAdjRegions(adj6);
    return regions;
  }

  private List<MoveOrder> getMoveOrders(List<Region> regions) {
    MoveOrder move12 = new MoveOrder(regions.get(0), regions.get(1), new Unit(5));// valid adjacent
    MoveOrder move23 = new MoveOrder(regions.get(1), regions.get(3), new Unit(2));// invalid (diff owner)
    MoveOrder move14 = new MoveOrder(regions.get(0), regions.get(2), new Unit(3));// valid not adjacent
    MoveOrder move15 = new MoveOrder(regions.get(0), regions.get(4), new Unit(1));// invalid no path
    MoveOrder move63= new MoveOrder(regions.get(5),regions.get(3),new Unit(1));//invalid source has plague
    MoveOrder move33= new MoveOrder(regions.get(3),regions.get(3), new Unit(1));//invalid, source and destination are equal
    List<MoveOrder> moves = new ArrayList<MoveOrder>();
    moves.add(move12);
    moves.add(move23);
    moves.add(move14);
    moves.add(move15);
    moves.add(move63);
    moves.add(move33);
    return moves;
  }

  private List<AttackMove> getAttackList(List<Region> regions) {
    AttackMove attack13 = new AttackMove(regions.get(0), regions.get(3), new Unit(4));// valid adjacent
    AttackMove attack23 = new AttackMove(regions.get(1), regions.get(3), new Unit(3));// invalid not adjacent
    AttackMove attack36 = new AttackMove(regions.get(3), regions.get(5), new Unit(2));// invalid same owner
     AttackMove attack33= new AttackMove(regions.get(3),regions.get(3), new Unit(1));//invalid, source and destination are equal
  
    List<AttackMove> attacks = new ArrayList<AttackMove>();
    attacks.add(attack13);
    attacks.add(attack23);
    attacks.add(attack36);
    attacks.add(attack33);
    return attacks;
  }

  private List<OrderInterface> getValidTeleport(List<Region> regions) {
    regions.get(1).getOwner().getMaxTechLevel().upgradeLevel();
    regions.get(1).getOwner().getMaxTechLevel().upgradeLevel();
    regions.get(1).getOwner().getMaxTechLevel().upgradeLevel();
    regions.get(1).getOwner().getMaxTechLevel().upgradeLevel();// upgrade p1 to level 4
    regions.get(1).getOwner().getResources().getTechResource().addTech(300);// set resources to be high enough for valid
                                                                            // teleport

    OrderInterface t25 = new TeleportOrder(regions.get(1), regions.get(4), new Unit(1));
    OrderInterface t51 = new TeleportOrder(regions.get(4), regions.get(0), new Unit(2));
    List<OrderInterface> teleports = new ArrayList<OrderInterface>();
    teleports.add(t25);
    teleports.add(t51);
    return teleports;
  }

  private List<PlacementOrder> getPlacementList(AbstractPlayer p1, AbstractPlayer p2) {
    List<Region> pRegions = getRegionsForPlacement(p1, p2);
    PlacementOrder po1 = new PlacementOrder(pRegions.get(0), new Unit(2));
    PlacementOrder po2 = new PlacementOrder(pRegions.get(1), new Unit(2));
    List<PlacementOrder> placements = new ArrayList<PlacementOrder>();
    placements.add(po1);
    placements.add(po2);
    return placements;
  }

  private List<Region> getRegionsForPlacement(AbstractPlayer p1, AbstractPlayer p2) {
    Region region1 = new Region("region1");
    region1.setOwner(p1);
    Region region2 = new Region("region2");
    region2.setOwner(p2);
    List<Region> pRegions = new ArrayList<Region>();
    pRegions.add(region1);
    pRegions.add(region2);
    return pRegions;
  }

  @Test
  public void test_validatorHelper() {
    AbstractPlayer p1 = new HumanPlayer("player 1");
    p1.getResources().getFuelResource().addFuel(10000);
    p1.getResources().getTechResource().addTech(300);
    p1.getMaxTechLevel().upgradeLevel();
    p1.getMaxTechLevel().upgradeLevel();
    p1.getMaxTechLevel().upgradeLevel();
   
    AbstractPlayer p2 = new HumanPlayer("player 2");
    List<Region> regions = getRegionList(p1, p2);
    Board b = new Board(regions);

    List<OrderInterface> orders = new ArrayList<OrderInterface>();
    List<OrderInterface> ordersValid = new ArrayList<OrderInterface>();

    OrderInterface attack13 = new AttackMove(regions.get(3), regions.get(0), new Unit(2));// valid adjacent
    OrderInterface attack23 = new AttackMove(regions.get(3), regions.get(1), new Unit(2));// invalid not adjacent
    OrderInterface attack36 = new AttackMove(regions.get(5), regions.get(3), new Unit(2));// invalid same owner
    orders.add(attack13);
    orders.add(attack23);
    orders.add(attack36);
    OrderInterface move12 = new MoveOrder(regions.get(3), regions.get(1), new Unit(2));// valid adjacent
    OrderInterface move23 = new MoveOrder(regions.get(4), regions.get(3), new Unit(3));// invalid (diff owner)
    OrderInterface move14 = new MoveOrder(regions.get(2), regions.get(0), new Unit(1));// valid not adjacent
    OrderInterface move15 = new MoveOrder(regions.get(4), regions.get(0), new Unit(3));// invalid no path

    orders.add(move12);
    orders.add(move23);
    orders.add(move14);
    orders.add(move15);
   
    // TODO: create valid regions and units test
    OrderInterface move21 = new MoveOrder(regions.get(1), regions.get(0), new Unit(1));// valid units
    OrderInterface move41 = new MoveOrder(regions.get(2), regions.get(0), new Unit(2));// valid
    OrderInterface attack34 = new AttackMove(regions.get(4), regions.get(3), new Unit(1));// invalid same owner
    OrderInterface rb4 = new ResourceBoost(regions.get(2));
    ordersValid.add(attack34);
    ordersValid.add(move21);
    ordersValid.add(move41);
    ordersValid.add(rb4);
    ValidatorHelper vh = new ValidatorHelper(p1, b);
    assertEquals(false, vh.allOrdersValid(orders));
    vh = new ValidatorHelper(p1, b);
    assertEquals(true, vh.allOrdersValid(ordersValid));
    ValidatorHelper th = new ValidatorHelper(p1, b);
    assertEquals(true, th.allOrdersValid(getValidTeleport(regions)));
    

  }

}
