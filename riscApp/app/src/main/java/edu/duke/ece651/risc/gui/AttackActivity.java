package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Region;

public class AttackActivity extends AppCompatActivity {
    List<Region> regions;
    String planetName;
    TextView name;
    TextView owner;
    TextView numUnits;
    TextView helpText;
    TextView orderHelper;
    String orderMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);
        Board board = ParentActivity.getBoard();
        regions = board.getRegions();
        name = findViewById(R.id.displayPame);
        owner = findViewById(R.id.displayOwner);
        numUnits = findViewById(R.id.displayUnits);
        helpText = findViewById(R.id.attackHelp);
        orderHelper = findViewById(R.id.orderHelper);
        Intent i = getIntent();
        orderMessage =  i.getStringExtra("ORDER");
        String h = "Select planet to " + orderMessage + " from";
        orderHelper.setText(h);
    }
    public void attackFrom(View view){
        if (planetName == null){
                helpText.setText("Please select a planet");
            } else {
                Intent i = new Intent(this, AttackActivityTwo.class);
            i.putExtra("PNAME", planetName);
            i.putExtra("ORDER",orderMessage);
            startActivity(i);
        }
    }
    public void planetOne(View view){
        Region r = regions.get(0);
        name.setText(r.getName());
        String o = "Owner: " + r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetTwo(View view){
        Region r = regions.get(1);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetThree(View view){
        Region r = regions.get(2);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetFour(View view){
        Region r = regions.get(3);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetFive(View view){
        Region r = regions.get(4);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetSix(View view){
        Region r = regions.get(5);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetSeven(View view){
        Region r = regions.get(6);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetEight(View view){
        Region r = regions.get(7);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetNine(View view){
        Region r = regions.get(8);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetTen(View view){
        Region r = regions.get(9);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetEleven(View view){
        Region r = regions.get(10);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
    public void planetTwelve(View view){
        Region r = regions.get(11);
        name.setText(r.getName());
        String o = "Owner: " +r.getOwner().getName();
        owner.setText(o);
        String u = "Units: " + Integer.toString(r.getUnits().getTotalUnits());
        numUnits.setText(u);
        this.planetName = r.getName();
    }
}
