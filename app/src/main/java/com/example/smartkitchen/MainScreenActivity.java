package com.example.smartkitchen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;

public class MainScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ImageButton settingsButton = findViewById(R.id.settings);

        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainScreenActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}