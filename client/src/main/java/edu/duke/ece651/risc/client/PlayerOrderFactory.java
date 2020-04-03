package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class PlayerOrderFactory {
  public static OrderInterface getOrder(String unitKeyWord, AbstractPlayer p){
   OrderInterface order = null;
    switch (unitKeyWord) {
  
    case "tech boost":
      order = new TechBoost(p);
      break;
     default: 
      break;
    }
    return order;   
  }
}
