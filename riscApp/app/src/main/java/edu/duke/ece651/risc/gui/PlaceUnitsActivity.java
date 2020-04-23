package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.PlacementOrder;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.Unit;
import edu.duke.ece651.risc.shared.ValidatorHelper;

public class PlaceUnitsActivity extends AppCompatActivity {
    TextView headerText,p1,p2,p3,p4,p5,p6;
    EditText r1,r2,r3,r4,r5,r6;
    Board board;
    Region region;
    List<Region> regionList = new ArrayList<Region>();
    HumanPlayer player;
    List<Region> playerRegions = new ArrayList<Region>();
    ExecuteClient executeClient;
    Connection connection;
    List<EditText> planetUnits = new ArrayList<EditText>();
    List<TextView> planetName = new ArrayList<TextView>();
    private Handler handler = new Handler();
    private ValidatorHelper validatorHelper;
    private int startUnits;
    PlanetDrawable planetDrawable;
    Map<Region, ImageView> regionImageViewMap;
    List<AbstractPlayer> tempPlayerList;
    List<Region> tempRegionOwners;
    Board tempBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_units);
        executeClient = new ExecuteClient(this);
        connection = ParentActivity.getConnection();
        executeClient.setConnection(connection);
        board = ParentActivity.getBoard();
        regionList = board.getRegions();
        player = ParentActivity.getPlayer();
        startUnits = Constants.UNIT_START_MULTIPLIER * board.getNumRegionsOwned(player);
        getRegionByOwner();
        //displayText();
        headerText = findViewById(R.id.headerText);
        headerText.setText("You have You have " + startUnits + " units to place on your planets. Hit submit when finished!");
        validatorHelper = new ValidatorHelper(player,new Unit(startUnits), board);
    }
    @Override
    protected void onResume(){
        super.onResume();
        headerText.setText("Those placements were invalid. You have You have " + startUnits + " units to place on your planets. Hit submit when finished!");
    }
    @Override
    protected void onStart() {
        super.onStart();
        List<ImageButton> planetButtons = getPlanetButtons();
        List<TextView> planetPlayers = getPlanetPlayers();
       // List<TextView> unitCircles = getUnitCircles();
        List<ImageView> playerColors = getPlayerColors();
        List<ImageView> planetViews = getPlanetViews();
        // create temp players with your players name
        updateBoardPlayers();
        for (int i = 0; i < tempPlayerList.size(); i++){
            Log.d("Players", tempPlayerList.get(i).getName());
        }
        updateRegionOwner();
        for (int i = 0; i < tempRegionOwners.size(); i++){
            Log.d("Region", tempRegionOwners.get(i).getName());
            Log.d("Region Owners", tempRegionOwners.get(i).getOwner().getName());
        }
        tempBoard = new Board();
        tempBoard.setRegions(tempRegionOwners);
        planetDrawable = new PlanetDrawable(tempBoard, planetButtons, playerColors, planetPlayers,planetViews);
        regionImageViewMap = planetDrawable.getRegionToPlanetViewMap();
        planetDrawable.setGreyOutlines();
        //planetDrawable.setAllUnitCircles();
        planetDrawable.setPlanetsNoUnits();
        for (AbstractPlayer p : tempPlayerList) {
            if (!p.getName().equals(player.getName())) { //if not player's planet, set view to outline
                if (p!=null){ //if owned by someone, set to their outline color and make button invisible
                    for (Region r : getPlayerRegionSetTemp(p)) {
                        regionImageViewMap.get(r).setBackgroundResource(planetDrawable.getPlayerToOutlineMap().get(p));
                        planetDrawable.setImageButtonsInvisible(p);
                    }
                }
                else{ //if player is null, set button invisible and set grey outline
                    planetDrawable.setGreyOutlines();
                    planetDrawable.setImageButtonsInvisible(p);
                }
            } else { //if player own's planet, set up visible planet
                for (Region r : getPlayerRegionSetTemp(p)) {
                   // planetDrawable.setPlanets();
                    regionImageViewMap.get(r).setBackgroundResource(planetDrawable.getRegionToPlanetDrawableMap().get(r));
                }
            }
        }
        disablePlanetButtons();
        disableEditText();
    }
    public void disableEditText(){
        int count = 0;
        List<EditText> placementsEditText = getUnitCircles();
        for (Region region : tempRegionOwners) {
            if (!region.getOwner().getName().equals(player.getName())){
                placementsEditText.get(count).setVisibility(View.INVISIBLE);
                placementsEditText.get(count).setEnabled(false);
            }
            count++;
        }
    }
    public void updateRegionOwner(){
        tempRegionOwners = new ArrayList<Region>();
        for (Region r : board.getRegions()){
            if (ParentActivity.getRegionGroup().equals(r.getOwner().getName())){
                Region region = r;
                region.setOwner(ParentActivity.getPlayer());
                tempRegionOwners.add(region);
            } else {
                tempRegionOwners.add(r);
            }
        }
    }
    //Creates a Set of all regions a player owns on the Board
    public Set<Region> getPlayerRegionSetTemp(AbstractPlayer p){
        List<Region> allRegions = this.tempRegionOwners;
        Set<Region> playerRegions = new HashSet<Region>();
        for (Region r : allRegions) { //for each region on the board
            if (r.getOwner() != null) {
                if (r.getOwner().getName().equals(p.getName())) { //if player owns it
                    playerRegions.add(r); //add that region to the set
                }
            }
        }
        return playerRegions;
    }
    public void updateBoardPlayers(){
        tempPlayerList = new ArrayList<AbstractPlayer>();
        for (AbstractPlayer p : board.getPlayerList()){
            if (ParentActivity.getRegionGroup().equals(p.getName())){
                tempPlayerList.add(new HumanPlayer(ParentActivity.getPlayer().getName()));
            } else {
                tempPlayerList.add(p);
            }
        }
    }

    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        // instead of going to new activity open up dialog fragment
        BackButtonDialogFragment backButtonDialogFragment = new BackButtonDialogFragment(this);
        backButtonDialogFragment.show(getSupportFragmentManager(),"back");
    }

    public void sendPlacements(View view){
        if (makePlacements()){
            executeClient.placementOrder(handler);
        } else {
            return;
        }
    }
    public void resetSelections(){
        for (int j = 0; j < playerRegions.size(); j++){
            if (j > 5){
                break;
            }
            planetUnits.get(j).setText("");
        }
    }
    public boolean makePlacements(){
        String unitPlacement;
        Unit unit;
        Region region;
        if (startUnits == 36) {
            unit = new Unit(3);
            for (int i = 0; i < playerRegions.size();i++){
                region = getRegionByName(board,playerRegions.get(i).getName());
                PlacementOrder placementOrder = new PlacementOrder(region,unit);
                ParentActivity parentActivity = new ParentActivity();
                parentActivity.setOrders(placementOrder);
            }
        } else {
            for (int j = 0; j < playerRegions.size(); j++) {
                if (j > 5) {
                    break;
                }
                unitPlacement = planetUnits.get(j).getText().toString();
                unit = new Unit(Integer.parseInt(unitPlacement));
                region = getRegionByName(board, playerRegions.get(j).getName());
                PlacementOrder placementOrder = new PlacementOrder(region, unit);
                ParentActivity parentActivity = new ParentActivity();
                parentActivity.setOrders(placementOrder);
            }
        }
        if(!validatorHelper.allPlacementsValid(ParentActivity.getOrders())){
            resetSelections();
            ParentActivity parentActivity = new ParentActivity();
            parentActivity.resetOrders();
            return false;
        }
        return true;
    }
    public Region getRegionByName(Board board, String name){
        Map<String, Region> nameToRegionMap = new HashMap<String, Region>();
        for (Region r : board.getRegions()){
            nameToRegionMap.put(r.getName(), r);
        }
        return nameToRegionMap.get(name);
    }
    public void getRegionByOwner(){
        for (int i = 0; i < regionList.size(); i++) {
            if (player.getName().equals(regionList.get(i).getOwner().getName())) {
                playerRegions.add(regionList.get(i));
            }
        }
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

    public List<ImageView> getPlayerColors() {
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
    public void disablePlanetButtons(){
        List<ImageButton> planetButtons = getPlanetButtons();
        for (ImageButton b : planetButtons){
            b.setEnabled(false);
        }
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
    public List<EditText> getUnitCircles() {
        //List<EditText> unitCircles = new ArrayList<EditText>();
        EditText unit0 = findViewById(R.id.p0units);
        EditText unit1 = findViewById(R.id.p1units);
        EditText unit2 = findViewById(R.id.p2units);
        EditText unit3 = findViewById(R.id.p3units);
        EditText unit4 = findViewById(R.id.p4units);
        EditText unit5 = findViewById(R.id.p5units);
        EditText unit6 = findViewById(R.id.p6units);
        EditText unit7 = findViewById(R.id.p7units);
        EditText unit8 = findViewById(R.id.p8units);
        EditText unit9 = findViewById(R.id.p9units);
        EditText unit10 = findViewById(R.id.p10units);
        EditText unit11 = findViewById(R.id.p11units);
        planetUnits.add(unit0);
        planetUnits.add(unit1);
        planetUnits.add(unit2);
        planetUnits.add(unit3);
        planetUnits.add(unit4);
        planetUnits.add(unit5);
        planetUnits.add(unit6);
        planetUnits.add(unit7);
        planetUnits.add(unit8);
        planetUnits.add(unit9);
        planetUnits.add(unit10);
        planetUnits.add(unit11);
        return planetUnits;
    }
}
