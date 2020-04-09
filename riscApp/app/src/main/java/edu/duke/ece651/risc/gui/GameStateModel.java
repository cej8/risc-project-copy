package edu.duke.ece651.risc.gui;

public class GameStateModel {
    private static String startGroup = null;
    private static boolean startConnection = false;
    private static String loginUsername = null;
    private static String loginPassword = null;
    private static String registerPassword = null;
    private static boolean loginResult = false;
    private static boolean registrationAlert = false;

    //------- Connection blocking
    public synchronized boolean getConnection() throws InterruptedException {
        while (!startConnection){
            wait();
        }
        return startConnection;
    }
    public synchronized void setConnection(boolean s){
        startConnection = s;
        notifyAll();
    }
    //-------- Login / Registration
    public synchronized void setRegistrationAlert(boolean reg){
        registrationAlert = reg;
    }
    public synchronized boolean getRegistrationAlert() throws InterruptedException{
        while(!registrationAlert){
            wait();
        }
        return registrationAlert;
    }
    //------- Login blocking
    synchronized String getLoginPassword() throws InterruptedException{
        while ((loginPassword == null)){
            wait();
        }
        return loginPassword;
    }
    synchronized String getLoginUsername() throws InterruptedException{
        while (loginUsername == null){
            wait();
        }
        return loginUsername;
    }
    synchronized void setLoginPassword(String s){
        loginPassword = s;
        notifyAll();
    }
    synchronized void setLoginUsername(String s){
        loginUsername = s;
        notifyAll();
    }
    synchronized boolean getLoginResult() throws InterruptedException{
        while (!loginResult){
            wait();
        }
        return loginResult;
    }
    synchronized void setLoginResult(Boolean r){
        loginResult = r;
        notifyAll();
    }
    //------- Registration blocking
    synchronized String getConfirmationPassword() throws InterruptedException{
        while (registerPassword == null){
            wait();
        }
        return registerPassword;
    }
    synchronized void setRegisterPassword(String s){
        registerPassword = s;
        notifyAll();
    }

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
        notifyAll();
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
