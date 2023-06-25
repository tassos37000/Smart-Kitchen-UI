package com.example.smartkitchen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MainScreenActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private KnobController knobController;
    private String lastValue = "0";
    private PopupWindow popupWindow;
    private ImageView line_separator;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Bundle extras = getIntent().getExtras();

        ImageButton settingsButton = findViewById(R.id.settings);
        ImageButton supportButton = findViewById(R.id.techSupport);
        Button topLeft = findViewById(R.id.topLeft);
        Button topRight = findViewById(R.id.topRight);
        Button bottomLeft = findViewById(R.id.bottomLeft);
        Button bottomRight = findViewById(R.id.bottomRight);
        line_separator = findViewById(R.id.line_seperator);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        updateButton(topLeft, sharedPreferences.getString("topLeft", ""));
        updateButton(topRight, sharedPreferences.getString("topRight", ""));
        updateButton(bottomLeft, sharedPreferences.getString("bottomLeft", ""));
        updateButton(bottomRight, sharedPreferences.getString("bottomRight", ""));

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
                    intent.putExtra("program-text", extras.getInt("program-text"));
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
            Button tempButton = findViewById(extras.getInt("buttonId"));
            if(extras.getString("stoveValue") != null){
                Log.e("stove Value Main Act", extras.getString("stoveValue"));
                updateButton(tempButton, extras.getString("stoveValue"));
            }

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
                bundle.putInt("program-text", extras.getInt("program-text"));
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
                    intent.putExtra("program-text", extras.getInt("program-text"));
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

        ImageButton infoButton = findViewById(R.id.info);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, getResources().getText(R.string.stove_info), Snackbar.LENGTH_LONG)
                        .setTextColor(Color.WHITE)
                        .setBackgroundTint(getResources().getColor(R.color.snackbarBG))
                        .setTextMaxLines(3);
                View snackbarView = snackbar.getView();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                params.gravity = Gravity.TOP;
                params.topMargin = 57;
                params.leftMargin = 57;
                params.height = 120;
                params.width = 554;
                snackbarView.setLayoutParams(params);

                TextView snbtv = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                snbtv.setTextSize(20);
                snbtv.setTypeface(Typeface.DEFAULT_BOLD);
                snbtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                snackbar.show();
            }
        });

//        NotificationManager mNotificationManager;
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
//        Intent ii = new Intent(getApplicationContext(), MainScreenActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
//
//        mBuilder.setContentIntent(pendingIntent);
//        mBuilder.setSmallIcon(R.drawable.thermometer);
//        mBuilder.setContentTitle("Oven Preheated");
//        mBuilder.setContentText("The oven is preheated");
//
//        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        String channelId = "Your_channel_id";
//        NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
//        mNotificationManager.createNotificationChannel(channel);
//        mBuilder.setChannelId(channelId);
        //enable-disable notification
//        mNotificationManager.notify(0, mBuilder.build());


        SharedPreferences.Editor editor = sharedPreferences.edit();
        stoveListener(topLeft, editor, R.id.topLeft);
        stoveListener(topRight, editor, R.id.topRight);
        stoveListener(bottomLeft, editor, R.id.bottomLeft);
        stoveListener(bottomRight, editor, R.id.bottomRight);

        editor.putString("topLeft", (String) topLeft.getText());
        editor.putString("topRight", (String) topRight.getText());
        editor.putString("bottomLeft", (String) bottomLeft.getText());
        editor.putString("bottomRight", (String) bottomRight.getText());
        editor.apply();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void stoveListener(Button button, SharedPreferences.Editor editor, int buttonId){
        button.setOnClickListener(view -> {
            int color = Color.parseColor("#FFFFFF");
            line_separator.setColorFilter(color);
            if(popupWindow != null){
                popupWindow.dismiss();
            }
            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_window, null);

            popupWindow = new PopupWindow(popupView,  680, 500, false);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(view, Gravity.END, -10, 65);

            // dismiss the popup window when touched
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });

            knobController = (KnobController) popupView.findViewById(R.id.knob_controller);

//            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if(button.getText() == ""){
                knobController.setKnob(0);
            }
            else{
                knobController.setKnob(Integer.parseInt((String) button.getText()));
            }

            knobController.setOnKnobChangeListener(new KnobController.OnKnobChangeListener() {
                @Override
                public void onKnobValueChanged(int value) {
//                    Log.e("KnobActivity", "Knob value changed: " + value);
                    lastValue = String.valueOf(value);
                    button.setText(lastValue);
                    updateButton(button, lastValue);
                }
            });

            Button closeButton = popupView.findViewById(R.id.closeButton);
            closeButton.setOnClickListener(view2 -> {
                switch (buttonId){
                    case R.id.topLeft:
                        editor.putString("topLeft", (String) button.getText());
                        break;
                    case R.id.topRight:
                        editor.putString("topRight", (String) button.getText());
                        break;
                    case R.id.bottomLeft:
                        editor.putString("bottomLeft", (String) button.getText());
                        break;
                    case R.id.bottomRight:
                        editor.putString("bottomRight", (String) button.getText());
                        break;
                }
                editor.apply();
                popupWindow.dismiss();
                int color2 = Color.parseColor("#000000");
                line_separator.setColorFilter(color2);
            });
        } );
    }

    private void updateButton(Button button, String value){
        if(Objects.equals(value, "") || Objects.equals(value, "0")){
            button.setText("");
            button.setBackgroundColor(getResources().getColor(R.color.stoveOff));
        }
        else{
            button.setText(value);
            button.setBackgroundColor(getResources().getColor(R.color.stoveOn));
        }
    }
}