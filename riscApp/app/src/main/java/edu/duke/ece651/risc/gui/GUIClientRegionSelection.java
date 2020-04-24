package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;



import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.client.OrderCreator;
import edu.duke.ece651.risc.client.OrderFactoryProducer;
import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.StringMessage;
import edu.duke.ece651.risc.shared.ConfirmationMessage;

public class GUIClientRegionSelection extends Thread implements ClientInterface {
    private Connection connection;
    private Board board;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    private HumanPlayer player;
    private Activity activity;
    private String regionGroup;
    private boolean firstCall;
    private boolean regionChosen=false;
    private Handler handler;

    private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES + .1;
    private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;


    public GUIClientRegionSelection(Handler regionHandler,boolean begin, String region, Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act) {
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.regionGroup = region;
        this.board= ParentActivity.getBoard();
        this.player=ParentActivity.getPlayer();
        this.handler = regionHandler;
    }

    public void setSocketTimeout(int timeout) throws SocketException {
        connection.getSocket().setSoTimeout(timeout);
    }
    public boolean chooseStartGroup() {
            try {
                //     Print prompt and get group name
                    String groupName = this.regionGroup;
                    //if (timeOut(startTime, maxTime)) {
                    if(timeOut(ParentActivity.getStartTime(),ParentActivity.getMaxTime())){
                        return false;
                    }
                    connection.sendObject(new StringMessage(groupName));

                    //  Wait for response
                    StringMessage responseMessage = (StringMessage) (connection.receiveObject());
                    String response = responseMessage.unpacker();
                    if (response.matches("^Fail:.*$")) {
                        return false;
                    }
                    if (response.matches("^Success:.*$")) {
                    }
                    ParentActivity parentActivity = new ParentActivity();
                parentActivity.setBoard((Board) (connection.receiveObject()));
                this.board = ParentActivity.getBoard();
            } catch (Exception e) {
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
            return true;
    }
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
    public boolean getRegionChosen(){
        return regionChosen;
    }
    public void repromptChooseRegions(){
        long startTime = -1;
        long maxTime = -1;
        try {
            this.board = (Board) (connection.receiveObject());
            Log.d("Game", "Received board");
            ParentActivity parentActivity = new ParentActivity();
            parentActivity.setBoard(board);
            clientOutput.displayBoard(ParentActivity.getBoard());
            // Return timeout to smaller value
            connection.getSocket().setSoTimeout((int) (TURN_WAIT_MINUTES * 60 * 1000));

            //Set max/start first time board received (start of turn)
            if (maxTime == -1) {
                parentActivity.setMaxTime((long) (connection.getSocket().getSoTimeout()));
                //Catch case for issues in testing, should never really happen
                if (maxTime == 0) {
                    parentActivity.setMaxTime((long) (TURN_WAIT_MINUTES * 60 * 1000));
                }
            }
            if (startTime == -1) {
                parentActivity.setStartTime(System.currentTimeMillis());
            }
        } catch (Exception e){
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
    @Override
    public void run() {
        try {
            if (chooseStartGroup()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent firstUnits= new Intent(activity, PlaceUnitsActivity.class);
                        activity.startActivity(firstUnits);
                    }
                });
            } else {
                ParentActivity pa = new ParentActivity();
                pa.setRepromptBoard("true");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent chooseRegions = new Intent(activity,RepromptRegionsActivity.class);
                        activity.startActivity(chooseRegions);
                    }
                });
            }
        } catch (Exception e) {
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

        @Override
    public ClientOutputInterface getClientOutput() {
        return clientOutput;
    }

    @Override
    public ClientInputInterface getClientInput() {
        return clientInput;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public AbstractPlayer getPlayer() {
        return player;
    }
}
