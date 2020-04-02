package edu.duke.ece651.risc.gui;

import android.util.Log;
import android.widget.TextView;

import edu.duke.ece651.risc.client.Client;
import edu.duke.ece651.risc.client.ClientInputInterface;
import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.client.ConsoleInput;
import edu.duke.ece651.risc.client.TextDisplay;

public class ExecuteClient {
    public void createGame(TextView textView) {
        ClientInputInterface clientInput = new ConsoleInput();
        ClientOutputInterface clientOutput = new GUITextDisplay(textView);

        String addr = "67.159.89.108";
        String portS = "12345";
        textView.setText("Waiting to connect");
        Log.d("Test Connection", "Test Connection");

        int port;
        try {
            port = Integer.parseInt(portS);
        } catch (NumberFormatException ne) {
            textView.setText("Port invalid");
            return;
        }

        Client client = new Client(clientInput, clientOutput, addr, port);
        client.start();
        //Log.d("End of task", "end of task");
    }
}
