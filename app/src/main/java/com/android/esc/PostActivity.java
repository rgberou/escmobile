package com.android.esc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

public class PostActivity extends Activity  implements AsyncResponse, View.OnClickListener {
    Bitmap userPost;
    EditText caption;
    private String encoded_string, image_name;
    private Button postBtn;
    private File file;
    private Uri file_uri;

    public PostActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        caption = (EditText)findViewById(R.id.captionTxt);

        userPost = this.getIntent().getParcelableExtra("picture");

        postBtn = (Button) findViewById(R.id.postBtn);
        postBtn.setOnClickListener(this);

        ImageView username = (ImageView)findViewById(R.id.imageView);
        username.setImageBitmap(userPost);

        //image_name = "pic0.jpg";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        userPost.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] array = stream.toByteArray();
        encoded_string = Base64.encodeToString(array, 0);


    }

    public void btnCancelPost(View v){
        Intent i = new Intent(this, DashboardActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            Toast.makeText(this, "Uploaded Successfully!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, DashboardActivity.class);
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
        //postData.put("image_name",image_name.toString());
        postData.put("caption", caption.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute(add.getIpaddress() + "ESCMOBILE/connect.php");

    }






}
