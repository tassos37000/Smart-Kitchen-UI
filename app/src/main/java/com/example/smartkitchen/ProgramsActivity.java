package com.example.smartkitchen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.*;
import java.util.Locale;

public class ProgramsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private SharedPreferences sharedPreferences;
    private TextToSpeech t1;

    int[] icons = {R.drawable.light, R.drawable.defrost_plus, R.drawable.fan_bake, R.drawable.bake, R.drawable.fan_forced, R.drawable.pastry_plus, R.drawable.fan_grill, R.drawable.grill, R.drawable.pyrolytic};

    int spinnerPosition;
    int counter = 0;
    ProgramAdapter programAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] programNames = getResources().getStringArray(R.array.program_names);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Spinner spinner = findViewById(R.id.programSelector);
        programAdapter = new ProgramAdapter(getApplicationContext(), icons, programNames);
        spinner.setAdapter(programAdapter);

        Bundle extras = getIntent().getExtras();
        Button selectedProgram = findViewById(R.id.buttonSelecProg);

        t1=new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = t1.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("error", "This Language is not supported");
                } else {
//                    Log.e("init TTS", "all ok");
                }
            } else {
                Log.e("error", String.valueOf(status));
            }
        });

        spinner.setSelection(sharedPreferences.getInt("pos", -1));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.e("counter before if", String.valueOf(counter));

                if (extras != null && counter == 0) {
                    spinnerPosition = extras.getInt("current_position");
                    if(extras.getBoolean("progSel"))
                        spinnerPosition = extras.getInt("position");
                    spinner.setSelection(spinnerPosition);
//                    Log.e("spinner position from extras", String.valueOf(spinnerPosition));

                    counter = 1;
                }
                else{
                    Log.e("Custom Adapter spinner Position", String.valueOf(programAdapter.sharedPreferences.getInt("pos", -1)));
                    spinnerPosition = programAdapter.sharedPreferences.getInt("pos", -1);
                }

                spinner.setSelection(spinnerPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TextView tempText = findViewById(R.id.textTemp);

        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> {
//            try {
                if (extras != null){
                    Log.e("second fragment in back button", String.valueOf(extras.getBoolean("second_fragment")));
//                    Log.e("program set", String.valueOf(extras.getBoolean("second_fragment")));
                    if(extras.getBoolean("second_fragment")){
                        Intent intent = new Intent(ProgramsActivity.this, MainScreenActivity.class);
                        intent.putExtra("progSel", true);
                        intent.putExtra("temperature", tempText.getText().toString());
                        intent.putExtra("program-text", programNames[programAdapter.sharedPreferences.getInt("pos", -1)]);
                        intent.putExtra("program-icon", icons[programAdapter.sharedPreferences.getInt("pos", -1)]);
                        intent.putExtra("position", spinnerPosition);
                        long timerEnd = extras.getLong("timer-hour");
//                        Log.e("Prog Act time", String.valueOf((timerEnd / (1000 * 60) % 60 )));
                        intent.putExtra("timer-hour", timerEnd);
                        startActivity(intent);

                    }
                    else{
//                        Log.e("back", "it continues");
                        Intent intent = new Intent(ProgramsActivity.this, MainScreenActivity.class);
                        intent.putExtra("current_position", spinnerPosition);
                        intent.putExtra("backButton", true);
                        startActivity(intent);
                    }
                }
                else{
                    Log.e("extras is null", "back");
                }

//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }

        });

        ImageButton supportButton = findViewById(R.id.techSupport);
        supportButton.setOnClickListener(view -> {

            if (extras != null) {
//                    Log.e("program set", String.valueOf(extras.getBoolean("second_fragment")));
                Log.e("second fragment in support button", String.valueOf(extras.getBoolean("second_fragment")));
                if (extras.getBoolean("second_fragment")) {
                    Intent intent = new Intent(ProgramsActivity.this, SupportActivity.class);
                    intent.putExtra("progSel", true);
                    intent.putExtra("temperature", tempText.getText().toString());
                    intent.putExtra("program-text", programNames[programAdapter.sharedPreferences.getInt("pos", -1)]);
                    intent.putExtra("program-icon", icons[programAdapter.sharedPreferences.getInt("pos", -1)]);
                    intent.putExtra("position", spinnerPosition);
                    long timerEnd = extras.getLong("timer-hour");
//                        Log.e("Prog Act time", String.valueOf((timerEnd / (1000 * 60) % 60 )));
                    intent.putExtra("timer-hour", timerEnd);
                    intent.putExtra("selprog", true);
                    startActivityForResult(intent,5);

                } else {
//                        Log.e("back", "it continues");
                    Intent intent = new Intent(ProgramsActivity.this, SupportActivity.class);
                    intent.putExtra("current_position", spinnerPosition);
                    intent.putExtra("supportButton", true);
                    startActivityForResult(intent, 5);
                }
            }
            else{
                Log.e("extras is null", "support");
            }

        });



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
                tempText.setText(String.valueOf(Integer.parseInt(currentTemp)-5));
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
                tempText.setText(String.valueOf(Integer.parseInt(currentTemp)+5));
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

        timerHour.setOnValueChangedListener((numberPicker, i, i1) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("hour", numberPicker.getValue());
            editor.apply();
        });

        NumberPicker timerMinutes = findViewById(R.id.timer_minutes);
        timerMinutes.setMinValue(0);
        timerMinutes.setMaxValue(59);
        timerMinutes.setWrapSelectorWheel(true);

        int minutes = sharedPreferences.getInt("minutes", 0);
        if(minutes != 0){
            timerMinutes.setValue(minutes);
        }

        timerMinutes.setOnValueChangedListener((numberPicker, i, i1) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("minutes", numberPicker.getValue());
            editor.apply();
        });

        selectedProgram.setOnClickListener(view ->{
            Intent intent = new Intent(ProgramsActivity.this, MainScreenActivity.class);
            intent.putExtra("progSel", true);
            intent.putExtra("temperature", tempText.getText().toString());
            Log.e("programs activity program-text", String.valueOf(programAdapter.sharedPreferences.getInt("pos", -1)));
            intent.putExtra("program-text", programAdapter.sharedPreferences.getInt("pos", -1));
            intent.putExtra("program-icon", icons[programAdapter.sharedPreferences.getInt("pos", -1)]);
            intent.putExtra("position", spinnerPosition);

            long timerEnd = System.currentTimeMillis() + ((long) timerHour.getValue() * 60 * 60 + timerMinutes.getValue() * 60L) * 1000;
            intent.putExtra("timer-hour", timerEnd);
            intent.putExtra("timer-minutes", timerMinutes.getValue());
            String text = getResources().getString(R.string.tts_oven_status) + " " + programNames[programAdapter.sharedPreferences.getInt("pos", -1)]
                    + " " + getResources().getString(R.string.tts_oven_status_middle) + " " + tempText.getText().toString() + " " + getResources().getString(R.string.tts_oven_status_end);
            if(extras.getBoolean("voiceresponces")) {
                t1.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }

//            Log.e("Program Activity position", String.valueOf(spinnerPosition));
//            Log.e("temperature", tempText.getText().toString());
//            Log.e("spinner position", String.valueOf(spinnerPosition));
//            Log.e("program with spinnerPosition", programNames[spinnerPosition]);
//            Log.e("program with Custom Adapter", programNames[customAdapter.sharedPreferences.getInt("pos", -1)]);
//            Log.e("hour", String.valueOf(timerHour.getValue()));
//            Log.e("minutes", String.valueOf(timerMinutes.getValue()));

            startActivity(intent);
        });

        ArrayAdapter<String> mForecastAdapter;
        String[] data = getResources().getStringArray(R.array.program_description);

        List<String> weekForecast = new ArrayList<>(Arrays.asList(data));
        mForecastAdapter = new ArrayAdapter<>(ProgramsActivity.this, // The current context (this activity)
                R.layout.list_item, // The name of the layout ID.
                R.id.list_item_program_textview, // The ID of the textview to populate.
                weekForecast);
        ListView listView = findViewById(R.id.listview_program);
        listView.setAdapter(mForecastAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}
