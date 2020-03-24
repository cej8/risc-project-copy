package edu.duke.ece651.risc.shared;

// Enables confirmation message unpacking and recieving for client
public class ConfirmationMessage implements MessageInterface<Boolean>{
  private boolean message;
   private static final long serialVersionUID = 4L;
  public ConfirmationMessage(boolean m){
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
