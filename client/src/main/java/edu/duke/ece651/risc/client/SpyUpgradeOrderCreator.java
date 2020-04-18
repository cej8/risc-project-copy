package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.List;

public class SpyUpgradeOrderCreator extends OrderCreator {
  public SpyUpgradeOrderCreator(ClientInterface c){
    this.client = c;
  }

  public void spyUpgradeHelper(List<OrderInterface> orderList, String destKeyWord){
    Region destination = promptForRegion(destKeyWord);
    OrderInterface order = DestinationOrderFactory.getOrder("Y", destination);
    orderList.add(order);
  }
  
  @Override
  public void addToOrderList(List<OrderInterface> orderList){
    spyUpgradeHelper(orderList, "upgrade a level 0 to a spy in");
  }
}
