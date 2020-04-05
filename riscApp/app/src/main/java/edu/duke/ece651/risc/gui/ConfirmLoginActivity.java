package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
