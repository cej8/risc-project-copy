package edu.duke.ece651.risc.gui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.AttackCombat;
import edu.duke.ece651.risc.shared.AttackMove;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.BoardGenerator;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.MoveOrder;
import edu.duke.ece651.risc.shared.OrderInterface;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.TechBoost;
import edu.duke.ece651.risc.shared.TeleportOrder;
import edu.duke.ece651.risc.shared.Unit;
import edu.duke.ece651.risc.shared.UnitBoost;

public class DisplayMapActivity extends AppCompatActivity {
    List<Region> regions;
    ExecuteClient executeClient;
    TextView helpText;
    List<OrderInterface> orders;
    Board board;
    ImageButton planet0;
    ImageButton planet1;
    ImageButton planet2;
    ImageButton planet3;
    ImageButton planet4;
    ImageButton planet5;
    ImageButton planet6;
    ImageButton planet7;
    ImageButton planet8;
    ImageButton planet9;
    ImageButton planet10;
    ImageButton planet11;
    ImageView square0;
    ImageView square1;
    ImageView square2;
    ImageView square3;
    ImageView square4;
    TextView player0;
    TextView player1;
    TextView player2;
    TextView player3;
    TextView player4;
    ParentActivity parentActivity = new ParentActivity();

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
        generateBoard();
        board = ParentActivity.getBoard();
        regions = board.getRegions();
        Log.d("Inside map regions",regions.get(0).getName());
        getOrders();
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<ImageButton> planetButtons = getPlanetButtons();
        List<TextView> planetPlayers = getPlanetPlayers();
        List<ImageView> planetSquares = getPlanetSquares();
        PlanetDrawable pd = new PlanetDrawable(board, planetButtons, planetSquares, planetPlayers);
        pd.setPlanets();
    }

    public List<TextView> getPlanetPlayers() {
        List<TextView> planetPlayers = new ArrayList<TextView>();
        player0 = findViewById(R.id.player0);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        player3 = findViewById(R.id.player3);
        player4 = findViewById(R.id.player4);
        planetPlayers.add(player0);
        planetPlayers.add(player1);
        planetPlayers.add(player2);
        planetPlayers.add(player3);
        planetPlayers.add(player4);
        return planetPlayers;
    }

    public List<ImageView> getPlanetSquares() {
        List<ImageView> planetSquares = new ArrayList<ImageView>();
        square0 = findViewById(R.id.square0);
        square1 = findViewById(R.id.square1);
        square2 = findViewById(R.id.square2);
        square3 = findViewById(R.id.square3);
        square4 = findViewById(R.id.square4);
        planetSquares.add(square0);
        planetSquares.add(square1);
        planetSquares.add(square2);
        planetSquares.add(square3);
        planetSquares.add(square4);
        return planetSquares;
    }
    public List<ImageButton> getPlanetButtons(){
        List<ImageButton> planetButtons = new ArrayList<ImageButton>();
        planet0 = findViewById(R.id.p0);
        planet1 = findViewById(R.id.p1);
        planet2 = findViewById(R.id.p2);
        planet3 = findViewById(R.id.p3);
        planet4 = findViewById(R.id.p4);
        planet5 = findViewById(R.id.p5);
        planet6 = findViewById(R.id.p6);
        planet7 = findViewById(R.id.p7);
        planet8 = findViewById(R.id.p8);
        planet9 = findViewById(R.id.p9);
        planet10 = findViewById(R.id.p10);
        planet11 = findViewById(R.id.p11);
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

    public void getOrders(){
        Intent i = getIntent();
        String order = i.getStringExtra("ORDER");
        String attackFrom = i.getStringExtra("ATTACKFROM");
        String attackTo = i.getStringExtra("ATTACKTO");
        ArrayList<Integer> unitList = i.getIntegerArrayListExtra("UNITS");
        Region source = getRegionByName(board,attackFrom);
        Region destination = getRegionByName(board,attackTo);
        Unit unit = new Unit(unitList);
        ParentActivity parentActivity = new ParentActivity();
        HumanPlayer player = parentActivity.getPlayer();
        Log.d("Board test",regions.get(0).getName());
        if (order == null){
            // do nothing
        } else {
            if (order.equals("move")) {
                MoveOrder moveOrder = new MoveOrder(source, destination, unit);
                parentActivity.setOrders(moveOrder);
            } else if (order.equals("attack")) {
                AttackMove attackMove = new AttackMove(source, destination, unit);
                parentActivity.setOrders(attackMove);
                AttackCombat attackCombat = new AttackCombat(source, destination, unit);
                parentActivity.setOrders(attackCombat);
            } else if (order.equals("boost units")) {
                UnitBoost unitBoost = new UnitBoost(source,unit);
                parentActivity.setOrders(unitBoost);
            }
            else if (order.equals("techBoost")) {
                //i put my method in the techboost activity itself
            } else if (order.equals("teleport")){
                TeleportOrder teleportOrder = new TeleportOrder(source,destination,unit);
                parentActivity.setOrders(teleportOrder);
            }
        }
        if (order != null) {
            List<OrderInterface> ordersToDate = ParentActivity.getOrders();
            for (int j = 0; j < ordersToDate.size(); j++) {
                Log.d("Order List", ordersToDate.get(j).doAction());
            }
        }
    }
    // SUBMIT ORDERS!!!!!!!!!!!!!
    public void submitAll(View view){
        // TODO: execute client when done
        orders = ParentActivity.getOrders();
        executeClient.playGame(handler,helpText,orders);
    }
    public void teleportOrder(View view){
        Intent teleport = new Intent(this,OrderActivity.class);
        teleport.putExtra("ORDER","teleport");
        startActivity(teleport);
    }
    public void moveOrder(View view){
        Intent attackSetup = new Intent(this,OrderActivity.class);
        attackSetup.putExtra("ORDER","move");
        startActivity(attackSetup);
    }
    public void attackOrder(View view){
        Intent attackSetup = new Intent(this,OrderActivity.class);
        attackSetup.putExtra("ORDER","attack");
        startActivity(attackSetup);
    }
    public void upgradeOrder(View view){
        Intent unitBoost = new Intent(this,BoostRegionActivity.class);
        unitBoost.putExtra("ORDER","boost units");
        startActivity(unitBoost);
    }

    public void techBoostOrder(View view){
        Intent techBoostSetup = new Intent(this,TechBoostActivity.class);
        techBoostSetup.putExtra("ORDER","techBoost");
        startActivity(techBoostSetup);
    }
    public Region getRegionByName(Board board, String name){
        Map<String, Region> nameToRegionMap = new HashMap<String, Region>();
        for (Region r : board.getRegions()){
            nameToRegionMap.put(r.getName(), r);
        }
        return nameToRegionMap.get(name);
    }

    public void planetTen(View view){
          Region region = regions.get(10);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P10");
    }
    public void planetOne(View view){
         Region region = regions.get(1);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P1");
    }
    public void planetSix(View view){
         Region region = regions.get(6);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P6");
    }
    public void planetEleven(View view){
        Region region = regions.get(11);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P11");
    }
    public void planetZero(View view){
        Region region = regions.get(0);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P0");
    }
    public void planetFive(View view){
        Region region = regions.get(5);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P5");
    }
    public void planetTwo(View view){
        Region region = regions.get(2);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P2");
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
    public void planetThree(View view){
        Region region = regions.get(3);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P3");
    }
    public void planetFour(View view){
        Region region = regions.get(4);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P4");
    }
    public void planetSeven(View view){
        Region region = regions.get(7);
        DisplayRegionInfoDialogFragment dialogFragment = new DisplayRegionInfoDialogFragment(region,region.getName(),region.getUnits().getTotalUnits(),region.getOwner());
        dialogFragment.show(getSupportFragmentManager(), "P7");
    }
    
    // Mock board
    public void generateBoard(){
        List<Region> regions = getRegions(4);
        Board b = new Board(regions);
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.setBoard(b);
        Log.d("Earth",Integer.toString(regions.get(0).getUnits().getTotalUnits()));
    }
    private List<Region> getRegions(int numPlayer) {
        AbstractPlayer p1 = new HumanPlayer("Player 1");
        AbstractPlayer p2 = new HumanPlayer("Bob");
        AbstractPlayer p3 = new HumanPlayer("Player 3");
        AbstractPlayer p4 = new HumanPlayer("Player 4");
        AbstractPlayer p5 = new HumanPlayer("Player 5");
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
                regions = getRegionHelper(p1, p2, p3, p4, p4, regionUnits);
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
        Region r5 = new Region(p3, units.get(5));
        r5.setName("Ego");
        Region r6 = new Region(p3, units.get(5));
        r6.setName("Terra Prime");
        Region r7 = new Region(p4, units.get(5));
        r7.setName("Arda");
        Region r8 = new Region(p4, units.get(5));
        r8.setName("Dune");
        Region r9 = new Region(p4, units.get(5));
        r9.setName("Solaris");
        Region r10 = new Region(p5, units.get(5));
        r10.setName("Gallifrey");
        Region r11 = new Region(null, units.get(5));
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
