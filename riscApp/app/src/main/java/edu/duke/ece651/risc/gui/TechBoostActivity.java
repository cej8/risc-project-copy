package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.TechBoost;

public class TechBoostActivity extends AppCompatActivity {
        HumanPlayer player;
        TextView yourFuel;
        TextView fuelCost;
        Integer yourLevel;
        ImageView level1;
        ImageView level2;
        ImageView lock2;
        ImageView level3;
        ImageView lock3;
        ImageView level4;
        ImageView lock4;
        ImageView level5;
        ImageView lock5;
        ImageView level6;
        ImageView lock6;
        Button submitButton;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tech_boost);
            //player = ParentActivity.getPlayer();
            player = new HumanPlayer("bob");
            yourFuel = findViewById(R.id.insertYourFuel);
            fuelCost = findViewById(R.id.insertFuelCost);
            submitButton = findViewById(R.id.yesBoost);

            level1 = findViewById(R.id.level1b);
            level2 = findViewById(R.id.level2b);
            lock2 = findViewById(R.id.lock2);
            level3 = findViewById(R.id.level3b);
            lock3 = findViewById(R.id.lock3);
            level4 = findViewById(R.id.level4b);
            lock4 = findViewById(R.id.lock4);
            level5 = findViewById(R.id.level5b);
            lock5 = findViewById(R.id.lock5);
            level6 = findViewById(R.id.level6b);
            lock6 = findViewById(R.id.lock6);
            unlockLevel();
            setFields();
            Intent intent = getIntent();
        }
        public void setFields(){
            String fuel = Integer.toString(player.getResources().getFuelResource().getFuel());
            yourFuel.setText(fuel);
            String cost = Integer.toString(player.getMaxTechLevel().getCostToUpgrade());
            fuelCost.setText(cost);
            //String level = Integer.toString(player.getMaxTechLevel().getMaxTechLevel());
        }

        public void hitButton(View view) {
            TechBoost techBoost = new TechBoost(player);
            ParentActivity pa = new ParentActivity();
            pa.setOrders(techBoost);
            Intent techBoostSetup = new Intent(this, DisplayMapActivity.class);
            startActivity(techBoostSetup);
        }

        public void unlockLevel(){
            level1.setVisibility(View.VISIBLE);
            lock2.setVisibility(View.INVISIBLE);
            level2.setVisibility(View.VISIBLE);
            level3.setVisibility(View.INVISIBLE);
            lock3.setVisibility(View.VISIBLE);
            level4.setVisibility(View.INVISIBLE);
            lock4.setVisibility(View.VISIBLE);
            level5.setVisibility(View.INVISIBLE);
            lock5.setVisibility(View.VISIBLE);
            level6.setVisibility(View.INVISIBLE);
            lock6.setVisibility(View.VISIBLE);
            switch(player.getMaxTechLevel().getMaxTechLevel()) {
                case 2:
                    lock2.setVisibility(View.GONE);
                    level2.setVisibility(View.VISIBLE);
                case 3:
                    lock3.setVisibility(View.GONE);
                    level3.setVisibility(View.VISIBLE);
                case 4:
                    lock4.setVisibility(View.GONE);
                    level4.setVisibility(View.VISIBLE);
                case 5:
                    lock5.setVisibility(View.GONE);
                    level5.setVisibility(View.VISIBLE);
                case 6:
                    lock6.setVisibility(View.GONE);
                    level6.setVisibility(View.VISIBLE);
                default:
                    break;
            }
        }
    }


