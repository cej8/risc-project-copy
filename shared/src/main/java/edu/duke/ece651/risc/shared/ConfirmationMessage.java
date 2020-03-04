package edu.duke.ece651.risc.shared;

public class ConfirmationMessage implements MessageInterface{
  private boolean message;
  ConfirmationMessage(boolean m){
    this.message = m;
  }
	// @Override
	// public MessageInterface unpacker() {
	// 	// TODO Auto-generated method stub
	// 	return null;
	// }

	public boolean getMessage() {
		return message;
	}

}
