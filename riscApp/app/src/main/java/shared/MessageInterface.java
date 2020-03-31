package shared;

import java.io.Serializable;

public interface MessageInterface<T> extends Serializable{
  public T unpacker();
}
