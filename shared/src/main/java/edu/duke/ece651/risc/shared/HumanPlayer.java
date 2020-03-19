package edu.duke.ece651.risc.shared;

import java.net.*;
import java.io.*;

public class HumanPlayer extends AbstractPlayer {
  private static final long serialVersionUID = 6L;
  // private Socket socket=null;
  public HumanPlayer(){
    
  }

  public HumanPlayer(String name) {//testing construcotr for tests that do no dpend on socket connection
    this.isPlaying = true;
    this.name = name;
  }
  
public HumanPlayer(String name, Socket s) throws IOException{
    //SHOULD BE DEPRECATED to decouple socket from inputstream and outputstream
    this.name = name;
    this.connection = new Connection(s);
    this.connection.getStreamsFromSocket();
    this.isPlaying = true;
  }



  public HumanPlayer(String name, InputStream in, OutputStream out) throws IOException{
    this.name = name;
    this.isPlaying = true;
    this.connection = new Connection(new ObjectInputStream(in), new ObjectOutputStream(out));
  }
  
}
