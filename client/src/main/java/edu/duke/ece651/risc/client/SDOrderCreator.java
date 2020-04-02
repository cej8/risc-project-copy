package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

// S(ource)D(estination)OrderCreator abstracted class from Client.java to make orders more extensible and manageable as code grows. Class handles all orders associated with game

public class SDOrderCreator {
  private Client client;

  public SDOrderCreator(Client c) {
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
  // public List<OrderInterface> moveAttackHelper(List<OrderInterface> orderList,
  // String sourceKeyWord, String destKeyWord,
  // String unitKeyWord) {
  // Region source = null;
  // Region destination = null;
  // while (source == null) {
  // client.getClientOutput().displayString("What region do you want to " +
  // sourceKeyWord + " from? (please type a region name, i.e. 'A')");
  // source = orderHelper(client.getClientInput().readInput());
  // }
  // while (destination == null) {
  // client.getClientOutput().displayString("What region do you want to " +
  // destKeyWord + "? (please type a region name, i.e. 'A')");
  // destination = orderHelper(client.getClientInput().readInput());
  // }
  // while (true) {
  // try {
  // // client.getClientOutput().displayString("How many units do you want to " +
  // unitKeyWord + "?");
  // // Unit units = new
  // Unit(Integer.parseInt(client.getClientInput().readInput()));
  // Unit units = getOrderUnits(source);
  // OrderInterface order = OrderFactory.getOrder(unitKeyWord, source,
  // destination, units);
  // if (order != null) {
  // orderList.add(order);
  // break;
  // }
  // } catch (NumberFormatException ne) {
  // // ne.printStackTrace();
  // client.getClientOutput().displayString("That was not an integer, please try
  // again.");
  // }
  // }
  // return orderList;
  // }
  public void moveHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord) {
    Region source = promptForRegion(sourceKeyWord);
    Region destination = promptForRegion(sourceKeyWord);

    while (true) {
      try {
        Unit units = getOrderUnits(source);

        OrderInterface order = OrderFactory.getOrder("move", source, destination, units);
        if (order != null) {
          orderList.add(order);
          break;
        }
      } catch (NumberFormatException ne) {
        // ne.printStackTrace();
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
    // return orderList;
  }

  private Region promptForRegion(String keyWord) {
    Region r = null;
    while (r == null) {
      client.getClientOutput()
          .displayString("What region do you want to " + keyWord + "  (please type a region name, i.e. 'A')");
      r = orderHelper(client.getClientInput().readInput());
    }
    return r;
  }

  public void attackHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord) {
    Region source = promptForRegion(sourceKeyWord);
    Region destination = promptForRegion(destKeyWord);
    while (true) {
      try {
        Unit units = getOrderUnits(source);
        OrderInterface order1 = OrderFactory.getOrder("attack move", source, destination, units);
        if (order1 != null) {
          OrderInterface order2 = OrderFactory.getOrder("attack combat", source, destination, units);
          if (order2 != null) {
            orderList.add(order1);
            orderList.add(order2);
            break;
          }
        }
      } catch (NumberFormatException ne) {
        // ne.printStackTrace();
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
    // return orderList;
  }

  // TODO -- WIP commented for testing
  public Unit getOrderUnits(Region source) {
    // Unit sourceUnits = (Unit) DeepCopy.deepCopy(source.getUnits().getUnits());
    // List<Integer> orderUnits = new ArrayList<Integer>();
    // int i = 0;
    // while (i < sourceUnits.getUnits().size()) {
    // if (sourceUnits.getUnits().get(i) > 0) {
    // client.getClientOutput().displayString("How many " +
    // sourceUnits.getTypeFromTech(i) + " units ("
    // + sourceUnits.getUnits() + ") do you want to select?");
    // // get number from user
    // Integer input = Integer.parseInt(client.getClientInput().readInput());
    // // if 0 > and < numOrType
    // if ((input >= 0) && (input < source.getUnits().getTotalUnits())) {
    // orderUnits.add(input);
    // i++;
    // } else {
    // client.getClientOutput().displayString("Invalid input (" + input + ") please
    // try again");
    // }
    // } else {
    // i++;
    // }
    // }
    // TODO:fix temp return value return new Unit(orderUnits);
    return new Unit(3);
  }

  public boolean getOrderList(List<OrderInterface> orderList, String response) {
    switch (response.toUpperCase()) {
      case "D":
        return false;
      case "M":
        // orderList = moveAttackHelper(orderList, "move units", "move units to",
        // "move");
        moveHelper(orderList, "move units from", "move units to");
        client.getClientOutput().displayString("You made a Move order, what else would you like to do?");
        break;
      case "A":
        // orderList = moveAttackHelper(orderList, "attack from", "attack", "attack");
        attackHelper(orderList, "attack from", "attack");
        client.getClientOutput().displayString("You made an Attack order, what else would you like to do?");
        break;
      // case "U":
      // TODO: upgrade
      // break;
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
      client.getClientOutput().displayString("You are " + client.getPlayer().getName()
          + ", what would you like to do?\n (M)ove\n (A)ttack\n (D)one\n (U)pgrade");
      response = client.getClientInput().readInput();
      orderSelect = getOrderList(orderList, response);
    }
    return orderList;
  }

}
