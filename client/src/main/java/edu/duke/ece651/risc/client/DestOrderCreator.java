package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class DestOrderCreator extends OrderCreator{
  //  private Client client;
  DestOrderFactory factory;
  public DestOrderCreator(Client c){
    this.client=c;
    this.factory = new DestOrderFactory();

    }
    public void placementOrderHelper(List<OrderInterface> placementList, String regionName,
      Region placement) {
    while (true) {
      try {
        client.getClientOutput().displayString("How many units would you like to place in " + regionName + "? (please enter a number)");
        Unit units = new Unit(Integer.parseInt(client.getClientInput().readInput()));
        OrderInterface placementOrder = factory.getOrder("placement", placement, units);
        placementList.add(placementOrder);
        break;
      } catch (NumberFormatException ne) {
        // ne.printStackTrace();
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
    // return placementList;
  }
  public List<OrderInterface> createPlacements() {
    // Prompt user for placements, create list of placementOrders, send to server
    int startUnits = Constants.UNIT_START_MULTIPLIER * client.getBoard().getNumRegionsOwned(client.getPlayer());
    client.getClientOutput().displayString("You are " + client.getPlayer().getName() + ", prepare to place " + startUnits + " units.");
    List<OrderInterface> placementList = new ArrayList<OrderInterface>();
    List<Region> regionList = client.getBoard().getRegions();
    Region placement;
    String regionName;
    for (int i = 0; i < regionList.size(); i++) {
      if (client.getPlayer().getName().equals(regionList.get(i).getOwner().getName())) {
        placement = regionList.get(i);
        regionName = regionList.get(i).getName();
        placementOrderHelper(placementList, regionName, placement);
      }
    }
    return placementList;
  }

  public void upgradeHelper(List<OrderInterface> orderList, String destKeyWord) {
    Region destination = promptForRegion(destKeyWord);

    while (true) {
      try {
        //TODO: Needs to be changed to reflect upgrade unit behvaior
         Unit units = getOrderUnits(destination);

          OrderInterface order = factory.getOrder("upgrade unit", destination, units);
        if (order != null) {
          orderList.add(order);
          break;
        }
      } catch (NumberFormatException ne) {
        // ne.printStackTrace();
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
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


}
