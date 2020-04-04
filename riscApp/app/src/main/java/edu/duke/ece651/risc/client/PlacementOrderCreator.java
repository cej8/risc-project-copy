package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.gui.ClientInterface;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;


public class PlacementOrderCreator extends OrderCreator {
  public PlacementOrderCreator(ClientInterface c){
    this.client = c;
  }
 
    public void placementOrderHelper(List<OrderInterface> placementList, String regionName,
      Region placement) {
    while (true) {
      try {
        client.getClientOutput().displayString("How many units would you like to place in " + regionName + "? (please enter a number)");
        Unit units = new Unit(Integer.parseInt(client.getClientInput().readInput()));
         OrderInterface placementOrder = DestOrderFactory.getOrder("placement", placement, units);
        
        placementList.add(placementOrder);
        break;
      } catch (NumberFormatException ne) {
        // ne.printStackTrace();
        client.getClientOutput().displayString("That was not an integer, please try again.");
      }
    }
    // return placementList;
  }
  public void createPlacements(List<OrderInterface>orderList) {
    // Prompt user for placements, create list of placementOrders, send to server
    int startUnits = Constants.UNIT_START_MULTIPLIER * client.getBoard().getNumRegionsOwned(client.getPlayer());
    client.getClientOutput().displayString("You are " + client.getPlayer().getName() + ", prepare to place " + startUnits + " units.");
    // List<OrderInterface> placementList = new ArrayList<OrderInterface>();
    List<Region> regionList = client.getBoard().getRegions();
    Region placement;
    String regionName;
    for (int i = 0; i < regionList.size(); i++) {
      if (client.getPlayer().getName().equals(regionList.get(i).getOwner().getName())) {
        placement = regionList.get(i);
        regionName = regionList.get(i).getName();
        placementOrderHelper(orderList, regionName, placement);
      }
    }
    // return placementList;
  }

@Override
public void addToOrderList(List<OrderInterface> orderList) {
	// TODO Auto-generated method stub
  createPlacements(orderList);
	
}

}
