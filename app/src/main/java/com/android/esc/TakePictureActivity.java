package com.android.esc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class TakePictureActivity extends Activity {
    public static final int CAMERA_REQUEST = 10;
    public static final int VIDEO_REQUEST = 10;
    private ImageView imgDisplay;
    private Bitmap cam;
    int stat = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takepicture);

        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);

    }

    public void btnTest(View v){
        Intent cameraTake = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraTake, CAMERA_REQUEST);
    }

    public void btnTest2(View v){
        Intent cameraTake = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(cameraTake, VIDEO_REQUEST);
    }

    public void btnCancel(View v){
        Intent i = new Intent(this, NewsfeedActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap camPic = (Bitmap) data.getExtras().get("data");
                imgDisplay.setImageBitmap(camPic);
                cam = camPic;
                stat = 1;
            }
        }
    }

    public void btnContinue(View v) {
        if(stat == 0){
            Toast.makeText(getApplication(),"NO IMAGE", Toast.LENGTH_LONG).show();
        }else{
            Intent i = new Intent(this, PostActivity.class);
            i.putExtra("picture", cam);
            startActivity(i);
        }

    }



}



