package edu.duke.ece651.risc.shared;

import java.io.Serializable;

public interface MessageInterface<T> extends Serializable{
  public T unpacker();
}
