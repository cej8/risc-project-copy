package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.widget.EditText;

import java.io.InputStream;
import java.util.Scanner;

import edu.duke.ece651.risc.client.ClientInputInterface;

public class GUIConsoleInput implements ClientInputInterface {
    //This class is responsible for reading user input from the console line by line.
    Scanner input;
    Activity activity;
    EditText editText;

    public GUIConsoleInput(){
        //this.input = new Scanner(System.in);
       // this.editText = new EditText();
    }

    public GUIConsoleInput(EditText editText, Activity act){
        //this.input = new Scanner(input);
        this.editText = editText;
        this.activity = act;
    }
    public void setEditText(EditText edit){
        this.editText = edit;
    }

    @Override
    public String readInput() {
        // runnable?
        return editText.getText().toString();
    }

    @Override
    public void close(){
        input.close();
    }

}
