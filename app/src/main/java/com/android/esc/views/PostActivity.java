package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.addholder.AddressHolder;
import com.android.esc.controllers.GPSService;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class PostActivity extends Activity  implements AsyncResponse, View.OnClickListener {
    Bitmap userPost;
    EditText caption;
    String gps = "";
    private String encoded_string;
    private Button postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        caption = (EditText)findViewById(R.id.captionTxt);

        userPost = this.getIntent().getParcelableExtra("picture");
        Intent intent = getIntent();
        gps = intent.getStringExtra("address");


        postBtn = (Button) findViewById(R.id.postBtn);
        postBtn.setOnClickListener(this);

        ImageView username = (ImageView)findViewById(R.id.imageView);
        username.setImageBitmap(userPost);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        userPost.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] array = stream.toByteArray();
        encoded_string = Base64.encodeToString(array, 0);

       TextView tvAddress = (TextView)findViewById(R.id.tvadd);

        tvAddress.setText(gps);
    }

    public void btnCancelPost(View v){
        Intent i = new Intent(this, NewsfeedActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            Toast.makeText(this, "Uploaded Successfully!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, NewsfeedActivity.class);
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(this, "Error in Uploading...", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {

        AddressHolder add=new AddressHolder();
        HashMap postData = new HashMap();
        postData.put("encoded_string",encoded_string);
        postData.put("caption", caption.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute(add.getIpaddress() + "ESCMOBILE/connect.php");

    }

    public void btnGPS(View view){

        final TextView tvAddress = (TextView)findViewById(R.id.tvadd);

        String address = "";
        GPSService mGPSService = new GPSService(getApplicationContext());
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            //Toast.makeText(getActivity(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
           // Toast.makeText(getActivity(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            address = mGPSService.getLocationAddress();

            //tvLocation.setText("Latitude: " + latitude + " \nLongitude: " + longitude);
            tvAddress.setText(address);
        }

        //Toast.makeText(getActivity(), "Your address is: " + address, Toast.LENGTH_SHORT).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();





    }



}
