package edu.duke.ece651.risc.gui;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.AttackCombat;
import edu.duke.ece651.risc.shared.AttackMove;
import edu.duke.ece651.risc.shared.AttackValidator;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.CloakOrder;
import edu.duke.ece651.risc.shared.CloakValidator;
import edu.duke.ece651.risc.shared.DeepCopy;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.MoveOrder;
import edu.duke.ece651.risc.shared.MoveValidator;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.RaidOrder;
import edu.duke.ece651.risc.shared.RaidValidator;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.ResourceBoost;
import edu.duke.ece651.risc.shared.ResourceBoostValidator;
import edu.duke.ece651.risc.shared.Spy;
import edu.duke.ece651.risc.shared.SpyMoveOrder;
import edu.duke.ece651.risc.shared.SpyMoveValidator;
import edu.duke.ece651.risc.shared.SpyUpgradeOrder;
import edu.duke.ece651.risc.shared.SpyUpgradeValidator;
import edu.duke.ece651.risc.shared.TeleportOrder;
import edu.duke.ece651.risc.shared.TeleportValidator;
import edu.duke.ece651.risc.shared.Unit;
import edu.duke.ece651.risc.shared.UnitBoost;
import edu.duke.ece651.risc.shared.UnitBoostValidator;
import edu.duke.ece651.risc.shared.ValidatorInterface;

public class DisplayMapActivity extends AppCompatActivity {
    List<Region> regions;
    ExecuteClient executeClient;
    TextView helpText;
    List<OrderInterface> orders;
    Board board;
    private ValidatorInterface validator;
    ParentActivity parentActivity = new ParentActivity();
    Board validationTempBoard;
    AbstractPlayer player;
    AbstractPlayer validationPlayerCopy;
    Activity activity;

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        executeClient = new ExecuteClient(this);
        helpText = findViewById(R.id.helpText);
        executeClient.setConnection(ParentActivity.getConnection());
      //  executeClient.displayServerBoard(helpText);
        // temp for testing
        // TODO: remove generateBoard for whole test
        //generateBoard();
        player = ParentActivity.getPlayer();
        board = ParentActivity.getBoard();
        List<String> playerNames = board.getPlayerStringList();
        //board.initializeSpies(playerNames);
        regions = board.getRegions();
        validationTempBoard= (Board) DeepCopy.deepCopy(this.board);
        validationPlayerCopy=(AbstractPlayer)DeepCopy.deepCopy(ParentActivity.getPlayer());
        activity = this;

        Log.d("Inside map regions",regions.get(0).getName());

