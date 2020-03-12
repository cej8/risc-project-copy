package edu.duke.ece651.risc.shared;

import java.net.*;
import java.io.*;

public class HumanPlayer extends AbstractPlayer {
  private static final long serialVersionUID = 6L;

  public HumanPlayer(){

  }

  public HumanPlayer(String name) {//testing construcotr for tests that do no dpend on socket connection
    this.isPlaying = true;
    this.name = name;
  }

  public HumanPlayer(String name, InputStream in, OutputStream out) throws IOException{
    this.name = name;
    this.isPlaying = true;
    this.outputStream = new ObjectOutputStream(out);
    this.inputStream = new ObjectInputStream(in);
  }


  public  boolean isWatching(){
   //TODO: implement method
   return true;
 }

  
}
