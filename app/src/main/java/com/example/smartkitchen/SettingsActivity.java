package com.example.smartkitchen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity{
    private SharedPreferences sharedPreferences;
    private int countLanguage = 0;
    private int countTheme = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton supportButton = findViewById(R.id.techSupport);

        SwitchCompat voiceResponseSwitch = findViewById(R.id.voiceResponsesSwitch);
        ImageView voiceResponseImage = findViewById(R.id.imageVoiceResponse);
        changeSaveSwitchImage(voiceResponseSwitch, voiceResponseImage, "VoiceResponseSS", R.drawable.sound, R.drawable.mute_sound, R.drawable.sound_white, R.drawable.mute_sound_white);

        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitch);
        ImageView notificationImage = findViewById(R.id.imageNotification);
        changeSaveSwitchImage(notificationSwitch, notificationImage, "NotificationSS", R.drawable.bell, R.drawable.mute_bell, R.drawable.bell_white, R.drawable.mute_bell_white);

        Spinner languageSpinner = findViewById(R.id.language_spinner);
        String[] languageNames = getResources().getStringArray(R.array.language_names);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.settings_spinner_item, languageNames);
        languageSpinner.setAdapter(spinnerAdapter);

        languageSpinner.setSelection(sharedPreferences.getInt("language_pos", 0));

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("language_pos", position);

                if(String.valueOf(position).equals("0")){
                    Locale locale = new Locale("en");
                    Configuration config = new Configuration();
                    config.setLocale(locale);
                    getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    editor.putString("language", "en");
                }
                else{
                    Locale locale = new Locale("el");
                    Configuration config = new Configuration();
                    config.setLocale(locale);
                    getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    editor.putString("language", "el");
                }

                editor.apply();
                if(countLanguage > 0){
                    backButton.performClick();
                }
                else{
                    countLanguage++;
                }
        }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner themeSpinner = findViewById(R.id.theme_spinner);
        String[] themeNames = getResources().getStringArray(R.array.theme_names);
        ArrayAdapter<String> spinnerThemeAdapter = new ArrayAdapter<>(this, R.layout.settings_spinner_item, themeNames);
        themeSpinner.setAdapter(spinnerThemeAdapter);

        themeSpinner.setSelection(sharedPreferences.getInt("theme_pos", 0));

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(String.valueOf(position).equals("0")){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("theme_pos", position);
                editor.apply();
                if(countTheme > 0){
                    backButton.performClick();
                }
                else{
                    countTheme++;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainScreenActivity.class);
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                if(extras.getBoolean("selprog")){
                    intent.putExtra("progSel", extras.getBoolean("selprog"));
                    intent.putExtra("position", extras.getInt("position"));
                    intent.putExtra("temperature", extras.getString("temperature"));
                    intent.putExtra("program-text", extras.getInt("program-text"));
                    intent.putExtra("program-icon", extras.getInt("program-icon"));
                    intent.putExtra("timer-hour", extras.getLong("timer-hour"));
                    intent.putExtra("timer-minutes", extras.getInt("timer-minutes"));

                }
                else{
                    intent.putExtra("progSel", false);
                }
            }
            intent.putExtra("settings", true);
            intent.putExtra("notifications", notificationSwitch.isChecked());
            intent.putExtra("voice_response", voiceResponseSwitch.isChecked());
            startActivity(intent);
            this.finish();
        });

        supportButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, SupportActivity.class);
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                if(extras.getBoolean("selprog")){
                    intent.putExtra("progSel", extras.getBoolean("selprog"));
                    intent.putExtra("position", extras.getInt("position"));
                    intent.putExtra("temperature", extras.getString("temperature"));
                    intent.putExtra("program-text", extras.getInt("program-text"));
                    intent.putExtra("program-icon", extras.getInt("program-icon"));
                    intent.putExtra("timer-hour", extras.getLong("timer-hour"));
                    intent.putExtra("timer-minutes", extras.getInt("timer-minutes"));

                }
                else{
                    intent.putExtra("progSel", false);
                }
            }
            intent.putExtra("settings", true);
            intent.putExtra("notifications", notificationSwitch.isChecked());
            intent.putExtra("voice_response", voiceResponseSwitch.isChecked());
            startActivity(intent);
            this.finish();
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Get configuration and resources before onCreate method
        Resources resources = newBase.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.uiMode = Configuration.UI_MODE_TYPE_UNDEFINED;
        Context context = newBase.createConfigurationContext(configuration);
        // Set locale with configuration saved
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sharedPreferences.getString("language", "en");
        Log.e("language in attachBaseContext", language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        super.attachBaseContext(newBase);
    }

    /**
     * @param toggleSwitch the switch object
     * @param imageView the image view object
     * @param key key of shared preferences
     * @param imageOn id of image when switch is on
     * @param imageOff id of image when switch is off
     */
    private void changeSaveSwitchImage(SwitchCompat toggleSwitch, ImageView imageView, String key, int imageOn, int imageOff, int imageOn_dark, int imageOff_dark){
        boolean SwitchState = sharedPreferences.getBoolean(key, true);
//        Log.i(key, String.valueOf(SwitchState));
        toggleSwitch.setChecked(SwitchState);
        if (SwitchState) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                imageView.setImageResource(imageOn_dark);
            } else {
                imageView.setImageResource(imageOn);
            }
        } else {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                imageView.setImageResource(imageOff_dark);
            } else {
                imageView.setImageResource(imageOff);
            }
        }
        toggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, isChecked);
            editor.apply();
            if (isChecked) {
                Log.e("uimode", String.valueOf(AppCompatDelegate.getDefaultNightMode()));
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    imageView.setImageResource(imageOn_dark);
                } else {
                    imageView.setImageResource(imageOn);
                }
            } else {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    imageView.setImageResource(imageOff_dark);
                } else {
                    imageView.setImageResource(imageOff);
                }
            }
        });
    }
}
