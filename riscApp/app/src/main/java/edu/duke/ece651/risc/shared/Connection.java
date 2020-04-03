package edu.duke.ece651.risc.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.Serializable;

// Class holds all data associated with a network connection
public class Connection implements Serializable{
  private static final long serialVersionUID = 11L;
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
  // Send generic object
  public <T> void sendObject(T object) throws IOException {
    outputStream.reset();
    outputStream.writeObject(object);
  }
  // Recieve generic object
  public <T> T receiveObject() throws IOException, ClassNotFoundException {
    return (T) inputStream.readObject();
  }

  public ObjectInputStream getInputStream() {
    return inputStream;
  }

  public ObjectOutputStream getOutputStream() {
    return outputStream;
  }
  // Close inputStream, outputStream, and socket 
  public void closeAll() {
    try {
      if(inputStream != null) { inputStream.close(); }
    } catch (IOException e) {
      e.printStackTrace(System.out);
    }
    try {
      if(outputStream != null) { outputStream.close(); }
    } catch (IOException e) {
      e.printStackTrace(System.out);
    }
    try {
      if(socket != null) { socket.close(); }
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
    this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    this.inputStream = new ObjectInputStream(socket.getInputStream());
  }
  
}
