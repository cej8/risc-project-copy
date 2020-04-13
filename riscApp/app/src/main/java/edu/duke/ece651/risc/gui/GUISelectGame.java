package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.ConfirmationMessage;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.IntegerMessage;
import edu.duke.ece651.risc.shared.StringMessage;

public class GUISelectGame extends Thread{
    Activity activity;
    private Connection connection;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    private boolean oldBoolean;
    private String gameNumber;
    private boolean getGames;
    private String gameList;
    private Boolean gotGames;
    private Boolean pickedGames;
    private Handler handler;

    public GUISelectGame(Handler newGameHandler,boolean getGames, String gameID,boolean bool, Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.oldBoolean = bool;
        this.gameNumber = gameID;
        this.getGames = getGames;
        this.gotGames = null;
        this.pickedGames = null;
        this.handler = newGameHandler;
    }
    // Get games
    public GUISelectGame(Handler gameHandler, boolean getGames, boolean bool, Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.oldBoolean = bool;
        this.getGames = getGames;
        this.gotGames = null;
        this.pickedGames = null;
        this.handler = gameHandler;
    }
    public String getGameList(){
        return this.gameList;
    }
    public Boolean getGotGames(){
        return this.gotGames;
    }
    public Boolean getPickedGames(){
        return this.pickedGames;
    }
    public void performGetGame() throws IOException,ClassNotFoundException{
        // boolean oldBoolean = queryYNAndRespond("Would you like to join a game you are already in? [Y/N]");
        connection.sendObject(new ConfirmationMessage(oldBoolean));
        //Server then sends back list of games
        StringMessage message = (StringMessage) (connection.receiveObject());
        gameList = message.unpacker();
        //clientOutput.displayString(str);
        this.gotGames = true;
    }
    //Method to mesh with selectGame() in loginServer
    public void performSelectGame() throws IOException, ClassNotFoundException{
        while(true){
//           // boolean oldBoolean = queryYNAndRespond("Would you like to join a game you are already in? [Y/N]");
//            connection.sendObject(new ConfirmationMessage(oldBoolean));
//            //Server then sends back list of games
//            String list = receiveAndDisplayString();
            Integer gameID;
            while(true){
                //clientOutput.displayString("Pick a game via ID");
                try{
                    //gameID = Integer.parseInt(clientInput.readInput());
                    gameID = Integer.parseInt(gameNumber);
                }
                catch (NumberFormatException ne) {
                    // ne.printStackTrace();
                    //clientOutput.displayString("That was not an integer.");
                    continue;
                }
                break;
            }
            //Send ID to server
            connection.sendObject(new IntegerMessage(gameID));

            //Get back response
            //String response = receiveAndDisplayString();
            StringMessage message = (StringMessage) (connection.receiveObject());
            String response = message.unpacker();
            //clientOutput.displayString(response);
            //Repeat if fail, continue if success
            if (response.matches("^Fail:.*$")) {
                continue;
            }
            if (response.matches("^Success:.*$")) {
                this.pickedGames = true;
                break;
            }
        }
        boolean firstCall = ((ConfirmationMessage) connection.receiveObject()).unpacker();
        ParentActivity pa = new ParentActivity();
        pa.setFirstCall(firstCall);
    }

    @Override
    public void run(){
        try {
            if (getGames == true) {
                performGetGame();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String games = getGameList();
                        Log.d("Game List", games);
                        Intent gamesIntent = new Intent(activity, NewGameActivity.class);
                        gamesIntent.putExtra("GAMELIST", games);
                        activity.startActivity(gamesIntent);
                    }
                });
            } else {
                performSelectGame();
                if (ParentActivity.getFirstCall()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Game", "Waiting for players");
                            Intent lobby = new Intent(activity, PlayerLobbyActivity.class);
                            activity.startActivity(lobby);
                        }
                    });
                } else {
                    // not first time entering
                    HumanPlayer player = (HumanPlayer) (connection.receiveObject());
                    ParentActivity pa = new ParentActivity();
                    pa.setPlayer(player);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Game", "Previously joined");
                            Intent lobby = new Intent(activity, WaitActivity.class);
                            activity.startActivity(lobby);
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
