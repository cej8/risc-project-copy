package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView outputText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExecuteClient executeClient = new ExecuteClient();
        outputText = findViewById(R.id.popUpText);
        executeClient.createGame(outputText,this);
    }
}
