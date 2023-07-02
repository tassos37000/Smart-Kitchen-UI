package com.example.smartkitchen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.util.Locale;
import java.util.Objects;

public class MainScreenActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private KnobController knobController;
    private String lastValue = "0";
    private PopupWindow popupWindow;
    private ImageView line_separator;
    private TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Bundle extras = getIntent().getExtras();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(extras != null){
            Log.e("bundle from settings", String.valueOf(extras.getBoolean("settings")));
            Log.e("notif switch", String.valueOf(extras.getBoolean("notifications")));
            if(extras.getBoolean("settings")){
                boolean notificationBoolean = extras.getBoolean("notifications");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("spnotification", notificationBoolean);
                editor.putBoolean("spvoiceres", extras.getBoolean("voice_response"));
                editor.apply();

            }
        }


        ImageButton settingsButton = findViewById(R.id.settings);
        ImageButton supportButton = findViewById(R.id.techSupport);
        Button topLeft = findViewById(R.id.topLeft);
        Button topRight = findViewById(R.id.topRight);
        Button bottomLeft = findViewById(R.id.bottomLeft);
        Button bottomRight = findViewById(R.id.bottomRight);
        line_separator = findViewById(R.id.line_seperator);

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

        updateButton(topLeft, sharedPreferences.getString("topLeft", ""));
        updateButton(topRight, sharedPreferences.getString("topRight", ""));
        updateButton(bottomLeft, sharedPreferences.getString("bottomLeft", ""));
        updateButton(bottomRight, sharedPreferences.getString("bottomRight", ""));

        supportButton.setOnClickListener(view -> {
            OvenOnFragment ovenOn = (OvenOnFragment) getSupportFragmentManager().findFragmentByTag("OVEN_ON");
            boolean ovenIsOn = false;
            if (ovenOn != null && ovenOn.isVisible()) {
                ovenIsOn = true;
            }
            Intent intent = new Intent(MainScreenActivity.this, SupportActivity.class);
            if (extras != null) {
                boolean ovenSetUp = extras.getBoolean("progSel");
                Log.e("oven setup in main", String.valueOf(ovenSetUp));
                if(ovenSetUp && ovenIsOn){
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
            intent.putExtra("voiceresponces", sharedPreferences.getBoolean("spvoiceres", true));
            startActivityForResult(intent, 5);
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putBoolean("voiceresponces", sharedPreferences.getBoolean("spvoiceres", true));
        OvenOffFragment ovenOffFragment = new OvenOffFragment();
        ovenOffFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, ovenOffFragment, "OVEN_OFF");
        if (extras != null) {
            Button tempButton = findViewById(extras.getInt("buttonId"));
            if(extras.getString("stoveValue") != null){
                Log.e("stove Value Main Act", extras.getString("stoveValue"));
                updateButton(tempButton, extras.getString("stoveValue"));
            }

            boolean backFromProgram = extras.getBoolean("backButton");
            boolean ovenSetUp = extras.getBoolean("progSel");
            if(backFromProgram){
                bundle.putInt("current_position", extras.getInt("current_position"));
                ovenOffFragment = new OvenOffFragment();
                ovenOffFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, ovenOffFragment, "OVEN_OFF");
            }

            else if(ovenSetUp) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                bundle.putInt("position", extras.getInt("position"));
                bundle.putString("temperature", extras.getString("temperature"));
                bundle.putInt("program-text", extras.getInt("program-text"));
                bundle.putInt("program-icon", extras.getInt("program-icon"));
                bundle.putLong("timer-hour", extras.getLong("timer-hour"));
                bundle.putInt("timer-minutes", extras.getInt("timer-minutes"));
                bundle.putBoolean("notifications", sharedPreferences.getBoolean("spnotification", true));

//                Log.e("Main screen temperature", extras.getString("temperature"));
//                Log.e("Main screen program text", extras.getString("program-text"));
//                Log.e("Main screen program icon", String.valueOf(extras.getInt("program-icon")));
//                Log.e("Main screen hour", String.valueOf(extras.getInt("timer-hour")));
//                Log.e("Main screen minutes", String.valueOf(extras.getInt("timer-minutes")));
                OvenOnFragment ovenOnFragment = new OvenOnFragment();
                ovenOnFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, ovenOnFragment, "OVEN_ON");
            }

        }

        fragmentTransaction.commit();


        settingsButton.setOnClickListener(view -> {
            OvenOnFragment ovenOn = (OvenOnFragment) getSupportFragmentManager().findFragmentByTag("OVEN_ON");
            boolean ovenIsOn = false;
            if (ovenOn != null && ovenOn.isVisible()) {
                ovenIsOn = true;
            }
            Intent intent = new Intent(MainScreenActivity.this, SettingsActivity.class);
            if (extras != null) {
                Log.e("progSel in MainScreen", String.valueOf(extras.getBoolean("progSel")));
                boolean ovenSetUp = extras.getBoolean("progSel");
                if(ovenSetUp && ovenIsOn){
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
        infoButton.setOnClickListener(v -> {
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

            TextView snbtv = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            snbtv.setTextSize(20);
            snbtv.setTypeface(Typeface.DEFAULT_BOLD);
            snbtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            snackbar.show();
        });

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
//            int color = Color.parseColor(Integer.toHexString(getColor(R.color.seperator)));
            line_separator.setColorFilter(getColor(R.color.seperator_off));
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

            knobController = popupView.findViewById(R.id.knob_controller);

//            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if(button.getText() == ""){
                knobController.setKnob(0);
            }
            else{
                knobController.setKnob(Integer.parseInt((String) button.getText()));
            }

            knobController.setOnKnobChangeListener(value -> {
//                    Log.e("KnobActivity", "Knob value changed: " + value);
                lastValue = String.valueOf(value);
                button.setText(lastValue);
                updateButton(button, lastValue);
            });

            Button closeButton = popupView.findViewById(R.id.closeButton);
            closeButton.setOnClickListener(view2 -> {
                String text;
                if(button.getText().equals("")){
                    text = getResources().getString(R.string.tts_stove_off) + " ";
                }
                else {
                    text = getResources().getString(R.string.tts_stove) + " " + button.getText() + " " + getResources().getString(R.string.tts_stove_middle) + " ";
                }
                switch (buttonId){
                    case R.id.topLeft:
                        editor.putString("topLeft", (String) button.getText());
                        text += getResources().getString(R.string.topLeft);
                        break;
                    case R.id.topRight:
                        editor.putString("topRight", (String) button.getText());
                        text += getResources().getString(R.string.topRight);
                        break;
                    case R.id.bottomLeft:
                        editor.putString("bottomLeft", (String) button.getText());
                        text += getResources().getString(R.string.bottomLeft);
                        break;
                    case R.id.bottomRight:
                        editor.putString("bottomRight", (String) button.getText());
                        text += getResources().getString(R.string.bottomRight);
                        break;
                }
                text += " " + getResources().getString(R.string.tts_stove_end);
                Log.e("final text", text);
                if(sharedPreferences.getBoolean("spvoiceres", true)){
                    t1.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }

                editor.apply();
                popupWindow.dismiss();
//                int color2 = Color.parseColor(Integer.toHexString(getColor(R.color.seperator_off)));
                line_separator.setColorFilter(getColor(R.color.seperator));
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