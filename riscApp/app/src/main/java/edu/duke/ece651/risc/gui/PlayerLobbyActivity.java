package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import edu.duke.ece651.risc.shared.Connection;

public class PlayerLobbyActivity extends AppCompatActivity {
    private Connection connection;
    private ExecuteClient executeClient;
    private EditText editRegionGroup;
    private TextView userPrompt;
    private Button begin;
    private Button ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_lobby);
        ready= findViewById(R.id.ready);
        begin = findViewById(R.id.begin);
        begin.setEnabled(false);
        connection = ParentActivity.getConnection();
        userPrompt = findViewById(R.id.helpText);
        //   executeClient.getBoardAssignments(userPrompt);
        userPrompt.setText("WAITING FOR OTHER PLAYERS TO JOIN......");

        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);



    }

    public void playerReady(View view)throws IOException, ClassNotFoundException, InterruptedException{
        userPrompt.setText("READY TO BEGIN!");
        executeClient.getBoardAssignments(userPrompt);
        begin.setEnabled(true);


    }
    public void beginGame(View view)throws IOException, ClassNotFoundException, InterruptedException {
       // userPrompt.setText("WAITING FOR OTHER PLAYERS TO JOIN......");

         executeClient.startPlacement();

    }
}
