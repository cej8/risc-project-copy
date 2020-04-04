package edu.duke.ece651.risc.shared;

public class StringMessage implements MessageInterface<String> {
  private String message;
  private static final long serialVersionUID = 3L;
  public StringMessage(String m){
    this.message = m;
  }
  
  @Override
  public String unpacker() {
    return message;
  }

  public String getMessage() {
	return message;
  }


}
