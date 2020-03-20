package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class ChildServer extends Thread{
  private AbstractPlayer player;  
  private ParentServer parent;
  //private ValidateHelper validator

  public ChildServer(AbstractPlayer player, ParentServer parent){
    this.player = player;
    this.parent = parent;
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


  @Override
  public void run(){
    //If player isn't playing or isn't watching then skip
    if(!player.isPlaying() || !player.isWatching()){
      return;
    }
    
    try{
      if(firstCall){
        //Prompt for region --> placement
        
        //Prompt for region
        while(true){
          //Send board
          player.getConnection().sendObject(parent.getBoard());
        
          //Attempt to get groupName string
          String groupName = (String)(player.getConnection().receiveObject());

          //Return failure if not assignable
          if(!parent.assignGroups(groupName, player)){
            player.getConnection().sendObject(new StringMessage("Fail: Group invalid or already taken."));
            continue;
          }
          break;
        }
        //Otherwise succeeds
        player.getConnection().sendObject("Success: Group assigned.");

        //Prompt for placement
        while(true){
          //Send board
          player.getConnection().sendObject(parent.getBoard());

          //Retrieve orders
          List<OrderInterface> placementOrders;
          placementOrders = (List<OrderInterface>)(player.getConnection().receiveObject());
          //TODO: Validate orders --> loop if fail
          //if(placementOrders not valid){
          //player.getConnection().sendObject(new StringMessage("Fail: placements invalid"));
          //continue;
          //}

          parent.addOrdersToMap(placementOrders);
          break;
        }
        //Succeeds
        player.getConnection().sendObject(new StringMessage("Success: placements valid."));
        //Prevent initial call again
        firstCall = false;
      }
      else{
        //If called then new turn --> send continue
        player.getConnection().sendObject(new StringMessage("Continue"));
        //Send player alive message
        player.getConnection().sendObject(new ConfirmationMessage(parent.isAlive(player)));
        //If alive then expecting orders
        if(parent.isAlive(player)){
          //Prompt for orders --> validate
        
          while(true){
            //Send board
            player.getConnection().sendObject(parent.getBoard());

            //Prompt for orders
            List<OrderInterface> orders = (List<OrderInterface>)(player.getConnection().receiveObject());

            //TODO: Validate orders --> loop if fail
            //if(orders not valid){
            //player.getConnection().sendObject(new StringMessage("Fail: orders invalid"));
            //continue;
            //}
            parent.addOrdersToMap(orders);
            break;
          }
          //Otherwise succeed
          player.getConnection().sendObject(new StringMessage("Success: orders valid."));
        }
      
        else{
          //If not alive --> check if watching
          //If watching null then haven't been prompted
          if(player.isWatching() == null){
            //Get confirmation message
            ConfirmationMessage spectateMessage = (ConfirmationMessage)(player.getConnection().receiveObject());
            //Set watching boolean
            player.setWatching(new Boolean(spectateMessage.getMessage()));
          }
          //If watching then send board (otherwise client disconnected)
          if(player.isWatching()){
            //Send board and success
            player.getConnection().sendObject(parent.getBoard());
            player.getConnection().sendObject(new StringMessage("Success: spectate"));
          }
        }
      }
    }
    catch(Exception e){
      //If anything fails then kill player
      e.printStackTrace();
      player.setPlaying(false);
      player.getConnection().closeAll();
      return;
    }
  }
}
