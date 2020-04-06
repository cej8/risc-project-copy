package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
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
    EditText bonus0;
    EditText bonus1;
    EditText bonus2;
    EditText bonus3;
    EditText bonus4;
    EditText bonus5;
    EditText bonus6;
    int u0,u1,u2,u3,u4,u5,u6;
    ArrayList<Integer> sendUnits;
    String attackFrom;
    String attackTo;
    String order;

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
        bonus0 = findViewById(R.id.bonus0);
        bonus1 = findViewById(R.id.bonus1);
        bonus2 = findViewById(R.id.bonus2);
        bonus3 = findViewById(R.id.bonus3);
        bonus4 = findViewById(R.id.bonus4);
        bonus5 = findViewById(R.id.bonus5);
        bonus6 = findViewById(R.id.bonus6);
        Intent i = getIntent();
        attackFrom = i.getStringExtra("PNAME");
        attackTo = i.getStringExtra("ATTACKTO");
        order = i.getStringExtra("ORDER");
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
        sendUnits = new ArrayList<Integer>();
    }
    public void next(View view){
        u0 = Integer.parseInt(bonus0.getText().toString());
        u1 = Integer.parseInt(bonus1.getText().toString());
        u2 = Integer.parseInt(bonus2.getText().toString());
        u3 = Integer.parseInt(bonus3.getText().toString());
        u4 = Integer.parseInt(bonus4.getText().toString());
        u5 = Integer.parseInt(bonus5.getText().toString());
        u6 = Integer.parseInt(bonus6.getText().toString());
        sendUnits.add(u0);
        sendUnits.add(u1);
        sendUnits.add(u2);
        sendUnits.add(u3);
        sendUnits.add(u4);
        sendUnits.add(u5);
        sendUnits.add(u6);
        Intent intent = new Intent(this,DisplayMapActivity.class);
        intent.putIntegerArrayListExtra("UNITS",sendUnits);
        intent.putExtra("ATTACKFROM",attackFrom);
        intent.putExtra("ATTACKTO",attackTo);
        intent.putExtra("ORDER",order);
        startActivity(intent);
    }
    public Region getRegionByName(Board board, String name){
        Map<String, Region> nameToRegionMap = new HashMap<String, Region>();
        for (Region r : board.getRegions()){
            nameToRegionMap.put(r.getName(), r);
        }
        return nameToRegionMap.get(name);
    }
}
