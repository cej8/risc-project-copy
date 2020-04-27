package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

// Creator for TechBoostOrder
public class TechBoostOrderCreator extends OrderCreator{
  PlayerOrderFactory factory;

  public TechBoostOrderCreator(ClientInterface c){
    this.client=c;
    factory = new PlayerOrderFactory();
  }
 
  public void techBoostHelper(List<OrderInterface> orderList) {
    while (true) {
      OrderInterface order = PlayerOrderFactory.getOrder("tech boost", client.getPlayer());
      if (order != null) {
        orderList.add(order);
        break;
      }
    }
  }

  @Override
  public void addToOrderList(List<OrderInterface> orderList) {
    techBoostHelper(orderList);	
  }
  
}
