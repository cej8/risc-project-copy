package edu.duke.ece651.risc.shared;

import java.io.*;

public abstract class AbstractPlayer implements Serializable{
  protected String name;
  protected boolean isPlaying;
  protected Boolean isWatching;
  protected ObjectInputStream inputStream;
  protected ObjectOutputStream outputStream;
  private static final long serialVersionUID = 5L;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public boolean isPlaying() {
    return isPlaying;
  }
  public void setPlaying(boolean isPlaying) {
    this.isPlaying = isPlaying;
  }
  public Boolean isWatching(){
    return isWatching;
  }
  public void setWatching(boolean isWatching){
    this.isWatching = new Boolean(isWatching);
  }

  public void closeAll(){
    this.isPlaying = false;
    
    try{
      inputStream.close();
      outputStream.close();
    }
    catch(IOException e){
      e.printStackTrace(System.out);
    }
  }
  public void sendObject(Object object) throws IOException{
    outputStream.writeObject(object);
  }
  public Object receiveObject() throws IOException, ClassNotFoundException{
    return inputStream.readObject();
  }

  public ObjectInputStream getInputStream(){
    return inputStream;
  }
  public ObjectOutputStream getOutputStream(){
    return outputStream;
  }


}

 
  



    
       
  

    
     
