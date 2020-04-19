package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import edu.duke.ece651.risc.shared.Connection;

public class WaitActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    TextView wait;
    Connection connection;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        wait = findViewById(R.id.waittext);
        connection = ParentActivity.getConnection();
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
        executeClient.displayServerBoard(handler,wait);
    }

    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        // instead of going to new activity open up dialog fragment
        BackButtonDialogFragment backButtonDialogFragment = new BackButtonDialogFragment(this);
        backButtonDialogFragment.show(getSupportFragmentManager(),"back");
    }
}
