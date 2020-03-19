package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
  //this class holds all data associated with a network connection
  private Socket socket = null;
  private ObjectInputStream inputStream = null;
  private ObjectOutputStream outputStream = null;

  public Connection(){
  }
  
  public Connection(Socket socket){
    this.socket = socket;
  }

  public Connection(ObjectInputStream inputStream, ObjectOutputStream outputStream){
    this.inputStream = inputStream;
    this.outputStream = outputStream;
  }

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
      if(inputStream != null) inputStream.close();
      if(outputStream != null) outputStream.close();
      if(socket != null) socket.close();
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

  public void getStreamsFromSocket() throws IOException{
    this.inputStream = new ObjectInputStream(socket.getInputStream());
    this.outputStream = new ObjectOutputStream(socket.getOutputStream());
  }
  
}
