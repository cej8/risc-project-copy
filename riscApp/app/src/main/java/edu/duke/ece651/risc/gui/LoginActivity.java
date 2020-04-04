package edu.duke.ece651.risc.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private TextView helpText;
    private EditText editUsername;
    private EditText editPassword;
    private Button loginButton;
    private Button registerButton;
    private TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        helpText = findViewById(R.id.helpText);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        welcomeText = findViewById(R.id.welcomeText);
        /*ExecuteClient executeClient = new ExecuteClient();
        outputText = findViewById(R.id.popUpText);
        executeClient.createGame(outputText,this);*/
    }
    // on button click open DisplayGameActivity.java
    public void userLogin(View view) {
        Intent loginIntent = new Intent(this, DisplayGamesActivity.class);
        startActivity(loginIntent);
    }
    public void userRegister(View view) {
        // TODO: registration
    }
}

