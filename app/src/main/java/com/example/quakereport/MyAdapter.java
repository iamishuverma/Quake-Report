package com.example.quakereport;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends ArrayAdapter<Earthquake> {

    public MyAdapter(@NonNull Activity context, ArrayList<Earthquake> arrayList) {
        super(context,0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView T = convertView.findViewById(R.id.mag);
        Earthquake earthquake = getItem(position);
        if(earthquake != null)
        {
            T.setText(String.valueOf(earthquake.getmMagnitude()));

            T = convertView.findViewById(R.id.dir);
            String[] location = split(earthquake.getmLocation());           //splits the location string in two parts without 'OF'
            if(!location[0].equals("Near"))
                location[0] += "of";
            T.setText(location[0]);

            T = convertView.findViewById(R.id.loc);
            T.setText(location[1].substring(1,location[1].length()));

            T = convertView.findViewById(R.id.date);

            Date d = new Date(earthquake.getMtime());
            String date;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            date = simpleDateFormat.format(d);
            T.setText(date);

            T = convertView.findViewById(R.id.time);
            String time;
            simpleDateFormat = new SimpleDateFormat("h: mm a");
            time = simpleDateFormat.format(d);
            T.setText(time);

            int mcolor = earthquake.getColor();
            GradientDrawable GD = (GradientDrawable) (convertView.findViewById(R.id.mag)).getBackground();      //getBackGround() returns the value of background key.
            GD.setColor(mcolor);
        }
        return convertView;
    }
    private String [] split(String string) {
        String [] array = {};
        if(string.contains("of")) {
            array = string.split("of");
            return array;
        } else {
            array = new String[]{"Near", string};
            return array;
        }
    }
}
