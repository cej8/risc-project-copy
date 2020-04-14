package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.net.SocketException;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.ConfirmationMessage;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;
import edu.duke.ece651.risc.shared.HumanPlayer;

public class GUIWaitingRoom extends Thread {
    private Connection connection;
    private Board board;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;
    private HumanPlayer player;
    private Activity activity;
   private Handler handler;
   private Button startGame;
   private ProgressBar status;
   private Button ready;


    private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES + .1;
    private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;

    private long startTime=-1;
    private long maxTime=-1;
    public GUIWaitingRoom(Button ready,ProgressBar p,Button b, Handler h, Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act) {
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.handler=h;
        this.startGame= b;
        this.status=p;
        this.player = ParentActivity.getPlayer();
        this.ready = ready;
    }

    public void getInitialBoard(){
        long startTime = -1;
        long maxTime = -1;
        try {
            connection.getSocket().setSoTimeout((int) (START_WAIT_MINUTES * 60 * 1000));
             this.board = (Board) (connection.receiveObject());
            Log.d("Game", "Received board");
            ParentActivity parentActivity = new ParentActivity();
             parentActivity.setBoard(board);
            // Return timeout to smaller value
            connection.getSocket().setSoTimeout((int) (TURN_WAIT_MINUTES * 60 * 1000));

            //Set max/start first time board received (start of turn)
            if (maxTime == -1) {
                // maxTime=(long) (connection.getSocket().getSoTimeout());
                parentActivity.setMaxTime((long) (connection.getSocket().getSoTimeout()));
                //Catch case for issues in testing, should never really happen
                if (maxTime == 0) {
                    //maxTime = (long) (TURN_WAIT_MINUTES * 60 * 1000);
                    parentActivity.setMaxTime((long) (TURN_WAIT_MINUTES * 60 * 1000));
                }
            }
            if (startTime == -1) {
                //startTime = System.currentTimeMillis();
                parentActivity.setStartTime(System.currentTimeMillis());
            }
            // return this.board;
        }
        catch(Exception e) {
            e.printStackTrace();
            connection.closeAll();
        }
    }

    public void setSocketTimeout(int timeout) throws SocketException {
        connection.getSocket().setSoTimeout(timeout);
    }
    public void run() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ready.setVisibility(View.INVISIBLE);
                status.setVisibility(View.VISIBLE);
            }
        });
        try {
          ParentActivity pa = new ParentActivity();
          pa.setPlayer((HumanPlayer) connection.receiveObject());
               this.player = ParentActivity.getPlayer();
               Log.d("Player",ParentActivity.getPlayer().getName());
   
            setSocketTimeout((int)(60*START_WAIT_MINUTES*1000));
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d("SetSocketTimeout","Exception");
            connection.closeAll();
        }

        Log.d("Connection",ParentActivity.getConnection().toString());
       //waitingToStart();
        getInitialBoard();
        handler.post(new Runnable() {
            @Override
            public void run() {

                    status.setVisibility(View.INVISIBLE);
                    startGame.setEnabled(true);
            }
        });
    }
}
