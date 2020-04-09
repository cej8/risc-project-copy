package edu.duke.ece651.risc.gui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import edu.duke.ece651.risc.shared.Connection;

public class LoginActivity extends AppCompatActivity {
    private TextView helpText;
    private EditText editUsername;
    private EditText editPassword;
    private Button loginButton;
    private Button registerButton;
    private TextView welcomeText;
    ExecuteClient executeClient;
    Connection connection;
    String loginResult;
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
        connection = ParentActivity.getConnection();
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
//        Intent loginIntent = getIntent();
//        loginResult = loginIntent.getStringExtra("LOGIN");
//        helpText.setText(loginResult);
    }

    // TODO: on button press we send object to
    public void userLogin(View view) throws IOException, ClassNotFoundException, InterruptedException{
        helpText.setText("");
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        executeClient.loginGame(username, password, helpText);
    }
}

