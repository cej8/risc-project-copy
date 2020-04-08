package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.client.OrderCreator;
import edu.duke.ece651.risc.client.OrderFactoryProducer;
import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.StringMessage;

public class GUIClientPlacementSelection extends Thread implements ClientInterface{
    Connection connection;
    ClientOutputInterface clientOutput;
    ClientInputInterface clientInput;
    Activity activity;
    Board board;
    HumanPlayer player;
    List<OrderInterface> placementOrders;
    private boolean placement = false;

    public GUIClientPlacementSelection(Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.board = ParentActivity.getBoard();
        this.player = ParentActivity.getPlayer();
        this.placementOrders = ParentActivity.getOrders();
    }
    public boolean chooseRegions() {
        try {
            while (true) {

                // Display and move into placements
                // TODO: not actually displaying board??
                //clientOutput.displayBoard(board);//should be in onStart() of placement activity
               //OrderCreator placement = OrderFactoryProducer.getOrderCreator("P", (edu.duke.ece651.risc.client.ClientInterface) this);
               // List<OrderInterface> placementOrders = new ArrayList<OrderInterface>();
                //placement.addToOrderList(placementOrders);
                //  if(timeOut(startTime, maxTime)) { return false; }
                if(timeOut(ParentActivity.getStartTime(), ParentActivity.getMaxTime())) { return false; }
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
    public Boolean getPlacement(){
        return this.placement;
    }
    @Override
    public void run() {
        try {
            if (chooseRegions()) {
                placement = true;
                Log.d("Placement Order", "Successful");
            }
        } catch (Exception e) {
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
