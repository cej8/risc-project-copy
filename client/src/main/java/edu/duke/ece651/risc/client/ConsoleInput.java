package edu.duke.ece651.risc.client;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleInput implements ClientInputInterface{
  //This class is responsible for reading user input from the console line by line.
  Scanner input;

  public ConsoleInput(){
    this.input = new Scanner(System.in);
  }
  
  public ConsoleInput(InputStream input){
    this.input = new Scanner(input);
  }
  
  @Override
  public String readInput() {
    return input.nextLine();
  }

  @Override
  public void close(){
    input.close();
  }

}
