package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.client.ConnectionManager;
import edu.duke.ece651.risc.shared.*;


public class ExecuteClient {
    Connection connection;
    ClientInputInterface clientInput;
    ClientOutputInterface clientOutput;
    Boolean loginResult;
    Activity act;
    String helpText;

    public ExecuteClient(Activity activity) {
        clientInput = new GUIEditTextInput(activity);
        clientOutput = new GUITextDisplay();
        this.act = activity;
    }

    //public void createGame(){
    public void createGame(){
        //String addr = "172.74.90.68"; localhost
        //String addr = "67.159.89.108"; old server
        String addr = "152.3.64.158";
        String portS = "12345";
        int port;
        try {
            port = Integer.parseInt(portS);
        } catch (NumberFormatException ne) {
            //textView.setText("Port invalid");
            Log.d("Port","Invalid");
            return;
        }
        ConnectionManager makeConnection = new ConnectionManager(addr,port);
        makeConnection.start();
        this.connection = makeConnection.getConnection();
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setConnection(connection);
    }
    public Connection getConnection(){
        return this.connection;
    }
    public void setConnection(Connection connection){
        this.connection = connection;
    }
    public void registerLogin(String username, String password, String confirmPassword, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
        clientOutput = new GUITextDisplay(textHelp,act);
        final GUIClientLogin clientLogin = new GUIClientLogin(connection,clientInput, clientOutput,username,password,act,confirmPassword);
        clientLogin.start();
        new Handler().postDelayed(new Runnable() {
            //private Boolean loginResult;
            @Override
            public void run() {
                // This method will be executed once the timer is over
                loginResult = clientLogin.getLoginResult();
                Log.d("Login Result", loginResult.toString());

                if (loginResult == false){
                    // set help text
                    //helpText.setText("Username or password not found. Please register if needed.");
                    helpText = "User already exists. Please choose another username";
                    setHelpText(helpText);
                    Log.d("Login","false");
                    Log.d("Helptext",helpText);
                    setLoginResult(loginResult);
                } else {
                    // start new intent aka display available games
                    Intent loginIntent = new Intent(act, DisplayGamesActivity.class);
                    Log.d("Login","true");
                    setLoginResult(loginResult);
                    act.startActivity(loginIntent);
                }
            }
        }, 6000);
    }
    public void loginGame(String username, String password,TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
        clientOutput = new GUITextDisplay(textHelp,act);
        final GUIClientLogin clientLogin = new GUIClientLogin(connection,clientInput, clientOutput,username,password,act);
        clientLogin.start();
        new Handler().postDelayed(new Runnable() {
            //private Boolean loginResult;
            @Override
            public void run() {
                // This method will be executed once the timer is over
                loginResult = clientLogin.getLoginResult();
                Log.d("Login Result", loginResult.toString());

                if (loginResult == false){
                    // set help text
                    helpText = "Username or password not found. Please register if needed.";
                    clientOutput.displayString("Incorrect username or password. If you are not registered please do so now.");
                    Log.d("Login","false");
                    Log.d("Helptext",helpText);
                    setLoginResult(loginResult);
                } else {
                    // start new intent aka display available games
                    Intent loginIntent = new Intent(act, DisplayGamesActivity.class);
                    Log.d("Login","true");
                    setLoginResult(loginResult);
                    act.startActivity(loginIntent);
                }
            }
        }, 6000);
    }
    public void startGame(TextView textView, Activity act, EditText editText) {
        //ClientInputInterface clientInput = new GUIConsoleInput(editText,act);
        //ClientOutputInterface clientOutput = new GUITextDisplay(textView,act);

        Log.d("Test Connection", "Test Connection");
        GUIClient client = new GUIClient(clientInput,clientOutput,connection);
        //GUIClient client = new GUIClient(clientInput, clientOutput, addr, port);
        //client.start();
    }
    public Boolean getLoginResult() {
        return this.loginResult;
    }
    public void setLoginResult(Boolean login){
        this.loginResult = login;
    }
    public String getHelpText(){
        return this.helpText;
    }
    public void setHelpText(String text){
        this.helpText = text;
    }
}
