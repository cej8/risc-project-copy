package edu.duke.ece651.risc.gui;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;

public class GameStateModel {
    private static String startGroup;
    private static boolean startConnection = false;

    // Connection blocking
    public synchronized boolean getConnection() throws InterruptedException {
        while (startConnection == false){
            wait();
        }
        return startConnection;
    }
    public synchronized void setConnection(boolean startConnection){
        this.startConnection = startConnection;
        notifyAll();
    }
//    synchronized String getLogin() throws InterruptedException{
//        while (login == null){
//            wait();
//        }
//    }

    synchronized String getStartGroup() throws InterruptedException {
        // value not ready
        while (startGroup == null){
            wait();
        }
        return startGroup;
    }
    // client
    synchronized void setStartGroup(String s){
        this.startGroup = s;
    }
}

//public class GUIClient{
//
//    public void playGame{
//        // other stuff
//        // read to send groups
//        GUIClientRegionSelection guiClientRegionSelection = new GUIClientRegionSelection();
//        // don't need to take string into constructor
//        guiClientRegionSelection.chooseStartGroup();
//    }
//}
