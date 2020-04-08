package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.Constants;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.PlacementOrder;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.Unit;

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
        getRegionByOwner();
        displayText();
    }

    public void sendPlacements(View view){
        makePlacements();
        executeClient.placementOrder();
    }
    public void makePlacements(){
        String unitPlacement;
        Unit unit;
        Region region;
        for (int j = 0; j < playerRegions.size(); j++){
            unitPlacement = planetUnits.get(j).getText().toString();
            unit = new Unit(Integer.parseInt(unitPlacement));
            region = getRegionByName(board,playerRegions.get(j).getName());
            PlacementOrder placementOrder = new PlacementOrder(region,unit);
            ParentActivity parentActivity = new ParentActivity();
            parentActivity.setOrders(placementOrder);
        }
    }
    public void displayText(){
        headerText = findViewById(R.id.headerText);
        int startUnits = Constants.UNIT_START_MULTIPLIER * board.getNumRegionsOwned(player);
        headerText.setText("You have You have " + startUnits + " units to place on your planets. Hit submit when finished!");
        p1 = findViewById(R.id.planet1);
        p2 = findViewById(R.id.planet2);
        p3 = findViewById(R.id.planet3);
        p4 = findViewById(R.id.planet4);
        p5 = findViewById(R.id.planet5);
        p6 = findViewById(R.id.planet6);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        r5 = findViewById(R.id.r5);
        r6 = findViewById(R.id.r6);
        planetName.add(p1);
        planetName.add(p2);
        planetName.add(p3);
        planetName.add(p4);
        planetName.add(p5);
        planetName.add(p6);
        planetUnits.add(r1);
        planetUnits.add(r2);
        planetUnits.add(r3);
        planetUnits.add(r4);
        planetUnits.add(r5);
        planetUnits.add(r6);
        for (int j = 0; j < playerRegions.size(); j++){
           // for (int j = 0; j < 6; j++){
            if (j > 6){
                break;
            }
            planetName.get(j).setVisibility(View.VISIBLE);
            planetUnits.get(j).setVisibility(View.VISIBLE);
            planetName.get(j).setText(playerRegions.get(j).getName());
        }
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
}
