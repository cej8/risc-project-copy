package edu.duke.ece651.risc.gui;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.risc.shared.AttackCombat;
import edu.duke.ece651.risc.shared.AttackMove;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.MoveOrder;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.Unit;
import edu.duke.ece651.risc.shared.UnitBoost;

public class Orders {
    private List<Region> regions;
    private Board board;
    private String order;
    private String attackTo;
    private String attackFrom;
    ArrayList<Integer> unitList;

    public Orders(String attackFrom, String attackTo, String order,ArrayList<Integer> unitList){
        this.board = ParentActivity.getBoard();
        this.regions = board.getRegions();
        this.attackFrom = attackFrom;
        this.attackTo = attackTo;
        this.order = order;
        this.unitList = unitList;
    }

    // Unit boost constructor
    public Orders(String attackFrom, String order,ArrayList<Integer> unitList){
        this.board = ParentActivity.getBoard();
        this.regions = board.getRegions();
        this.attackFrom = attackFrom;
        this.attackTo = attackTo;
        this.order = order;
        this.unitList = unitList;
    }

    public void getOrders(){
        Region source = getRegionByName(board,attackFrom);
        Region destination = getRegionByName(board,attackTo);
        Unit unit = new Unit(unitList);
        ParentActivity parentActivity = new ParentActivity();
        HumanPlayer player = parentActivity.getPlayer();
        Log.d("Board test",regions.get(0).getName());
        if (order == null){
            // do nothing
        } else {
            if (order.equals("move")) {
                MoveOrder moveOrder = new MoveOrder(source, destination, unit);
                parentActivity.setOrders(moveOrder);
            } else if (order.equals("attack")) {
                AttackMove attackMove = new AttackMove(source, destination, unit);
                parentActivity.setOrders(attackMove);
                AttackCombat attackCombat = new AttackCombat(source, destination, unit);
                parentActivity.setOrders(attackCombat);
            } else if (order.equals("boost units")) {
                UnitBoost unitBoost = new UnitBoost(source,unit);
                parentActivity.setOrders(unitBoost);
            }
            else if (order.equals("techBoost")) {
                //i put my method in the techboost activity itself
            }
        }
    }

    public Region getRegionByName(Board board, String name){
        Map<String, Region> nameToRegionMap = new HashMap<String, Region>();
        for (Region r : board.getRegions()){
            nameToRegionMap.put(r.getName(), r);
        }
        return nameToRegionMap.get(name);
    }
}
