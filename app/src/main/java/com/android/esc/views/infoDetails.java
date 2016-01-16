package com.android.esc.views;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.android.esc.R;


public class infoDetails extends MapsActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.info_detail);

        TextView displayDistance = (TextView) findViewById(R.id.textView8);
        TextView displayDuration = (TextView) findViewById(R.id.textView9);
        TextView displayMode = (TextView) findViewById(R.id.textView12);
        TextView displayStart = (TextView) findViewById(R.id.textView11);
        TextView displayEnd = (TextView) findViewById(R.id.textView10);



        //-----Possible display churvagalor -_-
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height =displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.5));

        //---End-->

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String distance = extras.getString("distance");
            String duration = extras.getString("duration");
            String mode = extras.getString("mode");
            String start = extras.getString("start");
            String end = extras.getString("end");

            displayDistance.setText(distance);
            displayDuration.setText(duration);
            displayStart.setText(start);
            displayMode.setText(mode);
            displayEnd.setText(end);

        }




    }
}