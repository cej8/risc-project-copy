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
   // Boolean loginResult;
    Activity act;
    String helpText;
    GameStateModel model;

    public ExecuteClient(Activity activity) {
        clientInput = new GUIEditTextInput(activity);
        clientOutput = new GUITextDisplay();
        this.act = activity;
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setActivity(act);
        this.model = new GameStateModel();
    }
    public ExecuteClient(Activity activity, TextView textHelp){
        clientInput = new GUIEditTextInput(activity);
        //clientOutput = new GUITextDisplay();
        this.act = activity;
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setActivity(act);
        this.model = new GameStateModel();
        clientOutput = new GUITextDisplay(textHelp);
        parentActivity.setClientOutput(clientOutput);
    }


    public void createGame() {
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
        GUIClient guiClient = new GUIClient(clientInput,clientOutput,addr,port);
        // Start Client thread with server
        guiClient.start();
        model.setConnection(true);
    }
    public void loginGame(String username, String password, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
       // clientOutput = new GUITextDisplay(textHelp);
       // final GUIClientLogin clientLogin = new GUIClientLogin(connection, clientInput, clientOutput, act);
        //clientLogin.start();
        model.setLoginUsername(username);
        model.setLoginPassword(password);
        //Boolean loginResult = clientLogin.getLoginResult();
//        while (loginResult == null) {
//            loginResult = clientLogin.getLoginResult();
//        }
       // Log.d("Login Result", loginResult.toString());
//        boolean loginResult = model.getLoginResult();
//        if (loginResult) {
//            Intent loginIntent = new Intent(act, GameTypeActivity.class);
//            Log.d("Login", "true");
//            act.startActivity(loginIntent);
//        }
    }
    public void registerLogin(String username, String password, String confirmPassword, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
        clientOutput = new GUITextDisplay(textHelp);
        final GUIClientLogin clientLogin = new GUIClientLogin(false,connection, clientInput, clientOutput,act);
        // clientLogin.start();
        Boolean loginResult = clientLogin.getLoginResult();
        while (loginResult == null){
            loginResult = clientLogin.getLoginResult();
        }
        loginResult = clientLogin.getLoginResult();
        Log.d("Login Result", loginResult.toString());

        if (!loginResult) {
            // set help text
            //helpText.setText("Username or password not found. Please register if needed.");
            helpText = "User already exists. Please choose another username";
            setHelpText(helpText);
            Log.d("Login", "false");
            Log.d("Helptext", helpText);
            // setLoginResult(loginResult);
        } else {
            // start new intent aka display available games
            Intent loginIntent = new Intent(act, GameTypeActivity.class);
            Log.d("Login", "true");
            // setLoginResult(loginResult);
            act.startActivity(loginIntent);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public void getGames(boolean gameType, boolean getgame) {
        // clientOutput = new GUITextDisplay(gameText,act);
        final GUISelectGame selectGame = new GUISelectGame(getgame, gameType, connection, clientInput, clientOutput, act);
        selectGame.start();
        Boolean gotGames = selectGame.getGotGames();
        while (gotGames == null){
            gotGames = selectGame.getGotGames();
        }
        String games = selectGame.getGameList();
        Log.d("Game List", games);
        Intent gamesIntent = new Intent(act, NewGameActivity.class);
        gamesIntent.putExtra("GAMELIST", games);
        act.startActivity(gamesIntent);
    }

    public void pickGame(boolean gameType, String id, boolean getgame, String gameList) {
        GUISelectGame selectGame = new GUISelectGame(getgame, id, gameType, connection, clientInput, clientOutput, act);
        selectGame.start();
        Boolean pickedGames = selectGame.getPickedGames();
        while (pickedGames == null){
            pickedGames = selectGame.getPickedGames();
        }
        Log.d("Game", "Waitiing for players");
        Intent lobby= new Intent(act, PlayerLobbyActivity.class);
        //placement.putExtra("GAMELIST", games);
        act.startActivity(lobby);
    }

    public String getHelpText() {
        return this.helpText;
    }

    public void setHelpText(String text) {
        this.helpText = text;
    }

    public void startPlacement(){
        Board board = ParentActivity.getBoard();
        if(board!=null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // TODO: where we actually play the game - DisplayMapActivity.java
                    Log.d("Game", "Received board");
                    clientOutput = new GUITextDisplay();
                    Intent newGame= new Intent(act, ChooseRegionsActivity.class);
                    act.startActivity(newGame);
                }
            }, 2000);
        }


    }
    public void getBoardAssignments(TextView helpText) {
        clientOutput = new GUITextDisplay(helpText);
        final GUIWaitingRoom initializeBoard = new GUIWaitingRoom(connection, clientInput, clientOutput, act);
        initializeBoard.start();

        while(!initializeBoard.getDoneRunning()){
//wait for board to come in

        }

    }
public void showStartBoard(TextView boardView) {
    clientOutput = new GUITextDisplay(boardView);
    clientOutput.displayBoard(ParentActivity.getBoard());
}

    public void chooseRegions(final TextView boardView, String regionGroup) {
        // TODO:
        // Model.setStartGroup(regionGroup);
        // Model.setClientOutput();
        // Model.getBoard(); // wait for board
       clientOutput = new GUITextDisplay(boardView);
        final GUIClientRegionSelection selection = new GUIClientRegionSelection(false,regionGroup, connection, clientInput, clientOutput, act);
        selection.start();
        while(!selection.getRegionChosen()) {
            //wait for thread to return
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
        // TODO: change to placement selection activity
        Log.d("Game", "Starting");
        //clientOutput.displayString("Waiting for board from server");
        clientOutput = new GUITextDisplay();
                Intent firstUnits= new Intent(act, PlaceUnitsActivity.class);
                act.startActivity(firstUnits);
        //displayServerBoard(boardView);
    }
}, 2000);
    }
    //set master board
    public void displayServerBoard(TextView helpText){
        clientOutput = new GUITextDisplay(helpText);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(true,connection,clientInput,clientOutput,act);
        guiPlayGame.start();
        while (!guiPlayGame.isGotBoard()){
            // wait
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // boolean gotBoard = guiPlayGame.isGotBoard();
                Intent firstUnits= new Intent(act, DisplayMapActivity.class);
                 act.startActivity(firstUnits);
            }
        },3000);
    }
    public void playGame(TextView helpText,List<OrderInterface> orders){
        clientOutput = new GUITextDisplay(helpText);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(orders,false, connection, clientInput, clientOutput, act);
        guiPlayGame.start();
        while (!guiPlayGame.getTurnOver()){
            // wait for it to return
        }
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.resetOrders();
        Intent intent = new Intent(act,WaitActivity.class);
        act.startActivity(intent);
    }
    public void placementOrder(){
       // clientOutput = new GUITextDisplay(helpText, act);
        GUIClientPlacementSelection guiClientPlacementSelection = new GUIClientPlacementSelection(connection,clientInput,clientOutput,act);
        guiClientPlacementSelection.start();
        while(!guiClientPlacementSelection.getPlacement()) {
            //wait for thread to return
        }
        // reset Orders list
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.resetOrders();
        Log.d("Placements Completed",guiClientPlacementSelection.getPlacement().toString());
        // display map
        Intent newGame= new Intent(act, WaitActivity.class);
        act.startActivity(newGame);
    }
}
