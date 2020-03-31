package com.example.risc;

import java.io.InputStream;

//this interface is used to take in user input from any input stream
//it will return a single string line to be read and handled
public interface ClientInputInterface {
    public String readInput();
    public void close();
}
