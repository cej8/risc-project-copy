package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadActivity extends AppCompatActivity {
    TextView helpText;
    ExecuteClient executeClient;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        helpText = findViewById(R.id.helpText);
        progressBar = findViewById(R.id.ctrlActivityIndicator);
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(ParentActivity.getConnection());
    }
    @Override
    protected void onStart(){
        super.onStart();
            executeClient.getBoardAssignments(helpText);
    }
}
