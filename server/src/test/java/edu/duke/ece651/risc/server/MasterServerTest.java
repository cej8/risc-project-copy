package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.io.*;
import java.net.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.*;

import edu.duke.ece651.risc.client.*;

public class MasterServerTest {
  @Test
  public void test_loginMap() throws IOException, ClassNotFoundException{
    MasterServer ms = new MasterServer("logins");
    Map<String, Pair<String, String>> logins = ms.getLoginMap();
    assert(logins.keySet().size() == 3);
    assert(logins.keySet().contains("perro"));
    assert(logins.keySet().contains("test1"));
    assert(logins.keySet().contains("test2"));

    Pair<String, String> p = new Pair<String, String>("pass", "salt");
    assert(!ms.addLogin("perro", p));
    assert(ms.addLogin("perro2", p));
    
    ms.setLoginFile("loginsOut");
    ms.saveMap();

    ms.setSocket(12351);
    ms.getServerSocket();
    
  }

}
