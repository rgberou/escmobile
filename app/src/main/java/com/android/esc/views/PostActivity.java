package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.addholder.AddressHolder;
import com.android.esc.controllers.GPSService;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class PostActivity extends Activity  implements AsyncResponse, View.OnClickListener{
    Bitmap userPost;
    EditText caption;
    String username, password, lati, lang, type;
    TextView tvAddress, lat, lng, typestatus;
    Spinner choice;
    String userid, traf_choice;
    String gps = "";
    private String encoded_string;
    private Button postBtn;
    final Timer timer=new Timer(120000,1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        timer.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        caption = (EditText)findViewById(R.id.captionTxt);
        lat = (TextView)findViewById(R.id.lat);
        lng = (TextView)findViewById(R.id.lng);
        tvAddress = (TextView)findViewById(R.id.tvadd);

        choice = (Spinner)findViewById(R.id.choice);
        choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                traf_choice=choice.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        //choice.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        //typestatus = (TextView)findViewById(R.id.namestatus);

        userPost = this.getIntent().getParcelableExtra("picture");
        Intent intent = getIntent();

        gps = intent.getStringExtra("address");
        if(gps=="IO Exception trying to get address:java.io.IOException: Timed out waiting for response from server"){
            Toast.makeText(getApplicationContext(), "Error retrieving location. Please try again!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,NewsfeedActivity.class);
            startActivity(i);
            finish();
        }else {
            userid = intent.getStringExtra("userid");
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
            lati = intent.getStringExtra("lat");
            lang = intent.getStringExtra("lng");
            type = intent.getStringExtra("type");

            postBtn = (Button) findViewById(R.id.postBtn);
            postBtn.setOnClickListener(this);

            ImageView username = (ImageView) findViewById(R.id.imageView);
            username.setImageBitmap(userPost);

            lat.setText(lati);
            lng.setText(lang);



            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            userPost.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);

            TextView tvAddress = (TextView) findViewById(R.id.tvadd);

            tvAddress.setText(gps);
        }
    }

    public void btnCancelPost(View v){
        timer.cancel();
        finish();
    }


    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            Toast.makeText(this, "Uploaded Successfully!", Toast.LENGTH_LONG).show();
            timer.cancel();
            finish();
        }
        else{
            Toast.makeText(this, "Error in Uploading...", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        timer.cancel();

        AddressHolder add=new AddressHolder();
        HashMap postData = new HashMap();
        postData.put("encoded_string",encoded_string);
        postData.put("caption", caption.getText().toString());
        postData.put("location", tvAddress.getText().toString());
        postData.put("dist_type", traf_choice.toString());
        postData.put("userid", userid);
        postData.put("latitude", lat.getText().toString());
        postData.put("longitude", lng.getText().toString());

        //postData.put("caption", gps);

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute(add.getIpaddress() + "ESCMOBILE/index.php/mobileuser/upload");

    }

    public void btnGPS(View view){



        String address = "";
        GPSService mGPSService = new GPSService(getApplicationContext());
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            //return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
             address = "";
        } else {

            // Getting location co-ordinates
            Double latitude = mGPSService.getLatitude();
            Double longitude = mGPSService.getLongitude();


           // Toast.makeText(getActivity(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            address = mGPSService.getLocationAddress();

            //tvLocation.setText("Latitude: " + latitude + " \nLongitude: " + longitude);
            lat.setText(latitude+"");
            lng.setText(longitude+"");
            tvAddress.setText(address);
        }

        //Toast.makeText(getActivity(), "Your address is: " + address, Toast.LENGTH_SHORT).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();





    }



    public class Timer extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            timer.cancel();
            Intent i = new Intent(getApplicationContext(), NewsfeedActivity.class);
            i.putExtra("userid", userid);
            i.putExtra("username", username);
            i.putExtra("password", password);
            startActivity(i);

            Toast.makeText(getApplicationContext(), "Your session has ended.", Toast.LENGTH_SHORT).show();
            finish();


        }
    }



}
