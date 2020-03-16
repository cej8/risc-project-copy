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
  boolean isPlaying = true;
  private ClientInputInterface clientInput;
  private ClientOutputInterface clientOutput;
  private HumanPlayer player;

  public void Client(){
    clientInput = new ConsoleInput();
    clientOutput = new TextDisplay();
  }

  public void Client(ClientInputInterface clientInput, ClientOutputInterface clientOutput){
    this.clientInput = clientInput;
    this.clientOutput = clientOutput;
  }
  

  public void makeConnection(String address, int port){
    try{
      socket = new Socket(address, port);
      socket.setSoTimeout(60*1000);
      fromServer = new ObjectInputStream(socket.getInputStream());
      toServer = new ObjectOutputStream(socket.getOutputStream());
      clientOutput.displayString("Connected to " + address + ":" + Integer.toString(port));
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
      //Set timeout to 5 minutes, wait this long for game start
      socket.setSoTimeout(5*60*1000);
      while(true){
        //Game starts with board message
        board = (Board)(receiveObject());
        //Return timeout to smaller value
        socket.setSoTimeout(60*1000);

        //Output board
        clientOutput.displayBoard(board);
        //Print prompt and get group name
        clientOutput.displayString("Please select a starting region");
        String groupName = clientInput.readInput(System.in);
        sendObject(new StringMessage(groupName));

        //Wait for response
        StringMessage responseMessage = (StringMessage)(receiveObject());
        String response = responseMessage.getMessage();
        if(response.matches("^Fail:")){ continue;}
        if(response.matches("^Success:")){ break;}
      }

      while(true){
        //Server then sends board again
        board = (Board)(receiveObject());

        //Display and move into placements
        clientOutput.displayBoard(board);
        createPlacements();

        //Wait for response
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
    //All prompting/building/sending internal to method
    
  }

  public void createOrders(){
    //TODO: prompt user for orders --> create list of OrderInterface --> send to server
    //All prompting/building/sending internal to method
    
  }


  public void playGame(String address, int port){
    try{
      //Make initial connection, waits for server to send back player's player object
      makeConnection(address, port);
      player = (HumanPlayer)(receiveObject());
      //After which choose regions
      chooseRegions();
      
      while(true){
        //Start of each turn will have continue message if game still going
        //Otherwise is winner message
        StringMessage startMessage = (StringMessage)(receiveObject());
        String start = startMessage.getMessage();
        if(!start.equals("Continue")){
          //If not continue then someone won --> print and exit
          clientOutput.displayString(start);
          return;
        }
        
        //Next is alive status for player
        ConfirmationMessage isAlive = (ConfirmationMessage)(receiveObject());
        //If null then something wrong
        if(isAlive == null){ return;}
        //Get primitive
        boolean alive = isAlive.getMessage();
        //If not same then player died on previous turn --> get spectate message
        if(alive != isPlaying){
          //Continue prompting until valid input (server closes after 60s)
          while(true){
            //Request input
            clientOutput.displayString("Would you like to keep spectating? [Y/N]");
            String spectateResponse = clientInput.readInput(System.in);
            spectateResponse = spectateResponse.toUpperCase();
            //If valid then do work
            if(spectateResponse.length() != 1){            
              if(spectateResponse.charAt(0) == 'Y'){
                sendObject(new ConfirmationMessage(true));
                break;
              }
              else if(spectateResponse.charAt(0) == 'N'){
                sendObject(new ConfirmationMessage(false));
                closeAll();
                return;
              }
            }
            //Otherwise repeat
            clientOutput.displayString("Invalid input.");
          }
        }

        while(true){
          //Next server sends board
          board = (Board)(receiveObject());
          //Display board
          clientOutput.displayBoard(board);
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
