package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import edu.duke.ece651.risc.shared.Connection;

public class ChooseRegionsActivity extends AppCompatActivity {
    private Connection connection;
    private ExecuteClient executeClient;
    private EditText editRegionGroup;
    private TextView userPrompt;
    private TextView boardView;
    private Button submit;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_regions);
        submit= findViewById(R.id.submit);
        boardView= findViewById(R.id.showBoard);
        connection = ParentActivity.getConnection();
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
        editRegionGroup= findViewById(R.id.regionName);
         userPrompt = findViewById(R.id.helpText);
        userPrompt.setText("Please select a starting group by typing in a group name (i.e. 'Group A')");
    }
    @Override
    protected void onStart() {
        super.onStart();
        executeClient.showStartBoard(boardView);
    }

    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        // instead of going to new activity open up dialog fragment
        BackButtonDialogFragment backButtonDialogFragment = new BackButtonDialogFragment(this);
        backButtonDialogFragment.show(getSupportFragmentManager(),"back");
    }

    public void chooseRegion(View view) throws IOException, ClassNotFoundException, InterruptedException{
        String regionGroup = editRegionGroup.getText().toString();
        executeClient.chooseRegions(handler,boardView, regionGroup);
    }
}
