package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class OrderHelper {
  private ClientInterface client;
  public OrderHelper(ClientInterface c) {
    this.client = c;

  }

  public boolean getOrderList(List<OrderInterface> orderList, String response) {
    response = response.toUpperCase();
    //System.out.println("Response = " + response);
    if (response.equals("D")) {
      return false;
    }
    OrderCreator oc = OrderFactoryProducer.getOrderCreator(response, client);
    if (oc == null) { // if order entered was invalid, add nothing to list
      // System.out.println("Order wasn't created");
      return true;
    }
    oc.addToOrderList(orderList);

    return true;
  }

  public List<OrderInterface> createOrders() {
    // prompt user for orders --> create list of OrderInterface --> send to server
    List<OrderInterface> orderList = new ArrayList<OrderInterface>();
    String response = null;
    boolean orderSelect = true;
    while (orderSelect) {
      // prompt user

      client.getClientOutput().displayString("You are " + client.getPlayer().getName()
          + ", what would you like to do?\n (M)ove\n (A)ttack\n (U)nit Boost\n (T)ech Boost\n T(E)leport\n (C)loak\n Sp(Y) Upgrade\n Spy Mo(V)e\n (D)one");
      response = client.getClientInput().readInput();
      orderSelect = getOrderList(orderList, response);
    }
    return orderList;
  }

}
