package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.util.List;

public class SpyMoveOrderCreator extends OrderCreator {
  public SpyMoveOrderCreator(ClientInterface c){
    this.client = c;
  }

  public void spyMoveHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord){
    Region source = promptForRegion(sourceKeyWord);
    Region destination = promptForRegion(destKeyWord);
    OrderInterface order = SourceDestinationPlayerOrderFactory.getOrder("V", source, destination, client.getPlayer());
    orderList.add(order);
  }
  
  @Override
  public void addToOrderList(List<OrderInterface> orderList){
    spyMoveHelper(orderList, "move a spy from", "move a spy to");
  }
}
