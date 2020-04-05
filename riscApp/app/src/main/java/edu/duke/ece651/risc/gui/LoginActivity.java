package edu.duke.ece651.risc.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private TextView helpText;
    private EditText editUsername;
    private EditText editPassword;
    private Button loginButton;
    private Button registerButton;
    private TextView welcomeText;
    ExecuteClient executeClient;
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
        executeClient = new ExecuteClient(this);
        executeClient.createGame();
        /*ExecuteClient executeClient = new ExecuteClient();
        outputText = findViewById(R.id.popUpText);
        executeClient.createGame(outputText,this,**figure out the edit text stuff);*/

    }
    // on button click open DisplayGameActivity.java
//    public void userLogin(View view) {
//        Intent loginIntent = new Intent(this, DisplayGamesActivity.class);
//        startActivity(loginIntent);
//    }
    // TODO: on button press we send object to
    // on button press do GUIClientLogin??
    public void userLogin(View view) throws IOException, ClassNotFoundException{
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        executeClient.loginGame(username, password);
        Boolean loginResult = executeClient.getLoginResult();
//        if (loginResult == false){
//            // set help text
//            helpText.setText("Username or password not found. Please register if needed.");
//        } else {
//            // start new intent aka display available games
//            //Intent loginIntent = new Intent(this, );
//        }
        helpText.setText("Username or password not found. Please register if needed");

    }
    public void userRegister(View view) {
        // TODO: registration
    }
}

