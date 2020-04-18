package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
      /*  this.connection = makeConnection.getConnection();
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setConnection(connection);*/
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

    }

    public void getGames(Handler gameHandler, boolean gameType, boolean getgame) {
        final GUISelectGame selectGame = new GUISelectGame(gameHandler,getgame, gameType, connection, clientInput, clientOutput, act);
        selectGame.start();

    }

    public void pickGame(Handler newGameHandler,boolean gameType, String id, boolean getgame, String gameList) {
        GUISelectGame selectGame = new GUISelectGame(gameList,newGameHandler,getgame, id, gameType, connection, clientInput, clientOutput, act);
        selectGame.start();
    }

    public void loginGame(Handler loginHandler,String username, String password, TextView textHelp) throws IOException, ClassNotFoundException, InterruptedException {
        clientOutput = new GUITextDisplay(textHelp, act);
        final GUIClientLogin clientLogin = new GUIClientLogin(loginHandler,connection, clientInput, clientOutput, username, password, act);
        clientLogin.start();

    }

    public String getHelpText() {
        return this.helpText;
    }

    public void setHelpText(String text) {
        this.helpText = text;
    }

    public void getBoardAssignments(Button ready,ProgressBar status, Button start, Handler handler, TextView helpText) throws InterruptedException {
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIWaitingRoom initializeBoard = new GUIWaitingRoom(ready,status, start,handler,connection, clientInput, clientOutput, act);
        initializeBoard.start();
    }

    public void showStartBoard(TextView boardView) {
        clientOutput = new GUITextDisplay(boardView, act);
        clientOutput.displayBoard(ParentActivity.getBoard());
    }

    public void chooseRegions(Handler handler,final TextView boardView, String regionGroup) {
       clientOutput = new GUITextDisplay(boardView, act);
        final GUIClientRegionSelection selection = new GUIClientRegionSelection(handler,false,regionGroup, connection, clientInput, clientOutput, act);
        selection.start();
    }
    //set master board
    public void displayServerBoard(Handler handler,TextView helpText){
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(handler,true,connection,clientInput,clientOutput,act);
        guiPlayGame.start();
         }
    public void playGame(Handler handler,TextView helpText,List<OrderInterface> orders){
        clientOutput = new GUITextDisplay(helpText, act);
        final GUIPlayGame guiPlayGame = new GUIPlayGame(handler,orders,false, connection, clientInput, clientOutput, act);
        guiPlayGame.start();
    }
    public void placementOrder(Handler handler){
       // clientOutput = new GUITextDisplay(helpText, act);
        GUIClientPlacementSelection guiClientPlacementSelection = new GUIClientPlacementSelection(handler,connection,clientInput,clientOutput,act);
        guiClientPlacementSelection.start();
    }
    public void spectate(boolean response,Handler handler){
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setSpectate(response);
        GUISpectate guiSpectate = new GUISpectate(handler,act,clientInput,clientOutput);
        guiSpectate.start();
    }

    public void endGame(){
           connection.closeAll();
           clientInput.close();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    public void endGameReturn(){
        connection.closeAll();
        clientInput.close();
        Intent intent = new Intent(act,ConfirmLoginActivity.class);
        act.startActivity(intent);
        //android.os.Process.killProcess(android.os.Process.myPid());
       // System.exit(1);
    }
}
