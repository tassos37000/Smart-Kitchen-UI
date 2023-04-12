package com.example.smartkitchen;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    public SharedPreferences sharedPreferences;
    Context context;
    int[] programIcons;
    String[] programNames;

    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, int[] programIcons, String[] programNames) {
        this.context = applicationContext;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        this.programIcons = programIcons;
        this.programNames = programNames;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return programIcons.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        Log.e("Custom Adapter before inflate i", String.valueOf(i));
        view = inflater.inflate(R.layout.spinner_items, null);
        ImageView icon = view.findViewById(R.id.programIcon);
        TextView names = view.findViewById(R.id.programText);
        icon.setImageResource(programIcons[i]);
        names.setText(programNames[i]);
//        Log.e("Custom Adapter after inflate i", String.valueOf(i));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("pos", i);
        editor.apply();
        return view;
    }
}
