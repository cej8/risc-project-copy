package edu.duke.ece651.risc.gui;

public class GameStartModel {
    private static String gameList=null;

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

}
