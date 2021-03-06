package edu.duke.ece651.risc.shared;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;

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
import java.net.InetAddress;


public class MockTests {
  //this class contains the methods to create a mocked socket, and mocked input and output streams
  public static ArrayList<Object> getObjsFromOutputStream(ByteArrayOutputStream os)throws IOException, ClassNotFoundException{
    //method to read objs from a mocked outputstream
    ByteArrayInputStream inp = new ByteArrayInputStream(os.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(inp);
    ArrayList<Object> objs = new ArrayList<Object>();
    while(ois.available()>0){
      objs.add(ois.readObject());
      
    }
    return objs;
  }
  public static InputStream setupMockInput(ArrayList<Object> inputs)throws IOException{
    //take in an arbitrary list of objects and write them to an inputstream to read out as bytes
    ByteArrayOutputStream temp = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(temp);
    for(Object o: inputs){
      oos.writeObject(o);
    }
    oos.flush();//write data to underlying byteoutputstream
    ByteArrayInputStream inp = new ByteArrayInputStream(temp.toByteArray());
   
    return inp;
    
  }


 public static OutputStream setupMockOutput()throws IOException{
   //create a new byteoutputstream
   return new ByteArrayOutputStream();
    
  }


  
 public static Socket setupMockSocket(ArrayList<Object>inputs)throws IOException{
   //create the mocked sokcet behavior by defining getinputstream, getoutputstream, and accpet by the parent server
    ServerSocket mockParentServer = mock(ServerSocket.class);
    Socket mockClientSocket = mock(Socket.class);
    when(mockParentServer.accept()).thenReturn(mockClientSocket);
    InputStream inp = setupMockInput(inputs);
    OutputStream out = setupMockOutput();
    when(mockClientSocket.getInputStream()).thenReturn(inp);
    when(mockClientSocket.getOutputStream()).thenReturn(out);
    when(mockClientSocket.getSoTimeout()).thenReturn(60*1000);
    doNothing().when(mockClientSocket).setSoTimeout(anyInt());
    when(mockParentServer.getSoTimeout()).thenReturn(60*1000);
    doNothing().when(mockParentServer).setSoTimeout(anyInt());
   
    return mockClientSocket;
  }

   
}
