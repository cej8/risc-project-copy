package com.example.risc;

import shared.*;

public class ClientProgram {
    //top level class for executing client
    public static void main(String[] args){

        ClientInputInterface clientInput = new ConsoleInput();
        ClientOutputInterface clientOutput = new TextDisplay();

        clientOutput.displayString("Address?");
        String addr = clientInput.readInput();
        clientOutput.displayString("Port?");
        String portS = clientInput.readInput();

        int port;
        try{
            port = Integer.parseInt(portS);
        }
        catch(NumberFormatException ne){
            clientOutput.displayString("Port invalid");
            return;
        }

        Client client = new Client(clientInput, clientOutput);

        client.makeConnection(addr, port);
        client.playGame();
    }
}
