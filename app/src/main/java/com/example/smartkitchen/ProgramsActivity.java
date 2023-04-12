package com.example.smartkitchen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
    String[] programNames ={"Light","Defrost Plus","Fan Forced","Fan Bake","Bake","Pastry Plus","Fan Grill","Grill","Pyrolytic Self-Clean"};
    int[] icons = {R.drawable.light, R.drawable.defrost_plus, R.drawable.fan_forced, R.drawable.fan_bake, R.drawable.bake, R.drawable.pastry_plus, R.drawable.fan_grill, R.drawable.grill, R.drawable.pyrolytic};

    int spinnerPosition;
    int counter = 0;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Spinner spinner = findViewById(R.id.programSelector);
        customAdapter = new CustomAdapter(getApplicationContext(), icons, programNames);
        spinner.setAdapter(customAdapter);

        Bundle extras = getIntent().getExtras();

        spinner.setSelection(sharedPreferences.getInt("pos", -1));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.e("counter before if", String.valueOf(counter));
                if (extras != null && counter == 0) {
                    spinnerPosition = extras.getInt("current_position");
                    spinner.setSelection(spinnerPosition);
                    Log.e("spinner position from extras", String.valueOf(spinnerPosition));

                    counter = 1;
                }
                else{
                    Log.e("Custom Adapter spinner Position", String.valueOf(customAdapter.sharedPreferences.getInt("pos", -1)));
                    spinnerPosition = customAdapter.sharedPreferences.getInt("pos", -1);
                }

                spinner.setSelection(spinnerPosition);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("pos", spinnerPosition);
//                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProgramsActivity.this, MainScreenActivity.class);
            intent.putExtra("current_position", spinnerPosition);
            intent.putExtra("backButton", true);
            startActivity(intent);
        });

        TextView programDesc = findViewById(R.id.programsDesc);
        programDesc.setMovementMethod(new ScrollingMovementMethod());

        TextView tempText = findViewById(R.id.textTemp);

        String temp = sharedPreferences.getString("temp", "100");
        tempText.setText(temp);

        Button minusButton = findViewById(R.id.minusTemp);
        Button plusButton = findViewById(R.id.plusTemp);

        minusButton.setOnClickListener(view -> {
            String currentTemp = tempText.getText().toString();
            if(currentTemp.equals("50")){
                tempText.setText("280");
            }
            else{
                tempText.setText(String.valueOf(Integer.parseInt(currentTemp)-10));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("temp", tempText.getText().toString());
            editor.apply();
        });

        plusButton.setOnClickListener(view -> {
            String currentTemp = tempText.getText().toString();
            if(currentTemp.equals("280")){
                tempText.setText("50");
            }
            else{
                tempText.setText(String.valueOf(Integer.parseInt(currentTemp)+10));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("temp", tempText.getText().toString());
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

        Button selectedProgram = findViewById(R.id.buttonSelecProg);
        selectedProgram.setOnClickListener(view ->{
            Intent intent = new Intent(ProgramsActivity.this, MainScreenActivity.class);
            intent.putExtra("progSel", true);
            intent.putExtra("temperature", tempText.getText().toString());
            intent.putExtra("program-text", programNames[customAdapter.sharedPreferences.getInt("pos", -1)]);
            intent.putExtra("program-icon", icons[customAdapter.sharedPreferences.getInt("pos", -1)]);
            intent.putExtra("position", spinnerPosition);
//            Log.e("Program Activity position", String.valueOf(spinnerPosition));
            intent.putExtra("timer-hour", timerHour.getValue());
            intent.putExtra("timer-minutes", timerMinutes.getValue());

//            Log.e("temperature", tempText.getText().toString());
//            Log.e("spinner position", String.valueOf(spinnerPosition));
//            Log.e("program with spinnerPosition", programNames[spinnerPosition]);
//            Log.e("program with Custom Adapter", programNames[customAdapter.sharedPreferences.getInt("pos", -1)]);
//            Log.e("hour", String.valueOf(timerHour.getValue()));
//            Log.e("minutes", String.valueOf(timerMinutes.getValue()));

            startActivity(intent);
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}
