package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketException;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;

public class PlayerLobbyActivity extends AppCompatActivity {
    private Connection connection;
    private ExecuteClient executeClient;
    private EditText editRegionGroup;
    private TextView userPrompt;
    private Button begin;
    private Button ready;
   private Handler handler = new Handler();
   private ProgressBar status;
    private double START_WAIT_MINUTES = Constants.START_WAIT_MINUTES + .1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_lobby);
        status= findViewById(R.id.ctrlActivityIndicator);
        begin = findViewById(R.id.begin);
        ready = findViewById(R.id.ready);
        begin.setEnabled(false);
        connection = ParentActivity.getConnection();
        userPrompt = findViewById(R.id.helpText);
        userPrompt.setText("WAITING FOR OTHER PLAYERS TO JOIN......");
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
    }

    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
      // instead of going to new activity open up dialog fragment
        BackButtonDialogFragment backButtonDialogFragment = new BackButtonDialogFragment(this);
        backButtonDialogFragment.show(getSupportFragmentManager(),"back");
    }
    public void readyGame(View view){
                try {
            executeClient.getBoardAssignments(ready,status,begin,handler,userPrompt);
        } catch (InterruptedException e) {
         e.printStackTrace();
        }
    }

    public void beginGame(View view)throws IOException, ClassNotFoundException, InterruptedException {

       Intent newGame= new Intent(this, ChooseRegionsActivity.class);
        startActivity(newGame);
    }
}
