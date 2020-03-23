package edu.duke.ece651.risc.shared;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


public class DeepCopyFailureExceptionTest {
  @Test
  public void test_DCFE() {
    
    Exception ex = new ClassNotFoundException();
    DeepCopyFailureException e = new DeepCopyFailureException(ex);
    assertEquals(ex, e.getCause());
    assertEquals(ex, e.exn);    
  }

  // public void testDeepCopyException(){
  //   Region region = new Region("Earth");
  //   List<Region> regions = new ArrayList<Region>();
  //   regions.add(region);
  //   Board board = new Board(regions);
  //   Board exceptionBoard = mock(Board.class);//new Board(regions);
    
  //   //   doThrow(ex).when(DeepCopy.deepCopy(exceptionBoard));
  //   Board copy = (Board) DeepCopy.deepCopy(board);
  //   Board copyException = (Board) DeepCopy.deepCopy(exceptionBoard);

  // }
}
