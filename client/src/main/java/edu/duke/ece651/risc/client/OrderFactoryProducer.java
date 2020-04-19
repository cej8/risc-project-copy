package edu.duke.ece651.risc.client;

public class OrderFactoryProducer {
  public static OrderCreator getOrderCreator(String orderKey, ClientInterface c) {
    OrderCreator factory = null;
    switch (orderKey) {
      case "M":// move
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
      case "C":// cloak
        factory = new CloakOrderCreator(c);
        c.getClientOutput().displayString("You made a Cloak order, what else would you like to do?");
        break;
      case "Y":// spy upgrade
        factory = new SpyUpgradeOrderCreator(c);
        c.getClientOutput().displayString("You made a Spy Upgrade order, what else would you like to do?");
        break;
         case "R":// resource boost
           factory = new ResourceBoostCreator(c);
         c.getClientOutput().displayString("You made a Upgrade resource order, what else would you like to do?");
        break;

      case "V":// spy move
        factory = new SpyMoveOrderCreator(c);
        c.getClientOutput().displayString("You made a Spy Move order, what else would you like to do?");
        break;
      default:
       c.getClientOutput().displayString("Please select either T, M, A, U, E, R, C, Y, V or D");
       break;
    }
    return factory;
  }
}
