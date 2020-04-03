package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class SourceDestOrderFactory {
  public static OrderInterface getOrder(String unitKeyWord, Region source, Region destination, Unit units){
   OrderInterface order = null;
    switch (unitKeyWord) {
    case "move":
      order = new MoveOrder(source, destination, units);
      break;
     case "attack move":
      order = new AttackMove(source, destination, units);
      break;
       case "attack combat":
      order = new AttackCombat(source, destination, units);
      break;
  
    default: //TODO: what would this be?
      break;
    }
    return order;   
  }

}
