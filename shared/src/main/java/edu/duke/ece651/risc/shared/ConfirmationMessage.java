package edu.duke.ece651.risc.shared;

public class ConfirmationMessage implements MessageInterface<Boolean>{
  private boolean message;
   private static final long serialVersionUID = 4L;
  ConfirmationMessage(boolean m){
    this.message = m;
  }
	@Override
	public Boolean unpacker() {
		return getMessage();
	}

	public boolean getMessage() {
		return message;
	}

}
