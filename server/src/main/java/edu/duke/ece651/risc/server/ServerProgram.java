package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

public class ServerProgram {

  public static void main(String[] args){
    int port = Constants.DEFAULT_PORT;
    System.out.println("~~~~~~");
    try{
      MasterServer ms = new MasterServer("logins", port);
      ms.run();
    }
    catch(Exception e){
      System.out.println("Failed to open");
    }
  }
}
