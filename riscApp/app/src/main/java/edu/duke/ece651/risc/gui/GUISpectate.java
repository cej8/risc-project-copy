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
import edu.duke.ece651.risc.shared.Constants;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.StringMessage;

public class GUISpectate extends Thread {
    private ParentActivity parentActivity;
    private boolean response;
    private Connection connection;
    private boolean alive;
    private Board board;
    private boolean gotBoard;
    private Activity activity;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES+.1;
    private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;
    private boolean isPlaying = true;
    private String winnerPrompt=null;
    private Handler handler;

    public GUISpectate(Handler h,Activity act,ClientInputInterface input,ClientOutputInterface output){
        this.response = ParentActivity.getSpectate();
        this.parentActivity = new ParentActivity();
        this.connection = ParentActivity.getConnection();
        this.alive = ParentActivity.getAlive();
        this.activity = act;
        this.clientInput = input;
        this.clientOutput = output;
        this.handler = h;
    }

    public void serverDisplayBoard(){
        try {
            if (alive != isPlaying) {
                isPlaying = alive;
                //Query for spectating
                //If no then kill connection
                connection.sendObject(new ConfirmationMessage(response));
                if (!response) {
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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
            return;
        }
    }
    public void setWinner(String prompt){
        this.winnerPrompt=prompt;
    }
    public String getWinner(){
        return this.winnerPrompt;
    }
    public void checkAlive(){
        try {
            parentActivity.setPlayer((HumanPlayer)connection.receiveObject());

            String turn = receiveAndDisplayString();
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
                setWinner(start);
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
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
            return;
        }
    }
    public String receiveAndDisplayString() throws IOException, ClassNotFoundException{
        StringMessage message = (StringMessage) (connection.receiveObject());
        String str = message.unpacker();
        return str;
    }

    public void allSpectate(){
        try {
            parentActivity.setPlayer((HumanPlayer)connection.receiveObject());

            String turn = receiveAndDisplayString();
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
                if (alive != isPlaying) {
                    isPlaying = alive;
                    //Query for spectating
                    //If no then kill connection
                    connection.sendObject(new ConfirmationMessage(response));
                    if (!response) {
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
                while (true) {
                // Next server sends board
                board = (Board) (connection.receiveObject());
                parentActivity.setBoard(board);
                gotBoard = true;

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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
            return;
        }
    }

    public void recieveBoard(){
        try {
            // Next server sends board
            board = (Board) (connection.receiveObject());
            // ParentActivity parentActivity = new ParentActivity();
            parentActivity.setBoard(board);
            gotBoard = true;

            //   while (true) {
            String response = receiveAndDisplayString();
            if (response.matches("^Fail:.*$")) {
               // continue;
            }
            if (response.matches("^Success:.*$")) {
              //  break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
            return;
        }
    }

    public void playGame() {
        try {
                String response = receiveAndDisplayString();
                if (response.matches("^Fail:.*$")) {
                }
                if (response.matches("^Success:.*$")) {
                }
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    activity.startActivity(intent);
                }
            });
            return;
        }
    }

    @Override
    public void run(){
        if (ParentActivity.getSpectateFirstCall()){
            parentActivity.setSpectateFirstCall(false);
            serverDisplayBoard();
            playGame();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(getWinner()!=null){
                        //game over someone has won
                        Intent end = new Intent(activity, EndGameActivity.class);
                        end.putExtra("WINNER", getWinner());
                        activity.startActivity(end);
                    } else {
                        Intent firstUnits = new Intent(activity, SpectateActivity.class);
                        activity.startActivity(firstUnits);
                    }
                }
            });
        } else {
           allSpectate();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(getWinner()!=null){
                        //game over someone has won
                        Intent end = new Intent(activity, EndGameActivity.class);
                        end.putExtra("WINNER", getWinner());
                        activity.startActivity(end);
                    } else {
                        Intent firstUnits = new Intent(activity, SpectateActivity.class);
                        activity.startActivity(firstUnits);
                    }
                }
            });
        }
    }
}
