package com.example.smartkitchen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.smartkitchen.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

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
        int hour = getArguments().getInt("timer-hour");
        int minutes = getArguments().getInt("timer-minutes");
        int position = getArguments().getInt("position");
        Log.e("Second frgament position", String.valueOf(position));

        binding.temperatureText.setText(temperature);
        binding.programIcon.setImageResource(program_icon);
        binding.ovenIcon.setImageResource(R.drawable.oven_on);

        binding.changeSettingsButton.setOnClickListener(view2 ->{
            Intent intent = new Intent(getActivity(), ProgramsActivity.class);
            intent.putExtra("current_position", position);
            startActivity(intent);
        });

        binding.turnOffButton.setOnClickListener(view2 ->{
            Intent intent = new Intent(getActivity(), MainScreenActivity.class);
            startActivity(intent);
        });


//        Log.e("Second fragment temperature", temperature);
//        Log.e("Second fragment program text", program_text);
//        Log.e("Second fragment program icon", String.valueOf(program_icon));
//        Log.e("Second fragment hour", String.valueOf(hour));
//        Log.e("Second fragment minutes", String.valueOf(minutes));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}