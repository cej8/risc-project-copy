package edu.duke.ece651.risc.shared;

import java.io.Serializable;

// Simple K/V pair method (easier than creating single entry maps)
// Used for saltedhash + salt pairs for logins
// Why doesn't java have this anyway???
public class Pair<K,V> implements Serializable{
  private final K first;
  private final V second;

  private static final long serialVersionUID = 31L;

  public Pair(K first, V second){
    this.first = first;
    this.second = second;
  }

  public K getFirst(){
    return first;
  }

  public V getSecond(){
    return second;
  }

  @Override
  public boolean equals(Object obj){
    if(obj == null){
      return false;
    }

    if(!Pair.class.isAssignableFrom(obj.getClass())){
      return false;
    }

    final Pair comp = (Pair) obj;
    return comp.getFirst().equals(first) && comp.getSecond().equals(second);
  }

}
