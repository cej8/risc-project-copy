package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class OrderHelper {
  private Client client;
  private SDOrderCreator sdoc;
  private DestOrderCreator doc;
  private PlayerOrderCreator poc;
  public OrderHelper(Client c){
    this.client = c;
   this.sdoc = new SDOrderCreator(client);
 this.doc = new DestOrderCreator(client);
 this.poc = new PlayerOrderCreator(client);
 
  }
  

 public boolean getOrderList(List<OrderInterface> orderList, String response) {
    switch (response.toUpperCase()) {
      case "D":
        return false;
      case "M":
       
        sdoc.moveHelper(orderList, "move units from", "move units to");
        client.getClientOutput().displayString("You made a Move order, what else would you like to do?");
        break;
      case "A":
        // orderList = moveAttackHelper(orderList, "attack from", "attack", "attack");
        sdoc.attackHelper(orderList, "attack from", "attack");
        client.getClientOutput().displayString("You made an Attack order, what else would you like to do?");
        break;
    case "U":
      doc.upgradeHelper(orderList, "upgrade units on");
        client.getClientOutput().displayString("You made an Upgrade units order, what else would you like to do?");
        break;
    case "T":
      poc.techBoostHelper(orderList);
        client.getClientOutput().displayString("You made an Upgrade technology level order, this will not be active until your next turn. What else would you like to do?");
        break;
 
      default:
        client.getClientOutput().displayString("Please select either T, M, A, U, or D");
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
