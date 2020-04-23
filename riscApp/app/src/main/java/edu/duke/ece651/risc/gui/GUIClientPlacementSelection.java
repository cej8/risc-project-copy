package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
    private Handler handler;

    public GUIClientPlacementSelection(Handler handler,Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.board = ParentActivity.getBoard();
        this.player = ParentActivity.getPlayer();
        this.placementOrders = ParentActivity.getOrders();
        this.handler = handler;
    }
    public boolean chooseRegions() {
        try {
            while (true) {

                if(timeOut(ParentActivity.getStartTime(), ParentActivity.getMaxTime())) { return false; }
                connection.sendObject(placementOrders);

                // Wait for response
                StringMessage responseMessage = (StringMessage) (connection.receiveObject());
                String response = responseMessage.unpacker();
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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity,ConfirmLoginActivity.class);
                    intent.putExtra("HELPTEXT","You took too long to submit placements, " +
                            "connection killed. Please login again");
                    activity.startActivity(intent);
                }
            });
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
                //placement = true;
                Log.d("Placement Order", "Successful");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ParentActivity parentActivity = new ParentActivity();
                        parentActivity.resetOrders();
                        // display map
                        Intent newGame= new Intent(activity, WaitActivity.class);
                        activity.startActivity(newGame);
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
