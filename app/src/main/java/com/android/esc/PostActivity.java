package com.android.esc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class PostActivity extends Activity {
    Bitmap userPost;
    EditText caption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        caption = (EditText)findViewById(R.id.captionTxt);

        userPost = (Bitmap)this.getIntent().getParcelableExtra("picture");

        ImageView username = (ImageView)findViewById(R.id.imageView);
        username.setImageBitmap(userPost);

    }

    public void btnCancelPost(View v){
        Intent i = new Intent(this, DashboardActivity.class);
        startActivity(i);
        finish();
    }

    public void btnPost(View v){
        String method = "picture";
        Bitmap post = userPost;
        String cap = caption.getText().toString();

    }


}
