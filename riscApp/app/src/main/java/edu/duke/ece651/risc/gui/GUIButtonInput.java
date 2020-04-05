package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.widget.EditText;

import java.io.InputStream;

import edu.duke.ece651.risc.client.ClientInputInterface;

public class GUIButtonInput implements ClientInputInterface {
    //This class is responsible for reading user input from auser input text box  
  
  Button button;
  Activity activity;
  public GUIButtonInput(Button b, Activity a){
    this.button= findViewByIy(R.id.b);
    this.activity= a;
       
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
