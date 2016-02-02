package com.android.esc.views;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.esc.R;

import java.text.DecimalFormat;


public class infoDetails extends MapsActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.info_detail);

        TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();
        TabHost.TabSpec spec = tab.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Details");
        tab.addTab(spec);
        spec = tab.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Summary");
        tab.addTab(spec);

        TextView displayDistance = (TextView) findViewById(R.id.textView8);
        TextView displayDuration = (TextView) findViewById(R.id.textView9);
        TextView displayMode = (TextView) findViewById(R.id.textView12);
        TextView displayStart = (TextView) findViewById(R.id.textView11);
        TextView displayEnd = (TextView) findViewById(R.id.textView10);
        TextView displayfare = (TextView) findViewById(R.id.textView13);
        TextView A = (TextView) findViewById(R.id.textA);
        TextView B = (TextView) findViewById(R.id.textB);
        TextView C = (TextView) findViewById(R.id.textC);
        TextView D = (TextView) findViewById(R.id.textD);



        Intent intent = getIntent();
        String distance = intent.getStringExtra("distance");
        String duration =intent.getStringExtra("duration");
        String mode = intent.getStringExtra("mode");
        String start = intent.getStringExtra("start");
        String end = intent.getStringExtra("end");

        String dis = distance.replaceAll("[^0-9]", "");
        String dur = duration.replaceAll("[^0-9]", "");

        double fare;
        fare = 40 + (((Double.valueOf(dis) * 1000)/300)+3.00)/*+((Double.valueOf(dur)/2)+3.50)*/;


        double time = fare;
        DecimalFormat df = new DecimalFormat("#.##");
        time = Double.valueOf(df.format(time));
        String out = String.valueOf(time);


        String AA=intent.getStringExtra("A");
        String BB=intent.getStringExtra("B");
        String CC=intent.getStringExtra("C");
        String DD=intent.getStringExtra("D");


        displayDistance.setText(distance);
        displayDuration.setText(duration);
        displayStart.setText(start);
        displayMode.setText(mode);
        displayEnd.setText(end);
        displayfare.setText("P "+out);

        A.setText(AA);
        B.setText(BB);
        C.setText(CC);
        D.setText(DD);






        //-----Possible display churvagalor -_-
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height =displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.5));

        //---End-->

       /* Bundle extras = getIntent().getExtras();
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

        }*/




    }
}