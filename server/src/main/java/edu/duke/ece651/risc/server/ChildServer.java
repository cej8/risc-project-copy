package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

// Class that handles each child implemtation for game (i.e. each player has their own ChildServer)
public class ChildServer implements Runnable{
  private AbstractPlayer player;  
  private ParentServer parent;
  private Connection playerConnection;
  private String turnMessage = "";

  private boolean firstCall = true;

  public ChildServer(AbstractPlayer player, ParentServer parent){
    this.player = player;
    this.parent = parent;
  }

  public ChildServer(AbstractPlayer player, Connection playerConnection, ParentServer parent){
    this.player = player;
    this.playerConnection = playerConnection;
    this.parent = parent;
  }

  public void setTurnMessage(String turnMessage){
    this.turnMessage = turnMessage;
  }
  
  // Getters & setters
  public Connection getPlayerConnection(){
    return playerConnection;
  }
  public void setPlayerConnection(Connection playerConnection){
    this.playerConnection = playerConnection;
  }
  public ParentServer getParentServer(){
    return parent;
  }
  public void setParentServer(ParentServer parent){
    this.parent = parent;
  }
  public AbstractPlayer getPlayer(){
    return player;
  }
  
  // end of getters & setters
  // enables ChildServer to be runnable
  @Override
  public void run(){
    long startTime = System.currentTimeMillis();
    //Timeout is Socket's timeout
    long maxTime = (long)(parent.getTURN_WAIT_MINUTES()*60*1000);
    
    //If player isn't playing or isn't watching then skip them
    if(!player.isPlaying()){
      if(!player.isWatching()){
        return;
      }
    }
    
    ValidatorHelper validator;
    try{
      System.out.println(player.getName() + " enter thread");
      playerConnection.getSocket().setSoTimeout((int)maxTime);
      if(firstCall){
        //Prompt for region --> placement
        //Prompt for region
        while(true){
          //Decrease timeout to maxTime-(current-start)
          //Ensures will time out at maxTime after start
          playerConnection.getSocket().setSoTimeout((int)(maxTime-(System.currentTimeMillis() - startTime)));
          //Send board to player
          playerConnection.sendObject(parent.getBoard());
          //Attempt to get groupName string
          StringMessage groupNameMessage = (StringMessage)(playerConnection.receiveObject());
          String groupName = groupNameMessage.unpacker();
          //Return failure if not assignable
          if(!parent.assignGroups(groupName, player)){
            playerConnection.sendObject(new StringMessage("Fail: Group invalid or already taken."));
            continue;
          }
          break;
        }
        //Otherwise succeeds
        playerConnection.sendObject(new StringMessage("Success: Group assigned."));
        int startUnits = Constants.UNIT_START_MULTIPLIER*parent.getBoard().getNumRegionsOwned(player);
        
        //Prompt for placement
        while(true){
          //Decrease timeout to maxTime-(current-start)
          //Ensures will time out at maxTime after start
          playerConnection.getSocket().setSoTimeout((int)(maxTime-(System.currentTimeMillis() - startTime)));     
          //Send board
          playerConnection.sendObject(parent.getBoard());
          //Retrieve orders
          List<OrderInterface> placementOrders;//changed for new order interface
          placementOrders = (ArrayList<OrderInterface>)(playerConnection.receiveObject());
          validator = new ValidatorHelper(player, new Unit(startUnits), parent.getBoard());
          //TODO: Validate orders --> loop if fail
          if(!validator.allPlacementsValid(placementOrders)){
            playerConnection.sendObject(new StringMessage("Fail: placements invalid"));
            continue;
          }
          
          //Convert to parent's board
          for(int i = 0; i < placementOrders.size(); i++){
            placementOrders.get(i).findValuesInBoard(parent.getBoard());
          }
          parent.addOrdersToMap(placementOrders);
          //Succeeds
          playerConnection.sendObject(new StringMessage("Success: placements valid."));
          break;
        }
        //Prevent initial call again
        firstCall = false;
      }
      else{
        //Send turn message
        playerConnection.sendObject(new StringMessage(turnMessage));
        //If called then new turn --> send continue
        playerConnection.sendObject(new StringMessage("Continue"));
        //Send player alive message
        playerConnection.sendObject(new ConfirmationMessage(parent.playerHasARegion(player)));
        //If alive then expecting orders
        if(parent.playerHasARegion(player)){
          //Prompt for orders --> validate
          while(true){
            //Decrease timeout to maxTime-(current-start)
            //Ensures will time out at maxTime after start
            playerConnection.getSocket().setSoTimeout((int)(maxTime-(System.currentTimeMillis() - startTime)));
            
            //Send board
            playerConnection.sendObject(parent.getBoard());

            //Prompt for orders
            List<OrderInterface> orders = (ArrayList<OrderInterface>)(playerConnection.receiveObject());
            validator = new ValidatorHelper(player, parent.getBoard());
            //TODO: Validate orders --> loop if fail
            if(!validator.allOrdersValid(orders)){
              playerConnection.sendObject(new StringMessage("Fail: orders invalid"));
              continue;
            }
            
            //Convert to parent's board
            for(int i = 0; i < orders.size(); i++){
              orders.get(i).findValuesInBoard(parent.getBoard());
            }
            parent.addOrdersToMap(orders);
            //Otherwise succeeds
            playerConnection.sendObject(new StringMessage("Success: orders valid."));
            break;
          }
        }
        else{
          //If here then no regions left --> mark not playing
          player.setPlaying(false);
          //If not alive --> check if watching
          //If watching null then haven't been prompted
          if(player.isWatching() == null){
            //Only valid Y/N send from player --> no need for timeout loop
            
            //Get confirmation message
            ConfirmationMessage spectateMessage = (ConfirmationMessage)(playerConnection.receiveObject());
            //Set watching boolean
            //player.setWatching(new Boolean(spectateMessage.getMessage()));
            player.setWatching(Boolean.valueOf(spectateMessage.getMessage()));
          }
          //If watching then send board (otherwise client disconnected)
          if(player.isWatching()){
            //Send board and success
            playerConnection.sendObject(parent.getBoard());
            playerConnection.sendObject(new StringMessage("Success: spectate"));
          }
        }
      }
    }
    catch(Exception e){
      //If anything fails then kill player
      //Server only so actually not huge concern the readability of this
      //TODO: make this more human readable
      System.out.println(player.getName() + " exception encountered, killing connection");
      //e.printStackTrace();
      player.setPlaying(false);
      playerConnection.closeAll();
      return;
    }
    System.out.println(player.getName() + " exiting thread gracefully");
  }
}
