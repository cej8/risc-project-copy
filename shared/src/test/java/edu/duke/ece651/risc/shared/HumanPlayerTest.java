package edu.duke.ece651.risc.shared;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HumanPlayerTest {
  @Test
  public void test_PlayerCreation() {
    Socket s = new Socket();
    HumanPlayer h = new HumanPlayer();
    h.setName("Human1");
    h.setPlaying(true);
    h.setSocket(s);
    assertEquals("Human1",h.getName());
    assertEquals(true, h.isPlaying());
    assertEquals(s, h.getSocket());
    assertEquals(true, h.isWatching());
    
  }

}
