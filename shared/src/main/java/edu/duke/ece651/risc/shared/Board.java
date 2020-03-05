package edu.duke.ece651.risc.shared;
import java.io.Serializable;
import java.util.List;
import java.io.*;
import java.net.*;

public class Board implements Serializable {
  private static final long serialVersionUID = 7L;
  List<Region> regions;

  public Board(List<Region> regionList){
    this.setRegions(regionList);
  }
  
  List<Region> getRegions(){
    return this.regions;
  }

 List<Region> setRegions(List<Region> regionList){
    this.regions = regionList;
  }

  
  public void updateBoard(Socket socket)  {
    Board masterBoard = null;
    try{
      ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
      masterBoard = (Board) is.readObject();
      is.close();
      this.regions = masterBoard.getRegions();
    }
    catch(IOException e) {
      System.out.println("IOException is caught");
    }
    catch(ClassNotFoundException e) {
      System.out.println("ClassNotFoundException is caught");
    } 
  }



  
}

