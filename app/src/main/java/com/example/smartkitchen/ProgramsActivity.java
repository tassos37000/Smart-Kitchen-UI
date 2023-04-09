package com.example.smartkitchen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProgramsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private SharedPreferences sharedPreferences;
    String[] countryNames={"Light","Defrost Plus","Fan Forced","Fan Bake","Bake","Pastry Plus","Fan Grill","Grill","Pyrolytic Self-Clean"};
    int[] flags = {R.drawable.light, R.drawable.defrost_plus, R.drawable.fan_forced, R.drawable.fan_bake, R.drawable.bake, R.drawable.pastry_plus, R.drawable.fan_grill, R.drawable.grill, R.drawable.pyrolytic};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Spinner spinner = findViewById(R.id.programSelector);
        spinner.setOnItemSelectedListener(this);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),flags,countryNames);
        spinner.setAdapter(customAdapter);

        int spinnerPosition = customAdapter.sharedPreferences.getInt("pos", -1);

        if (spinnerPosition != -1) {
            spinner.setSelection(spinnerPosition);
        }

        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProgramsActivity.this, MainScreenActivity.class);
            startActivity(intent);
        });

        TextView programDesc = findViewById(R.id.programsDesc);
        programDesc.setMovementMethod(new ScrollingMovementMethod());

        TextView programText = findViewById(R.id.textTemp);

        String temp = sharedPreferences.getString("temp", "100");
        programText.setText(temp);

        Button minusButton = findViewById(R.id.minusTemp);
        Button plusButton = findViewById(R.id.plusTemp);

        minusButton.setOnClickListener(view -> {
            String currentTemp = programText.getText().toString();
            if(currentTemp.equals("50")){
                programText.setText("280");
            }
            else{
                programText.setText(String.valueOf(Integer.parseInt(currentTemp)-10));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("temp", programText.getText().toString());
            editor.apply();
        });

        plusButton.setOnClickListener(view -> {
            String currentTemp = programText.getText().toString();
            if(currentTemp.equals("280")){
                programText.setText("50");
            }
            else{
                programText.setText(String.valueOf(Integer.parseInt(currentTemp)+10));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("temp", programText.getText().toString());
            editor.apply();
        });

        NumberPicker timerHour = findViewById(R.id.timer_hour);
        timerHour.setMinValue(0);
        timerHour.setMaxValue(24);
        timerHour.setWrapSelectorWheel(true);

        int hour = sharedPreferences.getInt("hour", 0);
        if(hour != 0){
            timerHour.setValue(hour);
        }

        timerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("hour", numberPicker.getValue());
                editor.apply();
            }
        });

        NumberPicker timerMinutes = findViewById(R.id.timer_minutes);
        timerMinutes.setMinValue(0);
        timerMinutes.setMaxValue(59);
        timerMinutes.setWrapSelectorWheel(true);

        int minutes = sharedPreferences.getInt("minutes", 0);
        if(minutes != 0){
            timerMinutes.setValue(minutes);
        }

        timerMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("minutes", numberPicker.getValue());
                editor.apply();
            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}
