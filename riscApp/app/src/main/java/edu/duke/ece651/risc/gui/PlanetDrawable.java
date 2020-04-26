package edu.duke.ece651.risc.gui;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Region;

public class PlanetDrawable {
    Board board;
    List<ImageButton> planetButtons;
    List<ImageView> playerColors;
    List<TextView> playerNames;
    List<TextView> unitCircles;
    List<ImageView> planetViews;
    List<AbstractPlayer> players;




    public PlanetDrawable(Board b, List<ImageButton> buttons , List<ImageView> squares, List<TextView> names, List<TextView> circles, List<ImageView> views){
        board = b;
        planetButtons = buttons;
        playerColors = squares;
        playerNames = names;
        players = board.getPlayerList();
        this.setPlayerColors();
        unitCircles = circles;
        planetViews = views;
    }
    // No units assigned
    public PlanetDrawable(Board b, List<ImageButton> buttons , List<ImageView> squares, List<TextView> names){
        board = b;
        planetButtons = buttons;
        playerColors = squares;
        playerNames = names;
        players = board.getPlayerList();
        this.setPlayerColors();
        unitCircles = new ArrayList<TextView>();
        planetViews = new ArrayList<ImageView>();
    }
    // placements
    public PlanetDrawable(Board b, List<ImageButton> buttons , List<ImageView> squares, List<TextView> names,List<ImageView> views){
        board = b;
        planetButtons = buttons;
        playerColors = squares;
        playerNames = names;
        players = board.getPlayerList();
        this.setPlayerColors();
        unitCircles = new ArrayList<TextView>();
        //planetViews = new ArrayList<ImageView>();
        planetViews = views;
    }
    // spectate
    public PlanetDrawable(Board b, List<ImageButton> buttons , List<ImageView> squares, List<TextView> names,List<ImageView> views,String s){
        board = b;
        planetButtons = buttons;
        playerColors = squares;
        playerNames = names;
        players = board.getPlayerList();
        this.setPlayerColors();
        unitCircles = new ArrayList<TextView>();
        //planetViews = new ArrayList<ImageView>();
        planetViews = views;
    }

    //setNotVisibleRegionsInvisible

    public void setInvisibleRegionsInvisible(AbstractPlayer player) {
        Set<Region> set = board.getSetVisibleRegions(player); //get all visible regions to player
        for (Region r : board.getRegions()) {
            if (!set.contains(r)) { //if not visible to player
                getRegionToPlanetViewMap().get(r).setBackgroundResource(R.drawable.grey_planet_outline);
                setImageButtonInvisible(r);
                Log.d("not visible", r.getName() + " ("+ r.getOwner().getName()+ ")");
            } else { //if player own's planet, set up visible planet
                Log.d("visible", r.getName() + " ("+ r.getOwner().getName()+ ")");
                getRegionToPlanetViewMap().get(r).setBackgroundResource(getRegionToPlanetDrawableMap().get(r));//.get(r).setBackgroundResource(R.drawable.grey_planet_outline);
                setUnitCircle(r);
            }
        }

    }

    //set units and planets for a particular player
    public void setPlanets(AbstractPlayer p) {
        this.setPlanetsNoUnits();
        // Set<Region> regionSet = board.getPlayerRegionSet(p);
        Set<Region> regionSet = board.getSetVisibleRegions(p);
        Log.d("inside setPlanets", "getRegionSetByName for " + p.getName());
        //this.setUnitCircles(regionSet);
        setInvisibleRegionsInvisible(p);
    }

    //set planets and units
    public void setPlanets() {
        this.setPlanetsNoUnits();
        this.setAllUnitCircles();
    }

    //set all regions to display units
    public void setAllUnitCircles() {
        Set<Region> regionSet = new HashSet<Region>(board.getRegions());
        this.setUnitCircles(regionSet);
    }

