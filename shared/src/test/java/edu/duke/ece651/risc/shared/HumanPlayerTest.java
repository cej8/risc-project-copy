package edu.duke.ece651.risc.shared;

import java.net.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HumanPlayerTest {
  @Test
  public void test_PlayerCreation() {
    Socket s = new Socket();
    //   HumanPlayer human = new HumanPlayer();
    //   try{
      AbstractPlayer h = new HumanPlayer("Human1");
       h.setName("Human1");
       h.setPlaying(true);
       //    human.setSocket(s);
      assertEquals("Human1",h.getName());
      assertEquals(true, h.isPlaying());
      // assertEquals(s, human .getSocket());
      //  assertEquals(true, h.isWatching());
      //   }
    // catch(IOException e){
    //   e.printStackTrace(System.out);
    // }
    
    
  }

}
