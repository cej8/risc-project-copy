package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PlaceUnitsActivity extends AppCompatActivity {
    TextView helpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_units);
    }

    public void sendPlacement(View view){

    }
}
