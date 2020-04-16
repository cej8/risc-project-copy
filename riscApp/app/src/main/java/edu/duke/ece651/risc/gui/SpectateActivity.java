package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Region;

public class SpectateActivity extends AppCompatActivity {
    Board board;
    List<Region> regions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectate);
        board = ParentActivity.getBoard();
        regions = board.getRegions();
    }

    public void planetOne(View view){
        Region region = regions.get(10);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P1");
    }
    public void planetTwo(View view){
        Region region = regions.get(1);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P2");
    }
    public void planetThree(View view){
        Region region = regions.get(6);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P3");
    }
    public void planetFour(View view){
        Region region = regions.get(11);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P4");
    }
    public void planetFive(View view){
        Region region = regions.get(0);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P5");
    }
    public void planetSix(View view){
        Region region = regions.get(5);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P6");
    }
    public void planetSeven(View view){
        Region region = regions.get(2);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P7");
    }
    public void planetEight(View view){
        Region region = regions.get(8);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P8");
    }
    public void planetNine(View view){
        Region region = regions.get(9);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P9");
    }
    public void planetTen(View view){
        Region region = regions.get(3);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P10");
    }
    public void planetEleven(View view){
        Region region = regions.get(4);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P11");
    }
    public void planetTwelve(View view){
        Region region = regions.get(7);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P12");
    }
}
