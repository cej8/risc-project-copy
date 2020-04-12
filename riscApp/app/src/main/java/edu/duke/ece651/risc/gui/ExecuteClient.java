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

    public ExecuteClient(Activity activity) {
        clientInput = new GUIEditTextInput(activity);
        clientOutput = new GUITextDisplay();
        this.act = activity;
    }

    //public void createGame(){
    public void createGame() {
        String addr = "152.3.64.158";
        String portS = "12345";
        int port;
        try {
            port = Integer.parseInt(portS);
        } catch (NumberFormatException ne) {
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

    public void registerLogin(Handler regHandler,String username, String password, String confirmPassword, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
        clientOutput = new GUITextDisplay(textHelp, act);
        final GUIClientLogin clientLogin = new GUIClientLogin(regHandler,connection, clientInput, clientOutput, username, password, act, confirmPassword);
        clientLogin.start();
      /*  Boolean loginResult = clientLogin.getLoginResult();
        while (loginResult == null){
            loginResult = clientLogin.getLoginResult();
        }
        loginResult = clientLogin.getLoginResult();
        Log.d("Login Result", loginResult.toString());

        if (loginResult == false) {
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
        }*/
    }

    public void getGames(Handler gameHandler, boolean gameType, boolean getgame) {
        final GUISelectGame selectGame = new GUISelectGame(gameHandler,getgame, gameType, connection, clientInput, clientOutput, act);
        selectGame.start();
        /*Boolean gotGames = selectGame.getGotGames();
        while (gotGames == null){
            gotGames = selectGame.getGotGames();
        }
        String games = selectGame.getGameList();
        Log.d("Game List", games);
        Intent gamesIntent = new Intent(act, NewGameActivity.class);
        gamesIntent.putExtra("GAMELIST", games);
        act.startActivity(gamesIntent);*/
    }

    public void pickGame(Handler newGameHandler,boolean gameType, String id, boolean getgame, String gameList) {
        GUISelectGame selectGame = new GUISelectGame(newGameHandler,getgame, id, gameType, connection, clientInput, clientOutput, act);
        selectGame.start();
      /*  Boolean pickedGames = selectGame.getPickedGames();
        while (pickedGames == null){
            pickedGames = selectGame.getPickedGames();
        }
        Log.d("Game", "Waitiing for players");
        Intent lobby= new Intent(act, PlayerLobbyActivity.class);
        act.startActivity(lobby);*/
    }

    public void loginGame(Handler loginHandler,String username, String password, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
        clientOutput = new GUITextDisplay(textHelp, act);
        final GUIClientLogin clientLogin = new GUIClientLogin(loginHandler,connection, clientInput, clientOutput, username, password, act);
        clientLogin.start();
      /*  Boolean loginResult = clientLogin.getLoginResult();
        while (loginResult == null) {
            loginResult = clientLogin.getLoginResult();
        }

       // clientLogin.close();
            Log.d("Login Result", loginResult.toString());

            if (loginResult == false) {
                // set help text
                helpText = "Username or password not found. Please register if needed.";
                clientOutput.displayString("Incorrect username or password. If you are not registered please do so now.");
                Log.d("Login", "false");
                Log.d("Helptext", helpText);
            } else {
                // start new intent aka display available games
                Intent loginIntent = new Intent(act, GameTypeActivity.class);
                Log.d("Login", "true");
                //setLoginResult(loginResult);
                act.startActivity(loginIntent);
            }*/
    }

    public void startGame(TextView textView, Activity act, EditText editText) {
        //ClientInputInterface clientInput = new GUIConsoleInput(editText,act);
        //ClientOutputInterface clientOutput = new GUITextDisplay(textView,act);

        Log.d("Test Connection", "Test Connection");
        GUIClient client = new GUIClient(clientInput, clientOutput, connection);
        //GUIClient client = new GUIClient(clientInput, clientOutput, addr, port);
        //client.start();
    }

    public String getHelpText() {
        return this.helpText;
    }

    public void setHelpText(String text) {
        this.helpText = text;
    }

   /* public void startPlacement(){
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
    }*/
    public void getBoardAssignments(TextView helpText) {
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIWaitingRoom initializeBoard = new GUIWaitingRoom(connection, clientInput, clientOutput, act);
        initializeBoard.start();
        /*while(!initializeBoard.getDoneRunning()){
//wait for board to come in
        }*/

    }
    public void showStartBoard(TextView boardView) {
        clientOutput = new GUITextDisplay(boardView, act);
        clientOutput.displayBoard(ParentActivity.getBoard());
    }

    public void chooseRegions(Handler handler,final TextView boardView, String regionGroup) {
       clientOutput = new GUITextDisplay(boardView, act);
        final GUIClientRegionSelection selection = new GUIClientRegionSelection(handler,false,regionGroup, connection, clientInput, clientOutput, act);
        selection.start();
      /*  while(!selection.getRegionChosen()) {
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
}, 2000);*/
    }
    //set master board
    public void displayServerBoard(Handler handler,TextView helpText){
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(handler,true,connection,clientInput,clientOutput,act);
        guiPlayGame.start();
      /*  boolean play = guiPlayGame.isGotBoard();
        String winner = guiPlayGame.getWinner();
        while (!play&&(winner==null)){
            // wait
            play = guiPlayGame.isGotBoard();
            winner = guiPlayGame.getWinner();
        }
        if(guiPlayGame.getWinner()!=null){
            //game over someone has won
            Intent end = new Intent(act, EndGameActivity.class);
            end.putExtra("WINNER", guiPlayGame.getWinner());
            act.startActivity(end);
            //return;
        }
                Intent firstUnits= new Intent(act, DisplayMapActivity.class);
                 act.startActivity(firstUnits);*/
    }
    public void playGame(Handler handler,TextView helpText,List<OrderInterface> orders){
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(handler,orders,false, connection, clientInput, clientOutput, act);
        guiPlayGame.start();
       /* while (!guiPlayGame.getTurnOver()){
            // wait for it to return
        }
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.resetOrders();
        Intent intent = new Intent(act,WaitActivity.class);
        act.startActivity(intent);*/
    }
    public void placementOrder(Handler handler){
       // clientOutput = new GUITextDisplay(helpText, act);
        GUIClientPlacementSelection guiClientPlacementSelection = new GUIClientPlacementSelection(handler,connection,clientInput,clientOutput,act);
        guiClientPlacementSelection.start();
        /*while(!guiClientPlacementSelection.getPlacement()) {
            //wait for thread to return
        }
        // reset Orders list
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.resetOrders();
        Log.d("Placements Completed",guiClientPlacementSelection.getPlacement().toString());
        // display map
        Intent newGame= new Intent(act, WaitActivity.class);
        act.startActivity(newGame);*/
    }

    public void endGame(){

           connection.closeAll();
           clientInput.close();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
