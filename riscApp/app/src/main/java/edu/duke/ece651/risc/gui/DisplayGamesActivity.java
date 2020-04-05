package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import java.io.Serializable;

import edu.duke.ece651.risc.shared.Connection;

public class DisplayGamesActivity extends AppCompatActivity {
    Button joinGameButton;
    Button newGameButton;
    Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_games);
        joinGameButton = findViewById(R.id.joinGame);
        newGameButton = findViewById(R.id.newGame);
    }
    // TODO: update onClick listeners to do tasks - after joining whatever game need to display map!
    public void joinGame(View view){
        Intent joinGameIntent = new Intent();
    }
    public void newGame(View view){
        Intent newGameIntent = new Intent();
    }
}
