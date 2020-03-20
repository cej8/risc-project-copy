package edu.duke.ece651.risc.shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeepCopy {

  static public Object deepCopy(Object obj) throws IOException, ClassNotFoundException {
   
    ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
    ObjectOutputStream oos = new ObjectOutputStream(bout); 
    oos.writeObject(obj);     // serialize and pass the object
  
    oos.flush();               
    ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray()); 
    ObjectInputStream ois = new ObjectInputStream(bin);
    return ois.readObject();   // return the new object

  }
  
}

