package edu.duke.ece651.risc.gui;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.OrderInterface;

public final class ParentActivity {
    private static Connection connection;
    private static Board board;
    private static HumanPlayer player;
    private static List<OrderInterface> orders = new ArrayList<OrderInterface>();
    private static long startTime;
    private static long maxTime;
    private static Activity activity;
    private static ClientOutputInterface clientOutput;

    final static ClientOutputInterface getClientOutput(){return clientOutput;}
    public void setClientOutput(ClientOutputInterface out){this.clientOutput = out;}

    final static Activity getActivity(){return activity;}
    public void setActivity(Activity a){this.activity = a;}

    final static long getMaxTime(){return maxTime;}
    public void setMaxTime(long t){this.maxTime=t;}

    final static long getStartTime(){return startTime;}
    public void setStartTime(long t){this.startTime=t;}


    final static HumanPlayer getPlayer(){return player;}
    public void setPlayer(HumanPlayer p){this.player=p;}
    final static Connection getConnection(){
        return connection;
    }
    public void setConnection(Connection connection){
        this.connection = connection;
    }
    final static Board getBoard(){return board;}
    public void setBoard(Board board){this.board = board;}
    public void setOrders(OrderInterface o){
        orders.add(o);
    }
    public void resetOrders(){
        this.orders = new ArrayList<OrderInterface>();
    }

    final static List<OrderInterface> getOrders(){
        return orders;
    }
}
