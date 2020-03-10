package edu.duke.ece651.risc.shared;

import java.io.Serializable;
import java.util.*;
import java.io.*;
import java.net.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

//@RunWith(MockitoJUnitrunner.class) 

public class BoardTest {
  /* @Test
  public void test_Board() throws IOException{
    //set up mock server
    ServerSocket mockParentServer = mock(ServerSocket.class);
    Socket mockClientSocket = mock(Socket.class);
    InputStream mockInputStream = mock(InputStream.class); 
    OutputStream mockOutputStream = mock(OutputStream.class); 
    
    //Socket socket = new Socket(mockParentServer.getInetAddress, 0);
    when(mockParentServer.accept()).thenReturn(mockClientSocket);
    // AbstractPlayer player1 = new HumanPlayer("Player 1", mockClientSocket);
    AbstractPlayer player1 = new HumanPlayer("Player 1");
  
    when(mockClientSocket.getInputStream()).thenReturn(mockInputStream);
    when(mockClientSocket.getOutputStream()).thenReturn(mockOutputStream);

    //set up units/regions/adjancies
    Unit unit = new Unit(10);
    Unit adjUnit = new Unit(15); 
    Unit adjUnit2 = new Unit(20);
    Region region = new Region(player1, unit);
    Region adjRegion = new Region(player1, adjUnit);
    Region adjRegion2 = new Region(player1, adjUnit2);
    List<Region> regions = new ArrayList<Region>();
    regions.add(region);
    regions.add(adjRegion);
    region.setAdjRegions(regions);
    adjRegion.setAdjRegions(regions);
    //create board, make sure get/set works 
    Board board = new Board(regions);
    List<Region> regions2 = new ArrayList<Region>();
    regions2.add(region);
    regions2.add(adjRegion);
    regions2.add(adjRegion2); 
    assertEquals(regions, board.getRegions());
    assertFalse(regions2 == board.getRegions());
    board.setRegions(regions2);
    assertEquals(regions2, board.getRegions());    */
  // }
}
