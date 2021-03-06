package edu.duke.ece651.risc.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Constants;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestOrderCreator {
  @Test
  public void test_Creator() throws FileNotFoundException {
    InputStream input = new FileInputStream(new File("src/test/resources/SDOrdersAskForUnits.txt"));
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(input);
    // Client client = new Client(ci, td);
 ConnectionManager connect = new ConnectionManager();
    Connection c = connect.getConnection();
    Client client = new Client(ci, td, c);
    HumanPlayer    p1 = new HumanPlayer("player 1");
    HumanPlayer p2 = new HumanPlayer("player 2");
    Board b= new Board(getRegionList(p1, p2));
    client.setBoard(b);
    client.setPlayer(p1);
    OrderHelper oc = new OrderHelper(client);
    List<OrderInterface> orders = oc.createOrders();
    assertEquals(8, orders.size());
    assertEquals(Constants.MOVE_PRIORITY, orders.get(0).getPriority());
    assertEquals(Constants.ATTACK_MOVE_PRIORITY, orders.get(1).getPriority());
    assertEquals(Constants.ATTACK_COMBAT_PRIORITY, orders.get(2).getPriority());
    assertEquals(Constants.UPGRADE_UNITS_PRIORITY,  orders.get(3).getPriority());
    assertEquals(Constants.UPGRADE_TECH_PRIORITY,  orders.get(4).getPriority());
    assertEquals(Constants.TELEPORT_ORDER_PRIORITY, orders.get(5).getPriority());
    assertEquals(Constants.UPGRADE_RESOURCE_PRIORITY, orders.get(6).getPriority());
    assertEquals(Constants.RAID_PRIORITY, orders.get(7).getPriority());
   
    OrderCreator poc = OrderFactoryProducer.getOrderCreator("P", client);
    poc.addToOrderList(orders);
    
    Unit unit = poc.getOrderUnits(b.getRegions().get(1));
    assertEquals(7, unit.getUnits().size());
  }

  @Test
  public void test_SpyCreator() throws FileNotFoundException{
    InputStream input = new FileInputStream(new File("src/test/resources/SpyOrders.txt"));
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(input);
    // Client client = new Client(ci, td);
    ConnectionManager connect = new ConnectionManager();
    Connection c = connect.getConnection();
    Client client = new Client(ci, td, c);
    HumanPlayer    p1 = new HumanPlayer("player 1");
    HumanPlayer p2 = new HumanPlayer("player 2");
    Board b= new Board(getRegionList(p1, p2));
    client.setBoard(b);
    client.setPlayer(p1);
    OrderHelper oc = new OrderHelper(client);
    List<OrderInterface> orders = oc.createOrders();
    assertEquals(orders.size(), 4);
    assertEquals(Constants.CLOAK_PRIORITY, orders.get(0).getPriority());
    assertEquals("r1", ((CloakOrder)orders.get(0)).getDestination().getName());
    assertEquals(Constants.SPYUPGRADE_PRIORITY, orders.get(1).getPriority());
    assertEquals("r5", ((SpyUpgradeOrder)orders.get(1)).getDestination().getName());
    assertEquals(Constants.SPYUPGRADE_PRIORITY, orders.get(2).getPriority());
    assertEquals("r2", ((SpyUpgradeOrder)orders.get(2)).getDestination().getName());
    assertEquals(Constants.SPYMOVE_PRIORITY, orders.get(3).getPriority());
    assertEquals("r5", ((SpyMoveOrder)orders.get(3)).getSource().getName());
    assertEquals("r1", ((SpyMoveOrder)orders.get(3)).getDestination().getName());
  }

  private List<Region> getRegionList(AbstractPlayer p1, AbstractPlayer p2) {
    List<Integer> list1 = new ArrayList<Integer>();
    list1.add(1);
    list1.add(2);
    list1.add(3);
    list1.add(4);
    list1.add(5);
    list1.add(0);
    list1.add(0);
    Region r1 = new Region(p1, new Unit(list1));
    //Region r1 = new Region(p1, new Unit(1));
    r1.setName("r1");
    r1.setSize(1);
    r1.setFuelProduction(100);
    
    Region r2 = new Region(p1, new Unit(2));
    r2.setName("r2");
    r2.setSize(2);
    r2.setFuelProduction(100);
  
    List<Integer> list4 = new ArrayList<Integer>();
    list4.add(1);
    list4.add(10);
    list4.add(0);
    list4.add(0);
    list4.add(0);
    list4.add(0);
    list4.add(0);
    Region r4 = new Region(p1, new Unit(list4));
    r4.setName("r4");
    r4.setSize(4);
    r4.setFuelProduction(100);
    
    
    Region r5 = new Region(p1, new Unit(5));
    r5.setName("r5");
    r5.setSize(1);
    r5.setFuelProduction(100);
  
    Region r3 = new Region(p2, new Unit(3));
    r3.setName("r3");
    r3.setSize(1);
    r3.setFuelProduction(100);
  
    Region r6 = new Region(p1, new Unit(6));
    r6.setName("r6");
    r6.setSize(6);
    r1.setFuelProduction(100);

    List<Integer> list7 = new ArrayList<Integer>();
    list7.add(0);
    list7.add(2);
    list7.add(3);
    list7.add(4);
    list7.add(0);
    list7.add(0);
    list7.add(0);
     Region r7 = new Region(p1, new Unit(list7));
    r7.setName("r7");
    r7.setSize(5);
    r7.setFuelProduction(100);
    
     Region r8 = new Region(p2, new Unit(8));
    r8.setName("r8");
    r8.setSize(5);
    r8.setFuelProduction(100);
   
  

    List<Region> regions = new ArrayList<Region>();
    regions.add(r1);
    regions.add(r2);
    regions.add(r4);
    regions.add(r3);
    regions.add(r5);
    regions.add(r6);
    regions.add(r7);
    regions.add(r8);

    List<Region> adj1 = new ArrayList<Region>();

    adj1.add(r2);
    adj1.add(r3);
     adj1.add(r7);

    r1.setAdjRegions(adj1);

    List<Region> adj2 = new ArrayList<Region>();
    adj2.add(r1);
    adj2.add(r4);
    r2.setAdjRegions(adj2);

    List<Region> adj3 = new ArrayList<Region>();
    adj3.add(r1);
    adj3.add(r5);
    adj3.add(r4);
    r3.setAdjRegions(adj3);

    List<Region> adj4 = new ArrayList<Region>();
    adj4.add(r2);
    adj4.add(r6);
    adj4.add(r3);
    adj4.add(r7);

    r4.setAdjRegions(adj4);

    List<Region> adj5 = new ArrayList<Region>();
    adj5.add(r3);
    adj5.add(r6);
    r5.setAdjRegions(adj5);

    List<Region> adj6 = new ArrayList<Region>();
    adj6.add(r4);
    adj6.add(r5);
    adj6.add(r8);

    r6.setAdjRegions(adj6);

     List<Region> adj7 = new ArrayList<Region>();
    adj7.add(r4);
    adj7.add(r1);
    adj7.add(r8);
    r7.setAdjRegions(adj7);

     List<Region> adj8 = new ArrayList<Region>();
    adj8.add(r7);
    adj8.add(r6);
    r8.setAdjRegions(adj8);
    return regions;
  }

}
