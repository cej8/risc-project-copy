package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

//top level class for executing client
//Again mostly legacy but allows you to input address/port then use console client
public class ClientProgram {
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
    
    ConnectionManager makeConnection = new ConnectionManager(addr,port);
    makeConnection.connectGame();
    Connection connection = makeConnection.getConnection();
    ClientLogin login = new ClientLogin(connection, clientInput, clientOutput);
    Client client = new Client(clientInput, clientOutput, connection, login.Login());
    client.playGame();
  }
}
