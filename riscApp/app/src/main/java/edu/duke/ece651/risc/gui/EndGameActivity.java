package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.duke.ece651.risc.shared.Connection;

public class EndGameActivity extends AppCompatActivity {
    private TextView helpText;
    private Button endButton;
    ExecuteClient executeClient;
    Connection connection;
    String winnerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        connection = ParentActivity.getConnection();
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
        endButton= findViewById(R.id.endgame);
        helpText= findViewById(R.id.prompt);
        Intent intent = getIntent();
       winnerName = intent.getStringExtra("WINNER");

    }
    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        // instead of going to new activity open up dialog fragment
        BackButtonDialogFragment backButtonDialogFragment = new BackButtonDialogFragment(this);
        backButtonDialogFragment.show(getSupportFragmentManager(),"back");
    }
    public void endGame(View view){
        executeClient.endGame();
    }
}
