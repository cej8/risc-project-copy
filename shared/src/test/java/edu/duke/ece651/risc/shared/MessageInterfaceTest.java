package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageInterfaceTest {
  @Test
  public void test_Messages() {
    MessageInterface c = new ConfirmationMessage(true);
    MessageInterface s = new StringMessage("test");
    assertEquals(true, c.unpacker());
    assertEquals("test", s.unpacker());
    

  }

}
