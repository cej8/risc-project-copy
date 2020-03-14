package edu.duke.ece651.risc.client;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleInput implements ClientInputInterface{
  //This class is responsible for reading user input from the console line by line.
	@Override
	public String readInput(InputStream input) {
      Scanner scanner = new Scanner(input);
      String line = scanner.nextLine();
      scanner.close();
        return line;
	}

}