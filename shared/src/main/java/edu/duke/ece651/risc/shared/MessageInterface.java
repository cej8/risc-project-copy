package edu.duke.ece651.risc.shared;

import java.io.Serializable;

// Interface for all "Message" types
public interface MessageInterface<T> extends Serializable{
  public T unpacker();
}
