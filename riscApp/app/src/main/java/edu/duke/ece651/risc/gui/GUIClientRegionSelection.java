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

public class GUIClientRegionSelection extends Thread implements ClientInterface{
    private Connection connection;
    private Board board;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    private HumanPlayer player;
    private Activity activity;
    private String regionGroup;


    private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES+.1;
    private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;

    private boolean firstCall = true;


    public GUIClientRegionSelection(String region,Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.regionGroup= region;
        this.board= ParentActivity.getBoard();
        this.player=ParentActivity.getPlayer();

    }

    public boolean chooseRegions() {

        //Initial to -1 for timers, don't set until turn actually starts
        long startTime = -1;
        long maxTime = -1;

        try {
            // Set timeout to constant, wait this long for game start
            // This will block on FIRST board = ...
            connection.getSocket().setSoTimeout((int) (START_WAIT_MINUTES * 60 * 1000));
            while (true) {
                // Game starts with board message
                board = (Board) (connection.receiveObject());
                // Return timeout to smaller value
                connection.getSocket().setSoTimeout((int) (TURN_WAIT_MINUTES * 60 * 1000));

                //Set max/start first time board received (start of turn)
                if(maxTime == -1){
                    maxTime = (long) (connection.getSocket().getSoTimeout());
                    //Catch case for issues in testing, should never really happen
                    if (maxTime == 0) {
                        maxTime = (long) (TURN_WAIT_MINUTES * 60 * 1000);
                    }
                }
                if(startTime == -1){
                    startTime = System.currentTimeMillis();
                }

                // Output board
                clientOutput.displayBoard(board);
                // Print prompt and get group name
                clientOutput.displayString("Please select a starting group by typing in a group name (i.e. 'Group A')");
                String groupName = clientInput.readInput();
                if(timeOut(startTime, maxTime)) { return false; }
                connection.sendObject(new StringMessage(groupName));

                // Wait for response
                StringMessage responseMessage = (StringMessage) (connection.receiveObject());
                String response = responseMessage.unpacker();
                clientOutput.displayString(response);
                if (response.matches("^Fail:.*$")) {
                    continue;
                }
                if (response.matches("^Success:.*$")) {
                    break;
                }
            }
            while (true) {
                // Server then sends board again
                board = (Board) (connection.receiveObject());

                // Display and move into placements
                clientOutput.displayBoard(board);
                OrderCreator placement = OrderFactoryProducer.getOrderCreator("P", (edu.duke.ece651.risc.client.ClientInterface) this);
                List<OrderInterface> placementOrders = new ArrayList<OrderInterface>();
                placement.addToOrderList(placementOrders);
                if(timeOut(startTime, maxTime)) { return false; }
                connection.sendObject(placementOrders);

                // Wait for response
                StringMessage responseMessage = (StringMessage) (connection.receiveObject());
                String response = responseMessage.unpacker();
                clientOutput.displayString(response);
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
            return false;
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
    @Override
    public void run() {
        if(firstCall) {
            if(!chooseRegions()) {return; }
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
