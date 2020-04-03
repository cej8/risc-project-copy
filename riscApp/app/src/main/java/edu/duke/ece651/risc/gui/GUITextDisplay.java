package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Region;

public class GUITextDisplay extends Thread implements ClientOutputInterface {

    TextView outputTextView;
    Activity activity;

    public GUITextDisplay(){
        try{
            this.outputTextView = outputTextView.findViewById(R.id.popUpText);
        }
        //Shouldn't be possible...
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public GUITextDisplay(TextView textView, Activity act) {
        this.outputTextView = textView;
        outputTextView = outputTextView.findViewById(R.id.popUpText);
        this.activity = act;
    }
    @Override
    //prints the board info to stdout
    public void displayBoard(Board b){
        String boardText = createBoard(b);
        final String output = boardText;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outputTextView.setText(output);
            }
        });
        Thread.dumpStack();
        //outputTextView.setText("test");
    }

    @Override
    public void displayString(String str){
        final String output = str;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outputTextView.setText(output);
            }
        });
       // outputTextView.setText(str);
    }

    //returns a String of all of the board info
    public String createBoard(Board b){
        StringBuilder boardText = new StringBuilder();
        Map<AbstractPlayer, List<Region>> playerRegionMap = b.getPlayerToRegionMap();
        List<AbstractPlayer> players = new ArrayList<AbstractPlayer>(playerRegionMap.keySet());
        Collections.sort(players);
        for(AbstractPlayer player : players) { //for each entry in the map
            boardText.append(player.getName() + ": \n---------\n"); //append player name
            for (Region r : playerRegionMap.get(player)){ //for each region the player has
                boardText.append(this.printRegionInfo(r)); //add its info to board
            }
            boardText.append("\n");
        }
        return boardText.toString();
    }

    //returns String w board info for a given region
    private String printRegionInfo(Region r){
        int numUnits = r.getUnits().getTotalUnits();
        String name = r.getName();
        StringBuilder sb = new StringBuilder(numUnits + " units in " + name); //add info on num units in region
        sb.append(this.printRegionAdjacencies(r)); //add adj info
        return sb.toString();
    }

    //returns String w adjacency info info for a given region
    private String printRegionAdjacencies(Region r){
        StringBuilder sb = new StringBuilder(" (next to:");
        List<Region> adjList = r.getAdjRegions();
        for (int i = 0; i < adjList.size(); i++){
            sb.append(" " + adjList.get(i).getName()); //add name of adjacent region to sb
            if (i <  adjList.size() -1){
                sb.append(","); //add a comma if not the last in list
            }
        }
        sb.append(")\n"); //close parentheses
        return sb.toString();
    }
}
