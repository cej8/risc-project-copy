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
     case "attack move":
      order = new AttackMove(source, destination, units);
      break;
       case "attack combat":
      order = new AttackCombat(source, destination, units);
      break;
  
    case "upgrade tech":
      order = new TechBoost(null);//TODO: how to add player?
      break;
    case "upgrade unit":
      order = new UnitBoost(destination, units);
        break;
    default: //TODO: what would this be?
      break;
    }
    return order;   
  }


}
