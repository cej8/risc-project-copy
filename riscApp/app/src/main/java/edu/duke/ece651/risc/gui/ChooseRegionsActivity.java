package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import edu.duke.ece651.risc.shared.Connection;

public class ChooseRegionsActivity extends AppCompatActivity {
    private Connection connection;
    private ExecuteClient executeClient;
    private EditText editRegionGroup;
    private TextView userPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_regions);
        connection = ParentActivity.getConnection();
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
        editRegionGroup= findViewById(R.id.regionName);
        userPrompt = findViewById(R.id.helpText);
        executeClient.getBoardAssignments(userPrompt);

    }

    public void chooseRegion(View view) throws IOException, ClassNotFoundException, InterruptedException{
        //String regionGroup = view.getTag().toString();
        String regionGroup = editRegionGroup.getText().toString();
        executeClient.chooseRegions(userPrompt, regionGroup);
    }
}
