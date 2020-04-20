package edu.duke.ece651.risc.gui;

import android.util.Log;
import android.view.View;
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
    List<ImageView> playerColors;
    List<TextView> playerNames;
    List<TextView> unitCircles;
    List<ImageView> planetViews;


    public PlanetDrawable(Board b, List<ImageButton> buttons , List<ImageView> squares, List<TextView> names, List<TextView> circles, List<ImageView> views){
        board = b;
        planetButtons = buttons;
        playerColors = squares;
        playerNames = names;
        unitCircles = circles;
        planetViews = views;
    }
    // No units assigned
    public PlanetDrawable(Board b, List<ImageButton> buttons , List<ImageView> squares, List<TextView> names){
        board = b;
        planetButtons = buttons;
        playerColors = squares;
        playerNames = names;
        unitCircles = new ArrayList<>();
        planetViews = new ArrayList<>();
    }
    public void setPlanetsNoUnit() {
        this.setPlayerColors();
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
                regionToButtonMap.get(r).setBackgroundResource(R.drawable.grey_planet_outline);
            }
        }
    }

    public void setPlanets() {
        this.setPlanetsNoUnit();
        this.setUnitCircles();
    }

    public void setUnitCircles(){
        Map<Region, TextView> regionToCircleMap = getRegionToCircleMap();
        for (Region r : board.getRegions()) {
                regionToCircleMap.get(r).setText(Integer.toString(r.getUnits().getTotalUnits()));
        }
    }


    public void setPlayerColors() {
        List<AbstractPlayer> players = board.getPlayerList();
        Map<AbstractPlayer, Integer> playerToColorMap = getPlayerToColorMap();
        Map<Integer, String> intToColorMap = getIntToColorMap();
        for (int i = 0; i < players.size(); i++){
           playerColors.get(i).setBackgroundResource(playerToColorMap.get(players.get(i)));
           playerNames.get(i).setText(players.get(i).getName());
        }
    }

//set all visible
    public void setImageViewVisible(){
        for (ImageView view : planetViews){
            view.setVisibility(View.VISIBLE);
        }
    }
//set all but one visible
    public void setImageViewVisible(ImageView iv){
        for (ImageView view : planetViews){
            if (view != iv) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }




    public Map<Region, ImageView> getRegionToPlanetViewMap(){
        Map<Region, ImageView> regionToViewMap = new HashMap<Region, ImageView>();
        for (int i = 0; i < planetViews.size(); i++){
            regionToViewMap.put(board.getRegions().get(i), planetViews.get(i));
        }
        return regionToViewMap;
    }

    public  Map<AbstractPlayer, Integer> getPlayerToColorMap(){
        List<AbstractPlayer> players = board.getPlayerList();
        List<Integer> planetDrawables = getPlanetDrawables();
        Map<Integer, String> intToColorMap = getIntToColorMap();
        Map<AbstractPlayer, Integer> playerToColorMap = new HashMap<AbstractPlayer, Integer>();
        for (int i = 0; i < players.size(); i++){
            playerToColorMap.put(players.get(i), planetDrawables.get(i));
//            Log.d(players.get(i).getName() + " is color ",  intToColorMap.get(playerToColorMap.get(players.get(i)))); //testing player colors
        }
        return playerToColorMap;
    }

    //regions
    public Map<Region, TextView> getRegionToCircleMap(){
        Map<Region, TextView> regionToCircleMap = new HashMap<Region, TextView>();
        for (int i = 0; i < unitCircles.size(); i++){
            regionToCircleMap.put(board.getRegions().get(i), unitCircles.get(i));
        }
        return regionToCircleMap;
    }

    //regions
    public Map<Region, ImageButton> getRegionToButtonMap(){
        Map<Region, ImageButton> regionToButtonMap = new HashMap<Region, ImageButton>();
        for (int i = 0; i < planetButtons.size(); i++){
            regionToButtonMap.put(board.getRegions().get(i), planetButtons.get(i));
        }
        return regionToButtonMap;
    }

    public Map<Integer, String> getIntToColorMap(){
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(R.drawable.red_planet, "RED");
        map.put(R.drawable.blue_planet, "BLUE");
        map.put(R.drawable.sky_planet, "SKY");
        map.put(R.drawable.green_planet, "GREEN");
        map.put(R.drawable.pink_planet, "PINK");
//        map.put(R.drawable.red_planet_outline,"RED");
//        map.put(R.drawable.blue_planet_outline, "BLUE");
//        map.put(R.drawable.green_planet_outline, "GREEN");
//        map.put(R.drawable.skyblue_planet_outline, "SKY");
//        map.put(R.drawable.pink_planet_outline, "PINK");
        return map;
    }



    public List<Integer> getPlanetDrawables(){
        Integer redPlanet = (R.drawable.red_planet);
        Integer bluePlanet = (R.drawable.blue_planet);
        Integer skyPlanet = (R.drawable.sky_planet);
        Integer greenPlanet = (R.drawable.green_planet);
        Integer pinkPlanet = (R.drawable.pink_planet);
//        Integer redPlanet = (R.drawable.red_planet_outline);
//        Integer bluePlanet = (R.drawable.blue_planet_outline);
//        Integer greenPlanet = (R.drawable.green_planet_outline);
//        Integer skyPlanet = (R.drawable.skyblue_planet_outline);
//        Integer pinkPlanet = (R.drawable.pink_planet_outline); //commented out, using full planets instead
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
