// package edu.duke.ece651.risc.client;

// public class OrderCreator {
//    public boolean getOrderList(List<OrderInterface> orderList, String response) {
//     switch (response.toUpperCase()) {
//       case "D":
//         return false;
//       case "M":
//         // orderList = moveAttackHelper(orderList, "move units", "move units to",
//         // "move");
//         moveHelper(orderList, "move units from", "move units to");
//         client.getClientOutput().displayString("You made a Move order, what else would you like to do?");
//         break;
//       case "A":
//         // orderList = moveAttackHelper(orderList, "attack from", "attack", "attack");
//         attackHelper(orderList, "attack from", "attack");
//         client.getClientOutput().displayString("You made an Attack order, what else would you like to do?");
//         break;
//     case "U":
//       DestOrderCreator doc = new DestOrderCreator(this.client);
//       doc.upgradeHelper(orderList, "upgrade units on");
//         client.getClientOutput().displayString("You made an Upgrade units order, what else would you like to do?");
//         break;
//     case "T":
//       PlayerOrderCreator poc = new PlayerOrderCreator(this.client);
//       poc.techBoostHelper(orderList);
//         client.getClientOutput().displayString("You made an Upgrade technology level order, this will not be active until your next turn. What else would you like to do?");
//         break;
 
//       default:
//         client.getClientOutput().displayString("Please select either T, M, A, U, or D");
//         break;
//     }
//     return true;
//   }
//   public List<OrderInterface> createOrders() {
//     // prompt user for orders --> create list of OrderInterface --> send to server
//     List<OrderInterface> orderList = new ArrayList<OrderInterface>();
//     String response = null;
//     boolean orderSelect = true;
//     while (orderSelect) {
//       // prompt user
//       client.getClientOutput().displayString("You are " + client.getPlayer().getName()
//           + ", what would you like to do?\n (M)ove\n (A)ttack\n (D)one\n (U)pgrade");
//       response = client.getClientInput().readInput();
//       orderSelect = getOrderList(orderList, response);
//     }
//     return orderList;
//   }


// }
