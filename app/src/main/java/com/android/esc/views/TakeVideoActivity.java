package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.esc.R;
import com.android.esc.controllers.GPSService;
import com.android.esc.model.Config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TakeVideoActivity extends Activity {
    private static final String TAG = TakeVideoActivity.class.getSimpleName();

    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;
    private VideoView vidDisplay;
    String username, password;
    String userid, type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_video);

        vidDisplay = (VideoView) findViewById(R.id.vidDisplay);
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        type= intent.getStringExtra("type");
    }

    public void btnCancel(View v){
        finish();
    }


    public void takeVid(View v){
        recordvideo();
    }

    private void recordvideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File( Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public void btnContinue(View v) {
            String address = "";
            Double latitude, longitude;
            GPSService mGPSService = new GPSService(getApplicationContext());
            mGPSService.getLocation();

            if (mGPSService.isLocationAvailable == false) {
                Toast.makeText(getApplicationContext(), "Please turn on GPS to continue", Toast.LENGTH_LONG).show();

                return;



            } else {

                // Getting location co-ordinates
                latitude = mGPSService.getLatitude();
                longitude = mGPSService.getLongitude();
                // Toast.makeText(getActivity(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                address = mGPSService.getLocationAddress();

                //tvLocation.setText("Latitude: " + latitude + " \nLongitude: " + longitude);

            }

            //Toast.makeText(getActivity(), "Your address is: " + address, Toast.LENGTH_SHORT).show();

            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();


            if(address=="IO Exception trying to get address:java.io.IOException: Timed out waiting for response from server"){
                Toast.makeText(getApplicationContext(), "Error retrieving location. Please try again!", Toast.LENGTH_SHORT).show();
            }else{
                //timer.cancel();
                Intent i = new Intent(this, PostVideoActivity.class);
                i.putExtra("filePath", fileUri.getPath());
                i.putExtra("address", address);
                i.putExtra("userid", userid);
                i.putExtra("username", username);
                i.putExtra("password", password);
                i.putExtra("type", type);
                i.putExtra("lat", String.valueOf(latitude));
                i.putExtra("lng", String.valueOf(longitude));
                startActivity(i);
                finish();
            }
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                vidDisplay.setVideoPath(fileUri.getPath());
                // start playing
                vidDisplay.start();


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}
