package edu.duke.ece651.risc.gui;

import edu.duke.ece651.risc.shared.Connection;

public class LoginModel {
    private static String startGroup = null;
    private static boolean startConnection = false;
    private static String loginUsername = null;
    private static String loginPassword = null;
    private static String registerPassword = null;
    private static boolean loginResult = false;
    private static boolean loginResultReady=false;
    private static boolean registrationAlert = false;
    private static boolean loginBooleanReady=false;
    private static Connection connection=null;
    private static boolean registrationReady=false;

    //------- Connection blocking
    public synchronized boolean getStartConnection() throws InterruptedException {
        while (!startConnection){
            wait();
        }
        return startConnection;
    }
    public synchronized void setStartConnection(boolean s){
        startConnection = s;
        notifyAll();
    }
    public synchronized Connection getConnection() throws InterruptedException {
        while(connection==null){
            wait();
        }
        return connection;
    }
    public synchronized void setConnection(Connection c){
        this.connection=c;
        notifyAll();

    }
    public synchronized void isLoginBooleanReady(boolean l){
        this.loginBooleanReady=l;
        notifyAll();
    }
    //-------- Login / Registration
    public synchronized void setRegistrationAlert(boolean reg){
        registrationAlert = reg;
        notifyAll();
    }
    public synchronized boolean getRegistrationAlert() throws InterruptedException{
        while(!registrationReady){
            wait();
        }
        return registrationAlert;
    }
    public synchronized void setRegistrationReady(boolean ready){
        registrationReady = ready;
        notifyAll();
    }
    //------- Login blocking
    public synchronized String getLoginPassword() throws InterruptedException{
        while ((loginPassword == null)){
            wait();
        }
        return loginPassword;
    }
    public synchronized String getLoginUsername() throws InterruptedException{
        while (loginUsername == null){
            wait();
        }
        return loginUsername;
    }
    public synchronized void setLoginPassword(String s){
        loginPassword = s;
        notifyAll();
    }
    public synchronized void setLoginUsername(String s){
        loginUsername = s;
        notifyAll();
    }
    public synchronized void setLoginResultReady(boolean r){
        this.loginResultReady=r;
        notifyAll();
    }
    public synchronized boolean getLoginResult() throws InterruptedException{
        while (!loginResultReady){
            wait();
        }
        return loginResult;
    }
    public synchronized void setLoginResult(Boolean r){
        loginResult = r;
        notifyAll();
    }
    //------- Registration blocking
    public synchronized String getConfirmationPassword() throws InterruptedException{
        while (registerPassword == null){
            wait();
        }
        return registerPassword;
    }
    public synchronized void setRegisterPassword(String s){
        registerPassword = s;
        notifyAll();
    }

    public synchronized String getStartGroup() throws InterruptedException {
        // value not ready
        while (startGroup == null){
            wait();
        }
        return startGroup;
    }
    // client
    public synchronized void setStartGroup(String s){
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
