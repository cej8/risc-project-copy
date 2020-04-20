package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.duke.ece651.risc.shared.Connection;

public class WaitActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    TextView wait;
    Connection connection;
    private Handler handler = new Handler();
    Button nextTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        wait = findViewById(R.id.waittext);
        nextTurn= findViewById(R.id.continuegame);
        nextTurn.setEnabled(false);
        connection = ParentActivity.getConnection();
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
       // executeClient.displayServerBoard(handler,wait);
        executeClient.displayServerBoard(handler,wait,nextTurn);
    }

    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        // instead of going to new activity open up dialog fragment
        BackButtonDialogFragment backButtonDialogFragment = new BackButtonDialogFragment(this);
        backButtonDialogFragment.show(getSupportFragmentManager(),"back");
    }

    public void continuePlay (View view){
         Intent firstUnits = new Intent(this, DisplayMapActivity.class);
         startActivity(firstUnits);

    }
}
