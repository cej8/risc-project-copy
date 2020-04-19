package edu.duke.ece651.risc.gui;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.Region;

public class PlanetDrawable {
    Board board;
    List<ImageButton> planetButtons;
    List<ImageView> playerSquares;
    List<TextView> playerNames;


    public PlanetDrawable(Board b, List<ImageButton> buttons , List<ImageView> squares, List<TextView> names){
        board = b;
        planetButtons = buttons;
        playerSquares = squares;
        playerNames = names;
    }

    public void setPlanets() {
        this.setPlayerSquares();
        List<AbstractPlayer> players = board.getPlayerList();
        List<Integer> planetDrawables = getPlanetDrawables();
        Map<AbstractPlayer, Integer> playerToColorMap = getPlayerToColorMap();
        Map<Region, ImageButton> regionToButtonMap = getRegionToButtonMap();
        Map<AbstractPlayer, List<Region>> playerToRegionMap = board.getPlayerToRegionMap();
        Map<Integer, String> intToColorMap = getIntToColorMap();
        for (AbstractPlayer p : players) { //for each entry in the map
            for (Region r : playerToRegionMap.get(p)) { //for each region the player has
                System.out.println(p.getName() + "'s region " + r.getName() + " is " + intToColorMap.get(playerToColorMap.get(p)));
                regionToButtonMap.get(r).setBackgroundResource(playerToColorMap.get(p)); //set the drawable of that region's corresponding image button to that player's color
            }
        }
        //set all unowned regions to grey
        for (Region r : board.getRegions()) {
            if (r.getOwner() == null) {
                regionToButtonMap.get(r).setBackgroundResource(R.drawable.grey_planet);
            }
        }
    }

    public void setPlayerSquares() {
        List<AbstractPlayer> players = board.getPlayerList();
        Map<AbstractPlayer, Integer> playerToColorMap = getPlayerToColorMap();
        Map<Integer, String> intToColorMap = getIntToColorMap();
        for (int i = 0; i < players.size(); i++){
           playerSquares.get(i).setBackgroundResource(playerToColorMap.get(players.get(i)));
           playerNames.get(i).setText(players.get(i).getName());
        }
    }

    public  Map<AbstractPlayer, Integer> getPlayerToColorMap(){
        List<AbstractPlayer> players = board.getPlayerList();
//        System.out.println("num of players " + players.size());
//        for (AbstractPlayer p : players){
//            System.out.println("Player = " + p.getName());
//        }
        List<Integer> planetDrawables = getPlanetDrawables();
        Map<Integer, String> intToColorMap = getIntToColorMap();
        Map<AbstractPlayer, Integer> playerToColorMap = new HashMap<AbstractPlayer, Integer>();
        for (int i = 0; i < players.size(); i++){
            playerToColorMap.put(players.get(i), planetDrawables.get(i));
//            Log.d(players.get(i).getName() + " is color ",  intToColorMap.get(playerToColorMap.get(players.get(i))));
        }
        return playerToColorMap;
    }

    //regions
    public Map<Region, ImageButton> getRegionToButtonMap(){
        Map<Region, ImageButton> regionToButtonMap = new HashMap<Region, ImageButton>();
        System.out.println("Number of planet buttons " + planetButtons.size());
        System.out.println("Number of regions " + board.getRegions().size());
        for (int i = 0; i < planetButtons.size(); i++){
            regionToButtonMap.put(board.getRegions().get(i), planetButtons.get(i));
        }
        return regionToButtonMap;
    }

    public Map<Integer, String> getIntToColorMap(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(R.drawable.red_planet, "RED");
        map.put(R.drawable.blue_planet, "BLUE");
        map.put(R.drawable.green_planet, "GREEN");
        map.put(R.drawable.sky_planet, "SKY");
        map.put(R.drawable.pink_planet, "PINK");
        return map;
    }



    public List<Integer> getPlanetDrawables(){
        Integer redPlanet = (R.drawable.red_planet);
        Integer bluePlanet = (R.drawable.blue_planet);
        Integer greenPlanet = (R.drawable.green_planet);
        Integer skyPlanet = (R.drawable.sky_planet);
        Integer pinkPlanet = (R.drawable.pink_planet);
        List<Integer>  planetDrawables = new ArrayList<Integer>();
        planetDrawables.add(redPlanet);
        planetDrawables.add(bluePlanet);
        planetDrawables.add(greenPlanet);
        planetDrawables.add(skyPlanet);
        planetDrawables.add(pinkPlanet);
        //planetDrawables.add(greyPlanet);
        return planetDrawables;
    }


}
