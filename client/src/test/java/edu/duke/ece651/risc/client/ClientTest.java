package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ClientTest {
  @Test
  public void test_ClientSimple() {
    Client client = new Client();
    client.makeConnection("localhost", 12345);
    StringMessage message;
    try{
      message = (StringMessage)(client.receiveObject());
      System.out.println(message.getMessage());
    }
    catch(Exception e){
      e.printStackTrace(System.out);
      return;
    }
    finally{
      try{
        client.closeAll();
      }
      catch(Exception e){
        e.printStackTrace(System.out);
      }
    }
  }

}
