package edu.duke.ece651.risc.client;

import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
  // private Socket socket;
  // private ObjectInputStream fromServer;
  // private ObjectOutputStream toServer;
  private Connection connection;
  private Board board;
  private boolean isPlaying;

  public Client() {
    this.board = new Board(null);
    this.isPlaying = true;
    this.connection = new Connection();
  };

  // create connection from already created socket
  public void makeConnection(Socket socket) {
    try {
      connection.setInputStream(new ObjectInputStream(socket.getInputStream()));
      connection.setOutputStream(new ObjectOutputStream(socket.getOutputStream()));

      System.out.println("Connected to " + socket.getLocalAddress() + ":" + socket.getLocalPort());
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  // creating connection by address and port TODO -- do we need this constructor?
  public void makeConnection(String address, int port) {
    try {
      connection.setSocket(new Socket(address, port));
      connection.setInputStream(new ObjectInputStream(connection.getSocket().getInputStream()));
      connection.setOutputStream(new ObjectOutputStream(connection.getSocket().getOutputStream()));

      System.out.println("Connected to " + address + ":" + Integer.toString(port));
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  void closeAll() {
    try {
      connection.getSocket().close();
      connection.closeAll();
    } catch (IOException e) {
      e.printStackTrace(System.out);
    }
  }

  public void updateClientBoard() {
    Board masterBoard = null;
    try {
      // masterBoard = (Board) fromServer.readObject();
       masterBoard = (Board)connection.getInputStream().readObject();
       this.setBoard(masterBoard);
      this.board.setRegions(masterBoard.getRegions());
    } catch (IOException e) {
      System.out.println("IOException is caught");
    } catch (ClassNotFoundException e) {
      System.out.println("ClassNotFoundException is caught");
    }
  }

  // public Socket getSocket() {
  //   return socket;
  // }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

public Connection getConnection() {
	return connection;
}

}
