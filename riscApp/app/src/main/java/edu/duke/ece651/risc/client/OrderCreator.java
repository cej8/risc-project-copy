package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;
public abstract class OrderCreator {
  
  protected ClientInterface client;
  protected List<OrderInterface> orderList;

  abstract public void addToOrderList(List<OrderInterface> orderList);
  public Region promptForRegion(String keyWord) {
    Region r = null;
    while (r == null) {
      client.getClientOutput()
          .displayString("What region do you want to " + keyWord + "  (please type a region name, i.e. 'A')");
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

  

}
