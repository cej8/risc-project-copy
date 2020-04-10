package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.ConfirmationMessage;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.IntegerMessage;
import edu.duke.ece651.risc.shared.StringMessage;

public class GUIClientLogin {//extends Thread{
    private Connection connection;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    Activity activity;
    Boolean loginResult;

   // Boolean registeredUser;
    LoginModel model;
    GameStartModel gameModel;

    // Login constructor
   /*public GUIClientLogin(LoginModel model,boolean registeredUser,Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){

        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.loginResult = null;
        this.registeredUser = registeredUser;
        this.model = model;
    }*/
    // Login constructor
    public GUIClientLogin(LoginModel model,GameStartModel g,Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act) {
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.loginResult = null;
        this.model = model;
        this.gameModel=g;
    }
        public boolean Login(){// throws IOException, ClassNotFoundException{
        boolean firstCall = true;
        try {
            performLogin();
            firstCall = performSelectGame();
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
        }
        return firstCall;
    }
    public String receiveAndDisplayString() throws IOException, ClassNotFoundException{
        StringMessage message = (StringMessage) (connection.receiveObject());
        String str = message.unpacker();
      //  ParentActivity.getClientOutput().displayString(str);
        return str;
    }
    public Boolean getLoginResult(){
        return this.loginResult;
    }
    //Method to mesh with loginProcess() in loginServer
    public void performLogin() throws IOException, ClassNotFoundException,InterruptedException{
        String initialSuccess = receiveAndDisplayString();
       while(true){
           boolean loginBoolean = model.getRegistrationAlert();//wait for UI thread to set whether the user is logging in or registering as a new user
            connection.sendObject(new ConfirmationMessage(loginBoolean));

            //---Login blocking start

            String username = model.getLoginUsername();//wait for UI thread to set username
            connection.sendObject(new StringMessage(username));
            Log.d("Login","Username sent");
            //---Login blocking end

            //We will get salt back
            String salt = ((StringMessage)(connection.receiveObject())).unpacker();
            Log.d("Salt",salt);
            //We will request a password
            //clientOutput.displayString("Password:");
            //String password1 = clientInput.readInput();

            //---Login blocking start
            //String password1 = password;
            String password1 = model.getLoginPassword();
            //---Login blocking end

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
            if(!loginBoolean){
            //Request repeat of password
            //clientOutput.displayString("Password (again):");

                //---Login blocking start
            //String password2 = confirmPassword;//clientInput.readInput();
                String password2 = model.getConfirmationPassword();
                //---Login blocking end

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
                model.setLoginResult(false);
                model.setLoginResultReady(true);
             //   ParentActivity.getClientOutput().displayString("Incorrect username or password. If you are not registered please do so now.");
                Log.d("GUIClientLogin", loginResult.toString());
               continue;
            }
            if (response.matches("^Success:.*$")) {
               this.loginResult = true;
                model.setLoginResult(true);
                model.setLoginResultReady(true);
                Log.d("GUIClientLogin", loginResult.toString());
               break;

            }
        //Log.d("GUIClientLogin", loginResult.toString());
        }

        //At this point user is logged in (either old or new)

    }

    //Method to mesh with selectGame() in loginServer
    public boolean performSelectGame() throws IOException, ClassNotFoundException, InterruptedException {
        while(true){
            boolean oldBoolean = gameModel.getOldBoolean();// queryYNAndRespond("Would you like to join a game you are already in? [Y/N]");
            connection.sendObject(new ConfirmationMessage(oldBoolean));
            //Server then sends back list of games
           //String list = receiveAndDisplayString();
            StringMessage message = (StringMessage) (connection.receiveObject());
            String gameList = message.unpacker();
            gameModel.setGameList(gameList);

            Integer gameID;
            while(true){
            //    clientOutput.displayString("Pick a game via ID");
                try{
                    gameID = Integer.parseInt(gameModel.getGameNumber());
                }
                catch (NumberFormatException | InterruptedException ne) {
                    // ne.printStackTrace();
                   // clientOutput.displayString("That was not an integer.");
                    continue;
                }
                break;
            }
            //Send ID to server
            connection.sendObject(new IntegerMessage(gameID));

            //Get back response
            StringMessage m = (StringMessage) (connection.receiveObject());
            String response = m.unpacker();
            //Repeat if fail, continue if success
            if (response.matches("^Fail:.*$")) {
                continue;
            }
            if (response.matches("^Success:.*$")) {
                break;
            }
        }
        return ((ConfirmationMessage)connection.receiveObject()).unpacker();
    }

    //Helper method to ask YN and send back ConfirmationMessage
    public boolean queryYNAndRespond(String query) throws IOException {
        while(true){
            // Request input
          //  ParentActivity.getClientOutput().displayString(query);
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
            ParentActivity.getClientOutput().displayString("Invalid input.");
        }
    }
   // @Override
//    public void run(){
//        try {
//                performLogin();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
