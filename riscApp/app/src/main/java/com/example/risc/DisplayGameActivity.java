package com.example.risc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_game);
        setContentView(R.layout.map);
        // Get Intent that started activity
        Intent intent = getIntent();

        // Capture layout's TextView
        //TextView textView = findViewById(R.id.textView);
        //textView.setText("Hello there");
    }
}
