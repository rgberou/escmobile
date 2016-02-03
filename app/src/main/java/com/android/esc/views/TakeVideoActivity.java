package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.android.esc.R;

public class TakeVideoActivity extends Activity {
    public static final int VIDEO_REQUEST = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_video);
    }

    public void btnCancel(View v){
        finish();
    }


    public void takeVid(View v){
        Intent cameraTake = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(cameraTake, VIDEO_REQUEST);
    }
}
