package edu.duke.ece651.risc.server;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.Region;

import static org.junit.jupiter.api.Assertions.*;
public class ParentServerTest {
  /*@Test
  public void test_ServerSimple() {
    /*    ParentServer ps = new ParentServer();
    try{
      System.out.println("Waiting for connection");
      ps.waitingForConnections();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
      return;
    }
    ChildServer child = ps.getChildren().get(0);
    StringMessage message = new StringMessage("I am testing networking");
    try{
      child.getPlayer().sendObject(message);
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
    ps.closeAll();
    }*/
  @Test
  public void test_createStartingGroups(){
    ParentServer ps = new ParentServer();
    HumanPlayer player = new HumanPlayer();
    player.setName("Player One");
    HumanPlayer playerTwo = new HumanPlayer();
    playerTwo.setName("Player Two");
    ChildServer cs = new ChildServer(player,ps);
    ChildServer csTwo = new ChildServer(playerTwo, ps);
    //ps.addPlayer(cs);
    //ps.addPlayer(csTwo);
    ps.createStartingGroups();
    Board board = ps.getBoard();
    List<Region> regions = board.getRegions();
    for (int i = 0; i < regions.size(); i++){
      System.out.println(i + ": " + regions.get(i).getOwner().getName() + " " + regions.get(i).getUnits().getUnits());
    }
    }

}
