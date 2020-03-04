package edu.duke.ece651.risc.shared;

public class StringMessage implements MessageInterface {
  private String message;
  public StringMessage(String m){
    this.message = m;
  }
  
	// @Override
	// public MessageInterface unpacker() {
	// 	// TODO Auto-generated method stub
  //       return new StringMessage(message);
	// }

	public String getMessage() {
		return message;
	}


}
