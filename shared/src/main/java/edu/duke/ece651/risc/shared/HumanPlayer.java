package edu.duke.ece651.risc.shared;

import java.net.Socket;

public class HumanPlayer extends AbstractPlayer {
  private Socket socket;

public Socket getSocket() {
	return socket;
}

public void setSocket(Socket socket) {
	this.socket = socket;
}
 public  boolean isWatching(){
   //TODO: implement method
   return true;
  }
  
}
