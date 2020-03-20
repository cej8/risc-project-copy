package edu.duke.ece651.risc.client;

//this interface is used to take in user input from any input stream
//it will return a single string line to be read and handled
public interface ClientInputInterface {
  public String readInput();
  public void close();
}
