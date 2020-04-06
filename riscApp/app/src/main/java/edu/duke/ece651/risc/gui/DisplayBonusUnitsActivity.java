package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayBonusUnitsActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bonus_units);
        textView = findViewById(R.id.textView);
        Intent i = getIntent();
        String attackFrom = i.getStringExtra("PNAME");
        String attackTo = i.getStringExtra("ATTACKTO");
        String s = attackFrom + attackTo;
        textView.setText(s);
    }
}
