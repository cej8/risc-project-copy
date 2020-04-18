package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.util.Set;

//Interface for the output displayed on the client's screen
public interface ClientOutputInterface {

  public void displayBoard(Board b);
  public void displayString(String str);
  public void displayBoard(Board b, String playerName, Set<String> visible);
}
