package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Region;

public class DisplayBonusUnitsActivity extends AppCompatActivity {
    TextView unit0;
    TextView unit1;
    TextView unit2;
    TextView unit3;
    TextView unit4;
    TextView unit5;
    TextView unit6;
    Board board;
    List<Region> regions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bonus_units);
        Intent i = getIntent();
        String attackFrom = i.getStringExtra("PNAME");
        String attackTo = i.getStringExtra("ATTACKTO");
        String s = attackFrom + attackTo;
        board = ParentActivity.getBoard();
        regions = board.getRegions();
        //for
       // String civilian = regions.
        //unit0.setText();
    }
}
