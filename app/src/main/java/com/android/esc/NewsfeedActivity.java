package com.android.esc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;

public class NewsfeedActivity extends Activity implements AsyncResponse {
    ListView lvPost;
    TextView hello;
    User user=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        lvPost=(ListView)findViewById(R.id.lvPost);
        PostResponseAsyncTask task = new PostResponseAsyncTask(this);
        task.execute("http://192.168.254.103/ESCMOBILE/retrieve.php?format=json");
    }

    @Override
    public void processFinish(String result) {


        ArrayList<Posts> userList =
                new JsonConverter<Posts>().toArrayList(result, Posts.class);

        ArrayList<String> titles = new ArrayList<String>();
        for(Posts value: userList){
            //user.setFirstname(value.account_firstname);
            //user.setLastname(value.account_lastname);
            titles.add(value.account_lastname + " " + value.account_firstname);
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                titles);
        lvPost.setAdapter(adapter);

    }

    public void postBtn(View v){
        Intent i = new Intent(this,TakePictureActivity.class);
        startActivity(i);
        finish();

    }
}
