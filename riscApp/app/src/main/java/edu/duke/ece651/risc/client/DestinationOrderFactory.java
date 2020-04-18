package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

public class DestinationOrderFactory extends AbstractOrderFactory{
  public static OrderInterface getOrder(String unitKeyWord, Region destination){
    switch(unitKeyWord){
    case "C":
      order = new CloakOrder(destination);
      break;
    case "Y":
      order = new SpyUpgradeOrder(destination);
      break;
    }
    return order;
  }
}
