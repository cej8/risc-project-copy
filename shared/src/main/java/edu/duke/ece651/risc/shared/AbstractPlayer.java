package edu.duke.ece651.risc.shared;

import java.io.*;

public abstract class AbstractPlayer implements Serializable{
  protected String name;
  protected boolean isPlaying;
  protected Connection connection;
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
public Connection getConnection() {
	return connection;
}
 

}

 
  



    
       
  

    
     
