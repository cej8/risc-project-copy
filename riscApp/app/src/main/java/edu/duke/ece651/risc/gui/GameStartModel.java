package edu.duke.ece651.risc.gui;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.HumanPlayer;

public class GameStartModel {
    private static String gameList=null;
    private static String startGroup=null;
    private static Board startBoard=null;
    private static boolean readyToBegin=false;
    private static HumanPlayer player=null;
    private static boolean regionChosen=false;

    public synchronized String getGameList() throws InterruptedException {
        while(gameList==null){
            wait();
        }
        return gameList;

    }
    public synchronized void setGameList(String list){
        this.gameList=list;
        notifyAll();
    }
    public synchronized String getStartGroup() throws InterruptedException {
        while(startGroup==null){
            wait();
        }
        return startGroup;

    }
    public synchronized void setStartGroup(String group){
        this.startGroup=group;
        notifyAll();
    }
    public synchronized void setStartBoard(Board b){
        this.startBoard=b;
        notifyAll();
    }
    public synchronized Board getStartBoard() throws InterruptedException {
        while(startBoard==null){
            wait();
        }
        return startBoard;

    }
    public synchronized void isReadyToBegin(boolean r){
        this.readyToBegin=r;
        notifyAll();
    }
    public synchronized boolean readyToBegin() throws InterruptedException {
        while(readyToBegin==false){
            wait();
        }
        return true;

    }

    public synchronized void setPlayer(HumanPlayer p){
        this.player=p;
        notifyAll();
    }
    public synchronized HumanPlayer getPlayer() throws InterruptedException {
        while(player==null){
            wait();
        }
        return player;

    }
    public synchronized void isRegionChosen(boolean r){
        this.regionChosen=r;
        notifyAll();
    }
    public synchronized boolean regionChosen() throws InterruptedException {
        while(regionChosen==false){
            wait();
        }
        return true;

    }





}
