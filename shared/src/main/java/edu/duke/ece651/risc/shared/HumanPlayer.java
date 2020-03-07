package edu.duke.ece651.risc.shared;

import java.net.*;
import java.io.*;

public class HumanPlayer extends AbstractPlayer {
  private Socket socket = null;
  private static final long serialVersionUID = 6L;

  public HumanPlayer(){

  }
  public HumanPlayer(String name){//testing construcotr for tests that do no dpend on socket connection
    this.isPlaying = true;
    this.name = name;
  }
  public HumanPlayer(String name, Socket s) throws IOException{
    this.name = name;
    this.socket = s;
    this.isPlaying = true;
    this.outputStream = new ObjectOutputStream(s.getOutputStream());
    this.inputStream = new ObjectInputStream(s.getInputStream());
  }
public Socket getSocket() {
	return socket;
}

public void setSocket(Socket socket) throws IOException{
  if(this.socket != null){ closeAll(); }
	this.socket = socket;
  this.outputStream = new ObjectOutputStream(socket.getOutputStream());
  this.inputStream = new ObjectInputStream(socket.getInputStream());
}
 public  boolean isWatching(){
   //TODO: implement method
   return true;
 }

  public void closeAll(){
    super.closeAll();
    try{
      socket.close();
    }
    catch(IOException e){
      e.printStackTrace(System.out);
    }
  }
  
}
