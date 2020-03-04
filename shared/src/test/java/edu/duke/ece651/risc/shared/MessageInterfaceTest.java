package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageInterfaceTest {
  @Test
  public void test_Messages() {
    ConfirmationMessage c = new ConfirmationMessage(true);
    StringMessage s = new StringMessage("test");
    assertEquals(true, c.getMessage());
    assertEquals("test", s.getMessage());
    

  }

}
