package edu.duke.ece651.risc.client;

public class OrderFactoryProducer {
  public static OrderCreator getOrderCreator(String orderKey, ClientInterface c) {
    OrderCreator factory = null;
    switch (orderKey) {
      case "M":// move
        //    c.getClientOutput().displayString("You made a Move order, what else would you like to do?");
        factory = new MoveOrderCreator(c);
         c.getClientOutput().displayString("You made a Move order, what else would you like to do?");
       
        break;
      case "A":// attack

      
        factory = new AttackOrderCreator(c);
        c.getClientOutput().displayString("You made an Attack order, what else would you like to do?");
      
        break;
      case "P":// placement
        factory = new PlacementOrderCreator(c);
        break;
      case "U":// uppgrade unit
        factory = new UnitBoostOrderCreator(c);
           c.getClientOutput().displayString("You made an Upgrade units order, what else would you like to do?");
     
        break;
      case "T":// tech boost
        factory = new TechBoostOrderCreator(c);
         c.getClientOutput().displayString("You made an Upgrade technology level order, this will not be active until your next turn. What else would you like to do?");
        break;
         case "E":// teleport
           factory = new TeleportOrderCreator(c);
         c.getClientOutput().displayString("You made a Teleport order, what else would you like to do?");
        break;
         case "R":// teleport
           factory = new ResourceBoostCreator(c);
         c.getClientOutput().displayString("You made a Upgrade resource order, what else would you like to do?");
        break;

    default:
       c.getClientOutput().displayString("Please select either T, M, A, U, E, or D");
        break;
    }
    return factory;
  }
}
