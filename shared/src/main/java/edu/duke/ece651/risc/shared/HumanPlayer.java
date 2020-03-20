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
    this.connection = new Connection();
    this.name = name;
    this.connection.setSocket(s);
    this.isPlaying = true;
    this.connection.setOutputStream(new ObjectOutputStream(s.getOutputStream()));
    this.connection.setInputStream(new ObjectInputStream(s.getInputStream()));
  }



  public HumanPlayer(String name, InputStream in, OutputStream out) throws IOException{
    this.connection = new Connection();
    this.name = name;
    this.isPlaying = true;
    this.connection.setOutputStream (new ObjectOutputStream(out));
    this.connection.setInputStream( new ObjectInputStream(in));
  }

  @Override
  public void closeAll(){
    super.closeAll();
    try{
      socket.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
}
