package com.android.esc.views;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.android.esc.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class infoDetails extends MapsActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.info_detail);



        TextView displayDistance = (TextView) findViewById(R.id.tvDis);
        TextView displayDuration = (TextView) findViewById(R.id.tvDur);
        TextView displayStart = (TextView) findViewById(R.id.tvFrom);
        TextView displayEnd = (TextView) findViewById(R.id.tvTo);
        TextView displayfare = (TextView) findViewById(R.id.tvFare);
        TextView displaypuj = (TextView) findViewById(R.id.tvPUJ);


        Intent intent = getIntent();
        String distance = intent.getStringExtra("distance");
        String duration =intent.getStringExtra("duration");
        String mode = intent.getStringExtra("mode");
        String start = intent.getStringExtra("start");
        String end = intent.getStringExtra("end");
        String pujs = intent.getStringExtra("jeep");
        String a = intent.getStringExtra("aa");
        String b = intent.getStringExtra("b");
        String c = intent.getStringExtra("c");
        String d = intent.getStringExtra("d");
        ArrayList<String> pujlist;
        pujlist = intent.getStringArrayListExtra("jeep");

        String dis = distance.replace("km", "");
        String dur = duration.replaceAll("[^0-9]", "");

        double fare = 40.0 + (Double.valueOf(dis) * 13.38);

        double estimated = fare + (Double.valueOf(dur) * 1.85);

        double estfare = estimated;
        DecimalFormat df = new DecimalFormat("#.##");
        estfare = Double.valueOf(df.format(estfare));
        String estimatedfare = String.valueOf(estfare);

        displayDistance.setText(distance);
        displayDuration.setText(duration);
        displayStart.setText(start);
        displayEnd.setText(end);
        displayfare.setText("P "+estimatedfare);
        displaypuj.setText(pujs);







        //-----Possible display churvagalor -_-
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height =displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));


        /*for(int i=0;i<pujlist.size();i++)
        {
            Toast.makeText(getApplicationContext(),pujlist.get(i),Toast.LENGTH_LONG).show();
        }*/

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