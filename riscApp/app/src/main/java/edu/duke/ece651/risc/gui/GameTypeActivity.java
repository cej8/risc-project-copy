package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import edu.duke.ece651.risc.shared.Connection;

public class GameTypeActivity extends AppCompatActivity {
    Connection connection;
    ExecuteClient executeClient;
    TextView textView;
    private Handler gametypeHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setContentView(R.layout.activity_game_type);
        executeClient = new ExecuteClient(this);
        connection = ParentActivity.getConnection();
        executeClient.setConnection(connection);*/
    }
    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        connection.closeAll();
        Intent intent = new Intent(this, ConfirmLoginActivity.class);
        intent.putExtra("HELPTEXT","Logged out, please login again");
        startActivity(intent);
        finish();
    }
    @Override
    public void onResume(){
        super.onResume();
        // check if back button hit or first time
        Intent intent = getIntent();
        String reprompt = intent.getStringExtra("REPROMPT");
        if (reprompt != null) {
            // connect again
            executeClient = new ExecuteClient(this);
            executeClient.createGame();
            executeClient.setConnection(connection);
            // login
            connection = ParentActivity.getConnection();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GUIClientLogin clientLogin = new GUIClientLogin(connection, ParentActivity.getUsername(), ParentActivity.getPassword1());
                    try {
                        clientLogin.performLogin();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    setContentView(R.layout.activity_game_type);
                }
            });
           // setContentView(R.layout.activity_game_type);
        } else {
            setContentView(R.layout.activity_game_type);
            executeClient = new ExecuteClient(this);
            connection = ParentActivity.getConnection();
            executeClient.setConnection(connection);
        }
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
