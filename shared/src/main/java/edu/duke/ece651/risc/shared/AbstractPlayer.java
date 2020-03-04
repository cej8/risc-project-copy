package edu.duke.ece651.risc.shared;

public abstract class AbstractPlayer implements Serializable{
  private String name;
  private boolean isPlaying;
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


}

 
  



    
       
  

    
     
