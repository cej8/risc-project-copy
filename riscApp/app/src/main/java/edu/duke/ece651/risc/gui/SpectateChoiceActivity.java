package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SpectateChoiceActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectate_choice);
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(ParentActivity.getConnection());
    }
    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        // instead of going to new activity open up dialog fragment
        BackButtonDialogFragment backButtonDialogFragment = new BackButtonDialogFragment(this);
        backButtonDialogFragment.show(getSupportFragmentManager(),"back");
    }

    public void spectateGame(View view){
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setSpectateFirstCall(true);
        executeClient.spectate(true,handler);
    }
    public void exitGame(View view){
        executeClient.spectate(false,handler);
    }
}
