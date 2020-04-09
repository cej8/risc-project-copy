package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import edu.duke.ece651.risc.shared.Connection;

public class RegisterActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    private TextView helpText;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private Button registerButton;
    private TextView welcomeText;
    Connection connection;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        helpText = findViewById(R.id.helpText);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.register);
        welcomeText = findViewById(R.id.welcomeText);
        executeClient = new ExecuteClient(this);
      connection = ParentActivity.getConnection();
      executeClient.setConnection(connection);
    }

    public void userRegister(View view) throws IOException, ClassNotFoundException, InterruptedException{
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword= editConfirmPassword.getText().toString();
        executeClient.registerLogin(username, password, confirmPassword,helpText);
    }
}
