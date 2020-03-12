package edu.duke.ece651.risc.shared;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class MockTests {
  public ArrayList<Object> objsFrom(ByteArrayOutputStream os)throws IOException, ClassNotFoundException{
    ByteArrayInputStream inp = new ByteArrayInputStream(os.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(inp);
    ArrayList<Object> objs = new ArrayList<Object>();
    while(ois.available()>0){
      objs.add(ois.readObject());
      
    }
    return objs;
  }
  static InputStream setupMockInput(ArrayList<Object> inputs)throws IOException{
    ByteArrayOutputStream temp = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(temp);
    for(Object o: inputs){
      oos.writeObject(o);
    }
    oos.flush();
    //  ByteArrayInputStream inp = new ByteArrayInputStream(oos.toByteArray());
    ByteArrayInputStream inp = new ByteArrayInputStream(temp.toByteArray());
   
    return inp;
    
  }

 public static Socket setupMockSocket(ArrayList<Object>inputs)throws IOException{
    // ServerSocket mockParentServer = mock(ServerSocket.class);
    Socket mockClientSocket = mock(Socket.class);
    //  InputStream mockInputStream = mock(InputStream.class); 
    // OutputStream mockOutputStream = mock(OutputStream.class); 
    
    //    when(mockParentServer.accept()).thenReturn(mockClientSocket);
    
    //  when(mockClientSocket.getInputStream()).thenReturn(mockInputStream);
    // when(mockClientSocket.getOutputStream()).thenReturn(mockOutputStream);

    //  Socket s = mock(Socket.class);
    InputStream inp = setupMockInput(inputs);
    when(mockClientSocket.getInputStream()).thenReturn(inp);
    return mockClientSocket;
  }

   
}
