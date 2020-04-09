package edu.duke.ece651.risc.client;

import android.util.Log;

import java.net.Socket;

import edu.duke.ece651.risc.gui.LoginModel;
import edu.duke.ece651.risc.gui.ParentActivity;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;

public class ConnectionManager {
   private Connection connection;
    private String address;
    private int port;
    private LoginModel model;

    public ConnectionManager(){
    connection = new Connection();
    this.model = new LoginModel();
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
            Log.d("Connection","True");
            // Set connection in client
            ParentActivity parentActivity = new ParentActivity();
            parentActivity.setConnection(connection);
          //  model.setConnection(connection);
            } catch (Exception e) {
            e.printStackTrace(System.out);
            Log.d("Connection","false");
        }
    }
  public void connectGame() throws InterruptedException{
          if(connection.getSocket() == null){
              //---- block ConnectionManager until true (ready to connect)
          //    LoginModel model = new LoginModel();
              model.getConnection();
              //---- unblock ConnectionManager
              makeConnection(address,port);
        }
  }
}

