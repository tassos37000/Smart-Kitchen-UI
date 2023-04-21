package com.example.smartkitchen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity{
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ImageButton backButton = findViewById(R.id.backButton);

        SwitchCompat voiceCommandsSwitch = findViewById(R.id.voiceCommandSwitch);
        ImageView voiceCommandImage = findViewById(R.id.imageVoiceCommand);
        changeSaveSwitchImage(voiceCommandsSwitch, voiceCommandImage, "VoiceCommandSS", R.drawable.microphone, R.drawable.microphone_mute, R.drawable.microphone_white, R.drawable.microphone_mute_white);

        SwitchCompat voiceResponseSwitch = findViewById(R.id.voiceResponsesSwitch);
        ImageView voiceResponseImage = findViewById(R.id.imageVoiceResponse);
        changeSaveSwitchImage(voiceResponseSwitch, voiceResponseImage, "VoiceResponseSS", R.drawable.sound, R.drawable.mute_sound, R.drawable.sound_white, R.drawable.mute_sound_white);

        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitch);
        ImageView notificationImage = findViewById(R.id.imageNotification);
        changeSaveSwitchImage(notificationSwitch, notificationImage, "NotificationSS", R.drawable.bell, R.drawable.mute_bell, R.drawable.bell_white, R.drawable.mute_bell_white);

        SwitchCompat languageSwitch = findViewById(R.id.languageSwitch);
//        Log.e("shared pref", String.valueOf(sharedPreferences.getBoolean("languageSwitch", false)));
        languageSwitch.setChecked(sharedPreferences.getBoolean("languageSwitch", false));

        if (languageSwitch.isChecked()) {
            Locale locale = new Locale("el");
            Configuration config = new Configuration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        } else {
            Locale locale = new Locale("en");
            Configuration config = new Configuration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        languageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String language;
            if (isChecked) {
                // Αλλαγή σε Ελληνικά
                language = "el";
            } else {
                // Επιστροφή στα Αγγλικά
                language = "en";
            }
//            Log.e("language switch", String.valueOf(isChecked));

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("languageSwitch", isChecked);
            editor.putString("language", language);
            editor.apply();

            Locale locale = new Locale(language);
            Configuration config = new Configuration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            backButton.performClick();
        });

        SwitchCompat themeSwitch = findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(sharedPreferences.getBoolean("themeSwitch", false));

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("themeSwitch", isChecked);
            editor.apply();

            backButton.performClick();
        });

        backButton.setOnClickListener(view -> {
                Intent intent = new Intent(SettingsActivity.this, MainScreenActivity.class);
                Bundle extras = getIntent().getExtras();
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
     * @param toggleSwitch
     * @param imageView
     * @param key
     * @param imageOn
     * @param imageOff
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