    //set a set of regions to display unit
    public void setUnitCircles(AbstractPlayer p){
        Set<Region> regionSet = board.getSetVisibleRegions(p); //get all visible regions to player
        Map<Region, TextView> regionToCircleMap = getRegionToCircleMap();
        for (Region r : board.getRegions()) {
            if (regionSet.contains(r)) {
                regionToCircleMap.get(r).setText(Integer.toString(r.getUnits().getTotalUnits()));
            }
        }
    }
    //set a set of regions to display unit
    public void setUnitCircle(Region region){
        Map<Region, TextView> regionToCircleMap = getRegionToCircleMap();
        regionToCircleMap.get(region).setText(Integer.toString(region.getUnits().getTotalUnits()));
    }

    //set a set of regions to display unit
    public void setUnitCircles(Set<Region> regionSet){
        Map<Region, TextView> regionToCircleMap = getRegionToCircleMap();
        for (Region r : board.getRegions()) {
            if (!regionSet.contains(r)) {
                regionToCircleMap.get(r).setText(Integer.toString(r.getUnits().getTotalUnits()));
            }
        }
    }
    //set planets without units
    public void setPlanetsNoUnits() {
        for (AbstractPlayer p : players){
            setPlayerDrawables(p);
        }
    }

//set unowned planets grey
    public void setGreyPlanets(){
        Set<String> names = new HashSet<String>(board.getPlayerStringList());
        for (Region r : board.getRegions()) {
            if (!names.contains(r.getOwner().getName())) { //if list of player names doesn't include this player (e.g. Group A, Group B, etc).
                getRegionToPlanetViewMap().get(r).setBackgroundResource(getRegionToPlanetDrawableMap().get(r));
                getRegionToButtonMap().get(r).setBackgroundResource(R.drawable.grey_planet);
                getRegionToButtonMap().get(r).setVisibility(View.VISIBLE);
                Log.d("grey planet", r.getName());
            }
        }
    }

    //set planets not owned by player to grey outline and remove button
    public void setGreyOutlines(){
        Set<String> names = new HashSet<String>(board.getPlayerStringList());
        for (Region r : board.getRegions()) {
            if (!names.contains(r.getOwner().getName())) {
                getRegionToPlanetViewMap().get(r).setBackgroundResource(R.drawable.grey_planet_outline);
                getRegionToButtonMap().get(r).setVisibility(View.INVISIBLE);
            }
        }
    }
public void setSpyImage(Region r) {
    ImageButton button = getRegionToButtonMap().get(r);
    button.setBackgroundResource(R.drawable.spytransparent);
}


//sets drawables to player colors
    public void setPlayerDrawables(AbstractPlayer p){
        for (Region r : board.getPlayerRegionSet(p)) { //for each region the player has
            getRegionToButtonMap().get(r).setBackgroundResource(getPlayerToColorMap().get(p)); //set the drawable of that region's corresponding image button to that player's color
        }
    }

//sets drawables to player outlines
    public void setPlayerOutlines(AbstractPlayer p){
        for (Region r : board.getPlayerRegionSet(p)) { //for each region the player has
            getRegionToButtonMap().get(r).setBackgroundResource(getPlayerToOutlineMap().get(p)); //set the drawable of that region's corresponding image button to that player's color
        }
    }



