package edu.duke.ece651.risc.shared;

import java.util.*;

public class AttackCombat extends SourceDestinationUnitOrder {
  // this class handles the actual combat resolution when an attack is issued by a
  // player
  // Repurposed code from doDestinationAction of AttackOrder

  private static final long serialVersionUID = 21L;

  public AttackCombat(Region attacker, Region defender, Unit attackingUnits) {
    this.source = attacker;
    this.destination = defender;
    this.units = attackingUnits;
  }

  @Override
  public int getPriority() {
    return Constants.ATTACK_COMBAT_PRIORITY;
  }

  @Override
  public List<Set<String>> getPlayersVisibleTo(){
    Set<String> playersDestination = new HashSet<String>();
    //Get destination + regions adjacent
    playersDestination.add(destination.getOwner().getName());
    for(Region adj : destination.getAdjRegions()){
      playersDestination.add(adj.getOwner().getName());
    }
    //ensure attacker can always see
    playersDestination.add(source.getOwner().getName());
    //Source can only see attacking somewhere, destination can only see attack with something
    return Arrays.asList(playersDestination, playersDestination, playersDestination);
  }

  @Override
  public List<String> doAction() {
    // set initial evaluation order
    boolean attackerIsStronger = true;
    // Continue executing attack until one player has no more units left in region
    // or attack group

    while (!isWinner(source, destination, units)) {
      Integer aUnitType = 0;
      Integer dUnitType = 0;
      Region loseUnits = null;

      // get types fo unit involved in each roll for bonus to be passed in
      // alternate betwwen highest bonus and lowest bonus
      if (attackerIsStronger) {
        aUnitType = getHighestBonus(units);
        dUnitType = getLowestBonus(destination.getUnits());
        attackerIsStronger = false;
      } else {
        aUnitType = getLowestBonus(units);
        dUnitType = getHighestBonus(destination.getUnits());
        attackerIsStronger = true;
      }
      loseUnits = rollHelper(source, destination, units.getBonusFromTech(aUnitType),
          destination.getUnits().getBonusFromTech(dUnitType));

      // change values after rolling
      if (loseUnits == destination) {
        // decrease number of units of specfic unit type in defending regions
        destination.getUnits().getUnits().set(dUnitType, destination.getUnits().getUnits().get(dUnitType) - 1);

      } else {
        // decrease number of units of specific unit type on attacking units
        units.getUnits().set(aUnitType, units.getUnits().get(aUnitType) - 1);
      }

      System.out.println("Defending units remaining of type " + destination.getUnits().getTypeFromTech(dUnitType) + ": "
          + destination.getUnits().getUnits().get(dUnitType) + "\nAttacking units remaining of type "
          + units.getTypeFromTech(aUnitType) + ": " + units.getUnits().get(aUnitType));

    }


    List<String> returnStrings = new ArrayList<String>();

    if (units.getTotalUnits() == 0) {
      returnStrings.add(destination.getOwner().getName());
      returnStrings.add(" (defender) retains their region ");
    } else {
      returnStrings.add(source.getOwner().getName());
      returnStrings.add(" (attacker) takes over the region ");
    }
    returnStrings.add(destination.getName() + ", " + destination.getUnits().getTotalUnits() + " units survived!");
    return returnStrings;

  }

  // method to retrieve the type of unit with the highest bonus
  private Integer getHighestBonus(Unit u) {
    Integer i;
    for (i = u.getUnits().size() - 1; i >= 0; i--) {
      if (u.getUnits().get(i) > 0) {
        break;// return unit type of highest available bonus
      }

    }
    return i;

  }

  // retrieve the type of unit with the lowest bonus
  private Integer getLowestBonus(Unit u) {
    Integer i;
    for (i = 0; i < u.getUnits().size(); i++) {
      if (u.getUnits().get(i) > 0) {
        break;
      }

    }
    return i;

  }

  // method to check if a player has won attack round (no units left of any type)
  private boolean isWinner(Region s, Region d, Unit u) {
    if (u.getTotalUnits() == 0) {
      System.out.println(d.getOwner().getName() + " (defender) retains their region " + d.getName());
      return true;
    } else if (d.getUnits().getTotalUnits() == 0) {
      System.out.println(s.getOwner().getName() + " (attacker) takes over the region " + d.getName());
      d.assignRegion(s.getOwner(), u);
      System.out.println("Updated " + d.getName() + " region: " + d.getOwner().getName() + " now owns " + d.getName()
          + " with " + d.getUnits().getTotalUnits() + " unit(s)");
      return true;
    } else {
      return false;
    }
  }

  // method to randomly select number on 20 sided dice
  private Region rollHelper(Region defRegion, Region attackRegion, Integer aBonus, Integer dBonus) {
    Random diceTwenty = new Random();
    int attackResult = diceTwenty.nextInt(20) + aBonus;
    int defResult = diceTwenty.nextInt(20) + dBonus;
    if (defResult >= attackResult) {
      return attackRegion;
    }
    return defRegion;
  }

}
