package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

//Interface for the output displayed on the client's screen
public interface ClientOutputInterface {
  public void displayString(String str);
  public void displayBoard(Board b);
}
