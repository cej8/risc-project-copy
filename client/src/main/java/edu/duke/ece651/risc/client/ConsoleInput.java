package edu.duke.ece651.risc.client;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleInput implements ClientInputInterface{
  //This class is responsible for reading user input from the console line by line.
  private Scanner scanner;
  public ConsoleInput(InputStream input){
    scanner = new Scanner(input);
  }
	@Override
	public String readInput() {
    // Scanner scanner = new Scanner(input);
      String line = scanner.nextLine();
       return line;
	}

}

