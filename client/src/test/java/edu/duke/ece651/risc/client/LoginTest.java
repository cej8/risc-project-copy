package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;
import java.util.*;
import java.io.*;
import java.net.*;

import org.mindrot.jbcrypt.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class LoginTest {
  @Test
  public void test_LoginTest() throws IOException{
    
     ArrayList<Object> objs = new ArrayList<Object>();
    //First connect success
    objs.add(new StringMessage("Success: connected"));
    //Send "salt"
    objs.add(new StringMessage(""));
    //Send fail
    objs.add(new StringMessage("Fail: invalid user/password"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Fail: user already exists"));
    //Send "salt"
    objs.add(new StringMessage(BCrypt.gensalt()));
    //Send fail
    objs.add(new StringMessage("Success: login"));
    //Send games
    objs.add(new StringMessage("games...."));
    //Send fail
    objs.add(new StringMessage("Fail: bad gameid"));
    //Send games
    objs.add(new StringMessage("games...."));
    //Send success
    objs.add(new StringMessage("Success: good game"));
    //Send firstturn
    objs.add(new ConfirmationMessage(true));

    Socket mockSocket = MockTests.setupMockSocket(objs);

    
    InputStream input = new FileInputStream(new File("src/test/resources/testLogin.txt"));
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(input);
    ConnectionManager cm = new ConnectionManager();
    cm.makeConnection(mockSocket);
    Connection conn = cm.getConnection();
    ClientLogin login = new ClientLogin(conn, ci, td);
    assert(login.Login());
    
  }

  @Test
  public void test_deadSocket() throws IOException{
     System.out.println("Should do bad input twice then socketclosed or EOF");
     ArrayList<Object> objs = new ArrayList<Object>();
    //First connect success
    objs.add(new StringMessage("Success: connected"));
    

    Socket mockSocket = MockTests.setupMockSocket(objs);

    
    InputStream input = new FileInputStream(new File("src/test/resources/testLogin.txt"));
    TextDisplay td = new TextDisplay();
    ConsoleInput ci = new ConsoleInput(input);
    ConnectionManager cm = new ConnectionManager();
    cm.makeConnection(mockSocket);
    Connection conn = cm.getConnection();
    ClientLogin login = new ClientLogin(conn, ci, td);
    mockSocket.close();
    login.Login();

  }

}
