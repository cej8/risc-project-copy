package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
  //this class holds all data associated with a network connection
  private Socket socket;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;

  public <T> void sendObject(T object) throws IOException {
    outputStream.writeObject(object);
  }

  public <T> T receiveObject() throws IOException, ClassNotFoundException {
    return (T) inputStream.readObject();
  }

  public ObjectInputStream getInputStream() {
    return inputStream;
  }

  public ObjectOutputStream getOutputStream() {
    return outputStream;
  }

  public void closeAll() {
    try {
      inputStream.close();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace(System.out);
    }
  }

  public void setInputStream(ObjectInputStream inputStream) {
    this.inputStream = inputStream;
  }

  public void setOutputStream(ObjectOutputStream outputStream) {
    this.outputStream = outputStream;
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }
  
}
