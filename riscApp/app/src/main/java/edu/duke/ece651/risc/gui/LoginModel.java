package edu.duke.ece651.risc.gui;

import edu.duke.ece651.risc.shared.Connection;

public class LoginModel {
    private static String startGroup = null;
    private static boolean startConnection = false;
    private static String loginUsername = null;
    private static String loginPassword = null;
    private static String registerPassword = null;
    //private static boolean loginResult = false;
    private static String loginResult = null;
    private static String registrationAlert = null;
    private static String firstCall = null;
    private static Connection connection = null;

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
//    synchronized Connection getConnection() throws InterruptedException{
//        while (connection == null){
//            wait();
//        }
//        return connection;
//    }
//    public synchronized void setConnection(Connection c){
//        connection = c;
//        notifyAll();
//    }
    //-------- Login / Registration
    public synchronized void setRegistrationAlert(String reg){
        registrationAlert = reg;
    }
    public synchronized String getRegistrationAlert() throws InterruptedException{
        while(registrationAlert == null){
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
    synchronized String getLoginResult() throws InterruptedException{
        while (loginResult == null){
            wait();
        }
        return loginResult;
    }
    synchronized void setLoginResult(String r){
        loginResult = r;
        notifyAll();
    }
    //------- First Call
    synchronized void setFirstCall(String s){
        firstCall = s;
        notifyAll();
    }
    synchronized String getFirstCall() throws InterruptedException{
        while(firstCall == null){
            wait();
        }
        return firstCall;
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
