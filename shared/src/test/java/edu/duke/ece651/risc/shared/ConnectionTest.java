package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.io.*;
import java.net.*;

import org.junit.jupiter.api.Test;

  class DummyServerSocket implements Runnable{
    public ServerSocket serverSocket;
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public boolean spin = true;

    public DummyServerSocket(int port){
      try{
        this.serverSocket = new ServerSocket(port);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }

    public void run(){
      while(socket == null){
        try{
          socket = serverSocket.accept();
          oos = new ObjectOutputStream(socket.getOutputStream());
          ois = new ObjectInputStream(socket.getInputStream());
        }
        catch(Exception e){
          return;
        }
      }
      try{
        oos.writeObject("test string");
        Thread.sleep(5000);
        serverSocket.close();
        socket.close();
        ois.close();
        oos.close();
      }
      catch(Exception e){
        e.printStackTrace();
      }
     }
  }

public class ConnectionTest {
  @Test
  public void test_Connection() {
    Connection conn = new Connection();
    int port = 12349;
    try{
      Thread t = new Thread(new DummyServerSocket(port));
      t.start();
      Thread.sleep(5000);
      Socket s = new Socket("localhost", port);
      conn = new Connection(s);
      conn.getStreamsFromSocket();
      System.out.println("receiving object");
      String str = (String)conn.receiveObject();
      System.out.println(str);
      conn.sendObject(str);
      ObjectInputStream ois = conn.getInputStream();
      ObjectOutputStream oos = conn.getOutputStream();
      conn.setSocket(s);
      assert(conn.getSocket().equals(s));
      conn.closeAll();
      conn = new Connection(ois, oos);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

}
