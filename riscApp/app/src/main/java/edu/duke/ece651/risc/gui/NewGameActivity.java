package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.Connection;

public class NewGameActivity extends AppCompatActivity {
    Connection connection;
    ExecuteClient executeClient;
    EditText gameID;
    String gameList;
    TextView textView;
    ClientOutputInterface clientOutput;
    private Handler newGameHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        gameID = findViewById(R.id.gameID);
        textView = findViewById(R.id.gameList);
        Intent intent = getIntent();
        gameList = intent.getStringExtra("GAMELIST");
        executeClient = new ExecuteClient(this);
        connection = ParentActivity.getConnection();
        executeClient.setConnection(connection);
        textView.setText(gameList);
    }

    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        connection.closeAll();
        //Intent intent = new Intent(this, GameTypeActivity.class);
        Intent intent = new Intent(this, ConfirmLoginActivity.class);
        intent.putExtra("REPROMPT","reprompt");
        startActivity(intent);
        finish();
    }

    public void newGame(View view){
        String idGame = gameID.getText().toString();
        executeClient.pickGame(newGameHandler,false,idGame,false,gameList);
    }
}
