package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;

import com.android.esc.R;

public class TakeVideoActivity extends Activity {
    public static final int VIDEO_REQUEST = 200;
    private VideoView vidDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_video);

        vidDisplay = (VideoView) findViewById(R.id.vidDisplay);
    }

    public void btnCancel(View v){
        finish();
    }


    public void takeVid(View v){
        Intent cameraTake = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(cameraTake, VIDEO_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == VIDEO_REQUEST) {
                Bitmap camVid = (Bitmap) data.getExtras().get("data");
                Uri uri = Uri.parse("android.resource://" + getPackageName());

                vidDisplay.setVideoPath(String.valueOf(camVid));
                //cam = camVid;
                //stat = 1;
            }
        }
    }

}
