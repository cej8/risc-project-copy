package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ParentServerTest {
  @Test
  public void test_ServerSimple() {
    ParentServer ps = new ParentServer();
    try{
      System.out.println("Waiting for connection");
      ps.waitingForConnections();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
      return;
    }
    ChildServer child = ps.getChildren().get(0);
    StringMessage message = new StringMessage("I am testing networking");
    try{
      child.getPlayer().sendObject(message);
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
    ps.closeAll();
  }

}
