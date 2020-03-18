// package edu.duke.ece651.risc.shared;

// import java.util.*;

// public class RegionValidator implements ValidatorInterface {
//   private boolean hasValidPath(Region start, Region end, Set<Region> visited) {
//     // helper method
//     // find a path of connected nodes from start to end
//     // Set<Region> visited = new HashSet<Region>();
//     visited.add(start);
//     for (Region neighbor : start.getAdjRegions()) {
//       if (visited.contains(neighbor)) {
//         continue;// check if already visited
//       }
//       visited.add(neighbor);
//       if (start.getOwner().getName().equals(neighbor.getOwner().getName())) {// owned by the same player
//         if (neighbor == end) {
//           return true;
//         }
//         return hasValidPath(neighbor, end, visited);
//       }
//     }
//     return false;
//   }

//   // helper method
//   public boolean isValidMove(MoveOrder m) {
//     // owned by the same person
//     if (hasValidPath(m.getSource(), m.getDestination(), new HashSet<Region>())) {
//       // and have path to get there via adjacent regions
//      return true;
//     }
//     return false;
//   }

//   // helper method
//   public boolean isValidPlacement(PlacementOrder p, AbstractPlayer player) {
//     // check that player owns the regions they are placing units in
//     if(p.getDestination().getOwner()==player){
//       return true;
//     }
//     return false;
//   }

//   // helper method
//   public boolean isValidAttack(AttackOrder a) {
//     // regions must be owned by different players
//     if (a.getSource().getOwner().getName().equals(a.getDestination().getOwner().getName())) {
//       return false;
//     }
//     // regions must be adjacent
//     for (Region neighbor : a.getSource().getAdjRegions()) {
//       if (neighbor == a.getDestination()) {
//         return true;
//       }
//     }
//     return false;
//   }

//   @Override
//   public boolean attacksAreValid(List<AttackOrder> attackList) {
//     for (AttackOrder attack : attackList) {
//       if (!isValidAttack(attack)) {
//         return false;
//       }
//       attack.doAction();
//     }
//     // if all attacks are valid
//     return true;
//   }

//   @Override
//   public boolean movesAreValid(List<MoveOrder> moveList) {
//     for (MoveOrder move : moveList) {
//       if (!isValidMove(move)) {
//         return false;
//       }
//       move.doAction(); 
//     }
//     // if all moves are valid
//     return true;
//   }

//   @Override
//   public boolean placementsAreValid(List<PlacementOrder> placementList, AbstractPlayer player) {
//     for (PlacementOrder place : placementList) {
//       if (!isValidPlacement(place, player)) {
//         return false;
//       }
//       place.doAction();
//     }
//     // if all placements are valid
//     return true;

//   }
// }
