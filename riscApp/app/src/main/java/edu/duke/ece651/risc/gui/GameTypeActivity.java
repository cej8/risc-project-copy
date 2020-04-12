package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import edu.duke.ece651.risc.shared.Connection;

public class GameTypeActivity extends AppCompatActivity {
    Connection connection;
    ExecuteClient executeClient;
    TextView textView;
    private Handler gametypeHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);
        executeClient = new ExecuteClient(this);
        connection = ParentActivity.getConnection();
        executeClient.setConnection(connection);
    }
    // Take me to page displaying new games to join
    public void gameNew(View view){
        executeClient.getGames(gametypeHandler,false,true);
    }
    // Take me to screen displaying games I previously joined
    public void gameOld(View view){
        executeClient.getGames(gametypeHandler,true,true);
    }
}
