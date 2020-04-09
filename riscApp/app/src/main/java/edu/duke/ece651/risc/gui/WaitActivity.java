package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import edu.duke.ece651.risc.shared.Connection;

public class WaitActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    TextView wait;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        wait = findViewById(R.id.waittext);
        connection = ParentActivity.getConnection();
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
        executeClient.displayServerBoard(wait);
    }
}
