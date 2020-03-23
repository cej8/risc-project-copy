package edu.duke.ece651.risc.client;

public class ClientProgram {
  //top level class for executing client
  public static void main(String[]args){
    System.out.println("Now running client of RISC Game......");
    Client client = new Client();
    client.playGame();
  }
}
