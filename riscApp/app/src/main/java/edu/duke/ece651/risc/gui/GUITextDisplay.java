package edu.duke.ece651.risc.gui;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.duke.ece651.risc.client.ClientOutputInterface;
import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.Spy;

public class GUITextDisplay extends Thread implements ClientOutputInterface {

    TextView outputTextView;
    Activity activity;

    public GUITextDisplay(){
        try{
            //this.outputTextView = outputTextView.findViewById(R.id.popUpText);
        }
        //Shouldn't be possible...
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public GUITextDisplay(TextView textView, Activity act) {
        this.outputTextView = textView;
        //outputTextView = outputTextView.findViewById(R.id.popUpText);
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

    @Override
    public void displayBoard(Board b, String playerName) {
        final String boardText = createBoard(b, playerName, b.getVisibleRegions(playerName));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                outputTextView.setText(boardText);
            }
        });
        //outputTextView.println(boardText);
       // outputTextView.flush();
    }

    public String createBoard(Board b){
        Set<String> visible = new HashSet<String>();
        for(Region r : b.getRegions()){
            visible.add(r.getName());
        }
        return createBoard(b, "", visible);
    }

    //returns a String of all of the board info
    public String createBoard(Board b, String playerName, Set<String> visible){
        System.out.println(visible);
        StringBuilder boardText = new StringBuilder();
        Map<AbstractPlayer, List<Region>> playerRegionMap = b.getPlayerToRegionMap();
        List<AbstractPlayer> players = new ArrayList<AbstractPlayer>(playerRegionMap.keySet());
        Collections.sort(players);
        for(AbstractPlayer player : players) { //for each entry in the map
            boardText.append(player.getName());
            if(playerName.equals(player.getName()) || playerName.equals("")){
                boardText.append(": \nFuel: " + player.getResources().getFuelResource().getFuel() + "  Tech: " + player.getResources().getTechResource().getTech() + " Tech Level: " + player.getMaxTechLevel().getMaxTechLevel());
            }
            boardText.append("\n------------------\n"); //append player name
            for (Region r : playerRegionMap.get(player)){ //for each region the player has
                boardText.append(printRegionInfo(r, playerName, visible.contains(r.getName()))); //add its info to board
            }
            boardText.append("\n");
        }
        return boardText.toString();
    }

    //returns String w board info for a given region
    private String printRegionInfo(Region r, String playerName, boolean visible){
        StringBuilder sb = new StringBuilder();
        //Say old if not visible or has cloaking and not owned by player
        if(!visible){
            sb.append("Last known information: ");
        }
        //Add unit list
        sb.append(r.getUnits().getUnits() + " units in " + r.getName());
        sb.append(", ");
        //Add spy info
        sb.append(getSpies(r, playerName));
        sb.append(", ");
        sb.append(printRegionAdjacencies(r)); //add adj info
        sb.append(" (Size " + r.getSize() + ")");


        //Add cloak info
        if(r.getCloakTurns() > 0){
            sb.append(" (Cloaked for " + r.getCloakTurns() + " more turns)");
        }
        //Add plague info
        if(r.getPlague()){
            sb.append(" (PLAGUED)");
        }
        sb.append("\n");
        return sb.toString();
    }

    private String getSpies(Region r, String playerName){
        List<Spy> spyList = r.getSpies(playerName);
        if(spyList == null || spyList.size() == 0){
            return "No spies";
        }
        int haveMoved = 0;
        for(Spy spy : spyList){
            if(spy.getHasMoved()){
                haveMoved++;
            }
        }
        return haveMoved + " moved spies, " + (spyList.size() - haveMoved) + " ready spies";
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
        sb.append(")"); //close parentheses
        return sb.toString();
    }

}
