package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.esc.R;
import com.android.esc.model.Users;
import com.android.esc.addholder.AddressHolder;
import com.android.esc.views.layout.FlyOutContainer;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class NewsfeedActivity extends Activity implements AsyncResponse {
    TextToSpeech t1;
    ListView lvPost;
    WebView view;
    String username, password;
    String userid;
    TextView useridTxt,usernameTxt,passwordTxt, name;
    //User user=new User();
    FlyOutContainer root;
    AddressHolder add=new AddressHolder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_newsfeed, null);
        this.setContentView(root);
        //
        // setContentView(R.layout.activity_newsfeed);


        //WEBVIEW
        String url = add.getIpaddress()+"Escape/index.php/mobileuser/newsfeed";
        view = (WebView) this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(url);
        //END

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");


        HashMap postData = new HashMap();
        postData.put("username", username);
        postData.put("password", password);
        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute(add.getIpaddress() + "ESCMOBILE/display_loggedin.php");

        name=(TextView)findViewById(R.id.user_fullname);

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

        ArrayList<Users> userList =
                new JsonConverter<Users>().toArrayList(result, Users.class);

        for(Users value: userList) {
            userid = (value.userid+" ");
            name.setText(value.account_firstname + " " + value.account_lastname);
        }


    }

    public void postBtn(View v){
        Intent i = new Intent(this,TakePictureActivity.class);
        i.putExtra("userid", userid);
        i.putExtra("username", username);
        i.putExtra("password", password);
        startActivity(i);


    }

    public void toggleMenu(View v){
        this.root.toggleMenu();
    }

    public void newsfeed(View v){
        Intent i = new Intent(this,NewsfeedActivity.class);
        i.putExtra("userid", userid);
        i.putExtra("username", username);
        i.putExtra("password", password);
        startActivity(i);
        finish();
    }

    public void logout(View v){
        t1.speak("You have successfully logged out", TextToSpeech.QUEUE_FLUSH, null);
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void mapview(View v){
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);

    }

    public void refresh(View v){
        view.reload();

    }

}
