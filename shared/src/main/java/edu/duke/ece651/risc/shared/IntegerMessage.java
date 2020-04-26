package edu.duke.ece651.risc.shared;

// Message to move Integers across socket
public class IntegerMessage implements MessageInterface<Integer> {
  private Integer message;
  private static final long serialVersionUID = 30L;
  public IntegerMessage(Integer m){
    this.message = m;
  }
  
  @Override
  public Integer unpacker() {
    return message;
  }

}
