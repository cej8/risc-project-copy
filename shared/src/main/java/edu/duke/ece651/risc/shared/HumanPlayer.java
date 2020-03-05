package edu.duke.ece651.risc.shared;

import java.net.Socket;

public class HumanPlayer extends AbstractPlayer {
  private Socket socket;
  private static final long serialVersionUID = 6L;

  public HumanPlayer(String name, Socket s){
    this.name= name;
    this.socket=s;
    this.isPlaying =true;
  }
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
