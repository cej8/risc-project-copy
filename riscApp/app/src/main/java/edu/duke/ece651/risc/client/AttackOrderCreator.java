package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.gui.ClientInterface;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class AttackOrderCreator extends OrderCreator{
  // private Client client;
 public AttackOrderCreator(ClientInterface c){
    this.client=c;
  }
  public void attackHelper(List<OrderInterface> orderList, String sourceKeyWord, String destKeyWord) {
    Region source = promptForRegion(sourceKeyWord);
    Region destination = promptForRegion(destKeyWord);
    while (true) {
      try {
        Unit units = getOrderUnits(source);
        OrderInterface order1 =SourceDestOrderFactory.getOrder("attack move", source, destination, units);
        if (order1 != null) {
          OrderInterface order2=SourceDestOrderFactory.getOrder("attack combat", source, destination, units);
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
@Override
public void addToOrderList(List<OrderInterface> orderList) {
	// TODO Auto-generated method stub
  attackHelper(orderList, "attack from", "attack");
	
}



}
