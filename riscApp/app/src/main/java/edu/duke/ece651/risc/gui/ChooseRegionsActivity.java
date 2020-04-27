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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Region;

public class ChooseRegionsActivity extends AppCompatActivity {
    private Connection connection;
    private ExecuteClient executeClient;
    private EditText editRegionGroup;
    private TextView userPrompt;
    private TextView boardView;
    private Button submit;
    private Handler handler = new Handler();
    private Board board;
    private List<Region> regions;
    private String regionGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_regions);
        submit= findViewById(R.id.submit);
        boardView= findViewById(R.id.showBoard);
        connection = ParentActivity.getConnection();
        executeClient = new ExecuteClient(this);
        executeClient.setConnection(connection);
        //editRegionGroup= findViewById(R.id.regionName);
        userPrompt = findViewById(R.id.helpText);
        //userPrompt.setText("Please select a starting group by typing in a group name (i.e. 'Group A')");
        userPrompt.setText("Please select a starting group by selecting a planet");
    }
    @Override
    protected void onStart() {
        super.onStart();
        //executeClient.showStartBoard(boardView);
        board = ParentActivity.getBoard();
        regions = board.getRegions();
        List<ImageButton> planetButtons = getPlanetButtons();
        List<TextView> planetPlayers = getPlanetPlayers();
        List<ImageView> planetSquares = getPlanetSquares();
        List<ImageView> planetViews = getPlanetViews();
        PlanetDrawable planetDrawable = new PlanetDrawable(ParentActivity.getBoard(), planetButtons, planetSquares, planetPlayers, planetViews);
        planetDrawable.setPlanetsNoUnits();
        planetDrawable.setGreyOutlines();
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
        //String regionGroup = editRegionGroup.getText().toString();
        if (regionGroup == null){
            // do nothing
        } else {
            ParentActivity pa = new ParentActivity();
            pa.setRegionGroup(regionGroup);
            executeClient.chooseRegions(handler, boardView, regionGroup);
        }
    }

    // Planet Buttons:
    public void planetZero(View view){
        Region r = regions.get(0);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + " with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetOne(View view){
        Region r = regions.get(1);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetTwo(View view){
        Region r = regions.get(2);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetThree(View view){
        Region r = regions.get(3);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetFour(View view){
        Region r = regions.get(4);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetFive(View view){
        Region r = regions.get(5);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetSix(View view){
        Region r = regions.get(6);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetSeven(View view){
        Region r = regions.get(7);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetEight(View view){
        Region r = regions.get(8);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetNine(View view){
        Region r = regions.get(9);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetTen(View view){
        Region r = regions.get(10);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    public void planetEleven(View view){
        Region r = regions.get(11);
        String o = r.getOwner().getName();
        String groupList = getPlanetsByOwner(o);
        String full = "You have selected:\n" + o + "with\n" + groupList + "hit submit to continue with selection";
        this.regionGroup = o;
        boardView.setText(full);
    }
    // Helper method
    private String getPlanetsByOwner(String owner){
        String temp = "";
        for (Region r : board.getRegions()) {
            if (owner.equals(r.getOwner().getName())){
                temp += r.getName() + "\n";
            }
        }
        return temp;
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

    public List<ImageView> getPlanetViews(){
        List<ImageView> views = new ArrayList<ImageView>();
        ImageView p0I = findViewById(R.id.p0I);
        ImageView p1I = findViewById(R.id.p1I);
        ImageView p2I = findViewById(R.id.p2I);
        ImageView p3I = findViewById(R.id.p3I);
        ImageView p4I = findViewById(R.id.p4I);
        ImageView p5I = findViewById(R.id.p5I);
        ImageView p6I = findViewById(R.id.p6I);
        ImageView p7I = findViewById(R.id.p7I);
        ImageView p8I = findViewById(R.id.p8I);
        ImageView p9I = findViewById(R.id.p9I);
        ImageView p10I = findViewById(R.id.p10I);
        ImageView p11I = findViewById(R.id.p11I);
        views.add(p0I);
        views.add(p1I);
        views.add(p2I);
        views.add(p3I);
        views.add(p4I);
        views.add(p5I);
        views.add(p6I);
        views.add(p7I);
        views.add(p8I);
        views.add(p9I);
        views.add(p10I);
        views.add(p11I);
        return views;
    }

}
