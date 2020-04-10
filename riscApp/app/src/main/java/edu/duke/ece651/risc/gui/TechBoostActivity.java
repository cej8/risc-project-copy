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
        Button yesButton;
        Button noButton;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tech_boost);
            player = ParentActivity.getPlayer();
            //player = new HumanPlayer("bob"); //player to pass in for testing
            yourFuel = findViewById(R.id.insertYourFuel);
            fuelCost = findViewById(R.id.insertFuelCost);
            yesButton = findViewById(R.id.yesBoost);
            noButton = findViewById(R.id.noBoost);
            level1 = findViewById(R.id.level1w);
            level2 = findViewById(R.id.level2w);
            lock2 = findViewById(R.id.lock2);
            level3 = findViewById(R.id.level3w);
            lock3 = findViewById(R.id.lock3);
            level4 = findViewById(R.id.level4w);
            lock4 = findViewById(R.id.lock4);
            level5 = findViewById(R.id.level5w);
            lock5 = findViewById(R.id.lock5);
            level6 = findViewById(R.id.level6w);
            lock6 = findViewById(R.id.lock6);
            Intent intent = getIntent();
        }

        @Override
        protected void onStart() {
            super.onStart();
            setFields();
           yourLevel = player.getMaxTechLevel().getMaxTechLevel();
            //yourLevel = 6;// uncomment out to just pass in a value for testing
            unlockLevel();

        }

        public void setFields(){
            String fuel = Integer.toString(player.getResources().getFuelResource().getFuel());
            yourFuel.setText(fuel);
            String cost = Integer.toString(player.getMaxTechLevel().getCostToUpgrade());
            fuelCost.setText(cost);
        }

        //makes order, passing into set orders, go back to display map activity
        public void yesButton(View view) {
            int before = player.getMaxTechLevel().getMaxTechLevel();
            TechBoost techBoost = new TechBoost(player);
            //techBoost.doAction();
            int after = player.getMaxTechLevel().getMaxTechLevel();
            Log.d("no button", "player level went from " + before + " to " + after);
            ParentActivity pa = new ParentActivity();
            pa.setOrders(techBoost);
            Intent techBoostSetup = new Intent(this, DisplayMapActivity.class);
            startActivity(techBoostSetup);
        }

        //doesn't make order, just go back to map
        public void noButton(View view) {
            Log.d("no button", "no button was pressed");
            Intent techBoostSetup = new Intent(this, DisplayMapActivity.class);
            startActivity(techBoostSetup);
        }

    //unlocks images for each unlocked level
        public void unlockLevel(){
            switch(this.yourLevel) {
                case 6:
                    lock6.setVisibility(View.INVISIBLE);
                    level6.setVisibility(View.VISIBLE);
                    yesButton.setVisibility(View.INVISIBLE); //no option to push yes if level 6. TODO: make disappear if player already done a tech upgrade this turn
                case 5:
                    lock5.setVisibility(View.INVISIBLE);
                    level5.setVisibility(View.VISIBLE);
                case 4:
                    lock4.setVisibility(View.INVISIBLE);
                    level4.setVisibility(View.VISIBLE);
                case 3:
                    lock3.setVisibility(View.INVISIBLE);
                    level3.setVisibility(View.VISIBLE);
                case 2:
                    lock2.setVisibility(View.INVISIBLE);
                    level2.setVisibility(View.VISIBLE);
                default:
                    break;
            }
        }
    }


