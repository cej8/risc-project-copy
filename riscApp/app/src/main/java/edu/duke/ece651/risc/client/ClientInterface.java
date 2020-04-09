package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;

// Client Interface - program to interface, not class
public interface ClientInterface {
  ClientOutputInterface getClientOutput();
  ClientInputInterface getClientInput();
  Board getBoard();
  AbstractPlayer getPlayer();
}
