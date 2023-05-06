package com.example.smartkitchen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class KnobActivity extends AppCompatActivity {
    private KnobController knobController;
    private String lastValue = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knob);

        Bundle extras = getIntent().getExtras();

        knobController = (KnobController) findViewById(R.id.knob_controller);

        knobController.setOnKnobChangeListener(new KnobController.OnKnobChangeListener() {
            @Override
            public void onKnobValueChanged(int value) {
                Log.e("KnobActivity", "Knob value changed: " + value);
                lastValue = String.valueOf(value);
            }
        });

        if(extras != null){
            knobController.setKnob(extras.getInt("buttonValue"));
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(KnobActivity.this, MainScreenActivity.class);
            if(extras != null){
                if(extras.getBoolean("selprog")){
                    intent.putExtra("progSel", extras.getBoolean("selprog"));
                    intent.putExtra("position", extras.getInt("position"));
                    intent.putExtra("temperature", extras.getString("temperature"));
                    intent.putExtra("program-text", extras.getString("program-text"));
                    intent.putExtra("program-icon", extras.getInt("program-icon"));
                    intent.putExtra("timer-hour", extras.getLong("timer-hour"));
                    intent.putExtra("timer-minutes", extras.getInt("timer-minutes"));
                }
            }
            startActivity(intent);
            this.finish();
        });

        Button okButton = findViewById(R.id.buttonSelectStoveValue);
        okButton.setOnClickListener(view -> {
            Intent intent = new Intent(KnobActivity.this, MainScreenActivity.class);
            if(extras != null){
                if(extras.getBoolean("selprog")){
                    intent.putExtra("progSel", extras.getBoolean("selprog"));
                    intent.putExtra("position", extras.getInt("position"));
                    intent.putExtra("temperature", extras.getString("temperature"));
                    intent.putExtra("program-text", extras.getString("program-text"));
                    intent.putExtra("program-icon", extras.getInt("program-icon"));
                    intent.putExtra("timer-hour", extras.getLong("timer-hour"));
                    intent.putExtra("timer-minutes", extras.getInt("timer-minutes"));
                }
                intent.putExtra("buttonId", extras.getInt("buttonId"));
                intent.putExtra("stoveValue", lastValue);
            }

            startActivity(intent);
            this.finish();
        });

    }
}
