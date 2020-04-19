package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        List<ImageButton> planetButtons = getPlanetButtons();
        List<TextView> planetPlayers = getPlanetPlayers();
        List<ImageView> planetSquares = getPlanetSquares();
        PlanetDrawable planetDrawable = new PlanetDrawable(ParentActivity.getBoard(), planetButtons, planetSquares, planetPlayers);
        planetDrawable.setPlanetsNoUnit();
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
    // Layout
    public List<TextView> getPlanetPlayers() {
        List<TextView> planetPlayers = new ArrayList<TextView>();
        TextView player0 = findViewById(R.id.player0);
        TextView player1 = findViewById(R.id.player1);
        TextView player2 = findViewById(R.id.player2);
        TextView player3 = findViewById(R.id.player3);
        TextView player4 = findViewById(R.id.player4);
        planetPlayers.add(player0);
        planetPlayers.add(player1);
        planetPlayers.add(player2);
        planetPlayers.add(player3);
        planetPlayers.add(player4);
        return planetPlayers;
    }

    public List<ImageView> getPlanetSquares() {
        List<ImageView> planetSquares = new ArrayList<ImageView>();
        ImageView square0 = findViewById(R.id.square0);
        ImageView square1 = findViewById(R.id.square1);
        ImageView  square2 = findViewById(R.id.square2);
        ImageView square3 = findViewById(R.id.square3);
        ImageView square4 = findViewById(R.id.square4);
        planetSquares.add(square0);
        planetSquares.add(square1);
        planetSquares.add(square2);
        planetSquares.add(square3);
        planetSquares.add(square4);
        return planetSquares;
    }
    public List<ImageButton> getPlanetButtons(){
        List<ImageButton > planetButtons = new ArrayList<ImageButton>();
        ImageButton planet0 = findViewById(R.id.p0);
        ImageButton planet1 = findViewById(R.id.p1);
        ImageButton planet2 = findViewById(R.id.p2);
        ImageButton planet3 = findViewById(R.id.p3);
        ImageButton planet4 = findViewById(R.id.p4);
        ImageButton planet5 = findViewById(R.id.p5);
        ImageButton planet6 = findViewById(R.id.p6);
        ImageButton planet7 = findViewById(R.id.p7);
        ImageButton planet8 = findViewById(R.id.p8);
        ImageButton  planet9 = findViewById(R.id.p9);
        ImageButton  planet10 = findViewById(R.id.p10);
        ImageButton  planet11 = findViewById(R.id.p11);
        planetButtons.add(planet0);
        planetButtons.add(planet1);
        planetButtons.add(planet2);
        planetButtons.add(planet3);
        planetButtons.add(planet4);
        planetButtons.add(planet5);
        planetButtons.add(planet6);
        planetButtons.add(planet7);
        planetButtons.add(planet8);
        planetButtons.add(planet9);
        planetButtons.add(planet10);
        planetButtons.add(planet11);
        return planetButtons;
    }
}
