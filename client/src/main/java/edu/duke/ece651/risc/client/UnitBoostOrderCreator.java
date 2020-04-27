package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

// Creator for UnitBoostOrder
public class UnitBoostOrderCreator extends OrderCreator {
  public UnitBoostOrderCreator(ClientInterface c) {
    this.client = c;
  }

  public void upgradeHelper(List<OrderInterface> orderList, String destKeyWord) {
    Region destination = promptForRegion(destKeyWord);

    while (true) {
      try {
        Unit units = getOrderUnits(destination);
        OrderInterface order = DestinationUnitOrderFactory.getOrder("upgrade unit", destination, units);
        if (order != null) {
          orderList.add(order);
          break;
        }
      } catch (NumberFormatException ne) {
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
  }

  @Override
  public void addToOrderList(List<OrderInterface> orderList) {
    upgradeHelper(orderList, "upgrade units on");
  }

}
