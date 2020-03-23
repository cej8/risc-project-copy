package edu.duke.ece651.risc.shared;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


public class DeepCopyFailureExceptionTest {
  @Test
  public void test_DCFE() {
    
    Exception cnf = new ClassNotFoundException();
    DeepCopyFailureException ecnf = new DeepCopyFailureException(cnf);
    assertEquals(cnf, ecnf.getCause());
    assertEquals(cnf, ecnf.exn);    

    Exception io = new IOException();
    DeepCopyFailureException eio = new DeepCopyFailureException(io);
    assertEquals(io, eio.getCause());
    assertEquals(io, eio.exn);

  
  }
}
