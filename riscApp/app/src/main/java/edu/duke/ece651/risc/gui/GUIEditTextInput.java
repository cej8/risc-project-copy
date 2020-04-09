package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.widget.EditText;

import java.io.InputStream;

import edu.duke.ece651.risc.client.ClientInputInterface;

public class GUIEditTextInput implements ClientInputInterface {
    //This class is responsible for reading user input from auser input text box  
  
    EditText editText;
 Activity activity;

  public GUIEditTextInput(EditText editText, Activity a){
        this.editText = editText;
        this.activity=a;
    }
    public GUIEditTextInput(Activity a) {
      this.activity = a;
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
        //input.close();
    }

}
