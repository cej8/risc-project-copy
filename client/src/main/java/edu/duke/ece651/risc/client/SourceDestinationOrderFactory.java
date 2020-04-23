package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

public class SourceDestinationOrderFactory extends AbstractOrderFactory{
  public static OrderInterface getOrder(String unitKeyWord, Region source, Region destination){
    switch(unitKeyWord){
    case "I":
      order = new RaidOrder(source destination);
      break;
    }
    return order;
  }
}
