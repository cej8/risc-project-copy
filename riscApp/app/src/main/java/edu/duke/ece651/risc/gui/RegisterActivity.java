package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        executeClient = new ExecuteClient(this);
        executeClient.createGame();
    }

    public void userRegister(View view) {
        // TODO: registration
    }
}