    public void setPlayerColors() {
        for (int i = 0; i < players.size(); i++){
           playerColors.get(i).setBackgroundResource(getPlayerToColorMap().get(players.get(i)));
           playerNames.get(i).setText(players.get(i).getName());
        }
    }


//set all but one visible
    public void setImageViewVisible(ImageView iv){
        for (ImageView view : planetViews){
            if ((view != iv)&& (view != null)){
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setImageViewToOutline(AbstractPlayer p){
        for (Region r : board.getRegions()) {
            if (r.getOwner() == p) {
                ImageView view = getRegionToPlanetViewMap().get(r);
                view.setBackgroundResource(getPlayerToOutlineMap().get(p));
            }
        }
    }

    //set single imageButton invisible
    public void setImageButtonInvisible(Region r){
            ImageButton button = getRegionToButtonMap().get(r);
            button.setVisibility(View.INVISIBLE);
    }

    //set all image buttons invisible for a player
    public void setImageButtonsInvisible(AbstractPlayer p){
        for (Region r : board.getPlayerRegionSet(p)) {
            ImageButton button = getRegionToButtonMap().get(r);
            button.setVisibility(View.INVISIBLE);
        }
    }


//player to colored outline
    public  Map<AbstractPlayer, Integer> getPlayerToOutlineMap(){
        List<AbstractPlayer> players = board.getPlayerList();
        List<Integer> planetDrawables = getPlanetOutlines();
        Map<AbstractPlayer, Integer> playerToOutlineMap = new HashMap<AbstractPlayer, Integer>();
        for (int i = 0; i < players.size(); i++){
            playerToOutlineMap.put(players.get(i), planetDrawables.get(i));
        }
        return playerToOutlineMap;
    }

    //player to colored sphere
    public  Map<AbstractPlayer, Integer> getPlayerToColorMap(){
        List<AbstractPlayer> players = board.getPlayerList();
        for (int i = 0; i < players.size();i++){
            Log.d("Players",players.get(i).getName());
        }
        List<Integer> planetDrawables = getColorDrawables();
        Map<AbstractPlayer, Integer> playerToColorMap = new HashMap<AbstractPlayer, Integer>();
        for (int i = 0; i < players.size(); i++){
            playerToColorMap.put(players.get(i), planetDrawables.get(i));
        }
        return playerToColorMap;
    }

    //region to (unit) circle
    public Map<Region, TextView> getRegionToCircleMap(){
        Map<Region, TextView> regionToCircleMap = new HashMap<Region, TextView>();
        for (int i = 0; i < unitCircles.size(); i++){
            regionToCircleMap.put(board.getRegions().get(i), unitCircles.get(i));
        }
        return regionToCircleMap;
    }

    //region to buttons
    public Map<Region, ImageButton> getRegionToButtonMap(){
        Map<Region, ImageButton> regionToButtonMap = new HashMap<Region, ImageButton>();
        for (int i = 0; i < planetButtons.size(); i++){
            regionToButtonMap.put(board.getRegions().get(i), planetButtons.get(i));
        }
        return regionToButtonMap;
    }

//region to planet image (12 to 12)
    public Map<Region, Integer> getRegionToPlanetDrawableMap(){
        List<Integer> drawables = getPlanetDrawables();
        Map<Region, Integer> regionToDrawableMap = new HashMap<Region, Integer>();
        for (int i = 0; i < board.getRegions().size(); i++){
            regionToDrawableMap.put(board.getRegions().get(i), drawables.get(i));
        }
        return regionToDrawableMap;
    }


//region to planet imageview
    public Map<Region, ImageView> getRegionToPlanetViewMap(){
        Map<Region, ImageView> regionToViewMap = new HashMap<Region, ImageView>();
        for (int i = 0; i < planetViews.size(); i++){
            regionToViewMap.put(board.getRegions().get(i), planetViews.get(i));
        }
        return regionToViewMap;
    }

    //planet images
    public List<Integer> getPlanetDrawables(){
	    List<Integer>  planetDrawables = new ArrayList<Integer>();
	    planetDrawables.add(R.drawable.p1nb);
	    planetDrawables.add(R.drawable.p2nb);
	    planetDrawables.add(R.drawable.p3nb);
	    planetDrawables.add(R.drawable.p4nb);
	    planetDrawables.add(R.drawable.p5nb);
	    planetDrawables.add(R.drawable.p6nb);
	    planetDrawables.add(R.drawable.p7nb);
	    planetDrawables.add(R.drawable.p8nb);
	    planetDrawables.add(R.drawable.p9nb);
	    planetDrawables.add(R.drawable.p10nb);
	    planetDrawables.add(R.drawable.p11nb);
	    planetDrawables.add(R.drawable.p12nb);
	    return planetDrawables;
    }

    //colored spheres
    public List<Integer> getColorDrawables(){
        Integer redPlanet = (R.drawable.red_planet);
        Integer bluePlanet = (R.drawable.blue_planet);
        Integer skyPlanet = (R.drawable.sky_planet);
        Integer greenPlanet = (R.drawable.green_planet);
        Integer pinkPlanet = (R.drawable.pink_planet);
        List<Integer>  planetDrawables = new ArrayList<Integer>();
        planetDrawables.add(redPlanet);
        planetDrawables.add(bluePlanet);
        planetDrawables.add(greenPlanet);
        planetDrawables.add(skyPlanet);
        planetDrawables.add(pinkPlanet);
        return planetDrawables;
    }


    public List<Integer> getPlanetOutlines(){
        Integer redPlanet = (R.drawable.red_planet_outline);
        Integer bluePlanet = (R.drawable.blue_planet_outline);
        Integer skyPlanet = (R.drawable.skyblue_planet_outline);
        Integer greenPlanet = (R.drawable.green_planet_outline);
        Integer pinkPlanet = (R.drawable.pink_planet_outline);
        List<Integer>  planetDrawables = new ArrayList<Integer>();
        planetDrawables.add(redPlanet);
        planetDrawables.add(bluePlanet);
        planetDrawables.add(greenPlanet);
        planetDrawables.add(skyPlanet);
        planetDrawables.add(pinkPlanet);
        return planetDrawables;
    }

    //probably unneeded methods, used for testing, commented out just in case i decide i want to use them in the future
//    public Map<Integer, String> getIntToColorMap(){
//        Map<Integer, String> map = new HashMap<Integer, String>();
//        map.put(R.drawable.red_planet, "RED");
//        map.put(R.drawable.blue_planet, "BLUE");
//        map.put(R.drawable.sky_planet, "SKY");
//        map.put(R.drawable.green_planet, "GREEN");
//        map.put(R.drawable.pink_planet, "PINK");
//        return map;
//    }
//    public Map<Integer, String> getIntToOutlineMap(){
//        Map<Integer, String> map = new HashMap<Integer, String>();
//        map.put(R.drawable.red_planet_outline,"RED");
//        map.put(R.drawable.blue_planet_outline, "BLUE");
//        map.put(R.drawable.skyblue_planet_outline, "SKY");
//        map.put(R.drawable.green_planet_outline, "GREEN");
//        map.put(R.drawable.pink_planet_outline, "PINK");
//        return map;
//    }

//    public Map<Integer, Integer> getPlanetToOutlineMap(){
//        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//        map.put(R.drawable.red_planet, R.drawable.red_planet_outline);
//        map.put(R.drawable.blue_planet, R.drawable.blue_planet_outline);
//        map.put(R.drawable.sky_planet, R.drawable.green_planet_outline);
//        map.put(R.drawable.green_planet, R.drawable.skyblue_planet_outline);
//        map.put(R.drawable.pink_planet, R.drawable.pink_planet_outline);
//        map.put(R.drawable.grey_planet, R.drawable.grey_planet_outline);
//        return map;
//    }
//
//    public  Map<AbstractPlayer, Integer> getPlayerToPlanetMap(){
//        List<AbstractPlayer> players = board.getPlayerList();
//        List<Integer> planetDrawables = getPlanetDrawables();
//        Map<AbstractPlayer, Integer> playerToOutlineMap = new HashMap<AbstractPlayer, Integer>();
//        for (int i = 0; i < players.size(); i++){
//            playerToOutlineMap.put(players.get(i), planetDrawables.get(i));
//        }
//        return playerToOutlineMap;
//    }



}
