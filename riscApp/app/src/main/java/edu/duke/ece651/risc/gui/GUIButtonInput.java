package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;

import edu.duke.ece651.risc.client.ClientInputInterface;

public class GUIButtonInput implements ClientInputInterface {
    //This class is responsible for reading user input from auser input text box  
  
  Button button;
  Activity activity;

  public GUIButtonInput(Button b, Activity a){
    this.button= b;//.findViewById(R.id.b);
    this.activity= a;
       
    }
   

    @Override
    public String readInput() {
        // runnable?
      // TODO: need this editText return statement
       // return editText.getText().toString();
      return "";
    }

    @Override
    public void close(){
        //input.close();
    }

}
