package edu.duke.ece651.risc.client;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import edu.duke.ece651.risc.shared.ConfirmationMessage;
import edu.duke.ece651.risc.shared.IntegerMessage;
import edu.duke.ece651.risc.shared.*;

public class ClientLogin {
    private Connection connection;
    private ClientInputInterface clientInput;
    private ClientOutputInterface clientOutput;

    public ClientLogin(Connection connect, ClientInputInterface input, ClientOutputInterface output){
        this.connection = connect;
        this.clientInput = input;
        this.clientOutput = output;
    }
    public boolean Login(){// throws IOException, ClassNotFoundException{
        boolean firstCall = true;
        try {
            performLogin();
            firstCall = performSelectGame();
        } catch (Exception e) {
            e.printStackTrace();
            connection.closeAll();
            clientInput.close();
        }
        return firstCall;
    }
    public String receiveAndDisplayString() throws IOException, ClassNotFoundException{
        StringMessage message = (StringMessage) (connection.receiveObject());
        String str = message.unpacker();
        clientOutput.displayString(str);
        return str;
    }

    //Method to mesh with loginProcess() in loginServer
    public void performLogin() throws IOException, ClassNotFoundException{
        String initalSuccess = receiveAndDisplayString();


        while(true){
            boolean loginBoolean = queryYNAndRespond("Do you already have a login? [Y/N]");
            //Either way request login
            clientOutput.displayString("Username:");
            connection.sendObject(new StringMessage(clientInput.readInput()));
            //We will get salt back
            String salt = ((StringMessage)(connection.receiveObject())).unpacker();

            //We will request a password
            clientOutput.displayString("Password:");
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
                String hashPassword2 = BCrypt.hashpw(password2, salt);
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
    public boolean performSelectGame() throws IOException, ClassNotFoundException{
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

        // Make initial connection, waits for server to send back player's firstCall state
        return ((ConfirmationMessage)connection.receiveObject()).unpacker();

    }

    //Helper method to ask YN and send back ConfirmationMessage
    public boolean queryYNAndRespond(String query) throws IOException {
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

}