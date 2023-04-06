package com.example.smartkitchen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Switch voiceCommandsSwitch = findViewById(R.id.voiceCommandSwitch);
        ImageView voiceCommandImage = findViewById(R.id.imageVoiceComand);

        if (savedInstanceState != null) {
            boolean switchState = savedInstanceState.getBoolean("switch_VC_state");
            voiceCommandsSwitch.setChecked(switchState);
        }

        voiceCommandsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    voiceCommandImage.setImageResource(R.drawable.microphone);
                } else {
                    voiceCommandImage.setImageResource(R.drawable.microphone_mute);
                }
            }
        });

        Switch voiceResponseSwitch = findViewById(R.id.voiceResponsesSwitch);
        ImageView voiceResponseImage = findViewById(R.id.imageVoiceResponse);

        voiceResponseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    voiceResponseImage.setImageResource(R.drawable.sound);
                } else {
                    voiceResponseImage.setImageResource(R.drawable.mute_sound);
                }
            }
        });

        Switch notificationSwitch = findViewById(R.id.notificationSwitch);
        ImageView notificationImage = findViewById(R.id.imageNotification);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationImage.setImageResource(R.drawable.bell);
                } else {
                    notificationImage.setImageResource(R.drawable.mute_bell);
                }
            }
        });

        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(SettingsActivity.this, MainScreen.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Switch voiceCS = findViewById(R.id.voiceCommandSwitch);
        boolean switchVCState = voiceCS.isChecked();
        outState.putBoolean("switch_VC_state", switchVCState);
    }
}
