package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;


public class TechBoostOrderCreator extends OrderCreator{
  //  private Client client;
  PlayerOrderFactory factory;
  public TechBoostOrderCreator(Client c){
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
