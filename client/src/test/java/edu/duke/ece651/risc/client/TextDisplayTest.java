package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.*;
import java.net.*;
import java.io.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TextDisplayTest {
  @Test
  public void test_TestDisplayTest() throws IOException{
    //setup Player
    InputStream mockInputStream = mock(InputStream.class); 
    OutputStream mockOutputStream = mock(OutputStream.class); 

    Socket mockClientSocket1 = mock(Socket.class);
    // AbstractPlayer player1 = new HumanPlayer("Player 1", mockClientSocket1);
    AbstractPlayer player1 = new HumanPlayer("Player 1");
    
    Socket mockClientSocket2 = mock(Socket.class);
    // AbstractPlayer player2 = new HumanPlayer("Player 2", mockClientSocket2);
    AbstractPlayer player2 = new HumanPlayer("Player 2");
   
    Socket mockClientSocket3 = mock(Socket.class);
    // AbstractPlayer player3 = new HumanPlayer("Player 3", mockClientSocket3);
    AbstractPlayer player3 = new HumanPlayer("Player 3");

      when(mockClientSocket1.getInputStream()).thenReturn(mockInputStream);
    when(mockClientSocket1.getOutputStream()).thenReturn(mockOutputStream);
      when(mockClientSocket2.getInputStream()).thenReturn(mockInputStream);
    when(mockClientSocket2.getOutputStream()).thenReturn(mockOutputStream);
      when(mockClientSocket3.getInputStream()).thenReturn(mockInputStream);
    when(mockClientSocket3.getOutputStream()).thenReturn(mockOutputStream);
    //set up units/regions/adjancies
    Unit unit1 = new Unit(10);
    Unit unit2 = new Unit(15); 
    Unit unit3 = new Unit(20);

    Region region1 = new Region(player1, unit1);
    region1.setName("A");
    Region region2 = new Region(player2, unit2);
    region2.setName("B");
    Region region3 = new Region(player3, unit3);
    region3.setName("C");
    //Region region3A = new Region(player3, unit3);
    //region3A.setName("D");

    List<Region> allRegions = new ArrayList<Region>();
    List<Region> adjRegions1 = new ArrayList<Region>();
    List<Region> adjRegions2 = new ArrayList<Region>();
    List<Region> adjRegions3 = new ArrayList<Region>();

    adjRegions1.add(region2);
    adjRegions1.add(region3);
    //adjRegions1.add(region3A);
    region1.setAdjRegions(adjRegions1);
    adjRegions2.add(region1);
    adjRegions2.add(region3);
    //adjRegions2.add(region3A);
    region2.setAdjRegions(adjRegions2);
    adjRegions3.add(region1);
    adjRegions3.add(region2);
    region3.setAdjRegions(adjRegions3);
    allRegions.add(region1);
    allRegions.add(region2);
    //allRegions.add(region3A);
    allRegions.add(region3);
    
    //create board, make sure get/set works 
    Board board = new Board(allRegions);
    ClientOutputInterface coi = new TextDisplay(); 
    coi.displayBoard(board);
    coi.displayString("Test string");
  }

    

}
