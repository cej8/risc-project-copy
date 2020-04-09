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
    LoginModel loginModel;
    GameStartModel gameStartModel;
    boolean firstCall;
    ParentActivity parentActivity;

    public ExecuteClient(Activity activity) {
        clientInput = new GUIEditTextInput(activity);
        clientOutput = new GUITextDisplay();
        this.act = activity;
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setActivity(act);
        this.loginModel = new LoginModel();
        this.gameStartModel= new GameStartModel();
    }
    public ExecuteClient(Activity activity, TextView textHelp){
        clientInput = new GUIEditTextInput(activity);
        //clientOutput = new GUITextDisplay();
        this.act = activity;
        parentActivity = new ParentActivity();
        parentActivity.setActivity(act);
        this.loginModel = new LoginModel();
        clientOutput = new GUITextDisplay(textHelp);
        parentActivity.setClientOutput(clientOutput);
        this.gameStartModel= new GameStartModel();
    }


    public void createGame() throws InterruptedException {
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

        ClientGUI clientGUI= new ClientGUI(loginModel,clientInput,clientOutput,addr,port);
        clientGUI.start();


    }
    public void registrationAlert(boolean registrationAlert){
        loginModel.setRegistrationAlert(registrationAlert);
        loginModel.setRegistrationReady(true);
        if (registrationAlert){
            Intent loginIntent = new Intent(act,LoginActivity.class);
            act.startActivity(loginIntent);
        } else {
            Intent regIntent = new Intent(act,RegisterActivity.class);
            act.startActivity(regIntent);
        }
    }
    public void loginGame(String username, String password, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
       //parentActivity.setClientOutput(new GUITextDisplay(textHelp));
        loginModel.setLoginUsername(username);
        loginModel.setLoginPassword(password);
       // loginModel.setRegistrationAlert(true);
        loginModel.isLoginBooleanReady(true);
           //  Log.d("line test","sadsalsaldkj");
        if(loginModel.getLoginResult()) {
        // TODO: error handling if wrong login, currently still sends you to GameTypeActivity.class
            Intent loginIntent = new Intent(act, GameTypeActivity.class);
            Log.d("Login", "true");
            act.startActivity(loginIntent);
        }
    }
    public void registerLogin(String username, String password, String confirmPassword, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
      //  parentActivity.setClientOutput(new GUITextDisplay(textHelp));
        loginModel.setLoginUsername(username);
        loginModel.setLoginPassword(password);
        loginModel.setRegisterPassword(confirmPassword);
        loginModel.isLoginBooleanReady(true);
        if(loginModel.getLoginResult()) {
            Intent loginIntent = new Intent(act, GameTypeActivity.class);
            Log.d("Register Login", "true");
            act.startActivity(loginIntent);
        }
    }




    public void getGames(boolean gameType, boolean getgame) throws InterruptedException {
     //   parentActivity.setClientOutput = new GUITextDisplay(gameText,act);
        //final GUISelectGame selectGame = new GUISelectGame(getgame, gameType, connection, clientInput, clientOutput, act);
       // selectGame.start();
       // Boolean gotGames = selectGame.getGotGames();
        //while (gotGames == null){
          //  gotGames = selectGame.getGotGames();
        //}
        gameStartModel.setOldBoolean(gameType);
        String games = gameStartModel.getGameList();
        Log.d("Game List", games);
        Intent gamesIntent = new Intent(act, NewGameActivity.class);
        gamesIntent.putExtra("GAMELIST", games);
        act.startActivity(gamesIntent);
    }

    public void pickGame(boolean gameType, String id, boolean getgame, String gameList) {
        gameStartModel.setOldBoolean(gameType);
        gameStartModel.setGameList(gameList);
        gameStartModel.setGameNumber(id);
    // GUISelectGame selectGame = new GUISelectGame(getgame, id, gameType, connection, clientInput, clientOutput, act);
        //selectGame.start();
        //Boolean pickedGames = selectGame.getPickedGames();
        //while (pickedGames == null){
          //  pickedGames = selectGame.getPickedGames();
        //}
        Log.d("Game", "Waitiing for players");
        Intent lobby= new Intent(act, PlayerLobbyActivity.class);
        //placement.putExtra("GAMELIST", games);
        act.startActivity(lobby);
    }
    public void getBoardAssignments(TextView helpText) throws InterruptedException {
        parentActivity.setClientOutput(new GUITextDisplay(helpText));
       // final GUIWaitingRoom initializeBoard = new GUIWaitingRoom(connection, clientInput, clientOutput, act);
        //initializeBoard.start();
        boolean ready= gameStartModel.readyToBegin();


    }

    public void startPlacement() throws InterruptedException {
      //  Board board = ParentActivity.getBoard();
        Board board=gameStartModel.getStartBoard();
                    // This method will be executed once the timer is over
                    // TODO: where we actually play the game - DisplayMapActivity.java
                    Log.d("Game", "Received board");
          //          clientOutput = new GUITextDisplay();
                    Intent newGame= new Intent(act, ChooseRegionsActivity.class);
                    act.startActivity(newGame);


    }

    public void showStartBoard(TextView boardView) {
        clientOutput = new GUITextDisplay(boardView);
        clientOutput.displayBoard(ParentActivity.getBoard());
    }
    public void chooseRegions(final TextView boardView, String regionGroup) throws InterruptedException {
        // TODO:
         gameStartModel.setStartGroup(regionGroup);
        // Model.setClientOutput();
        gameStartModel.getStartBoard(); // wait for board
       // clientOutput = new GUITextDisplay(boardView);
        //final GUIClientRegionSelection selection = new GUIClientRegionSelection(false,regionGroup, connection, clientInput, clientOutput, act);
        //selection.start();
        //while(!selection.getRegionChosen()) {
            //wait for thread to return
        //}
        //new Handler().postDelayed(new Runnable() {
           // @Override
            //public void run() {
                // This method will be executed once the timer is over
                // TODO: change to placement selection activity
                Log.d("Game", "Starting");
                //clientOutput.displayString("Waiting for board from server");
                clientOutput = new GUITextDisplay();
                Intent firstUnits= new Intent(act, PlaceUnitsActivity.class);
                act.startActivity(firstUnits);
                //displayServerBoard(boardView);
          //  }
        //}, 2000);
    }
    public String getHelpText() {
        return this.helpText;
    }

    public void setHelpText(String text) {
        this.helpText = text;
    }


    //set master board
    public void displayServerBoard(TextView helpText){
        clientOutput = new GUITextDisplay(helpText);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(true,connection,clientInput,clientOutput,act);
        guiPlayGame.start();
        while (!guiPlayGame.isGotBoard()&&(guiPlayGame.getWinner()==null)){
            // wait
        }
        if(guiPlayGame.getWinner()!=null){
            //game over someone has won
            Intent end = new Intent(act, EndGameActivity.class);
            end.putExtra("WINNER", guiPlayGame.getWinner());
            act.startActivity(end);
            return;

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
    public void endGame(){

           connection.closeAll();
           clientInput.close();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
