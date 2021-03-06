package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.ConfirmationMessage;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.IntegerMessage;
import edu.duke.ece651.risc.shared.StringMessage;

public class GUIClientLogin extends Thread{
    private Connection connection;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    private String username;
    private String password;
    private String confirmPassword;
    Activity activity;
    Boolean loginResult;
    Boolean registeredUser;
    private Handler loginHandler;
    // Handler constructor
    public GUIClientLogin(Handler loginHandler,Connection connect, ClientInputInterface input, ClientOutputInterface output, String username, String password, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.username = username;
        this.password = password;
        this.loginResult = null;
        this.registeredUser = true;
        this.confirmPassword = null;
        this.loginHandler = loginHandler;
    }
    // register handler constructor
    public GUIClientLogin(Handler regHandler,Connection connect, ClientInputInterface input, ClientOutputInterface output, String username, String password, Activity act, String password2){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.username = username;
        this.password = password;
        this.loginResult = null;
        this.registeredUser = false;
        this.confirmPassword = password2;
        this.loginHandler = regHandler;
    }
    // Login constructor
    public GUIClientLogin(Connection connect, ClientInputInterface input, ClientOutputInterface output, String username, String password, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.username = username;
        this.password = password;
        this.loginResult = null;
        this.registeredUser = true;
        this.confirmPassword = null;
    }
    // Registration Constructor
    public GUIClientLogin(Connection connect, ClientInputInterface input, ClientOutputInterface output, String username, String password, Activity act, String password2){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.username = username;
        this.password = password;
        this.loginResult = null;
        this.registeredUser = false;
        this.confirmPassword = password2;

    }
    // reprompt login
    public GUIClientLogin(Connection connection,String username, String password){
        this.connection = connection;
        this.username = username;
        this.password = password;
        this.registeredUser = true;
    }
    public void Login(){// throws IOException, ClassNotFoundException{
        try {
            //performLogin();
            performSelectGame();
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
        }
    }
    public String receiveAndDisplayString() throws IOException, ClassNotFoundException{
        StringMessage message = (StringMessage) (connection.receiveObject());
        String str = message.unpacker();
        clientOutput.displayString(str);
        return str;
    }
    public Boolean getLoginResult(){
        return this.loginResult;
    }
    //Method to mesh with loginProcess() in loginServer
    public void performLogin() throws IOException, ClassNotFoundException{
        String initalSuccess = receiveAndDisplayString();
            //Either way request login
            connection.sendObject(new ConfirmationMessage(registeredUser));
            connection.sendObject(new StringMessage(username));
            //We will get salt back
            String salt = ((StringMessage)(connection.receiveObject())).unpacker();
            Log.d("Salt",salt);
            //We will request a password

            String password1 = password;
            //Hash password
            String hashPassword1;
            if(!salt.equals("")){
                hashPassword1 = BCrypt.hashpw(password1, salt);
            }
            else{
                hashPassword1 = "";
            }

            //Send hashed password back
            connection.sendObject(new StringMessage(hashPassword1));

            //If true then has login (nothing extra)
            //If false then registering (need second password entry)
            if(!registeredUser){
            //Request repeat of password
            String password2 = confirmPassword;//clientInput.readInput();
            //Hash password
            String hashPassword2 = BCrypt.hashpw(password2, salt);
            //Send copy back
            connection.sendObject(new StringMessage(hashPassword2));
            }

            //Get back response - checks login
            String response = receiveAndDisplayString();
            //Repeat if fail, continue if success
            if (response.matches("^Fail:.*$")) {
                this.loginResult = false;

                Log.d("GUIClientLogin", loginResult.toString());
               // continue;
            }
            if (response.matches("^Success:.*$")) {
                this.loginResult = true;
                Log.d("GUIClientLogin", loginResult.toString());
               // break;

            }
    }

    //Method to mesh with selectGame() in loginServer
    public void performSelectGame() throws IOException, ClassNotFoundException{
        while(true){
            boolean oldBoolean = queryYNAndRespond("Would you like to join a game you are already in? [Y/N]");

            //Server then sends back list of games
            String list = receiveAndDisplayString();
            Integer gameID;
            while(true){
                clientOutput.displayString("Pick a game via ID");
                try{
                    gameID = Integer.parseInt(clientInput.readInput());
                }
                catch (NumberFormatException ne) {
                    // ne.printStackTrace();
                    clientOutput.displayString("That was not an integer.");
                    continue;
                }
                break;
            }
            //Send ID to server
            connection.sendObject(new IntegerMessage(gameID));

            //Get back response
            String response = receiveAndDisplayString();
            //Repeat if fail, continue if success
            if (response.matches("^Fail:.*$")) {
                continue;
            }
            if (response.matches("^Success:.*$")) {
                break;
            }
        }
    }

    //Helper method to ask YN and send back ConfirmationMessage
    public boolean queryYNAndRespond(String query) throws IOException {
        while(true){
            // Request input
            clientOutput.displayString(query);
            String spectateResponse = clientInput.readInput();

            spectateResponse = spectateResponse.toUpperCase();
            // If valid then do work
            if (spectateResponse.length() == 1) {
                if (spectateResponse.charAt(0) == 'Y') {
                    connection.sendObject(new ConfirmationMessage(true));
                    return true;
                } else if (spectateResponse.charAt(0) == 'N') {
                    connection.sendObject(new ConfirmationMessage(false));
                    return false;
                }
            }
            // Otherwise repeat
            clientOutput.displayString("Invalid input.");
        }
    }
    @Override
    public void run(){

        try {
            performLogin();
            loginHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (loginResult){
                        // send to GameTypeActivity.class
                        Intent loginIntent = new Intent(activity, GameTypeActivity.class);
                        Log.d("Login", "true");
                        activity.startActivity(loginIntent);
                    } else {
                        // reprompt
                        clientOutput.displayString("Incorrect username or password. If you are not registered please do so now.");
                        Intent confirmLogin = new Intent(activity, ConfirmLoginActivity.class);
                        activity.startActivity(confirmLogin);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            loginHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            loginHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
