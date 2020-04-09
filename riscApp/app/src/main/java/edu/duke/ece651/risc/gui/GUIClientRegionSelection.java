package edu.duke.ece651.risc.gui;

import android.app.Activity;

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

public class GUIClientRegionSelection {//extends Thread implements ClientInterface {
    private Connection connection;
    private Board board;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    private HumanPlayer player;
    private Activity activity;
    private String regionGroup;
    private boolean firstCall;
    private GameStartModel model;


    private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES + .1;
    private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;


    public GUIClientRegionSelection(GameStartModel m,boolean begin, String region, Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act) {
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.regionGroup = region;
        this.board= ParentActivity.getBoard();
        this.player=ParentActivity.getPlayer();
        this.model=m;
    }

    public void setSocketTimeout(int timeout) throws SocketException {
        connection.getSocket().setSoTimeout(timeout);
    }
    public boolean chooseStartGroup() {
            try {
                while(true) {
                    //Output board

                //    clientOutput.displayBoard(board);
                    //     Print prompt and get group name
                   // clientOutput.displayString("Please select a starting group by typing in a group name (i.e. 'Group A')");

                    String groupName = model.getStartGroup();
                    //String groupName = Model.getGroupName();
                    //if (timeOut(startTime, maxTime)) {
                    if(timeOut(ParentActivity.getStartTime(),ParentActivity.getMaxTime())){
                        return false;
                    }
                    connection.sendObject(new StringMessage(groupName));

                    //  Wait for response
                    StringMessage responseMessage = (StringMessage) (connection.receiveObject());
                    String response = responseMessage.unpacker();
                   // clientOutput.displayString(response);
                    if (response.matches("^Fail:.*$")) {
                        continue;
                    }
                    if (response.matches("^Success:.*$")) {

                        break;
                    }

                }
                model.isRegionChosen(true);
                ParentActivity parentActivity = new ParentActivity();
                parentActivity.setBoard((Board) (connection.receiveObject()));

                this.board = ParentActivity.getBoard();
                model.setStartBoard(this.board);
            } catch (Exception e) {


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
   //  @Override
    public void run() {
        try {
            if (chooseStartGroup()) {
            //    regionChosen=true;
            }
        } catch (Exception e) {

        }
    }


}
