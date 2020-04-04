package edu.duke.ece651.risc.client;

import android.util.Log;

import java.net.Socket;

import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;

public class MakeConnection extends Thread {
   private Connection connection;
    private String address;
    private int port;

  public MakeConnection(){
    connection = new Connection();
  }
    public MakeConnection(String address, int port){
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
            Log.d("Connection","Should be connected");
            } catch (Exception e) {
            e.printStackTrace(System.out);
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

