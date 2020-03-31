package com.example.risc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    // Called when user taps start game button
    public void startGame(View view) {
        Intent intent = new Intent(this, DisplayGameActivity.class);
        startActivity(intent);
    }
}
