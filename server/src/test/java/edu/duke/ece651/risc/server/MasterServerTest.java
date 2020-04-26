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

  @Test
  public void test_dclogout() throws IOException, ClassNotFoundException, InterruptedException{
    int port = 12401;
    MasterServer ms = new MasterServer("logins", port);
    Thread masterThread = new Thread(ms);
    ms.start();

    Thread.sleep(1000);
    //Headless sockets connect
    Socket c1 = new Socket("localhost", port);
    ObjectInputStream c1in = new ObjectInputStream(c1.getInputStream());
    ObjectOutputStream c1out = new ObjectOutputStream(c1.getOutputStream());

    TextDisplay cout = new TextDisplay();
    StringMessage string;
    //After connection need to register as new players (player1, player2);
    //Player 1 register
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Success...");
    cout.displayString(string.unpacker());
    c1out.writeObject(new ConfirmationMessage(false));
    c1out.writeObject(new StringMessage("perro"));
    string = (StringMessage)(c1in.readObject());
    cout.displayString("Salt");
    cout.displayString(string.unpacker());
    c1.close();
    Thread.sleep(1000);
  }

  @Test
  public void test_assorted() throws IOException, ClassNotFoundException, InterruptedException{
    MasterServer ms = new MasterServer("");
    LoginServer ls = new LoginServer(ms, null);
    ls.setUser("test");
    assert(ms.addPlayer(ls));
    assert(!ms.addPlayer(ls));
    ms.removePlayer(ls);
    ls.setActiveGameID(1);
    ms.addPlayer(ls);
    ms.removePlayer("test", 2);
    ms.removePlayer("test", 1);
    
  }

}
