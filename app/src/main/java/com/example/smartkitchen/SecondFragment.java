package com.example.smartkitchen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String temperature = getArguments().getString("temperature");
        String program_text = getArguments().getString("program-text");
        int program_icon = getArguments().getInt("program-icon");
        timeLeftInMillis = getArguments().getLong("timer-hour");
        Log.e("Second Frag time", String.valueOf(timeLeftInMillis));
        int minutes = getArguments().getInt("timer-minutes");
        int position = getArguments().getInt("position");

//        Log.e("Second frgament position", String.valueOf(position));

        binding.temperatureText.setText(temperature);
        binding.programIcon.setImageResource(program_icon);
        binding.ovenIcon.setImageResource(R.drawable.oven_on);

        binding.changeSettingsButton.setOnClickListener(view2 ->{
            Intent intent = new Intent(getActivity(), ProgramsActivity.class);
            intent.putExtra("current_position", position);
            intent.putExtra("second_fragment", true);
            Log.e("chnge set button", String.valueOf(timeLeftInMillis));
            intent.putExtra("timer-hour", timeLeftInMillis + System.currentTimeMillis());
            startActivityForResult(intent, 5);
        });

        binding.turnOffButton.setOnClickListener(view2 ->{
            Intent intent = new Intent(getActivity(), MainScreenActivity.class);
            intent.putExtra("second_fragment", false);
            startActivity(intent);
        });


//        Log.e("Second fragment temperature", temperature);
//        Log.e("Second fragment program text", program_text);
//        Log.e("Second fragment program icon", String.valueOf(program_icon));
//        Log.e("Second fragment hour", String.valueOf(hour));
//        Log.e("Second fragment minutes", String.valueOf(minutes));

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
        timeLeftInMillis = time;

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
                NotificationManager mNotificationManager;

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "notify_001");
                Intent ii = new Intent(getActivity().getApplicationContext(), MainScreenActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, ii, 0);

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.drawable.thermometer);
                mBuilder.setContentTitle("Timer end");
                mBuilder.setContentText("The timer has ended");

                mNotificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
                //enable-disable notification
                mNotificationManager.notify(0, mBuilder.build());

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new FirstFragment());
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