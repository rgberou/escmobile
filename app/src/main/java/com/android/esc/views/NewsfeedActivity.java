package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.esc.R;
import com.android.esc.User;
import com.android.esc.addholder.AddressHolder;
import com.android.esc.views.layout.FlyOutContainer;
import com.android.esc.model.Posts;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.Locale;

public class NewsfeedActivity extends Activity implements AsyncResponse {
    TextToSpeech t1;
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
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });
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
        t1.speak("Logout successfully", TextToSpeech.QUEUE_FLUSH, null);
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void mapview(View v){
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);

    }


}
