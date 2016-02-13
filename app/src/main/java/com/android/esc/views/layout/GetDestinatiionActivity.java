package com.android.esc.views.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.addholder.AddressHolder;
import com.android.esc.model.Routes;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;

public class GetDestinatiionActivity extends Activity implements AsyncResponse {
    TextView firstT, secondT;
    String startPoint;
    String endPoint;
    String jeep;
    boolean[] ridecheck;
    String[] jeepneycount;
    String[] check;
    int count=1;
    AddressHolder add=new AddressHolder();
    String latdest,lngdest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_destinatiion);
        firstT = (TextView) findViewById(R.id.firstF);
        secondT = (TextView) findViewById(R.id.secondF);
        Intent intent = getIntent();
        startPoint= intent.getStringExtra("startPoint");
        endPoint = intent.getStringExtra("endPoint");
        jeep = intent.getStringExtra("jeep");
        latdest = intent.getStringExtra("latdest");
        lngdest = intent.getStringExtra("latdest");


        Toast.makeText(getApplicationContext(), jeep, Toast.LENGTH_LONG).show();
        for(int index=0;index<jeep.length();index++){
            if(jeep.charAt(index)==','){

                count++;
            }

        }
        jeepneycount= new String[count];
        ridecheck= new boolean[count];
        int jeepcount=0;
        String store="";
        for(int index=0;index<jeep.length();index++){
            if(jeep.charAt(index)!=','){
                store+=jeep.charAt(index);
            }else{
                jeepneycount[jeepcount]=store;
                store="";
                jeepcount++;

            }

        }

        firstT.setText(startPoint);
        secondT.setText(endPoint);

        //Toast.makeText(getApplicationContext(), jeepneycount[0], Toast.LENGTH_LONG).show();

    }
    public void btnClick(View v) {
        PostResponseAsyncTask taskmark = new PostResponseAsyncTask(this);
        taskmark.execute(add.getIpaddress() + "Escape/index.php/mobileuser/fetchRoutes/" + latdest + "/" + lngdest);



    }


    @Override
    public void processFinish(String s) {
        int counter;
        ArrayList<Routes> routesList = new JsonConverter<Routes>().toArrayList(s, Routes.class);
        for(int i=0;i<jeepneycount.length;i++){
            for (Routes value: routesList) {
                if(routesList.contains(jeepneycount[i])){
                    ridecheck[i]=true;
                }
            }
        }
        for(int i=0;i<jeepneycount.length;i++){
            String print="";
            if(ridecheck[i]==false){
                print="1";
            }else{
                print="0";
            }
            Toast.makeText(getApplicationContext(),jeepneycount[i]+"="+print,Toast.LENGTH_LONG);
        }



    }
}
