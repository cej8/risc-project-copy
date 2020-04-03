package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;


public class PlayerOrderCreator {
   private Client client;

  public PlayerOrderCreator(Client c){
    this.client=c;

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
}
