package com.android.esc.views.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.esc.R;
import com.android.esc.views.MapsActivity;

/**
 * Created by Rg on 2/11/2016.
 */
public class trial extends Activity {
    String startPoint;
    String endPoint;
    String jeep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_destinatiion);

        Intent intent = getIntent();
        startPoint= intent.getStringExtra("startPoint");
        endPoint = intent.getStringExtra("endPoint");

        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("startPoint", startPoint);
        i.putExtra("endPoint", endPoint);
        i.putExtra("jeep",jeep);
        startActivity(i);
    }

}


