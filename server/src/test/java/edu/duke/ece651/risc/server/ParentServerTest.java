package edu.duke.ece651.risc.server;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.PlacementOrder;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.Unit;
public class ParentServerTest {
  /*@Test
  public void test_ServerSimple() {
    /*    ParentServer ps = new ParentServer();
    try{
      System.out.println("Waiting for connection");
      ps.waitingForConnections();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
      return;
    }
    ChildServer child = ps.getChildren().get(0);
    StringMessage message = new StringMessage("I am testing networking");
    try{
      child.getPlayer().sendObject(message);
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
    ps.closeAll();
    }*/
  /* @Test
  public void test_createStartingGroups(){
    ParentServer ps = new ParentServer();
    HumanPlayer player = new HumanPlayer();
    player.setName("Player One");
    HumanPlayer playerTwo = new HumanPlayer();
    playerTwo.setName("Player Two");
    ChildServer cs = new ChildServer(player,ps);
    ChildServer csTwo = new ChildServer(playerTwo, ps);
    //ps.addPlayer(cs);
    //ps.addPlayer(csTwo);
    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    for (int i = 0; i < regions.size(); i++){
      System.out.println(i + ": " + regions.get(i).getOwner().getName() + " " + regions.get(i).getUnits().getUnits());
    }
    }*/
  @Test
  public void test_startingGroups(){
    ParentServer ps = new ParentServer();
    //ps.createBoard();
    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    List<Region> adjRegion;
    for (int i = 0; i < regions.size(); i++){
      adjRegion = regions.get(i).getAdjRegions();
      System.out.println(regions.get(i).getName() + ": Group Name: " + regions.get(i).getOwner().getName() + " Units: " + regions.get(i).getUnits().getUnits());
      System.out.print("Adj Regions: ");
      for (int j = 0; j < adjRegion.size(); j++) {
        System.out.print(adjRegion.get(j).getName() + " ");
      }
      System.out.println();
    }
  }
   @Test
  public void test_placementOrder() {
    ParentServer ps = new ParentServer();
    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    for(int i = 0; i < regions.size(); i++) {
      OrderInterface placement = new PlacementOrder(regions.get(i),new Unit(i + 5));
      placement.doAction(board);
      System.out.println("Placement number: " + (i + 5));
    }
    assertEquals(5,regions.get(0).getUnits().getUnits());
    assertEquals(6,regions.get(1).getUnits().getUnits());
    assertEquals(7,regions.get(2).getUnits().getUnits());
    assertEquals(8,regions.get(3).getUnits().getUnits());
    assertEquals(9,regions.get(4).getUnits().getUnits());
    assertEquals(10,regions.get(5).getUnits().getUnits());
    assertEquals(11,regions.get(6).getUnits().getUnits());
    assertEquals(12,regions.get(7).getUnits().getUnits());
    assertEquals(13,regions.get(8).getUnits().getUnits());
    assertEquals(14,regions.get(9).getUnits().getUnits());
    assertEquals(15,regions.get(10).getUnits().getUnits());
    assertEquals(16,regions.get(11).getUnits().getUnits());
  }

}
