package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.Region;

public class BoostUnitActivity extends AppCompatActivity {
    List<Region> regions;
    String planetName;
    TextView name;
    TextView owner;
    TextView numUnits;
    TextView helpText;
    TextView orderHelper;
    String orderMessage;
    Board board;
    AbstractPlayer player;
    PlanetDrawable planetDrawable;
    Map<Region, ImageView> regionImageViewMap;
    Map<Region, ImageButton> regionImageButtonMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost_region);
        board = ParentActivity.getBoard();
        player = ParentActivity.getPlayer();
        Log.d("Player Name", player.getName());
        regions = board.getRegions();
        List<ImageButton> planetButtons = getPlanetButtons();
        List<TextView> planetPlayers = getPlanetPlayers();
        List<TextView> unitCircles = getUnitCircles();
        List<ImageView> planetSquares = getPlayerColors();
        List<ImageView> planetViews = getPlanetViews();
        planetDrawable = new PlanetDrawable(board, planetButtons, planetSquares, planetPlayers, unitCircles, planetViews);
        name = findViewById(R.id.displayPame);
        owner = findViewById(R.id.displayOwner);
        numUnits = findViewById(R.id.displayUnits);
        helpText = findViewById(R.id.attackHelp);
        orderHelper = findViewById(R.id.orderHelper);
        Intent i = getIntent();
        orderMessage =  i.getStringExtra("ORDER");
        String h = "Select planet to " + orderMessage;
        orderHelper.setText(h);
        plagueDraw();
    }


    public void boostPlanet(View view){
        if (planetName == null){
            helpText.setText("Please select a planet");
        } else {
            Intent i = new Intent(this, UnitBoostActivity.class);
            i.putExtra("PNAME", planetName);
            i.putExtra("ORDER",orderMessage);
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        regionImageViewMap = planetDrawable.getRegionToPlanetViewMap();
        regionImageButtonMap = planetDrawable.getRegionToButtonMap();
        planetDrawable.setGreyOutlines();
        planetDrawable.setAllUnitCircles();
        for (AbstractPlayer p : board.getPlayerList()) {
            if (p != player) { //if not player's planet, set view to outline
                if (p!=null){ //if owned by someone, set to their outline color and make button invisible
                    for (Region r : board.getPlayerRegionSet(p)) {
                        regionImageViewMap.get(r).setBackgroundResource(planetDrawable.getPlayerToOutlineMap().get(p));
                        planetDrawable.setImageButtonsInvisible(p);
                    }
                }
                else{ //if player is null, set button invisible and set grey outline
                    planetDrawable.setGreyOutlines();
                    planetDrawable.setImageButtonsInvisible(p);
                }
            } else { //if player own's planet, set up visible planet
                for (Region r : board.getPlayerRegionSet(p)) {
                    planetDrawable.setPlanets();
                    regionImageViewMap.get(r).setBackgroundResource(planetDrawable.getRegionToPlanetDrawableMap().get(r));
                }
            }
        }
    }

    // plague
    public void plagueDraw(){
        int increment = 0;
        Resources r = getResources();
        Drawable[] layers = new Drawable[2];
        for (Region region: regions){
            if (region.getPlague()){
                layers[0] = getPlanetDrawable().get(increment);
                layers[1] = r.getDrawable(R.drawable.skulltransparent);
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                ImageView imageView = getPlanetViews().get(increment);
                TextView textView = getUnitCircles().get(increment);
                textView.setVisibility(View.INVISIBLE);
                imageView.setImageDrawable(layerDrawable);
                break;
            }
            increment++;
        }
    }
    public List<Drawable> getPlanetDrawable(){
        List<Drawable> drawables = new ArrayList<Drawable>();
        Resources r = getResources();
        drawables.add(r.getDrawable(R.drawable.p1nb));
        drawables.add(r.getDrawable(R.drawable.p2nb));
        drawables.add(r.getDrawable(R.drawable.p3nb));
        drawables.add(r.getDrawable(R.drawable.p4nb));
        drawables.add(r.getDrawable(R.drawable.p5nb));
        drawables.add(r.getDrawable(R.drawable.p6nb));
        drawables.add(r.getDrawable(R.drawable.p7nb));
        drawables.add(r.getDrawable(R.drawable.p8nb));
        drawables.add(r.getDrawable(R.drawable.p9nb));
        drawables.add(r.getDrawable(R.drawable.p10nb));
        drawables.add(r.getDrawable(R.drawable.p11nb));
        drawables.add(r.getDrawable(R.drawable.p12nb));
        return drawables;
    }



    public void setSelectionInvisible(Region r) {
        planetDrawable.setImageViewVisible(null);
        regionImageViewMap.get(r).setVisibility(View.INVISIBLE);
    }


    public void setPlanetInfo(Region r){
        name.setText(r.getName());
        if (r.getOwner()!=null) {
            String o = "Owner: " + r.getOwner().getName();
            owner.setText(o);
            String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
            numUnits.setText(u);
            this.planetName = r.getName();
        }
        else{
            String o = "Owner: none";
            owner.setText(o);
            String u = "Units: none";
            numUnits.setText(u);
            this.planetName = r.getName();
        }
    }

    public void planetZero(View view){
        Region r = regions.get(0);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetOne(View view){
        Region r = regions.get(1);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetTwo(View view){
        Region r = regions.get(2);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetThree(View view){
        Region r = regions.get(3);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetFour(View view){
        Region r = regions.get(4);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetFive(View view){
        Region r = regions.get(5);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetSix(View view){
        Region r = regions.get(6);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetSeven(View view){
        Region r = regions.get(7);
        setPlanetInfo(r);
        setSelectionInvisible(r);

    }
    public void planetEight(View view){
        Region r = regions.get(8);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetNine(View view){
        Region r = regions.get(9);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetTen(View view){
        Region r = regions.get(10);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }
    public void planetEleven(View view){
        Region r = regions.get(11);
        setPlanetInfo(r);
        setSelectionInvisible(r);
    }


    public List<ImageView> getPlanetViews(){
        List<ImageView> views = new ArrayList<ImageView>();
        ImageView p0I = findViewById(R.id.p0I);
        ImageView p1I = findViewById(R.id.p1I);
        ImageView p2I = findViewById(R.id.p2I);
        ImageView p3I = findViewById(R.id.p3I);
        ImageView p4I = findViewById(R.id.p4I);
        ImageView p5I = findViewById(R.id.p5I);
        ImageView p6I = findViewById(R.id.p6I);
        ImageView p7I = findViewById(R.id.p7I);
        ImageView p8I = findViewById(R.id.p8I);
        ImageView p9I = findViewById(R.id.p9I);
        ImageView p10I = findViewById(R.id.p10I);
        ImageView p11I = findViewById(R.id.p11I);
        views.add(p0I);
        views.add(p1I);
        views.add(p2I);
        views.add(p3I);
        views.add(p4I);
        views.add(p5I);
        views.add(p6I);
        views.add(p7I);
        views.add(p8I);
        views.add(p9I);
        views.add(p10I);
        views.add(p11I);
        return views;
    }

    public List<TextView> getUnitCircles() {
        List<TextView> unitCircles = new ArrayList<TextView>();
        TextView unit0 = findViewById(R.id.p0units);
        TextView unit1 = findViewById(R.id.p1units);
        TextView unit2 = findViewById(R.id.p2units);
        TextView unit3 = findViewById(R.id.p3units);
        TextView unit4 = findViewById(R.id.p4units);
        TextView unit5 = findViewById(R.id.p5units);
        TextView unit6 = findViewById(R.id.p6units);
        TextView unit7 = findViewById(R.id.p7units);
        TextView unit8 = findViewById(R.id.p8units);
        TextView unit9 = findViewById(R.id.p9units);
        TextView unit10 = findViewById(R.id.p10units);
        TextView unit11 = findViewById(R.id.p11units);
        unitCircles.add(unit0);
        unitCircles.add(unit1);
        unitCircles.add(unit2);
        unitCircles.add(unit3);
        unitCircles.add(unit4);
        unitCircles.add(unit5);
        unitCircles.add(unit6);
        unitCircles.add(unit7);
        unitCircles.add(unit8);
        unitCircles.add(unit9);
        unitCircles.add(unit10);
        unitCircles.add(unit11);
        return unitCircles;
    }

    public List<TextView> getPlanetPlayers() {
        List<TextView> planetPlayers = new ArrayList<TextView>();
        TextView player0 = findViewById(R.id.player0);
        TextView player1 = findViewById(R.id.player1);
        TextView player2 = findViewById(R.id.player2);
        TextView player3 = findViewById(R.id.player3);
        TextView player4 = findViewById(R.id.player4);
        planetPlayers.add(player0);
        planetPlayers.add(player1);
        planetPlayers.add(player2);
        planetPlayers.add(player3);
        planetPlayers.add(player4);
        return planetPlayers;
    }

    public List<ImageView> getPlayerColors() {
        List<ImageView> planetSquares = new ArrayList<ImageView>();
        ImageView square0 = findViewById(R.id.square0);
        ImageView square1 = findViewById(R.id.square1);
        ImageView  square2 = findViewById(R.id.square2);
        ImageView square3 = findViewById(R.id.square3);
        ImageView square4 = findViewById(R.id.square4);
        planetSquares.add(square0);
        planetSquares.add(square1);
        planetSquares.add(square2);
        planetSquares.add(square3);
        planetSquares.add(square4);
        return planetSquares;
    }
    public List<ImageButton> getPlanetButtons(){
        List<ImageButton > planetButtons = new ArrayList<ImageButton>();
        ImageButton planet0 = findViewById(R.id.p0);
        ImageButton planet1 = findViewById(R.id.p1);
        ImageButton planet2 = findViewById(R.id.p2);
        ImageButton planet3 = findViewById(R.id.p3);
        ImageButton planet4 = findViewById(R.id.p4);
        ImageButton planet5 = findViewById(R.id.p5);
        ImageButton planet6 = findViewById(R.id.p6);
        ImageButton planet7 = findViewById(R.id.p7);
        ImageButton planet8 = findViewById(R.id.p8);
        ImageButton  planet9 = findViewById(R.id.p9);
        ImageButton  planet10 = findViewById(R.id.p10);
        ImageButton  planet11 = findViewById(R.id.p11);
        planetButtons.add(planet0);
        planetButtons.add(planet1);
        planetButtons.add(planet2);
        planetButtons.add(planet3);
        planetButtons.add(planet4);
        planetButtons.add(planet5);
        planetButtons.add(planet6);
        planetButtons.add(planet7);
        planetButtons.add(planet8);
        planetButtons.add(planet9);
        planetButtons.add(planet10);
        planetButtons.add(planet11);
        return planetButtons;
    }

}
