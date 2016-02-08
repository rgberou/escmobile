package com.android.esc.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.User;
import com.android.esc.addholder.AddressHolder;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends Activity implements AsyncResponse, View.OnClickListener{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextToSpeech t1;
    EditText user_log, pass_log;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        User user=new User();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if(user.getFirstname()==null){
            setContentView(R.layout.activity_login);
            user_log = (EditText)findViewById(R.id.usertxt);
            pass_log = (EditText)findViewById(R.id.passtxt);
            loginBtn = (Button) findViewById(R.id.loginbtn);
            loginBtn.setOnClickListener(this);
        }else{
            setContentView(R.layout.activity_newsfeed);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void register_form(View arg0){
        Intent i = new Intent(this,RegistrationActivity.class);
        startActivity(i);
    }

    @Override
    public void processFinish(String result) {

        if(result.equals("Success")){
            t1.playSilence(1250, TextToSpeech.QUEUE_ADD, null);
            t1.speak("You have successfully logged in", TextToSpeech.QUEUE_FLUSH, null);
            Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, NewsfeedActivity.class);
            i.putExtra("username", user_log.getText().toString());
            i.putExtra("password", pass_log.getText().toString());
            startActivity(i);
            finish();
        }
        else if(result.equals("failed")){
            Toast.makeText(this, "Incorrect Username/Password.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Connection to Server Failed", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        AddressHolder add=new AddressHolder();
        final LocationManager manager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
            Toast.makeText(getApplicationContext(), "Please turn on Your GPS Location to continue", Toast.LENGTH_LONG).show();
        }
        else{
            HashMap postData = new HashMap();
            if(user_log.length()==0){
                user_log.requestFocus();
                user_log.setError("Input Username");
            }
            else if(pass_log.length()==0){
                pass_log.requestFocus();
                pass_log.setError("Input Password");
            }
            else {
                postData.put("escape", "esc_mobile");
                postData.put("username", user_log.getText().toString());
                postData.put("password", pass_log.getText().toString());

                PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
                task.execute(add.getIpaddress()+"Escape/index.php/mobileuser/login");
            }
        }


    }
}
