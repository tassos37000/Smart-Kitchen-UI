package com.example.smartkitchen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity{
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SwitchCompat voiceCommandsSwitch = findViewById(R.id.voiceCommandSwitch);
        ImageView voiceCommandImage = findViewById(R.id.imageVoiceCommand);
        changeSaveSwitchImage(voiceCommandsSwitch, voiceCommandImage, "VoiceCommandSS", R.drawable.microphone, R.drawable.microphone_mute);

        SwitchCompat voiceResponseSwitch = findViewById(R.id.voiceResponsesSwitch);
        ImageView voiceResponseImage = findViewById(R.id.imageVoiceResponse);
        changeSaveSwitchImage(voiceResponseSwitch, voiceResponseImage, "VoiceResponseSS", R.drawable.sound, R.drawable.mute_sound);

        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitch);
        ImageView notificationImage = findViewById(R.id.imageNotification);
        changeSaveSwitchImage(notificationSwitch, notificationImage, "NotificationSS", R.drawable.bell, R.drawable.mute_bell);

        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainScreenActivity.class);
            startActivity(intent);
        });
    }

    /**
     * @param toggleSwitch
     * @param imageView
     * @param key
     * @param imageOn
     * @param imageOff
     */
    private void changeSaveSwitchImage(SwitchCompat toggleSwitch, ImageView imageView, String key, int imageOn, int imageOff){
        boolean SwitchState = sharedPreferences.getBoolean(key, true);
//        Log.i(key, String.valueOf(SwitchState));
        toggleSwitch.setChecked(SwitchState);
        if (SwitchState) {
            imageView.setImageResource(imageOn);
        } else {
            imageView.setImageResource(imageOff);
        }
        toggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, isChecked);
            editor.apply();
            if (isChecked) {
                imageView.setImageResource(imageOn);
            } else {
                imageView.setImageResource(imageOff);
            }
        });
    }
}
