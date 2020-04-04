package edu.duke.ece651.risc.client;

public class FactoryProducer {
 public static AbstractOrderFactory getOrderFactory(String orderKey){
   AbstractOrderFactory factory = null;
    switch(orderKey){
    case "M"://move 
    case "A"://attack
      return new SourceDestOrderFactory();
    case "P"://placement
    case "U"://uppgrade unit
      return new DestOrderFactory();
    case "T"://tech boost
      return new PlayerOrderFactory();
   
    }

    return factory;


  }
}
