package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.duke.ece651.risc.shared.AbstractPlayer;
import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.HumanPlayer;
import edu.duke.ece651.risc.shared.Region;
import edu.duke.ece651.risc.shared.TechBoost;

public class TechBoostActivity extends AppCompatActivity {
        HumanPlayer player;
        Board board;
        TextView yourFuel;
        TextView fuelCost;
        TextView yourLevel;
        Button submitButton;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tech_boost);
            board = ParentActivity.getBoard();
            player = ParentActivity.getPlayer();
            yourFuel = findViewById(R.id.insertYourFuel);
            yourLevel = findViewById(R.id.insertLevel);
            fuelCost = findViewById(R.id.insertFuelCost);
            submitButton = findViewById(R.id.yesBoost);
            setFields();
            Intent intent = getIntent();
        }
        public void setFields(){
            String fuel = Integer.toString(player.getResources().getFuelResource().getFuel());
            yourFuel.setText(fuel);
            String cost = Integer.toString(player.getMaxTechLevel().getCostToUpgrade());
            fuelCost.setText(cost);
            String level = Integer.toString(player.getMaxTechLevel().getMaxTechLevel());
            yourLevel.setText(level);
        }

        public void hitButton(View view){
            TechBoost techBoost = new TechBoost(player);
            ParentActivity pa = new ParentActivity();
            pa.setOrders(techBoost);
            Intent techBoostSetup = new Intent(this,DisplayMapActivity.class);
            startActivity(techBoostSetup);
        }

}
