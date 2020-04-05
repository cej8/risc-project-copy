package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;


import edu.duke.ece651.risc.shared.Connection;

public class ConfirmLoginActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_login);
        executeClient = new ExecuteClient(this);
        executeClient.createGame();
    }

    public void login(View view){
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
   }
    public void register(View view){
        Intent regIntent = new Intent(this,RegisterActivity.class);
        startActivity(regIntent);
    }
}
