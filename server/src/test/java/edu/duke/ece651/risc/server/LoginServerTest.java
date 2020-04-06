package edu.duke.ece651.risc.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class LoginServerTest {
  @Test
  public void test_Login() throws IOException, ClassNotFoundException{

    
    ArrayList<Object> objs = new ArrayList<Object>();
    ////Register first
    objs.add(new ConfirmationMessage(false));
    //Username
    objs.add(new StringMessage("user"));
    //Password
    objs.add(new StringMessage("pw"));
    //Failed Password copy
    objs.add(new StringMessage("pw2"));
    //Register first
    objs.add(new ConfirmationMessage(false));
    //Username
    objs.add(new StringMessage("user"));
    //Password
    objs.add(new StringMessage("pw"));
    //Good PW
    objs.add(new StringMessage("pw"));
    //RETURNS
    
    ////Try logging in with previous
    objs.add(new ConfirmationMessage(true));
    //Username
    objs.add(new StringMessage("user"));
    //Password
    objs.add(new StringMessage("pw"));
    //RETURNS
    
    //Try overwritting user, then create user2
    objs.add(new ConfirmationMessage(false));
    //Username
    objs.add(new StringMessage("user"));
    //Password
    objs.add(new StringMessage("pw2"));
    //Good PW
    objs.add(new StringMessage("pw2"));
    //Should fail you --> try again
    objs.add(new ConfirmationMessage(false));
    //Username
    objs.add(new StringMessage("user2"));
    //Password
    objs.add(new StringMessage("pw2"));
    //Good PW
    objs.add(new StringMessage("pw2"));
    //RETURNS
    
    

    
    

    Socket mockSocket = MockTests.setupMockSocket(objs);
    Connection conn = new Connection(mockSocket);
    conn.getStreamsFromSocket();
    //Using unsaved/fresh map
    MasterServer ms = new MasterServer("");
    LoginServer ls = new LoginServer(ms, conn);
    Map<String, Pair<String, String>> logins = ms.getLoginMap();
    
    ls.loginProcess();
    //Should have (user, password) in map (with some random salt)
    assert(logins.keySet().contains("user"));
    assert(logins.get("user").getFirst().equals("pw"));
    assert(logins.size() == 1);
    assert(ls.getUser().equals("user"));

    //"Logout" user
    ms.removePlayer(ls);
    //Ensure no changes
    ls.loginProcess();
    assert(logins.keySet().contains("user"));
    assert(logins.get("user").getFirst().equals("pw"));
    assert(logins.size() == 1);
    assert(ls.getUser().equals("user"));
    
    //"Logout" user
    ms.removePlayer(ls);
    //See if new
    ls.loginProcess();
    assert(logins.keySet().contains("user"));
    assert(logins.get("user").getFirst().equals("pw"));
    assert(logins.keySet().contains("user2"));
    assert(logins.get("user2").getFirst().equals("pw2"));
    assert(logins.size() == 2);
    assert(ls.getUser().equals("user2"));

    //Create new LoginServer, attempt to login as user then user2
    ArrayList<Object> objs2 = new ArrayList<Object>();
    ////Try to login as already logged in user (user2)
    objs2.add(new ConfirmationMessage(true));
    //Username
    objs2.add(new StringMessage("user2"));
    //Password
    objs2.add(new StringMessage("pw2"));
    ////Try to login as not registered
    objs2.add(new ConfirmationMessage(true));
    //Username
    objs2.add(new StringMessage("user3"));
    //Password
    objs2.add(new StringMessage("pw2"));
    ////Finally login as user
    objs2.add(new ConfirmationMessage(true));
    //Username
    objs2.add(new StringMessage("user"));
    //Password
    objs2.add(new StringMessage("pw"));
    //RETURNS

    Socket mockSocket2 = MockTests.setupMockSocket(objs2);
    Connection conn2 = new Connection(mockSocket2);
    conn2.getStreamsFromSocket();
    //Using unsaved/fresh map
    LoginServer ls2 = new LoginServer(ms, conn2);

    ls2.loginProcess();
    assert(logins.keySet().contains("user"));
    assert(logins.get("user").getFirst().equals("pw"));
    assert(logins.keySet().contains("user2"));
    assert(logins.get("user2").getFirst().equals("pw2"));
    assert(logins.size() == 2);
    assert(ls.getUser().equals("user2"));
    assert(ls2.getUser().equals("user"));
      
  }

  @Test
  public void test_SelectGame() throws IOException, ClassNotFoundException{

    MasterServer ms = new MasterServer("");
    
    ArrayList<Object> objs = new ArrayList<Object>();
    //Try out of range
    objs.add(new ConfirmationMessage(true));
    objs.add(new IntegerMessage(0));
    //Try out of range
    objs.add(new ConfirmationMessage(true));
    objs.add(new IntegerMessage(8));
    //Try in progress games
    objs.add(new ConfirmationMessage(true));
    //Try join valid not in progress (should fail)
    objs.add(new IntegerMessage(6));
    //Try in progress games
    objs.add(new ConfirmationMessage(true));
    //Try join invalid in progress (should fail)
    objs.add(new IntegerMessage(3));
    //Try in progress games
    objs.add(new ConfirmationMessage(true));
    //Try join valid in progress
    objs.add(new IntegerMessage(4));
    //RETURN

    //Try out of range
    objs.add(new ConfirmationMessage(false));
    objs.add(new IntegerMessage(-2));
    //Try out of range
    objs.add(new ConfirmationMessage(false));
    objs.add(new IntegerMessage(8));
    //Try new games
    objs.add(new ConfirmationMessage(false));
    //Try join invalid new game (already in, fail)
    objs.add(new IntegerMessage(2));
    //Try new games
    objs.add(new ConfirmationMessage(false));
    //Try join invalid new game (already full, fail)
    objs.add(new IntegerMessage(5));
    //Try new games
    objs.add(new ConfirmationMessage(false));
    //Try join valid new game
    objs.add(new IntegerMessage(6));
    //RETURN
    
    objs.add(new ConfirmationMessage(false));
    //Try create new game
    objs.add(new IntegerMessage(0));
    //RETURN
    
    
    Socket mockSocket = MockTests.setupMockSocket(objs);
    Connection conn = new Connection(mockSocket);
    conn.getStreamsFromSocket();
    //Using unsaved/fresh map
    LoginServer ls = new LoginServer(ms, conn);
    ls.setUser("ls");
    LoginServer ls2 = new LoginServer(ms, null);
    ls2.setUser("ls2");
    LoginServer ls3 = new LoginServer(ms, null);
    ls3.setUser("ls3");
    LoginServer ls4 = new LoginServer(ms, null);
    ls4.setUser("ls4");
    LoginServer ls5 = new LoginServer(ms, null);
    ls5.setUser("ls5");
    LoginServer ls6 = new LoginServer(ms, null);
    ls6.setUser("ls6");
    
    ms.addPlayer(ls);
    ms.addPlayer(ls2);
    ms.addPlayer(ls3);
    ms.addPlayer(ls4);
    ms.addPlayer(ls5);
    ms.addPlayer(ls6);
    //ps1 is open game with ls
    ParentServer ps1 = new ParentServer(2, ms);
    ps1.tryJoin(ls);
    //ps2 is progress game with ls2,ls3
    ParentServer ps2 = new ParentServer(3, ms);
    ps2.tryJoin(ls2);
    ps2.tryJoin(ls3);
    ps2.setNotStarted(false);
    //ps3 is progress game with ls,ls3,ls4
    ParentServer ps3 = new ParentServer(4, ms);
    ps3.tryJoin(ls);
    ps3.tryJoin(ls3);
    ps3.tryJoin(ls4);
    ps3.setNotStarted(false);
    //ps4 is open game with ls2,ls3,ls4,ls5,ls6
    ParentServer ps4 = new ParentServer(5, ms);
    ps4.tryJoin(ls2);
    ps4.tryJoin(ls3);
    ps4.tryJoin(ls4);
    ps4.tryJoin(ls5);
    ps4.tryJoin(ls6);
    //ps6 is open game with ls2
    ParentServer ps5 = new ParentServer(6, ms);
    ps5.tryJoin(ls2);

    ms.addParentServer(ps1);
    ms.addParentServer(ps2);
    ms.addParentServer(ps3);
    ms.addParentServer(ps4);
    ms.addParentServer(ps5);

    Map<Integer, ParentServer> parentServers = ms.getParentServers();

    assert(parentServers.size() == 5);
    assert(parentServers.get(4).hasPlayer("ls"));
    assert(!parentServers.get(6).hasPlayer("ls"));
    assert(parentServers.get(1) == null);
    
    ls.selectGame();
    assert(parentServers.size() == 5);
    assert(parentServers.get(4).hasPlayer("ls"));
    assert(!parentServers.get(6).hasPlayer("ls"));
    assert(parentServers.get(1) == null);
        
    ls.selectGame();
    assert(parentServers.size() == 5);
    assert(parentServers.get(4).hasPlayer("ls"));
    assert(parentServers.get(6).hasPlayer("ls"));
    assert(parentServers.get(1) == null);
    
    ls.selectGame();
    assert(parentServers.size() == 6);
    assert(parentServers.get(4).hasPlayer("ls"));
    assert(parentServers.get(6).hasPlayer("ls"));
    assert(parentServers.get(1).hasPlayer("ls"));
    
    
  }

}
