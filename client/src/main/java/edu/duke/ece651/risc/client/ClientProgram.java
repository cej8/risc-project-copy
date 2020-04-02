package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

public class ClientProgram {
  //top level class for executing client
  public static void main(String[] args){

    ClientInputInterface clientInput = new ConsoleInput();
    ClientOutputInterface clientOutput = new TextDisplay();

    clientOutput.displayString("Address?");
    String addr = clientInput.readInput();
    clientOutput.displayString("Port?");
    String portS = clientInput.readInput();

    int port;
    try{
      port = Integer.parseInt(portS);
    }
    catch(NumberFormatException ne){
      clientOutput.displayString("Port invalid");
      return;
    }
    
    
    Client client = new Client(clientInput, clientOutput);
    //client.setSTART_WAIT_MINUTES(10.0/60);
    //client.setTURN_WAIT_MINUTES(15.0/60);
    
    //client.makeConnection(addr, port);
    client.playGame();
  }
}
