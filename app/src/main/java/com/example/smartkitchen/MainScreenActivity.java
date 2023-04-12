package com.example.smartkitchen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

public class MainScreenActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ImageButton settingsButton = findViewById(R.id.settings);

        Bundle extras = getIntent().getExtras();
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
                bundle.putInt("timer-hour", extras.getInt("timer-hour"));
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
            startActivity(intent);
        });
    }
}