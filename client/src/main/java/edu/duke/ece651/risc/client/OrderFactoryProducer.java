package edu.duke.ece651.risc.client;

public class OrderFactoryProducer {
  public static AbstractOrderFactory getOrderFactory(String orderKey) {
    AbstractOrderFactory factory = null;
    switch (orderKey) {
      case "M":// move
        factory = new SourceDestOrderFactory();
        break;
      case "A":// attack
        factory = new SourceDestOrderFactory();
        break;
      case "P":// placement
        factory = new DestOrderFactory();
        break;
      case "U":// uppgrade unit
        factory = new DestOrderFactory();
        break;
      case "T":// tech boost
        factory = new PlayerOrderFactory();
        break;
    }
    return factory;
  }
}
