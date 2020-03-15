package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class ChildServer extends Thread{
  private AbstractPlayer player;  
  private ParentServer parent;
  //private ValidateHelper validator

  boolean firstCall = true;

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
    
    if(!player.isPlaying() || !player.isWatching()){
      return;
    }
    
    try{
      if(firstCall){
        //Prompt for region --> placement
        
        //Prompt for region
        while(true){
          player.sendObject(parent.getBoard());
        
          //Attempt to get groupName string
          String groupName = (String)(player.receiveObject());

          if(!parent.assignGroups(groupName, player)){
            player.sendObject(new StringMessage("Fail: Group invalid or already taken."));
            continue;
          }
          break;
        }
        player.sendObject("Success: Group assigned.");

        //Prompt for placement
        while(true){
          player.sendObject(parent.getBoard());

          List<OrderInterface> placementOrders;
          placementOrders = (List<OrderInterface>)(player.receiveObject());
          //TODO: Validate orders --> loop if fail
          parent.addOrdersToMap(placementOrders);
          break;
        }
        player.sendObject(new StringMessage("Success: placements valid."));
      
      
        firstCall = false;
      }
      else{
        player.sendObject(new StringMessage("Continue"));
        player.sendObject(new ConfirmationMessage(parent.isAlive(player)));
        if(parent.isAlive(player)){
          //Prompt for orders --> validate
        
          while(true){
            player.sendObject(parent.getBoard());

            List<OrderInterface> orders = (List<OrderInterface>)(player.receiveObject());

            //TODO: Validate orders --> loop if fail
            parent.addOrdersToMap(orders);
            break;
          }
          player.sendObject(new StringMessage("Success: orders valid."));
        }
      
        else{
          //If not alive --> check if watching
          if(player.isWatching() == null){
            ConfirmationMessage spectateMessage = (ConfirmationMessage)(player.receiveObject());
            player.setWatching(new Boolean(spectateMessage.getMessage()));
          }

          if(player.isWatching()){
            player.sendObject(parent.getBoard());
            player.sendObject(new StringMessage("Success: spectate"));
          }
        }
      }
    }
    catch(Exception e){
      e.printStackTrace();
      player.setPlaying(false);
      player.closeAll();
      return;
    }
  }
}
