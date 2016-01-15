package com.android.esc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.esc.flyoutmenu.view.viewgroup.FlyOutContainer;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;

public class NewsfeedActivity extends Activity implements AsyncResponse {
    ListView lvPost;
    TextView hello;
    User user=new User();
    FlyOutContainer root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_newsfeed, null);
        this.setContentView(root);
        //
        // setContentView(R.layout.activity_newsfeed);
        AddressHolder add=new AddressHolder();
        lvPost=(ListView)findViewById(R.id.lvPost);
        PostResponseAsyncTask task = new PostResponseAsyncTask(this);
        task.execute(add.getIpaddress()+"ESCMOBILE/retrieve.php?format=json");
    }

    @Override
    public void processFinish(String result) {


        ArrayList<Posts> userList =
                new JsonConverter<Posts>().toArrayList(result, Posts.class);

        ArrayList<String> titles = new ArrayList<String>();
        for(Posts value: userList){
            //user.setFirstname(value.account_firstname);
            //user.setLastname(value.account_lastname);
            titles.add(value.caption + " ");
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

    public void toggleMenu(View v){
        this.root.toggleMenu();
    }

    public void newsfeed(View v){
        Intent i = new Intent(this,NewsfeedActivity.class);
        startActivity(i);
        finish();
    }

    public void logout(View v){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }


}
