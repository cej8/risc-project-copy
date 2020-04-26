package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

// Creator for AttackCombat + AttackMove sister orders
public class AttackOrderCreator extends OrderCreator{
 public AttackOrderCreator(ClientInterface c){
    this.client=c;
  }

  public void attackHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord) {
    Region source = promptForRegion(sourceKeyWord);
    Region destination = promptForRegion(destKeyWord);
    while (true) {
      try {
        Unit units = getOrderUnits(source);
        OrderInterface order1 =SourceDestinationUnitOrderFactory.getOrder("attack move", source, destination, units);
        if (order1 != null) { //should not ever be null -- would have gone to default in switch case instead of A
          OrderInterface order2=SourceDestinationUnitOrderFactory.getOrder("attack combat", source, destination, (Unit)DeepCopy.deepCopy(units));
          if (order2 != null) {
            orderList.add(order1);
            orderList.add(order2);
            break;
          }
        }
      } catch (NumberFormatException ne) {
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
  }

  @Override
  public void addToOrderList(List<OrderInterface> orderList) {
    attackHelper(orderList, "attack from", "attack");
  }

}
