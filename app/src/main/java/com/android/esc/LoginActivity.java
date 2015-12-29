package com.android.esc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginActivity extends Activity implements AsyncResponse, View.OnClickListener{
    EditText user_log, pass_log;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_log = (EditText)findViewById(R.id.usertxt);
        pass_log = (EditText)findViewById(R.id.passtxt);
        loginBtn = (Button) findViewById(R.id.loginbtn);
        loginBtn.setOnClickListener(this);


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
            Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        AddressHolder add=new AddressHolder();
        HashMap postData = new HashMap();
        if(user_log.length()==0){
            user_log.requestFocus();
            user_log.setError("Input Username");
        }
        else if(pass_log.length()==0){
            pass_log.requestFocus();
            pass_log.setError("Input Username");
        }
        else {
            postData.put("escape", "esc_mobile");
            postData.put("username", user_log.getText().toString());
            postData.put("password", pass_log.getText().toString());

            PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
            task.execute(add.getIpaddress()+"ESCMOBILE/login.php");
            //task.execute(add.getIpaddress()+"ESCMOBILE/login.php");
        }
    }
}
