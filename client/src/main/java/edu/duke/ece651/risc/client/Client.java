package edu.duke.ece651.risc.client;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
  private Socket socket;
  private ObjectInputStream fromServer;
  private ObjectOutputStream toServer;
  Board board;
  boolean isPlaying;

  public void Client(){
    isPlaying = true;
  }

  public void makeConnection(String address, int port){
    try{
      socket = new Socket(address, port);
      fromServer = new ObjectInputStream(socket.getInputStream());
      toServer = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("Connected to " + address + ":" + Integer.toString(port));
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }

  public Object receiveObject() throws IOException, ClassNotFoundException{
    return fromServer.readObject();
  }

  public void sendObject(Object object) throws IOException{
    toServer.writeObject(object);
  }

  public Object receiveObjectOrClose(){
    try{
      return fromServer.readObject();
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return null;
    }
  }

  public void closeAll(){
    try{
      socket.close();
      fromServer.close();
      toServer.close();
    }
    catch(IOException e){
      e.printStackTrace(System.out);
    }
  }

  public void chooseRegions(){
    try{
      while(true){
        board = (Board)(receiveObject());
        //TODO: output board
        //TODO: print prompt for region, receive input
        String groupName = "Group A";
        sendObject(new StringMessage(groupName));
      
        StringMessage responseMessage = (StringMessage)(receiveObject());
        String response = responseMessage.getMessage();
        if(response.matches("^Fail:")){ continue;}
        if(response.matches("^Success:")){ break;}
      }

      while(true){
        board = (Board)(receiveObject());
        //TODO: output board
        createPlacements();
      
        StringMessage responseMessage = (StringMessage)(receiveObject());
        String response = responseMessage.getMessage();
        if(response.matches("^Fail:")){ continue;}
        if(response.matches("^Success:")){ break;}
      }
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return;
    }
  }

  public void createPlacements(){
    //TODO: prompt user for placements --> create list of placementOrders --> send to server
    
  }

  public void createOrders(){
    //TODO: prompt user for orders --> create list of OrderInterface --> send to server
    
  }


  public void playGame(){
    
    String address = "localhost";
    int port = 12345;
    try{
      makeConnection(address, port);
      chooseRegions();
      
      while(true){
        //Start of each turn will have continue message if game still going
        //Otherwise is winner message
        StringMessage winnerMessage = (StringMessage)(receiveObject());
        String winner = winnerMessage.getMessage();
        if(!winner.equals("Continue")){
          //Print winner message
          return;
        }
        //Next is alive status for player
        ConfirmationMessage isAlive = (ConfirmationMessage)(receiveObject());
        if(isAlive == null){ return;}
        boolean alive = isAlive.getMessage();
        //If not same then player died on previous turn --> get spectate message
        if(alive != isPlaying){
          //TODO: get Y/N from user for spectate
          //Send Y/N to server, if N then return
          sendObject(new ConfirmationMessage(true));
        }

        while(true){
          //Next server sends board
          board = (Board)(receiveObject());
          //TODO: display board
          //Client generates orders --> sends 
          if(alive){
            createOrders();
          }

          StringMessage responseMessage = (StringMessage)(receiveObject());
          String response = responseMessage.getMessage();
          if(response.matches("^Fail:")){ continue;}
          if(response.matches("^Success:")){ break;}
        }
      }
    }
    catch(Exception e){
      e.printStackTrace();
      closeAll();
      return;
    }
        
  }
}
