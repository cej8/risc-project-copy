package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class MoveOrderCreator extends OrderCreator {
  public MoveOrderCreator(ClientInterface c) {
    this.client = c;
  }

  public void moveHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord) {
    Region source = promptForRegion(sourceKeyWord);
    Region destination = promptForRegion(sourceKeyWord);

    while (true) {
      try {
        Unit units = getOrderUnits(source);

        OrderInterface order = SourceDestOrderFactory.getOrder("move", source, destination, units);
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
    // TODO Auto-generated method stub
    moveHelper(orderList, "move units from", "move untis to");

  }

}
