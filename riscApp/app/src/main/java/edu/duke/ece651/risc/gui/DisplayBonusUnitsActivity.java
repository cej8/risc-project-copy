package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Region;

public class DisplayBonusUnitsActivity extends AppCompatActivity {
    TextView unitName0;
    TextView unitName1;
    TextView unitName2;
    TextView unitName3;
    TextView unitName4;
    TextView unitName5;
    TextView unitName6;
    Board board;
    List<Region> regions;
    TextView header;
    TextView helptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bonus_units);
        header = findViewById(R.id.header);
        helptext = findViewById(R.id.helpText);
        unitName0 = findViewById(R.id.unitName0);
        unitName1 = findViewById(R.id.unitName1);
        unitName2 = findViewById(R.id.unitName2);
        unitName3 = findViewById(R.id.unitName3);
        unitName4 = findViewById(R.id.unitName4);
        unitName5 = findViewById(R.id.unitName5);
        unitName6 = findViewById(R.id.unitName6);
        Intent i = getIntent();
        String attackFrom = i.getStringExtra("PNAME");
        String attackTo = i.getStringExtra("ATTACKTO");
        String order = i.getStringExtra("ORDER");
        header.setText("Units to " + order + " with:");
        helptext.setText("Please type number of units to send");
        String s = attackFrom + attackTo;
        board = ParentActivity.getBoard();
        regions = board.getRegions();
        Region region = getRegionByName(board,attackFrom);
        List<Integer> unitList = region.getUnits().getUnits();
        unitName0.setText("Civilian (" + unitList.get(0) + " units available)");
        unitName1.setText("Trainee (" + unitList.get(1) + " units available)");
        unitName2.setText("Junior Technicain (" + unitList.get(2) + " units available)");
        unitName3.setText("Aerospace Engineer (" + unitList.get(3) + " units available)");
        unitName4.setText("Space Cadet (" + unitList.get(4) + " units available)");
        unitName5.setText("Astronaut (" + unitList.get(5) + " units available)");
        unitName6.setText("Space Captain (" + unitList.get(6) + " units available)");
        //for
       // String civilian = regions.
        //unit0.setText();
    }
    public void next(View view){

    }
    public Region getRegionByName(Board board, String name){
        Map<String, Region> nameToRegionMap = new HashMap<String, Region>();
        for (Region r : board.getRegions()){
            nameToRegionMap.put(r.getName(), r);
        }
        return nameToRegionMap.get(name);
    }
}
