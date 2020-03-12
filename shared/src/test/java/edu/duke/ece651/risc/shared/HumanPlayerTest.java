package edu.duke.ece651.risc.shared;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HumanPlayerTest {
  @Test
  public void test_PlayerCreation() {
       try{
      AbstractPlayer h = new HumanPlayer("Human1");
      ArrayList<Object> someObjects = new ArrayList<Object>();
      someObjects.add(new ArrayList<Region>());
      AbstractPlayer player1 = new HumanPlayer("Player 1", MockTests.setupMockSocket(someObjects));

       h.setPlaying(true);
      assertEquals("Human1",h.getName());
      assertEquals(true, h.isPlaying());
      //assertEquals(s, human.getSocket());
      //  assertEquals(true, h.isWatching());
         }
     catch(IOException e){
      e.printStackTrace(System.out);
   }
    
    
  }

}
