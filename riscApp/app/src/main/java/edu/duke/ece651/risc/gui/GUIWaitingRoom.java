package edu.duke.ece651.risc.gui;

import android.app.Activity;

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
    //private String regionGroup;
    private boolean firstCall;
    //private boolean waitingForPlayers;
    private boolean doneRunning=false;
    private GameStartModel model;


    private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES + .1;
    private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;
    private long startTime=-1;
    private long maxTime=-1;
    public GUIWaitingRoom(GameStartModel m,Connection connect, ClientInputInterface input, ClientOutputInterface output, Activity act) {
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
        this.activity = act;
        this.model=m;
        // this.board= ParentActivity.getBoard();
        //this.player=ParentActivity.getPlayer();
        //this.firstCall=begin;
        //this.waitingForPlayers = begin;


    }
    public boolean getDoneRunning(){
        return doneRunning;
    }
    public void getInitialBoard(){
        long startTime = -1;
        long maxTime = -1;
        try {
            ParentActivity parentActivity = new ParentActivity();
            parentActivity.setBoard((Board) (connection.receiveObject()));

            this.board = ParentActivity.getBoard();
            model.setStartBoard(this.board);
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
            model.isReadyToBegin(true);
        }
 catch(Exception e) {
                e.printStackTrace();
                connection.closeAll();
            }
//return null;

        }

            public void waitingToStart() {
        try {
            connection.getSocket().setSoTimeout((int) (START_WAIT_MINUTES * 60 * 1000));
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
        }
    }
    public void run() {
        try {
            firstCall = ((ConfirmationMessage) connection.receiveObject()).unpacker();
            //model.setFirstCall(firstCall);
            ParentActivity pa = new ParentActivity();
            pa.setPlayer((HumanPlayer) connection.receiveObject());
            this.player = ParentActivity.getPlayer();
            model.setPlayer(this.player);
        }
        catch(Exception e) {
            e.printStackTrace();
            connection.closeAll();
        }
    if(firstCall){
        waitingToStart();
      getInitialBoard();
    //  doneRunning =true;
     // return;
    }
  //  doneRunning=true;
        }
}
