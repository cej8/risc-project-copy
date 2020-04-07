package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WaitActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    TextView wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        wait = findViewById(R.id.waittext);
        executeClient = new ExecuteClient(this);
        executeClient.displayServerBoard(wait);
    }
}
