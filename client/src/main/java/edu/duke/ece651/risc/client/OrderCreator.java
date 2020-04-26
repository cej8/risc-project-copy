package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

// Main order creator class, used to prompt/create orders
public abstract class OrderCreator {

  protected ClientInterface client;
  protected List<OrderInterface> orderList;

  abstract public void addToOrderList(List<OrderInterface> orderList);

  public Region promptForRegion(String keyWord) {
    Region r = null;
    while (r == null) {
      client.getClientOutput()
          .displayString("What region do you want to " + keyWord + "?  (please type a region name, i.e. 'A')");
      r = orderHelper(client.getClientInput().readInput());
    }
    return r;
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

  //prompts the user to select one from each type of unit
  public Unit getOrderUnits(Region region) {
    Unit regionUnits = (Unit) DeepCopy.deepCopy(region.getUnits());
    List<Integer> orderUnits = new ArrayList<Integer>();
    int bonusLevel = 0;
    while (bonusLevel < regionUnits.getUnits().size()) {
      if (regionUnits.getUnits().get(bonusLevel) > 0) { // if player has at lest 1 of that type
        client.getClientOutput().displayString("How many " + regionUnits.getTypeFromTech(bonusLevel) + " units ("
            + regionUnits.getUnits().get(bonusLevel) + " total) do you want to select?");
        // get number from user
        Integer input = Integer.parseInt(client.getClientInput().readInput());
        // if 0 > and <= numOrType
        if ((input >= 0) && (input <= region.getUnits().getUnits().get(bonusLevel))) { //NOTE TO SELF: <= is invalid for attack/move but not upgrade...shouldn't be a problem bc validator will catch invalid move/attack
          orderUnits.add(input);
          bonusLevel++;
        } else {
          client.getClientOutput().displayString("Invalid input [" + input + " " + regionUnits.getTypeFromTech(bonusLevel) + "(s)]: please try again");
        }
      } else {
        bonusLevel++;
        orderUnits.add(0); //adds a 0 to that index if none
      }
    }
    return new Unit(orderUnits);
  }

}
