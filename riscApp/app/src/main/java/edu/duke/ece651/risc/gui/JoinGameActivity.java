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

public class JoinGameActivity extends AppCompatActivity {
    Connection connection;
    ExecuteClient executeClient;
    EditText gameID;
    String gameList;
    TextView textView;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        gameID = findViewById(R.id.gameIDJoin);
        textView = findViewById(R.id.gameListJoin);
        Intent intent = getIntent();
        gameList = intent.getStringExtra("GAMELIST");
        executeClient = new ExecuteClient(this);
        connection = ParentActivity.getConnection();
        executeClient.setConnection(connection);
        textView.setText(gameList);
    }

    public void joinGame(View view){
        String idGame = gameID.getText().toString();
        executeClient.pickGame(handler,true,idGame,false,gameList);
    }
}
