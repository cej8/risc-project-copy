package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class DestOrderFactory extends AbstractOrderFactory{
  public static OrderInterface getOrder(String unitKeyWord, Region destination, Unit units){
    // OrderInterface order = null;
    switch (unitKeyWord) {
  
    case "placement":
      order = new PlacementOrder(destination, units);
      break;
    case "upgrade unit":
      order = new UnitBoost(destination, units);
        break;
  
    }
    return order;   
  }

}
