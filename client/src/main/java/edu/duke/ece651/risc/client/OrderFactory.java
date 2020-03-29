package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class OrderFactory {
  public static OrderInterface getOrder(String unitKeyWord, Region source, Region destination, Unit units){
   OrderInterface order = null;
    switch (unitKeyWord) {
    case "move":
      order = new MoveOrder(source, destination, units);
      break;
     case "attack":
      order = new AttackOrder(source, destination, units);
      break;
    case "upgrade":
      // TODO: something to the effect of order = new UpgradeOrder(source);
      break;
    default: //TODO: what would this be?
      break;
    }
    return order;   
  }


}
