package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

/*Thread to handle communication to client, run() called once per turn.

Attempts to handle all message passing between client/server for single turn within a game.

Has handling for missing client connections (not connected to this thread/game) and deals with timeouts internally.
*/

// Class that handles each child implemtation for game (i.e. each player has their own ChildServer)
public class ChildServer implements Runnable{
  //Player object for client
  private AbstractPlayer player;
  //ParentServer that owns this child
  private ParentServer parent; 
  //Connection for player
  private Connection playerConnection;
  //Message to send for turn
  private String turnMessage = "";

  //Boolean for if firstCall of threads (placement)
  private boolean firstCall = true;
  //Boolean for first turn with actual moves, if true then get server's PlacementBoard
  private boolean firstTurnWithMoves = true;
  //Boolean for if connection has failed (playerConnection null/DC'ed)
  private boolean connectionFailed = false;

  //Number of turns without input successful
  private int missedTurns = 0;
  //Time for turn start
  private long startTime;
  //Maximum time for turn
  private long maxTime;
  //Boolean for if turn complete (passed successfully)
  private boolean finishedTurn = true;

  //Client's version of the board
  private Board clientBoard;

  public ChildServer(AbstractPlayer player, ParentServer parent){
    this.player = player;
    this.parent = parent;
  }

  public ChildServer(AbstractPlayer player, Connection playerConnection, ParentServer parent){
    this.player = player;
    this.playerConnection = playerConnection;
    this.parent = parent;
  }

  
  // Getters & setters
  public AbstractPlayer getPlayer(){
    return player;
  }

  public void setPlayer(AbstractPlayer player){
    this.player = player;
  }

  public ParentServer getParentServer(){
    return parent;
  }
  public void setParentServer(ParentServer parent){
    this.parent = parent;
  }

  public Connection getPlayerConnection(){
    return playerConnection;
  }
  public void setPlayerConnection(Connection playerConnection){
    this.playerConnection = playerConnection;
  }

  public void setTurnMessage(String turnMessage){
    this.turnMessage = turnMessage;
  }

  public boolean getFirstCall(){
    return firstCall;
  }

  public boolean isFinished(){
    return finishedTurn;
  }

  public Board getClientBoard(){
    return clientBoard;
  }

  public void firstTurnCall() throws IOException, SocketException, ClassNotFoundException{
    ValidatorHelper validator;
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
      //Validate orders --> loop if fail
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
  }

  //Method to call turn, return true if "successful"
  //Return false if socket has exception
  public boolean performTurn(){
    //If player isn't playing or isn't watching then skip them
    if(!player.isPlaying()){
      //if not playing then previously was --> not first call
      firstCall = false;
      if(player.isWatching() != null && !player.isWatching()){
        return true;
      }
    }
    //If no connection then fails
    if(playerConnection == null){
      return false;
    }
    
    ValidatorHelper validator;
    try{
      System.out.println(parent.getGameID() + " : (" + player.getName() + ") enter thread");
      playerConnection.getSocket().setSoTimeout((int)maxTime);
      if(firstCall){
        firstTurnCall();
        //Prevent first turn again
        firstCall = false;
      }
      else{
        //Get client's visible version of the board locally
        if(parent.getFOG_OF_WAR()){
          if(firstTurnWithMoves){ //If first turn with moves then get placement board
            clientBoard = (Board)DeepCopy.deepCopy(parent.getBoard());
          }
          clientBoard.updateVisible(player.getName(), parent.getBoard());
        }
        else{
          clientBoard = parent.getBoard();
        }
        //Send player
        playerConnection.sendObject(player);
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
            playerConnection.sendObject(clientBoard);

            //Prompt for orders
            List<OrderInterface> orders = (ArrayList<OrderInterface>)(playerConnection.receiveObject());
            validator = new ValidatorHelper(player, parent.getBoard());
            //Validate orders --> loop if fail
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
            boolean spectate = Boolean.valueOf(spectateMessage.getMessage());
            //Set watching boolean
            //player.setWatching(new Boolean(spectateMessage.getMessage()));
            player.setWatching(spectate);
            if(spectate == false){
              parent.removePlayer(player.getName());
            }
          }
          //If watching then send board (otherwise client disconnected)
          if(player.isWatching()){
            //Send board and success
            playerConnection.sendObject(parent.getBoard());
            playerConnection.sendObject(new StringMessage("Success: spectate"));
          }
        }
        firstTurnWithMoves = false;
      }
    }
    catch(Exception e){
      System.out.println(parent.getGameID() + " : (" + player.getName() + ") had some issue, disconnected");
      if(!(e instanceof SocketTimeoutException)){
        e.printStackTrace();
      }
      playerConnection.closeAll();
      playerConnection = null;
      parent.getMasterServer().removePlayer(player.getName(), parent.getGameID());
      player.setWatchingNull();
      return false;
    }
    System.out.println(parent.getGameID() + " : (" + player.getName() + ") exiting thread gracefully");
    return true;
  }
  
  // end of getters & setters
  // enables ChildServer to be runnable
  @Override
  public void run(){
    finishedTurn = false;
    startTime = System.currentTimeMillis();
    //Timeout is Socket's timeout
    maxTime = (long)(parent.getTURN_WAIT_MINUTES()*60*1000);
    //If turn fails --> socket failed or not connected
    while(maxTime > (System.currentTimeMillis() - startTime)){
      if(performTurn()){
        //If successful then decrement missed turns to zero
        missedTurns = (missedTurns > 0) ? (missedTurns - 1) : (0);
        System.out.println(parent.getGameID() + " : (" + player.getName() + ") input turn, missed now " + missedTurns);
        finishedTurn = true;
        return;
      }
    }
    //If exits then never successfully performTurn
    //Increment missedTurns
    missedTurns++;
    System.out.println(parent.getGameID() + " : (" + player.getName() + ") did not input turn, missed now " + missedTurns);
    //If past maximum then mark as not playing
    if(missedTurns > parent.getMAX_MISSED()){
      System.out.println(parent.getGameID() + " : (" + player.getName() + ") missed more than " + parent.getMAX_MISSED() + " turns, marking as dead");
      player.setPlaying(false);
    }
    if(missedTurns == 1 && firstCall == true){
      System.out.println(parent.getGameID() + " : (" + player.getName() + ") missed initial placements, marking as dead");
      player.setPlaying(false);
      playerConnection.closeAll();
      playerConnection = null;
      parent.getMasterServer().removePlayer(player.getName(), parent.getGameID());
      player.setWatchingNull();
    }
  }
}
