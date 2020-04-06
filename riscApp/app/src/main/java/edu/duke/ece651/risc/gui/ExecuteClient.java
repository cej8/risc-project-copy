package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public void createGame() {
        //String addr = "172.74.90.68"; localhost
        //String addr = "67.159.89.108"; old server
        String addr = "152.3.64.158";
        String portS = "12345";
        int port;
        try {
            port = Integer.parseInt(portS);
        } catch (NumberFormatException ne) {
            //textView.setText("Port invalid");
            Log.d("Port", "Invalid");
            return;
        }
        ConnectionManager makeConnection = new ConnectionManager(addr, port);
        makeConnection.start();
        this.connection = makeConnection.getConnection();
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setConnection(connection);
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void registerLogin(String username, String password, String confirmPassword, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
        clientOutput = new GUITextDisplay(textHelp, act);
        final GUIClientLogin clientLogin = new GUIClientLogin(connection, clientInput, clientOutput, username, password, act, confirmPassword);
        clientLogin.start();
        new Handler().postDelayed(new Runnable() {
            //private Boolean loginResult;
            @Override
            public void run() {
                // This method will be executed once the timer is over
                loginResult = clientLogin.getLoginResult();
                Log.d("Login Result", loginResult.toString());

                if (loginResult == false) {
                    // set help text
                    //helpText.setText("Username or password not found. Please register if needed.");
                    helpText = "User already exists. Please choose another username";
                    setHelpText(helpText);
                    Log.d("Login", "false");
                    Log.d("Helptext", helpText);
                    setLoginResult(loginResult);
                } else {
                    // start new intent aka display available games
                    Intent loginIntent = new Intent(act, GameTypeActivity.class);
                    Log.d("Login", "true");
                    setLoginResult(loginResult);
                    act.startActivity(loginIntent);
                }
            }
        }, 7000);
    }

    public void getGames(boolean gameType, boolean getgame) {
        // clientOutput = new GUITextDisplay(gameText,act);
        final GUISelectGame selectGame = new GUISelectGame(getgame, gameType, connection, clientInput, clientOutput, act);
        selectGame.start();
        new Handler().postDelayed(new Runnable() {
            //private Boolean loginResult;
            @Override
            public void run() {
                // This method will be executed once the timer is over
                String games = selectGame.getGameList();
                Log.d("Game List", games);
                Intent gamesIntent = new Intent(act, NewGameActivity.class);
                gamesIntent.putExtra("GAMELIST", games);
                act.startActivity(gamesIntent);
            }
        }, 2000);
    }

    public void pickGame(boolean gameType, String id, boolean getgame, String gameList) {
        GUISelectGame selectGame = new GUISelectGame(getgame, id, gameType, connection, clientInput, clientOutput, act);
        selectGame.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // TODO: where we actually play the game - DisplayMapActivity.java
                Log.d("Game", "Placement");
                Intent placement= new Intent(act, ChooseRegionsActivity.class);
                //placement.putExtra("GAMELIST", games);
                act.startActivity(placement);
            }
        }, 2000);
    }

    public void loginGame(String username, String password, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
        clientOutput = new GUITextDisplay(textHelp, act);
        final GUIClientLogin clientLogin = new GUIClientLogin(connection, clientInput, clientOutput, username, password, act);
        clientLogin.start();
        new Handler().postDelayed(new Runnable() {
            //private Boolean loginResult;
            @Override
            public void run() {
                // This method will be executed once the timer is over
                loginResult = clientLogin.getLoginResult();
                Log.d("Login Result", loginResult.toString());

                if (loginResult == false) {
                    // set help text
                    helpText = "Username or password not found. Please register if needed.";
                    clientOutput.displayString("Incorrect username or password. If you are not registered please do so now.");
                    Log.d("Login", "false");
                    Log.d("Helptext", helpText);
                    setLoginResult(loginResult);
                } else {
                    // start new intent aka display available games
                    Intent loginIntent = new Intent(act, GameTypeActivity.class);
                    Log.d("Login", "true");
                    setLoginResult(loginResult);
                    act.startActivity(loginIntent);
                }
            }
        }, 7000);
    }

    public void startGame(TextView textView, Activity act, EditText editText) {
        //ClientInputInterface clientInput = new GUIConsoleInput(editText,act);
        //ClientOutputInterface clientOutput = new GUITextDisplay(textView,act);

        Log.d("Test Connection", "Test Connection");
        GUIClient client = new GUIClient(clientInput, clientOutput, connection);
        //GUIClient client = new GUIClient(clientInput, clientOutput, addr, port);
        //client.start();
    }

    public Boolean getLoginResult() {
        return this.loginResult;
    }

    public void setLoginResult(Boolean login) {
        this.loginResult = login;
    }

    public String getHelpText() {
        return this.helpText;
    }

    public void setHelpText(String text) {
        this.helpText = text;
    }


    public void chooseRegions(final TextView helpText, String regionGroup) {
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIClientRegionSelection selection = new GUIClientRegionSelection(regionGroup, connection, clientInput, clientOutput, act);
        selection.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // TODO: where we actually play the game - DisplayMapActivity.java
                Log.d("Game", "Starting");
                clientOutput.displayString("Waiting for board from server");
                clientOutput = new GUITextDisplay();
                displayServerBoard(helpText);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent newGame= new Intent(act, DisplayMapActivity.class);
//                        act.startActivity(newGame);
//                    }
//                },2000);
            }
        }, 2000);
    }
    //set master board
    public void displayServerBoard(TextView helpText){
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(true,connection,clientInput,clientOutput,act);
        guiPlayGame.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // boolean gotBoard = guiPlayGame.isGotBoard();
                Intent newGame= new Intent(act, DisplayMapActivity.class);
                 act.startActivity(newGame);
            }
        },3000);
    }
    public void playGame(TextView helpText,List<OrderInterface> orders){
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(orders,false, connection, clientInput, clientOutput, act);
        guiPlayGame.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(act,WaitActivity.class);
                act.startActivity(intent);
            }
        },6000);
    }
}
