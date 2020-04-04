package edu.duke.ece651.risc.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.BoardGenerator;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.Unit;

public class DisplayMapActivity extends AppCompatActivity {
    List<Region> regions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        BoardGenerator boardGenerator = new BoardGenerator();
        boardGenerator.createBoard();
        Board board = boardGenerator.getBoard();
        regions = board.getRegions();
        Unit u = new Unit(5);
        for (int i = 0; i < regions.size(); i++){
            regions.get(i).setUnits(u);
        }
        Log.d("Inside map regions",regions.get(0).getName());
    }
    public void setRegions(ArrayList<Region> regions){
        this.regions = regions;
    }
    public List<Region> getRegions(){
        return regions;
    }
    // TODO: change from hardcoded to based off of regionList
    public void planetOne(View view){
          Region region = regions.get(0);

        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Hoth",5);
        dialogFragment.show(getSupportFragmentManager(), "P1");
    }
    public void planetTwo(View view){
         Region region = regions.get(1);
         DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 2",5);
        dialogFragment.show(getSupportFragmentManager(), "P2");
    }
    public void planetThree(View view){
         Region region = regions.get(2);
         DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 3",5);
        dialogFragment.show(getSupportFragmentManager(), "P3");
    }
    public void planetFour(View view){
        Region region = regions.get(3);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 4",5);
        dialogFragment.show(getSupportFragmentManager(), "P4");
    }
    public void planetFive(View view){
        Region region = regions.get(4);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
       // DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 5",5);
        dialogFragment.show(getSupportFragmentManager(), "P5");
    }
    public void planetSix(View view){
        Region region = regions.get(5);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 6",5);
        dialogFragment.show(getSupportFragmentManager(), "P6");
    }
    public void planetSeven(View view){
        Region region = regions.get(6);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 7",5);
        dialogFragment.show(getSupportFragmentManager(), "P7");
    }
    public void planetEight(View view){
        Region region = regions.get(7);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 8",5);
        dialogFragment.show(getSupportFragmentManager(), "P8");
    }
    public void planetNine(View view){
        Region region = regions.get(8);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 9",5);
        dialogFragment.show(getSupportFragmentManager(), "P9");
    }
    public void planetTen(View view){
        Region region = regions.get(9);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 10",5);
        dialogFragment.show(getSupportFragmentManager(), "P10");
    }
    public void planetEleven(View view){
        Region region = regions.get(10);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 11",5);
        dialogFragment.show(getSupportFragmentManager(), "P11");
    }
    public void planetTwelve(View view){
        Region region = regions.get(11);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region.getName(),region.getUnits().getTotalUnits());
        //DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment("Planet 12",5);
        dialogFragment.show(getSupportFragmentManager(), "P12");
    }

}
