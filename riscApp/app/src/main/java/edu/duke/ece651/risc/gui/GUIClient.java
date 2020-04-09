package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.risc.client.Client;
import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientLogin;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.client.ConnectionManager;
import edu.duke.ece651.risc.client.ConsoleInput;
import edu.duke.ece651.risc.client.OrderCreator;
import edu.duke.ece651.risc.client.OrderFactoryProducer;
import edu.duke.ece651.risc.client.OrderHelper;
import edu.duke.ece651.risc.client.TextDisplay;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.ConfirmationMessage;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.IntegerMessage;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.StringMessage;

public class GUIClient extends Thread implements ClientInterface, edu.duke.ece651.risc.client.ClientInterface {
        private Connection connection;
        private Board board;
        private boolean isPlaying = true;
        private ClientInputInterface clientInput;
        private ClientOutputInterface clientOutput;
        private HumanPlayer player;
        private String address;
        private int port;

    private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES+.1;
    private double LOGIN_WAIT_MINUTES = Constants.LOGIN_WAIT_MINUTES;

    private boolean firstCall = true;

        public GUIClient() {
            clientInput = new ConsoleInput();
            clientOutput = new TextDisplay();
            board = new Board();
           // connection = new Connection();
        }

        public GUIClient(ClientInputInterface clientInput, ClientOutputInterface clientOutput) {
            this();
            this.clientInput = clientInput;
            this.clientOutput = clientOutput;
        }

        public GUIClient(ClientInputInterface clientInput, ClientOutputInterface clientOutput,Connection connection) {
        this();
        this.clientInput = clientInput;
        this.clientOutput = clientOutput;
        this.connection = connection;
        }

        // Constructor needed for Android threads
        public GUIClient(ClientInputInterface clientInput, ClientOutputInterface clientOutput, String address, int port) {
            this();
            this.clientInput = clientInput;
            this.clientOutput = clientOutput;
            this.address = address;
            this.port = port;
        }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public Connection getConnection() {
        return connection;
    }

    public ClientInputInterface getClientInput() {
        return clientInput;
    }

    public void setClientInput(ClientInputInterface clientInput) {
        this.clientInput.close();
        this.clientInput = clientInput;
    }

    public ClientOutputInterface getClientOutput() {
        return clientOutput;
    }

    public void setPlayer(HumanPlayer player) {
        this.player = player;
    }

    public HumanPlayer getPlayer() {
        return this.player;
    }

    public void playGame() throws InterruptedException {
        // 1) Connection
        ConnectionManager makeConnection = new ConnectionManager(address,port);
        makeConnection.connectGame();
        this.connection = ParentActivity.getConnection();
        try {
            // 2) Perform login / registration
            GameStateModel model = new GameStateModel();
           // GUIClientLogin clientLogin = new GUIClientLogin(model,true,connection, clientInput, ParentActivity.getClientOutput(), ParentActivity.getActivity());
            GUIClientLogin clientLogin = new GUIClientLogin(model,model.getRegistrationAlert(),connection, clientInput, ParentActivity.getClientOutput(), ParentActivity.getActivity());
         //   Log.d("Reg/Login", String.valueOf(model.getRegistrationAlert()));
            clientLogin.performLogin();
            // 3) Perform select game
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
            return;
        }
    }

    @Override
    public void run(){
        try {
            playGame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    }

