package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

// Factory for PlayerOrder types
public class PlayerOrderFactory extends AbstractOrderFactory{
  public static OrderInterface getOrder(String unitKeyWord, AbstractPlayer p){
    switch (unitKeyWord) {
			case "tech boost":
				order = new TechBoost(p);
				break;
		}
    return order;   
  }
  
}
