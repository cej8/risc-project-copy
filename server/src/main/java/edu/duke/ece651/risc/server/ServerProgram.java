package edu.duke.ece651.risc.server;

import edu.duke.ece651.risc.shared.*;

public class ServerProgram {

  public static void main(String[] args){
    int port = Constants.DEFAULT_PORT;
    while(true){
      System.out.println("~~~~~~");
      try{
        ParentServer ps = new ParentServer(port);
        //ps.setTURN_WAIT_MINUTES(10.0/60);
        //ps.setSTART_WAIT_MINUTES(15.0/60);
        //ps.setMAX_PLAYERS(3);
        ps.run();
      }
      catch(Exception e){
        System.out.println("Failed to open");
      }
    }
  }
}