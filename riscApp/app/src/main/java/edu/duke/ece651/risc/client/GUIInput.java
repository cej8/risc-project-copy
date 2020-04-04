package edu.duke.ece651.risc.client;

import java.io.InputStream;

public class GUIInput implements ClientInputInterface{

    public GUIInput(InputStream inputStream){
    }
    @Override
    public String readInput() {
        return null;
    }

    @Override
    public void close() {

    }
}
