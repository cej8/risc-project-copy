package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class SDOrderCreator {
  private Client client;

  public SDOrderCreator(Client c){
    this.client = c;
  }
  
 public Region orderHelper(String response) {
   List<Region> regionList = client.getBoard().getRegions();
    for (int i = 0; i < regionList.size(); i++) {
      if (response.equals(regionList.get(i).getName())) {
        return regionList.get(i);
      }
    }
    client.getClientOutput().displayString("Region does not exist.");
    return null;
  }

  // Helper method to create MoveOrder or AttackOrder
  public List<OrderInterface> moveAttackHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord,
      String unitKeyWord) {
    Region source = null;
    Region destination = null;
    while (source == null) {
      client.getClientOutput().displayString("What region do you want to " + sourceKeyWord + " from? (please type a region name, i.e. 'A')");
      source = orderHelper(client.getClientInput().readInput());
    }
    while (destination == null) {
      client.getClientOutput().displayString("What region do you want to " + destKeyWord + "? (please type a region name, i.e. 'A')");
      destination = orderHelper(client.getClientInput().readInput());
    }
    while (true) {
      try {
        client.getClientOutput().displayString("How many units do you want to " + unitKeyWord + "?");
        Unit units = new Unit(Integer.parseInt(client.getClientInput().readInput()));
        OrderInterface order = OrderFactory.getOrder(unitKeyWord, source, destination, units);
        if (order != null) {
          orderList.add(order);
          break;
        }
      } catch (NumberFormatException ne) {
        // ne.printStackTrace();
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
    return orderList;
  }

  public boolean getOrderList(List<OrderInterface> orderList, boolean orderSelect, String response) {
    switch(response.toUpperCase()){
    case "D":
        return false;
    case "M":
      orderList = moveAttackHelper(orderList, "move units", "move units to", "move");
      client.getClientOutput().displayString("You made a Move order, what else would you like to do?");
      break;
    case "A":
        orderList = moveAttackHelper(orderList, "attack from", "attack", "attack");
        client.getClientOutput().displayString("You made an Attack order, what else would you like to do?");
        break;
    case "U":
      //TODO: upgrade
      break;
    default:
      client.getClientOutput().displayString("Please select either M, A, U, or D");
        break;
    }
    return true;
  }
  
  public List<OrderInterface> createOrders() {
    // prompt user for orders --> create list of OrderInterface --> send to server
    List<OrderInterface> orderList = new ArrayList<OrderInterface>();
    String response = null;
    boolean orderSelect = true;
    while (orderSelect) {
      // prompt user
      client.getClientOutput()
        .displayString("You are " + client.getPlayer().getName() + ", what would you like to do?\n (M)ove\n (A)ttack\n (D)one");
      response = client.getClientInput().readInput();
      orderSelect = getOrderList(orderList, orderSelect, response);
    }
    return orderList;
  }

}
