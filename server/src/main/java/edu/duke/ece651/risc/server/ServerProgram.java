package edu.duke.ece651.risc.server;

public class ServerProgram {
  //top level wrapper class for running server
  void main(){
    ParentServer ps = new ParentServer();
    ps.run();

  }
}