        getOrders();
        plagueDraw();
    }
    public List<String>getPlayerNames(List<AbstractPlayer> p){
        List<String>list= new ArrayList<String>();
        for(AbstractPlayer player: p){
            list.add(player.getName());
        }
        return list;
    }
    // what to do when back button pressed
    @Override
    public void onBackPressed()
    {
        // instead of going to new activity open up dialog fragment
        BackButtonDialogFragment backButtonDialogFragment = new BackButtonDialogFragment(this);
        backButtonDialogFragment.show(getSupportFragmentManager(),"back");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        player.printSeenRegions();

        List<ImageButton> planetButtons = getPlanetButtons();
        List<TextView> planetPlayers = getPlanetPlayers();
        List<TextView> unitCircles = getUnitCircles();
        List<ImageView> planetSquares = getPlanetSquares();
        List<ImageView> planetViews = getPlanetViews();
        PlanetDrawable pd = new PlanetDrawable(board, planetButtons, planetSquares, planetPlayers, unitCircles, planetViews);
        Set<Region> regionSet = board.getSetVisibleRegions(player);
        pd.setGreyPlanets();
        pd.setPlanetsNoUnits();
        pd.setInvisibleRegionsInvisible(player);
        setPlayerInfo();

    }
    public void showSpies(){
        int increment = 0;
        Resources r = getResources();
        Drawable[] layers = new Drawable[2];

        for (Region region : regions) {
            if (region.getSpies(ParentActivity.getPlayer().getName()).size() > 0) {//if player has a spy on the region
                layers[0] = getPlanetDrawable().get(increment);
                layers[1] = r.getDrawable(R.drawable.spytransparent);
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                ImageView imageView = getPlanetViews().get(increment);
                TextView textView = getUnitCircles().get(increment);
                textView.setVisibility(View.INVISIBLE);
                imageView.setImageDrawable(layerDrawable);

            }
            increment++;
        }
    }


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
    public List<Drawable> getLevelDrawable(){
        List<Drawable> drawables = new ArrayList<Drawable>();
        Resources r = getResources();
        drawables.add(r.getDrawable(R.drawable.level0b));
        drawables.add(r.getDrawable(R.drawable.level1b));
        drawables.add(r.getDrawable(R.drawable.level2b));
        drawables.add(r.getDrawable(R.drawable.level3b));
        drawables.add(r.getDrawable(R.drawable.level4b));
        drawables.add(r.getDrawable(R.drawable.level5b));
        drawables.add(r.getDrawable(R.drawable.level6b));
        return drawables;
    }

    public void setPlayerInfo(){
	TextView fuelAmount = findViewById(R.id.fuelAmount);
	TextView techAmount = findViewById(R.id.techAmount);
	TextView techLevel= findViewById(R.id.techLevel);
	String fuel= "Fuel : "+ Integer.toString(ParentActivity.getPlayer().getResources().getFuelResource().getFuel());
	fuelAmount.setText(fuel);
	String tech="Tech : "+ Integer.toString(ParentActivity.getPlayer().getResources().getTechResource().getTech());
	techAmount.setText(tech);
	String level= "Level: "+  Integer.toString(ParentActivity.getPlayer().getMaxTechLevel().getMaxTechLevel());
	techLevel.setText(level);
    }

    public void getOrders(){
        Intent i = getIntent();
        String order = i.getStringExtra("ORDER");
        String attackFrom = i.getStringExtra("ATTACKFROM");
        String attackTo = i.getStringExtra("ATTACKTO");
        ArrayList<Integer> unitList = i.getIntegerArrayListExtra("UNITS");
        Region source = board.getRegionByName(attackFrom);
        Region destination = board.getRegionByName(attackTo);
        Unit unit = new Unit(unitList);
        ParentActivity parentActivity = new ParentActivity();
         Log.d("Board test",regions.get(0).getName());
        String invalidFlag = null;
        if (order == null){
            // do nothing
        } else {
            if (order.equals("move")) {
                MoveOrder moveOrder = new MoveOrder(source, destination, unit);
                List<MoveOrder>m= new ArrayList<MoveOrder>();
                m.add(moveOrder);
                  validator= new MoveValidator(validationPlayerCopy,validationTempBoard);
                 if(validator.validateOrders(m)) {//if order is valid, add to list to be sent
                   parentActivity.setOrders(moveOrder);
                    moveOrder.doAction();
                 }
                 else{
                //invalid move, set reprompt flag or string
                   invalidFlag= "move";
                }

            } else if (order.equals("attack")) {
                AttackMove attackMove = new AttackMove(source, destination, unit);
                List<AttackMove>a= new ArrayList<AttackMove>();
                a.add(attackMove);
                validator= new AttackValidator(validationPlayerCopy,validationTempBoard);
                if(validator.validateOrders(a)) {//if order is valid, add to list to be sent
                    parentActivity.setOrders(attackMove);
                    attackMove.doAction();
                    AttackCombat attackCombat = new AttackCombat(source, destination, unit);
                    parentActivity.setOrders(attackCombat);
                }
                else{
                  invalidFlag= "attack";
                }
            } else if (order.equals("raid")){
                RaidOrder raidOrder = new RaidOrder(source,destination);
                List<RaidOrder> r = new ArrayList<RaidOrder>();
                r.add(raidOrder);
                validator = new RaidValidator(validationPlayerCopy,validationTempBoard);
                if (validator.validateOrders(r)){
                    parentActivity.setOrders(raidOrder);
                } else {
                    invalidFlag = "raid";
                }
            }else if (order.equals("boost units")) {
                UnitBoost unitBoost = new UnitBoost(source,unit);
                List<UnitBoost>u= new ArrayList<UnitBoost>();
                u.add(unitBoost);
                validator= new UnitBoostValidator(validationPlayerCopy,validationTempBoard);
                if(validator.validateOrders(u)) {//if order is valid, add to list to be sent
                    parentActivity.setOrders(unitBoost);
                    unitBoost.doAction();
                }
                else{
                    invalidFlag="upgrade unit";
                }
            } else if (order.equals("resource boost")) {
               ResourceBoost resourceBoost= new ResourceBoost(destination);
                List<ResourceBoost>r= new ArrayList<ResourceBoost>();
                r.add(resourceBoost);
                validator= new ResourceBoostValidator(validationPlayerCopy,validationTempBoard);
                if(validator.validateOrders(r)) {//if order is valid, add to list to be sent
                    parentActivity.setOrders(resourceBoost);
                    resourceBoost.doAction();
                }
                else{
                    invalidFlag="upgrade region";
                }
            }
            else if (order.equals("techBoost")) {
                //i put my method in the techboost activity itself
            } else if (order.equals("teleport")){
                TeleportOrder teleportOrder = new TeleportOrder(source,destination,unit);
                List<TeleportOrder>t= new ArrayList<TeleportOrder>();
                t.add(teleportOrder);
                validator= new TeleportValidator(validationPlayerCopy,validationTempBoard);
                if(validator.validateOrders(t)) {//if order is valid, add to list to be sent
                    parentActivity.setOrders(teleportOrder);
                    teleportOrder.doAction();
                }
                else{
                    invalidFlag="teleport";
                }
            }
            else if (order.equals("spy upgrade")){
                SpyUpgradeOrder spyOrder = new SpyUpgradeOrder(destination);
                List<SpyUpgradeOrder>s= new ArrayList<SpyUpgradeOrder>();
                s.add(spyOrder);
                validator= new SpyUpgradeValidator(validationPlayerCopy,validationTempBoard);
                if(validator.validateOrders(s)) {//if order is valid, add to list to be sent
                    parentActivity.setOrders(spyOrder);
                    spyOrder.doAction();
                }
                else{
                    invalidFlag="spy upgrade";
                }
            }
            else if (order.equals("spy move")){
                SpyMoveOrder spyMoveOrder = new SpyMoveOrder(source, destination, player);
                List<SpyMoveOrder>s= new ArrayList<SpyMoveOrder>();
                s.add(spyMoveOrder);
                validator= new SpyMoveValidator(validationPlayerCopy,validationTempBoard);
                if(validator.validateOrders(s)) {//if order is valid, add to list to be sent
                    parentActivity.setOrders(spyMoveOrder);
                    spyMoveOrder.doAction();
                }
                else{
                    invalidFlag="spy move";
                }
            }
            else if (order.equals("cloak")){
                CloakOrder cloakOrder = new CloakOrder(destination);
                List<CloakOrder> orders= new ArrayList<CloakOrder>();
                orders.add(cloakOrder);
                validator= new CloakValidator(validationPlayerCopy,validationTempBoard);
                if(validator.validateOrders(orders)) {//if order is valid, add to list to be sent
                    parentActivity.setOrders(cloakOrder);
                    cloakOrder.doAction();
                }
                else{
                    invalidFlag="cloak";
                }
            }
        }
        if(invalidFlag!=null) {
            //set reprompt for order with in it
            helpText.setText("Your " + invalidFlag+" order was invalid. Please try again or issue another order");
        }
        else {
            helpText.setText("Issue an order or click submit when done");

        }
    }
   public void popupMenu(final View view){
       final PopupMenu popupMenu = new PopupMenu(this, view);
       popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
       popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
           public boolean onMenuItemClick(MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.exit:
                       ExitGameDialogFragment exitFrag = new ExitGameDialogFragment(activity,executeClient);
                       exitFrag.show(getSupportFragmentManager(),"exit");
                       return true;
                   case R.id.viewSpies:
                       showSpies();
                       return true;
                   case R.id.backpack:
                       BackpackDialogFragment backpackFrag = new BackpackDialogFragment(getLevelDrawable());
                       backpackFrag.show(getSupportFragmentManager(),"backpack");
                       return true;
                   case R.id.instructions:
                       // TODO: instructions
                       return true;
                   case R.id.move:
                       LayoutInflater inflater = getLayoutInflater();
                       View helpView = inflater.inflate(R.layout.help_move, null);
                       HelpDialogFragment helpDialogFragment = new HelpDialogFragment("Move",helpView);
                       helpDialogFragment.show(getSupportFragmentManager(),"move");
                       return true;
                   case R.id.teleport:
                       LayoutInflater inflater4 = getLayoutInflater();
                       View helpView4 = inflater4.inflate(R.layout.help_teleport, null);
                       HelpDialogFragment helpDialogFragment4 = new HelpDialogFragment("Teleport",helpView4);
                       helpDialogFragment4.show(getSupportFragmentManager(),"teleport");
                       return true;
                   case R.id.attack:
                       LayoutInflater inflater2 = getLayoutInflater();
                       View helpView2 = inflater2.inflate(R.layout.help_attack, null);
                       HelpDialogFragment helpDialogFragment2 = new HelpDialogFragment("Attack",helpView2);
                       helpDialogFragment2.show(getSupportFragmentManager(),"attack");
                       return true;
                   case R.id.raid:
                       LayoutInflater inflater3 = getLayoutInflater();
                       View helpView3 = inflater3.inflate(R.layout.help_raid, null);
                       HelpDialogFragment helpDialogFragment3 = new HelpDialogFragment("Raid",helpView3);
                       helpDialogFragment3.show(getSupportFragmentManager(),"raid");
                       return true;
                   case R.id.boost:
                       LayoutInflater inflater5 = getLayoutInflater();
                       View helpView5 = inflater5.inflate(R.layout.help_boost, null);
                       HelpDialogFragment helpDialogFragment5 = new HelpDialogFragment("Boost",helpView5);
                       helpDialogFragment5.show(getSupportFragmentManager(),"boost");
                       return true;
                   default:
                       return false;
               }
           }
       });
       popupMenu.show();
    }
    // SUBMIT ORDERS!!!!!!!!!!!!!
    public void submitAll(View view){
        orders = ParentActivity.getOrders();
        executeClient.playGame(handler,helpText,orders);
    }
    public void travelOrder(View view){
        // open travel popup
        MoveDialogFragment moveFragment = new MoveDialogFragment(this);
        moveFragment.show(getSupportFragmentManager(),"travel");
    }
    public void offenseOrder(View view){
        // open offense popup
        OffenseDialogFragment offenseFragment = new OffenseDialogFragment(this);
        offenseFragment.show(getSupportFragmentManager(),"offense");
    }
    public void boostOrder(View view){
        // boost popup
        BoostDialogFragment boostFragment = new BoostDialogFragment(this);
        boostFragment.show(getSupportFragmentManager(),"boost");
    }

    public void fowOrder(View view){
        // FOW order popup
        FOWDialogFragment fowFragment = new FOWDialogFragment(this);
        fowFragment.show(getSupportFragmentManager(),"fow");
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

    public List<ImageView> getPlanetSquares() {
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
    public String planetOwner(Region region){
        String owner;
        if (region.getOwner() == null){
            owner = "Unattached";
        } else {
            owner = region.getOwner().getName();
        }
        return owner;
    }
    public void planetTen(View view){
        Region region = regions.get(10);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P10");
    }
    public void planetOne(View view){
         Region region = regions.get(1);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P1");
    }
    public void planetSix(View view){
         Region region = regions.get(6);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P6");
    }
    public void planetEleven(View view){
        Region region = regions.get(11);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P11");
    }
    public void planetZero(View view){
        Region region = regions.get(0);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P0");
    }
    public void planetFive(View view){
        Region region = regions.get(5);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P5");
    }
    public void planetTwo(View view){
        Region region = regions.get(2);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P2");
    }
    public void planetEight(View view){
        Region region = regions.get(8);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P8");
    }
    public void planetNine(View view){
        Region region = regions.get(9);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P9");
    }
    public void planetThree(View view){
        Region region = regions.get(3);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P3");
    }
    public void planetFour(View view){
        Region region = regions.get(4);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P4");
    }
    public void planetSeven(View view){
        Region region = regions.get(7);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),planetOwner(region));
        dialogFragment.show(getSupportFragmentManager(), "P7");
    }

    // Mock board
    public void generateBoard(){
        HumanPlayer p1 = new HumanPlayer("Player 1");
        List<Region> regions = getRegions(p1, 4);
        Board b = new Board(regions);
        b.initializeSpies(getPlayerNames(b.getPlayerList()));
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setBoard(b);
        parentActivity.setPlayer(p1);
        Log.d("Earth",Integer.toString(regions.get(0).getUnits().getTotalUnits()));
    }
    private List<Region> getRegions(HumanPlayer p1, int numPlayer) {
        HumanPlayer p2 = new HumanPlayer("Bob");
        HumanPlayer p3 = new HumanPlayer("Player 3");
        HumanPlayer p4 = new HumanPlayer("Player 4");
        HumanPlayer p5 = new HumanPlayer("Player 5");
        List<Region> regions = null;
        List<Unit> regionUnits = get6UnitList(5, 10, 15, 20, 25, 30);
        switch (numPlayer) {
            case 1:
                regions = getRegionHelper(p1, p1, p1, p1, p1, regionUnits);
                break;
            case 2:
                regions = getRegionHelper(p1, p1, p2, p2, p2, regionUnits);
                break;
            case 3:
                regions = getRegionHelper(p1, p2, p2, p3, p3, regionUnits);
                break;
            case 4:
                regions = getRegionHelper(p4, p2, p3, p1, p1, regionUnits);
                break;
            case 5:
                regions = getRegionHelper(p1, p2, p3, p4, p5, regionUnits);
                break;
            default:
                break;
        }
        return regions;
    }
    private List<Unit> get6UnitList(int u0, int u1, int u2, int u3, int u4, int u5) {
        List<Unit> units = new ArrayList<Unit>();
        List<Integer> un0 = listOfUnitInts(u0, u0, u0, u0, u0, u0, u0);
        List<Integer> un1 = listOfUnitInts(u1, u1, u1, u1, u1, u1, u1);
        List<Integer> un2 = listOfUnitInts(u2, u2, u2, u2, u2, u2, u2);
        List<Integer> un3 = listOfUnitInts(u3, u3, u3, u3, u3, u3, u3);
        List<Integer> un4 = listOfUnitInts(u4, u4, u4, u4, u4, u4, u4);
        List<Integer> un5 = listOfUnitInts(u5, u5, u5, u5, u5, u5, u5);
        Unit unit0 = new Unit(un0);
        units.add(unit0);
        Unit unit1 = new Unit(un1);
        units.add(unit1);
        Unit unit2 = new Unit(un2);
        units.add(unit2);
        Unit unit3 = new Unit(un3);
        units.add(unit3);
        Unit unit4 = new Unit(un4);
        units.add(unit4);
        Unit unit5 = new Unit(un5);
        units.add(unit5);
        return units;
    }

    private List<Integer> listOfUnitInts(int u0, int u1, int u2, int u3, int u4, int u5, int u6) {
        List<Integer> unit = new ArrayList<Integer>();
        unit.add(u0);
        unit.add(u1);
        unit.add(u2);
        unit.add(u3);
        unit.add(u4);
        unit.add(u5);
        unit.add(u6);
        return unit;
    }

    private List<Region> getRegionHelper(AbstractPlayer p1, AbstractPlayer p2, AbstractPlayer p3, AbstractPlayer p4, AbstractPlayer p5, List<Unit> units) {
    p4.setMaxTechLevel(5);


        Region r0 = new Region(p1, units.get(0));
        r0.setName("Caprica");
        Region r1 = new Region(p1, units.get(1));
        r1.setName("Hoth");
        Region r2 = new Region(p2, units.get(2));
        r2.setName("Worlorn");

        Region r3 = new Region(p2, units.get(3));
        r3.setName("Dagobah");


        Region r4 = new Region(p3, units.get(4));
        r4.setName("Krypton");
        r4.setCloakTurns(3);
        Region r5 = new Region(p3, units.get(5));
        r5.setName("Ego");
        Region r6 = new Region(p3, units.get(0));
        r6.setName("Terra Prime");
        Region r7 = new Region(p4, units.get(1));
        r7.setName("Arda");
        r7.setCloakTurns(3);
        Region r8 = new Region(p4, units.get(2));
        r8.setName("Dune");
        Region r9 = new Region(p4, units.get(3));
        r9.setName("Solaris");
        Region r10 = new Region(p5, units.get(4));
        r10.setName("Gallifrey");
        Region r11 = new Region(new HumanPlayer("Group A"), new Unit(0));
        r11.setName("Cybertron");

        List<Region> adj0 = new ArrayList<Region>();
        adj0.add(r5);
        adj0.add(r1);
        r0.setAdjRegions(adj0);

        List<Region> adj1 = new ArrayList<Region>();
        adj1.add(r0);
        adj1.add(r2);
        adj1.add(r5);
        r1.setAdjRegions(adj1);

        List<Region> adj2 = new ArrayList<Region>();
        adj2.add(r1);
        adj2.add(r3);
        adj2.add(r4);
        r2.setAdjRegions(adj2);

        List<Region> adj3 = new ArrayList<Region>();
        adj3.add(r2);
        adj3.add(r4);
        adj3.add(r8);
        r3.setAdjRegions(adj3);

        List<Region> adj4 = new ArrayList<Region>();
        adj4.add(r3);
        adj4.add(r5);
        adj4.add(r7);
        adj4.add(r2);
        r4.setAdjRegions(adj4);

        List<Region> adj5 = new ArrayList<Region>();
        adj5.add(r4);
        adj5.add(r0);
        adj5.add(r1);
        adj5.add(r6);
        r5.setAdjRegions(adj5);

        List<Region> adj6 = new ArrayList<Region>();
        adj6.add(r5);
        adj6.add(r7);
        adj6.add(r10);
        adj6.add(r11);
        r6.setAdjRegions(adj6);

        List<Region> adj7 = new ArrayList<Region>();
        adj7.add(r4);
        adj7.add(r6);
        adj7.add(r8);
        adj7.add(r9);
        r7.setAdjRegions(adj7);

        List<Region> adj8 = new ArrayList<Region>();
        adj8.add(r7);
        adj8.add(r9);
        adj8.add(r3);
        r8.setAdjRegions(adj8);

        List<Region> adj9 = new ArrayList<Region>();
        adj9.add(r7);
        adj9.add(r8);
        adj9.add(r10);
        r9.setAdjRegions(adj9);

        List<Region> adj10 = new ArrayList<Region>();
        adj10.add(r9);
        adj10.add(r6);
        adj10.add(r11);
        r10.setAdjRegions(adj10);

        List<Region> adj11 = new ArrayList<Region>();
        adj11.add(r6);
        adj11.add(r0);
        adj11.add(r10);
        r11.setAdjRegions(adj11);


        List<Region> regions = new ArrayList<Region>();
        regions.add(r0);
        regions.add(r1);
        regions.add(r2);
        regions.add(r3);
        regions.add(r4);
        regions.add(r5);
        regions.add(r6);
        regions.add(r7);
        regions.add(r8);
        regions.add(r9);
        regions.add(r10);
        regions.add(r11);

        return regions;
    }





}
