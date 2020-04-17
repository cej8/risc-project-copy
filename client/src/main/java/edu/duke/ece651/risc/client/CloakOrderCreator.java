package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.util.List;

public class CloakOrderCreator extends OrderCreator {
  public CloakOrderCreator(ClientInterface c){
    this.client = c;
  }

  public void cloakHelper(List<OrderInterface> orderList, String destKeyWord){
    Region destination = promptForRegion(destKeyWord);
    OrderInterface order = DestinationOrderFactory.getOrder("C", destination);
  }
  
  @Override
  public void addToOrderList(List<OrderInterface> orderList){
    cloakHelper(orderList, "cloak");
  }
}
