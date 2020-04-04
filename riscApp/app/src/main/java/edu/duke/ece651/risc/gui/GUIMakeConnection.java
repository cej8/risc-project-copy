package edu.duke.ece651.risc.gui;

import android.util.Log;

import java.net.Socket;

import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;

public class GUIMakeConnection extends Thread{
    private Connection connection;
    private String address;
    private int port;

    public GUIMakeConnection(String address, int port){
        connection = new Connection();
        this.address = address;
        this.port = port;
    }
    public Connection getConnection() {
        return connection;
    }
    public void makeConnection(String address, int port) {
        Socket socket;
        try {
            socket = new Socket(address, port);
            makeConnection(socket);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void makeConnection(Socket socket) {
        try {
            connection.setSocket(socket);
            connection.getStreamsFromSocket();
            socket.setSoTimeout((int) (Constants.START_WAIT_MINUTES * 60 * 1000));
            Log.d("Initial Connection","Probably Connected");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void run(){
        if(connection.getSocket() == null){
            makeConnection(address,port);
        }
    }
}
