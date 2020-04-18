package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

public class SourceDestinationPlayerOrderFactory extends AbstractOrderFactory {
  public static OrderInterface getOrder(String unitKeyWord, Region source, Region destination, AbstractPlayer player){
    switch(unitKeyWord){
    case "V":
      order = new SpyMoveOrder(source, destination, player);
      break;
    }
    return order;
  }

}
