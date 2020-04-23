package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.List;

public class RaidOrderCreator extends OrderCreator {
  public RaidOrderCreator(ClientInterface c){
    this.client = c;
  }

  public void raidHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord){
    Region source = promptForRegion(sourceKeyWord);
    Region destination = promptForRegion(destKeyWord);
    OrderInterface order = SourceDestinationOrderFactory.getOrder("I", destination);
    orderList.add(order);
  }
  
  @Override
  public void addToOrderList(List<OrderInterface> orderList){
    raidHelper(orderList, "start a raid from", "raid");
  }
}
