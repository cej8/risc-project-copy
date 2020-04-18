package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.List;

import edu.duke.ece651.risc.shared.OrderInterface;
// Creates teleport order based on "teleport" keyword
public class TeleportOrderCreator extends OrderCreator {
  public TeleportOrderCreator(ClientInterface c){
    this.client = c;
  }
  public void teleportHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord) {
    Region source = promptForRegion(sourceKeyWord);
    Region destination = promptForRegion(sourceKeyWord);

    while (true) {
      try {
        Unit units = getOrderUnits(source);

        OrderInterface order = SourceDestinationUnitOrderFactory.getOrder("teleport", source, destination, units);
        if (order != null) { //should not ever be null -- would have gone to defaul in switch case instead of M
          orderList.add(order);
          break;
        }
      } catch (NumberFormatException ne) {
        // ne.printStackTrace();
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
  }

  @Override
  public void addToOrderList(List<OrderInterface> orderList) {
    teleportHelper(orderList, "teleport from", "teleport to");
  }
}
