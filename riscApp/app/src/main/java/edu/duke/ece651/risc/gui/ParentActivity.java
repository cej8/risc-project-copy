package edu.duke.ece651.risc.gui;

import java.util.ArrayList;
import java.util.List;

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
    private static boolean isAlive;
    private static boolean firstCall;
    private static boolean spectateFirstCall;
    private static boolean spectate;
    private static String repromptBoard;

    // login credentials
    private static String username;
    private static String password1;
    private static String password2;

    public static String getPassword1(){
        return password1;
    }
    public static String getPassword2(){
        return password2;
    }
    public static String getUsername(){
        return username;
    }
    public void setPassword1(String p){
        password1 = p;
    }
    public void setPassword2(String p){
        password2 = p;
    }
    public void setUsername(String u){
        username = u;
    }

    public static String getRepromptGroup(){
        return repromptBoard;
    }
    public void setRepromptBoard(String b){
        repromptBoard = b;
    }

    public void setSpectate(boolean s){
        spectate = s;
    }
    public static boolean getSpectate(){
        return spectate;
    }

    public void setSpectateFirstCall(boolean s){
        spectateFirstCall = s;
    }
    public static boolean getSpectateFirstCall(){
        return spectateFirstCall;
    }

    public void setFirstCall(boolean f){
        firstCall = f;
    }
    public static boolean getFirstCall(){
        return firstCall;
    }

    public void setAlive(boolean a){
        isAlive = a;
    }
    public static boolean getAlive(){
        return isAlive;
    }

    final static long getMaxTime(){return maxTime;}
    public void setMaxTime(long t){this.maxTime=t;}

    final static long getStartTime(){return startTime;}
    public void setStartTime(long t){this.startTime=t;}


    static HumanPlayer getPlayer(){return player;}
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
