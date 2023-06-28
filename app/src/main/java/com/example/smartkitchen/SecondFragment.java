package com.example.smartkitchen;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.smartkitchen.databinding.FragmentSecondBinding;

import java.util.Locale;
import java.lang.*;

public class SecondFragment extends Fragment {

    private TextView countdownText;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private FragmentSecondBinding binding;
    private TextToSpeech t1;

    public MainScreenActivity activity;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        t1=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = t1.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {
                        Log.e("init TTS", "all ok");
                    }
                } else {
                    Log.e("error", String.valueOf(status));
                }
            }
        });
        return binding.getRoot();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainScreenActivity) activity;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String temperature = getArguments().getString("temperature");
        int program_text = getArguments().getInt("program-text");
        int program_icon = getArguments().getInt("program-icon");
        timeLeftInMillis = getArguments().getLong("timer-hour");
        Log.e("Second Frag program_text", String.valueOf(program_text));
        int minutes = getArguments().getInt("timer-minutes");
        int position = getArguments().getInt("position");

//        Log.e("Second frgament position", String.valueOf(position));
        String[] programNames = getResources().getStringArray(R.array.program_names);

        binding.temperatureText.setText(temperature);
        binding.programIcon.setImageResource(program_icon);
        binding.ovenIcon.setImageResource(R.drawable.oven_on);
        binding.programText.setText(programNames[program_text]);

        binding.changeSettingsButton.setOnClickListener(view2 ->{
            Intent intent = new Intent(getActivity(), ProgramsActivity.class);
            intent.putExtra("current_position", position);
            intent.putExtra("second_fragment", true);
            Log.e("chnge set button", String.valueOf(timeLeftInMillis));
            intent.putExtra("timer-hour", timeLeftInMillis + System.currentTimeMillis());
            intent.putExtra("voiceresponces", getArguments().getBoolean("voiceresponces"));
            if(getArguments().getBoolean("voiceresponces")) {
                t1.speak(getResources().getString(R.string.tts_program), TextToSpeech.QUEUE_FLUSH, null, null);
            }
            startActivityForResult(intent, 5);
        });

        binding.turnOffButton.setOnClickListener(view2 ->{
            Intent intent = new Intent(getActivity(), MainScreenActivity.class);
            intent.putExtra("second_fragment", false);
            if(getArguments().getBoolean("voiceresponces")) {
                t1.speak(getResources().getString(R.string.tts_oven_off), TextToSpeech.QUEUE_FLUSH, null, null);
            }
            startActivity(intent);
        });


//        Log.e("Second fragment temperature", temperature);
//        Log.e("Second fragment program text", program_text);
//        Log.e("Second fragment program icon", String.valueOf(program_icon));
//        Log.e("Second fragment hour", String.valueOf(hour));
//        Log.e("Second fragment minutes", String.valueOf(minutes));
        if(getArguments().getLong("timer-hour") == 0)
            return;

        countdownText = binding.timerText;
        startTimer(timeLeftInMillis - System.currentTimeMillis());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void startTimer(long time) {
//        timeLeftInMillis = (hours * 60 * 60 + minutes * 60) * 1000;
        Log.e("Second fragment time in start timer", String.valueOf(time));
        timeLeftInMillis = time;

        if(time < 0){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new FirstFragment());
            fragmentTransaction.commit();
            return;
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
//                Log.e("onTick", String.valueOf(millisUntilFinished));
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountdownText();
                if(getArguments().getBoolean("voiceresponces")) {
                    t1.speak(getResources().getString(R.string.tts_oven_off), TextToSpeech.QUEUE_FLUSH, null, null);
                }
                NotificationManager mNotificationManager;

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, "notify_001");
                Intent ii = new Intent(activity, MainScreenActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, ii, 0);

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.drawable.thermometer);
                mBuilder.setContentTitle("Timer end");
                mBuilder.setContentText("The timer has ended");

                mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
                //enable-disable notification
                Log.e("getArguments", String.valueOf(getArguments()));
                Log.e("not bool", String.valueOf(getArguments().getBoolean("notifications")));
                if(getArguments() == null || getArguments().getBoolean("notifications")){
                    mNotificationManager.notify(0, mBuilder.build());
                }

                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putBoolean("voiceresponces", getArguments().getBoolean("voiceresponces"));
                FirstFragment firstFragment = new FirstFragment();
                firstFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, firstFragment);
                fragmentTransaction.commit();
            }
        }.start();
    }

    private void updateCountdownText() {
        int hours = (int) ((timeLeftInMillis / (1000 * 60 * 60)) % 24);
        int minutes = (int) ((timeLeftInMillis / (1000 * 60)) % 60);
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        countdownText.setText(timeLeftFormatted);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}