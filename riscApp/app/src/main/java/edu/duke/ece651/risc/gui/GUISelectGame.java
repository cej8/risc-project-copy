package edu.duke.ece651.risc.gui;

import android.app.Activity;

import java.io.IOException;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
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

    public GUISelectGame(boolean getGames, String gameID,boolean bool, Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.oldBoolean = bool;
        this.gameNumber = gameID;
        this.getGames = getGames;
        this.gotGames = null;
        this.pickedGames = null;
    }
    // Get games
    public GUISelectGame(boolean getGames, boolean bool, Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.oldBoolean = bool;
        this.getGames = getGames;
        this.gotGames = null;
        this.pickedGames = null;
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



    }
    public String receiveAndDisplayString() throws IOException, ClassNotFoundException{
        StringMessage message = (StringMessage) (connection.receiveObject());
        String str = message.unpacker();
        clientOutput.displayString(str);
        return str;
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
            if (getGames == true) {
                performGetGame();
            } else {
                performSelectGame();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
