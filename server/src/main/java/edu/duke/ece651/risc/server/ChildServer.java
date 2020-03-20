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
}
