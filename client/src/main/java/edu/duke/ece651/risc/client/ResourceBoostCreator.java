package edu.duke.ece651.risc.client;

import java.util.List;

import edu.duke.ece651.risc.shared.*;

public class ResourceBoostCreator extends OrderCreator {
  public ResourceBoostCreator(ClientInterface c) {
    this.client = c;
  }
   public void boostHelper(List<OrderInterface> orderList,String destKeyWord) {
    Region destination = promptForRegion(destKeyWord);

    while (true) {
      try {
        OrderInterface order = DestinationUnitOrderFactory.getOrder("resource boost",destination, new Unit(0));//pass in 0 for units to method
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
  boostHelper(orderList, "upgrade resources on");	
}

}
