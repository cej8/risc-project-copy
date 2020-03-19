package edu.duke.ece651.risc.client;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.Test;

public class ConsoleInputTest {
  @Test
  public void test_readConsole() throws FileNotFoundException{
   
    InputStream input = new FileInputStream(new File("src/test/resources/testConsoleInput.txt"));
    ClientInputInterface i = new ConsoleInput(input);
   
    assertEquals("This is a test", i.readInput());
    assertEquals("This is a second test", i.readInput());
  
  }
}

