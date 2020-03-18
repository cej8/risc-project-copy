package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.*;
import java.io.*;
import java.net.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ClientTest {
  /*@Test
  public void test_updateBoard() throws IOException {
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    Board board = getTestBoard(player1);
    ArrayList<Object> inputs = new ArrayList<Object>();
    inputs.add(board);
    Socket mockSocket = MockTests.setupMockSocket(inputs);
    Client client = new Client();
    client.makeConnection(mockSocket);
    try {
      assertEquals(null, client.getBoard().getRegions());
      client.updateClientBoard();
      //make sure region list same size
      assertEquals(board.getRegions().size(), client.getBoard().getRegions().size());
      //test to make sure region added all regions by name
      Set<String> regionNames = new HashSet<String>();
      for (Region r : board.getRegions()){
        regionNames.add(r.getName());
      }
      for (Region r : client.getBoard().getRegions()){
        assertTrue(regionNames.contains(r.getName()));
      }

    } catch (Exception e) { //TODO -- test exception handling
      e.printStackTrace(System.out);
      return;
    } finally {
      try {
        //client.closeAll(); //TODO - mocking closing the connection
      } catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }

    
  }*/
  @Test
  public void test_placement(){
    AbstractPlayer player1 = new HumanPlayer("player1");
    Board board = getTestBoard(player1);
    Client client = new Client(board);
    client.createPlacements();
  }
  private Board getTestBoard(AbstractPlayer player1) {
    Unit unit = new Unit(10);
    Unit adjUnit = new Unit(15);
    Unit adjUnit2 = new Unit(20);
    Region region = new Region(player1, unit);
    region.setName("Earth");
    Region adjRegion = new Region(player1, adjUnit);
    adjRegion.setName("Mars");
    Region adjRegion2 = new Region(player1, adjUnit2);
    adjRegion2.setName("Pluto");
    List<Region> regions = new ArrayList<Region>();
    regions.add(region);
    regions.add(adjRegion);
    regions.add(adjRegion2);
    region.setAdjRegions(regions);
    adjRegion.setAdjRegions(regions);
    //create board, make sure get/set works 
    Board board = new Board(regions);
    return board;
  }

  /* @Test
  public void test_ClientSimple() throws IOException{
    Client client = new Client();
    ArrayList<Object> objs = new ArrayList<Object>();
    StringMessage message = new StringMessage("test sending string message");
    objs.add(message);
    Socket mockSocket = MockTests.setupMockSocket(objs);
    //    client.makeConnection("localhost", 12345);
    client.makeConnection(mockSocket);
  
    try {
      message = (StringMessage) (client.receiveObject());
      System.out.println(message.getMessage());
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return;
    } finally {
      try {
        // client.closeAll(); //TODO -- mocking closeAll
      } catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }
  }*/


}
