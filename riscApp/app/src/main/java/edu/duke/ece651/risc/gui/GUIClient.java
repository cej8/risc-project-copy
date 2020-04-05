package edu.duke.ece651.risc.gui;

import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
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

public class GUIClient extends Thread implements ClientInterface {
        private Connection connection;
        private Board board;
        private boolean isPlaying = true;
        private ClientInputInterface clientInput;
        private ClientOutputInterface clientOutput;
        private HumanPlayer player;
        private String address;
        private int port;

        private double TURN_WAIT_MINUTES = Constants.TURN_WAIT_MINUTES;
        private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES;

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
        public GUIClient(ClientInputInterface clientInput, ClientOutputInterface clientOutput,String address, int port) {
            this();
            this.clientInput = clientInput;
            this.clientOutput = clientOutput;
            this.address = address;
            this.port = port;
        }

        public void setTURN_WAIT_MINUTES(double TURN_WAIT_MINUTES){
            this.TURN_WAIT_MINUTES = TURN_WAIT_MINUTES;
        }
        public void setSTART_WAIT_MINUTES(double START_WAIT_MINUTES){
            this.START_WAIT_MINUTES = START_WAIT_MINUTES;
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

        public void setSocketTimeout(int timeout) throws SocketException {
            connection.getSocket().setSoTimeout(timeout);
        }

//        public void makeConnection(String address, int port) {
//            Socket socket;
//            try {
//                socket = new Socket(address, port);
//                makeConnection(socket);
//            } catch (Exception e) {
//                e.printStackTrace(System.out);
//            }
//        }
//
//        public void makeConnection(Socket socket) {
//            try {
//                connection.setSocket(socket);
//                connection.getStreamsFromSocket();
//                socket.setSoTimeout((int) (Constants.START_WAIT_MINUTES * 60 * 1000));
//            } catch (Exception e) {
//                e.printStackTrace(System.out);
//            }
//        }

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

        public void updateClientBoard() {
            Board masterBoard = null;
            try {
                // masterBoard = (Board) fromServer.readObject();
                masterBoard = (Board) connection.getInputStream().readObject();
                this.setBoard(masterBoard);
                this.board.setRegions(masterBoard.getRegions());
            } catch (IOException e) {
                System.out.println("IOException is caught");
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException is caught");
            }
        }

        public boolean chooseRegions() {

            //Initial to -1 for timers, don't set until turn actually starts
            long startTime = -1;
            long maxTime = -1;

            try {
                // Set timeout to constant, wait this long for game start
                // This will block on FIRST board = ...
                connection.getSocket().setSoTimeout((int) (Constants.START_WAIT_MINUTES * 60 * 1000));
                while (true) {
                    // Game starts with board message
                    Log.d("Pre Board:", "About to get board");
                    board = (Board) (connection.receiveObject());
                    Log.d("Post Board:","Should have board");
                    Thread.dumpStack();
                    // Return timeout to smaller value
                    connection.getSocket().setSoTimeout((int) (Constants.TURN_WAIT_MINUTES * 60 * 1000));

                    //Set max/start first time board received (start of turn)
                    if(maxTime == -1){
                        maxTime = (long) (connection.getSocket().getSoTimeout());
                        //Catch case for issues in testing, should never really happen
                        if (maxTime == 0) {
                            maxTime = (long) (Constants.TURN_WAIT_MINUTES * 60 * 1000);
                        }
                    }
                    if(startTime == -1){
                        startTime = System.currentTimeMillis();
                    }

                    // Output board
                    Log.d("Pre Board:", "About to print board");
                    clientOutput.displayBoard(board);
                    Thread.dumpStack();
                    Log.d("Board print:","Board should be printed");
                    // Print prompt and get group name
                    clientOutput.displayString("Please select a starting group by typing in a group name (i.e. 'Group A')");
                    String groupName = clientInput.readInput();
                    if(timeOut(startTime, maxTime)) { return false; }
                    connection.sendObject(new StringMessage(groupName));

                    // Wait for response
                    StringMessage responseMessage = (StringMessage) (connection.receiveObject());
                    String response = responseMessage.getMessage();
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
                    String response = responseMessage.getMessage();
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


        public String receiveAndDisplayString() throws IOException, ClassNotFoundException{
            StringMessage message = (StringMessage) (connection.receiveObject());
            String str = message.getMessage();
            clientOutput.displayString(str);
            return str;
        }

        //Method to mesh with loginProcess() in loginServer
        public void performLogin() throws IOException, ClassNotFoundException{
            String initalSuccess = receiveAndDisplayString();
            while(true){
                boolean loginBoolean = queryYNAndRespond("Do you already have a login? [Y/N]");
                //Either way request login
                //clientOutput.displayString("Username:");
                connection.sendObject(new StringMessage(clientInput.readInput()));
                //We will get salt back
                String salt = ((StringMessage)(connection.receiveObject())).unpacker();

                //We will request a password
                //clientOutput.displayString("Password:");
                String password1 = clientInput.readInput();
                //Hash password
                String hashPassword1;
                if(!salt.equals("")){
                    hashPassword1 = BCrypt.hashpw(password1, salt);
                }
                else{
                    hashPassword1 = "";
                }

                //Send hashed password back
                connection.sendObject(new StringMessage(hashPassword1));

                //If true then has login (nothing extra)
                //If false then registering (need second password entry)
                if(!loginBoolean){
                    //Request repeat of password
                    clientOutput.displayString("Password (again):");
                    String password2 = clientInput.readInput();
                    //Hash password
                    String hashPassword2 = BCrypt.hashpw(password1, salt);
                    //Send copy back
                    connection.sendObject(new StringMessage(hashPassword2));
                }

                //Get back response
                String response = receiveAndDisplayString();
                //Repeat if fail, continue if success
                if (response.matches("^Fail:.*$")) {
                    continue;
                }
                if (response.matches("^Success:.*$")) {
                    break;
                }
            }

            //At this point user is logged in (either old or new)

        }

        //Method to mesh with selectGame() in loginServer
        public void performSelectGame() throws IOException, ClassNotFoundException{
            while(true){
                boolean oldBoolean = queryYNAndRespond("Would you like to join a game you are already in? [Y/N]");

                //Server then sends back list of games
                String list = receiveAndDisplayString();
                Integer gameID;
                while(true){
                    clientOutput.displayString("Pick a game via ID");
                    try{
                        gameID = Integer.parseInt(clientInput.readInput());
                    }
                    catch (NumberFormatException ne) {
                        // ne.printStackTrace();
                        clientOutput.displayString("That was not an integer.");
                        continue;
                    }
                    break;
                }
                //Send ID to server
                connection.sendObject(new IntegerMessage(gameID));

                //Get back response
                String response = receiveAndDisplayString();
                //Repeat if fail, continue if success
                if (response.matches("^Fail:.*$")) {
                    continue;
                }
                if (response.matches("^Success:.*$")) {
                    break;
                }
            }

        }

        //Helper method to ask YN and send back ConfirmationMessage
        public boolean queryYNAndRespond(String query) throws IOException{
            while(true){
                // Request input
                clientOutput.displayString(query);
                String spectateResponse = clientInput.readInput();

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
                clientOutput.displayString("Invalid input.");
            }
        }


        public void playGame() {
//            if(connection.getSocket() == null){
//                makeConnection(address,port);
//            }
            try {
                performLogin();
                performSelectGame();
                // Make initial connection, waits for server to send back player's player object
                // Get initial player object (for name)
                player = (HumanPlayer) (connection.receiveObject());
                clientOutput.displayString("Successfully connected, you are named: " + player.getName());
                clientOutput.displayString("Please wait for more players to connect");
                // After which choose regions
                if(!chooseRegions()) {return; }
                while (true) {
                    long startTime = System.currentTimeMillis();
                    long maxTime = (long) (connection.getSocket().getSoTimeout());
                    //Catch case for issues in testing, should never really happen
                    if (maxTime == 0) {
                        maxTime = (long) (Constants.TURN_WAIT_MINUTES * 60 * 1000);
                    }

                    String turn = receiveAndDisplayString();

                    // Start of each turn will have continue message if game still going
                    // Otherwise is winner message
                    StringMessage startMessage = (StringMessage) (connection.receiveObject());
                    String start = startMessage.getMessage();
                    if (!start.equals("Continue")) {
                        // If not continue then someone won --> print and exit
                        clientOutput.displayString(start);
                        connection.closeAll();
                        clientInput.close();
                        return;
                    }

                    // Next is alive status for player
                    ConfirmationMessage isAlive = (ConfirmationMessage) (connection.receiveObject());
                    // If null then something wrong
                    if (isAlive == null) {
                        return;
                    }
                    // Get primitive
                    boolean alive = isAlive.getMessage();
                    // If not same then player died on previous turn --> get spectate message
                    if (alive != isPlaying) {
                        isPlaying = alive;
                        //Query for spectating
                        //If no then kill connection
                        if(!queryYNAndRespond("Would you like to keep spectating? [Y/N]")){
                            connection.closeAll();
                            clientInput.close();
                        }
                    }

                    while (true) {
                        // Next server sends board
                        board = (Board) (connection.receiveObject());
                        // Display board
                        clientOutput.displayBoard(board);
                        // Client generates orders --> sends
                        if (alive) {
                            //new OrderCreator
                            OrderHelper orderhelper = new OrderHelper((edu.duke.ece651.risc.client.ClientInterface) this);
                            List<OrderInterface> orders = orderhelper.createOrders();
                            //If too long --> kill player
                            if(timeOut(startTime, maxTime)){ return;}
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                connection.closeAll();
                clientInput.close();
                return;
            }
        }

        @Override
        public void run(){
            playGame();
        }
    }
