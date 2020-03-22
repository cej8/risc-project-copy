package edu.duke.ece651.risc.shared;

import java.io.*;

public class DeepCopy {

  static public Object deepCopy(Object obj) { 
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    Object copy= null;
    try{
      ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
      oos = new ObjectOutputStream(bout); 
      oos.writeObject(obj);     // serialize and pass the object
      oos.flush();               
      ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray()); 
      ois = new ObjectInputStream(bin);
      copy = ois.readObject();   // return the new object
    }
<<<<<<< HEAD
    catch(Exception e){
      throw new DeepCopyFailureException(e);
=======
    catch(IOException e){
      e.printStackTrace();
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
>>>>>>> 5c35b677619b559629b0419e04b7b23b3dec524e
    }
    finally{
      if (oos != null && ois != null) {
        try{
          oos.close();
          ois.close();
        }
        catch(IOException e){
          e.printStackTrace();
        }
      }
    }
     return copy;
  }
  
}

