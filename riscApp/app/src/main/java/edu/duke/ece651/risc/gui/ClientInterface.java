package edu.duke.ece651.risc.gui;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;

public interface ClientInterface {
    ClientOutputInterface getClientOutput();
    ClientInputInterface getClientInput();
    Board getBoard();
    AbstractPlayer getPlayer();
}
