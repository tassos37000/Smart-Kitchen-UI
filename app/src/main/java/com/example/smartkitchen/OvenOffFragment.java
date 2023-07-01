package com.example.smartkitchen;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartkitchen.databinding.FragmentOvenOffBinding;

import java.util.Locale;

public class OvenOffFragment extends Fragment {
    int position = 0;
    private FragmentOvenOffBinding binding;
    private TextToSpeech t1;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentOvenOffBinding.inflate(inflater, container, false);
        t1=new TextToSpeech(getContext(), status -> {
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
        });
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(getArguments() != null){
            position = getArguments().getInt("current_position");
        }

        binding.ovenOnOff.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ProgramsActivity.class);
            intent.putExtra("current_position", position);
            intent.putExtra("voiceresponces", getArguments().getBoolean("voiceresponces"));
            if(getArguments().getBoolean("voiceresponces")) {
                t1.speak(getResources().getString(R.string.tts_program), TextToSpeech.QUEUE_FLUSH, null, null);
            }
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}