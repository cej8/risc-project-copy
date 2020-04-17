package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.client.OrderHelper;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.ConfirmationMessage;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.StringMessage;


public class GUIPlayGame extends Thread{
    private Connection connection;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    private boolean isPlaying = true;
    private Activity activity;
    private Board board;
    private long startTime;
    private long maxTime;
    private boolean displayBoard;
    private List<OrderInterface> orders;
    boolean alive;
    private boolean gotBoard;
    private boolean turnOver = false;
    private String winnerPrompt=null;
    private Handler handler;
    private ParentActivity parentActivity = new ParentActivity();

    private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES+.1;
    private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;
    // display board constructor
    public GUIPlayGame(Handler handler,boolean displayBoard,Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.board = ParentActivity.getBoard();
        this.displayBoard = displayBoard;
        this.gotBoard = false;
        this.handler = handler;
    }
    // send orders
    public GUIPlayGame(Handler handler,List<OrderInterface> orders,boolean displayBoard,Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.board = ParentActivity.getBoard();
        this.displayBoard = displayBoard;
        this.orders = orders;
        this.handler = handler;
    }
    public boolean getAlive(){
        return this.alive;
    }
    public boolean getTurnOver(){return this.turnOver;}

    public boolean timeOut(long startTime, long maxTime){
        // If too long --> kill player (prevent trying to write to closed pipe)
        if (System.currentTimeMillis() - startTime > maxTime) {
            clientOutput.displayString("Player took too long, killing connection");
            connection.closeAll();
            clientInput.close();
            return true;
        }
        return false;
    }
    public void setWinner(String prompt){
        this.winnerPrompt=prompt;
    }
    public String getWinner(){
        return this.winnerPrompt;
    }
    public void serverDisplayBoard(){
        try {
            if (alive != isPlaying) {
                isPlaying = alive;
                //Query for spectating
                //If no then kill connection
                if (!queryYNAndRespond("Would you like to keep spectating? [Y/N]")) {
                    // TODO: no spectating allowed for now
                    connection.closeAll();
                    clientInput.close();
                }
            }
            // Next server sends board
            board = (Board) (connection.receiveObject());
            // ParentActivity parentActivity = new ParentActivity();
            parentActivity.setBoard(board);
            gotBoard = true;
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            return;
        }
    }
    public void checkAlive(){
        try {
           // while (true) {
                String turn = receiveAndDisplayString();
               // ParentActivity parentActivity = new ParentActivity();
                //startTime = System.currentTimeMillis();
                parentActivity.setStartTime(System.currentTimeMillis());
                parentActivity.setMaxTime((long) (connection.getSocket().getSoTimeout()));//(long) (connection.getSocket().getSoTimeout());
                //Catch case for issues in testing, should never really happen
                if (ParentActivity.getMaxTime() == 0) {
                    parentActivity.setMaxTime((long) (TURN_WAIT_MINUTES * 60 * 1000)); //= (long) (TURN_WAIT_MINUTES * 60 * 1000);
                }
                // Start of each turn will have continue message if game still going
                // Otherwise is winner message
                StringMessage startMessage = (StringMessage) (connection.receiveObject());
                String start = startMessage.unpacker();
                if (!start.equals("Continue")) {
                    // If not continue then someone won --> print and exit
                  //  clientOutput.displayString(start);  // help text on map
                    setWinner(start);
                 //   connection.closeAll();
                 //   clientInput.close();
                    return;
                }
                // Next is alive status for player
                ConfirmationMessage isAlive = (ConfirmationMessage) (connection.receiveObject());
                // If null then something wrong
                if (isAlive == null) {
                    return;
                }
                // Get primitive
                alive = isAlive.getMessage();
                parentActivity.setAlive(alive);
                /////------------------------///// Separate method
                // If not same then player died on previous turn --> get spectate message
               /* if (alive != isPlaying) {
                    isPlaying = alive;
                    //Query for spectating
                    //If no then kill connection
                    if (!queryYNAndRespond("Would you like to keep spectating? [Y/N]")) {
                        // TODO: no spectating allowed for now
                        connection.closeAll();
                        clientInput.close();
                    }
                }
                // Next server sends board
                board = (Board) (connection.receiveObject());
               // ParentActivity parentActivity = new ParentActivity();
                parentActivity.setBoard(board);
                gotBoard = true;*/
           // }
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            return;
        }
    }
    public boolean isGotBoard(){
        return gotBoard;
    }
    public void playGame() {
        try {
                while (true) {
                    /////TODO probably seperate this part
                    if(ParentActivity.getAlive()){
                        //If too long --> kill player
                        if(timeOut(ParentActivity.getStartTime(), ParentActivity.getMaxTime())){ return;}
                        connection.sendObject(orders);
                    }

                    String response = receiveAndDisplayString();
                    if (response.matches("^Fail:.*$")) {
                        continue;
                    }
                    if (response.matches("^Success:.*$")) {
                        break;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            return;
        }
    }

    //Helper method to ask YN and send back ConfirmationMessage
    public boolean queryYNAndRespond(String query) throws IOException{
        while(true){
            // Request input
            clientOutput.displayString(query);
            //String spectateResponse = clientInput.readInput();
            String spectateResponse = "N";
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
            //clientOutput.displayString("Invalid input.");
        }
    }

    public String receiveAndDisplayString() throws IOException, ClassNotFoundException{
        StringMessage message = (StringMessage) (connection.receiveObject());
        String str = message.unpacker();
        clientOutput.displayString(str);
        return str;
    }

    @Override
    public void run(){
        if (displayBoard == true) {
            checkAlive();
            if (ParentActivity.getAlive() == true) {
                serverDisplayBoard();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(getWinner()!=null){
                            //game over someone has won
                            Intent end = new Intent(activity, EndGameActivity.class);
                            end.putExtra("WINNER", getWinner());
                            activity.startActivity(end);
                            //return;
                        } else {
                            Intent firstUnits = new Intent(activity, DisplayMapActivity.class);
                            activity.startActivity(firstUnits);
                        }
                    }
                });
            } else { // not alive, so ask if we want to spectate
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(getWinner()!=null){
                            //game over someone has won
                            Intent end = new Intent(activity, EndGameActivity.class);
                            end.putExtra("WINNER", getWinner());
                            activity.startActivity(end);
                            //return;
                        } else {
                            Intent spectateIntent = new Intent(activity, SpectateChoiceActivity.class);
                            activity.startActivity(spectateIntent);
                        }
                    }
                });
            }
        } else {
            playGame();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ParentActivity parentActivity = new ParentActivity();
                    parentActivity.resetOrders();
                    Intent intent = new Intent(activity,WaitActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
