package edu.duke.ece651.risc.client;

import android.util.Log;

import java.net.Socket;

import edu.duke.ece651.risc.gui.ParentActivity;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;

public class ConnectionManager extends Thread {
   private Connection connection;
    private String address;
    private int port;

  public ConnectionManager(){
    connection = new Connection();
  }
    public ConnectionManager(String address, int port){
      //connection = new Connection();
      this();
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
            ParentActivity parentActivity = new ParentActivity();
            parentActivity.setConnection(connection);
            Log.d("Connection","True");
            } catch (Exception e) {
            e.printStackTrace(System.out);
            Log.d("Connection","False");
        }
    }
  public void connectGame(){
          if(connection.getSocket() == null){
            makeConnection(address,port);
        }
  
  }
    @Override
    public void run(){
        if(connection.getSocket() == null){
            makeConnection(address,port);
        }
    }
}

