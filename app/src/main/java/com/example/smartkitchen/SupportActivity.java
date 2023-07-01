package com.example.smartkitchen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SupportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        Bundle extras = getIntent().getExtras();

        Button stoveButton = findViewById(R.id.buttonSupportStove);
        stoveButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.haier.co.nz/cooking/cooktops/induction-cooktop-60cm-4-zones-with-flexi-zone-hci604ftb3-61823.html"));
            startActivity(browserIntent);
        });

        Button ovenButton = findViewById(R.id.buttonSupportOven);
        ovenButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.haier.co.nz/cooking/ovens/oven-60cm-8-function-self-cleaning-hwo60s8epb2-61744.html"));
            startActivity(browserIntent);
        });

        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(SupportActivity.this, Class.forName(getCallingActivity().getClassName()));
                if(extras != null){
//                        Bundle bundle = new Bundle();
                    boolean backFromProgram = extras.getBoolean("supportButton");
                    boolean ovenSetUp = extras.getBoolean("selprog");
                    Log.e("oven setup in support", String.valueOf(ovenSetUp));
                    Log.e("back from program", String.valueOf(backFromProgram));
                    if(backFromProgram){
                        intent.putExtra("current_position", extras.getInt("current_position"));
                    }
                    else if(ovenSetUp){
                        intent.putExtra("position", extras.getInt("position"));
                        intent.putExtra("temperature", extras.getString("temperature"));
                        intent.putExtra("program-text", extras.getInt("program-text"));
                        intent.putExtra("program-icon", extras.getInt("program-icon"));
                        intent.putExtra("timer-hour", extras.getLong("timer-hour"));
                        intent.putExtra("timer-minutes", extras.getInt("timer-minutes"));
                        intent.putExtra("progSel", true);
                        intent.putExtra("second_fragment", true);
                        intent.putExtra("selprog", true);
                    }
                }
                else{
                    Log.e("extras is null", "back button in support activity");
                }

                startActivityForResult(intent, 5);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
