package com.example.smartkitchen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.widget.ImageButton;

public class MainScreenActivity extends AppCompatActivity {
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Bundle extras = getIntent().getExtras();
        ImageButton settingsButton = findViewById(R.id.settings);

        ImageButton supportButton = findViewById(R.id.techSupport);
        supportButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainScreenActivity.this, SupportActivity.class);
            if (extras != null) {
                boolean ovenSetUp = extras.getBoolean("progSel");
                Log.e("oven setup in main", String.valueOf(ovenSetUp));
                if(ovenSetUp){
                    Log.e("send data to support", "true");
                    intent.putExtra("selprog", true);
                    intent.putExtra("position", extras.getInt("position"));
                    intent.putExtra("temperature", extras.getString("temperature"));
                    intent.putExtra("program-text", extras.getString("program-text"));
                    intent.putExtra("program-icon", extras.getInt("program-icon"));
                    intent.putExtra("timer-hour", extras.getLong("timer-hour"));
                    intent.putExtra("timer-minutes", extras.getInt("timer-minutes"));
                }
                else{
                    intent.putExtra("selprog", false);
                    intent.putExtra("supportButton", false);
                }
            }
            startActivityForResult(intent, 5);
        });


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new FirstFragment());
        if (extras != null) {
            Bundle bundle = new Bundle();
            boolean backFromProgram = extras.getBoolean("backButton");
            boolean ovenSetUp = extras.getBoolean("progSel");
            if(backFromProgram){
                bundle.putInt("current_position", extras.getInt("current_position"));
                FirstFragment firstFragment = new FirstFragment();
                firstFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, firstFragment);
            }

            else if(ovenSetUp) {
                bundle.putInt("position", extras.getInt("position"));
                bundle.putString("temperature", extras.getString("temperature"));
                bundle.putString("program-text", extras.getString("program-text"));
                bundle.putInt("program-icon", extras.getInt("program-icon"));
                bundle.putLong("timer-hour", extras.getLong("timer-hour"));
                bundle.putInt("timer-minutes", extras.getInt("timer-minutes"));

//                Log.e("Main screen temperature", extras.getString("temperature"));
//                Log.e("Main screen program text", extras.getString("program-text"));
//                Log.e("Main screen program icon", String.valueOf(extras.getInt("program-icon")));
//                Log.e("Main screen hour", String.valueOf(extras.getInt("timer-hour")));
//                Log.e("Main screen minutes", String.valueOf(extras.getInt("timer-minutes")));
                SecondFragment secondFragment = new SecondFragment();
                secondFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, secondFragment);
            }

        }

        fragmentTransaction.commit();


        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainScreenActivity.this, SettingsActivity.class);
            if (extras != null) {
                boolean ovenSetUp = extras.getBoolean("progSel");
                if(ovenSetUp){
                    intent.putExtra("selprog", true);
                    intent.putExtra("position", extras.getInt("position"));
                    intent.putExtra("temperature", extras.getString("temperature"));
                    intent.putExtra("program-text", extras.getString("program-text"));
                    intent.putExtra("program-icon", extras.getInt("program-icon"));
                    intent.putExtra("timer-hour", extras.getLong("timer-hour"));
                    intent.putExtra("timer-minutes", extras.getInt("timer-minutes"));
                }
                else{
                    intent.putExtra("selprog", false);
                }
            }
            startActivityForResult(intent, 10);

        });

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), MainScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.thermometer);
        mBuilder.setContentTitle("Oven Preheated");
        mBuilder.setContentText("The oven is preheated");

        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "Your_channel_id";
        NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(channel);
        mBuilder.setChannelId(channelId);
        //enable-disable notification
//        mNotificationManager.notify(0, mBuilder.build());
    }
}