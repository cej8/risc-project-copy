package edu.duke.ece651.risc.server;

public class ServerProgram {
  //top level wrapper class for running server
  public static void main(String[]args){
    System.out.println("Server is now running...");
    ParentServer ps = new ParentServer();
    ps.run();

  }
}
