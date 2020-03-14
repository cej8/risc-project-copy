package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.*;
import java.io.*;
import java.net.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ClientTest {
@Test
  public void test_updateBoard() throws IOException {
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    Board board = getTestBoard(player1);
    ArrayList<Object> inputs = new ArrayList<Object>();
    inputs.add(board);
    Socket mockSocket = MockTests.setupMockSocket(inputs);
    Client client = new Client();
    client.makeConnection(mockSocket);
    

    //System.out.println("Connected to " + client.getSocket().getLocalAddress() + ":" +  client.getSocket().getLocalPort());
    try {
     
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return;
    } finally {
      try {
        //client.closeAll();
      } catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }

    
  }

  private Board getTestBoard(AbstractPlayer player1) {
    Unit unit = new Unit(10);
    Unit adjUnit = new Unit(15);
    Unit adjUnit2 = new Unit(20);
    Region region = new Region(player1, unit);
    Region adjRegion = new Region(player1, adjUnit);
    Region adjRegion2 = new Region(player1, adjUnit2);
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


 //@Test
  public void test_ClientSimple() {
    Client client = new Client();
    client.makeConnection("localhost", 12345);
    StringMessage message;
    try {
      message = (StringMessage) (client.receiveObject());
      System.out.println(message.getMessage());
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return;
    } finally {
      try {
        client.closeAll();
      } catch (Exception e) {
        e.printStackTrace(System.out);
      }
    }
  }


}
