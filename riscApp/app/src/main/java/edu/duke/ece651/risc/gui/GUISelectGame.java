package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
    private boolean correctID;

    public GUISelectGame(String gameList, Handler newGameHandler,boolean getGames, String gameID,boolean bool, Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
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
        this.gameList = gameList;
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
    public void performGetGame() throws IOException, ClassNotFoundException{
        connection.sendObject(new ConfirmationMessage(oldBoolean));
        //Server then sends back list of games
        StringMessage message = (StringMessage) (connection.receiveObject());
        gameList = message.unpacker();
        this.gotGames = true;
    }
    //Method to mesh with selectGame() in loginServer
    public void performSelectGame() throws IOException, ClassNotFoundException{
            Integer gameID;
            while(true){
                try{
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
            StringMessage message = (StringMessage) (connection.receiveObject());
            String response = message.unpacker();
            //Repeat if fail, continue if success
            if (response.matches("^Fail:.*$")) {
                this.correctID = false;
                return;
            }
            if (response.matches("^Success:.*$")) {
                this.pickedGames = true;
                this.correctID = true;
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
                if (correctID) {
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
                } else {
                    // entered wrong ID number
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
            }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("GUISelectGame","IOException, run");
            connection.closeAll();
            clientInput.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("GUISelectGame","ClassNotFoundException, run");
            connection.closeAll();
            clientInput.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
