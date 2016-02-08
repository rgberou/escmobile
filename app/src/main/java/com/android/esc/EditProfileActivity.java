package com.android.esc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.esc.addholder.AddressHolder;
import com.android.esc.views.ProfileActivity;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class EditProfileActivity extends Activity  implements AsyncResponse {
    AddressHolder add=new AddressHolder();
    String userid,username,lname, fname, email, password;
    TextView usernameET;
    EditText  lnameET, fnameET, emailET, passwordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        username = intent.getStringExtra("username");
        lname = intent.getStringExtra("lname");
        fname = intent.getStringExtra("fname");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        usernameET = (TextView) findViewById(R.id.usernameTxt);
        lnameET = (EditText) findViewById(R.id.lnameTxt);
        fnameET = (EditText) findViewById(R.id.fnameTxt);
        emailET = (EditText) findViewById(R.id.emailTxt);
        passwordET = (EditText) findViewById(R.id.passwordTxt);

        usernameET.setText(username+"");
        lnameET.setText(lname+"");
        fnameET.setText(fname+"");
        emailET.setText(email+"");
        passwordET.setText(password+"");


    }

    public void save(View v){
        HashMap postData = new HashMap();
        postData.put("userid", userid);
        postData.put("username", username);
        postData.put("password", password);
        postData.put("lname", lnameET.getText().toString());
        postData.put("fname", fnameET.getText().toString());
        postData.put("email", emailET.getText().toString());
        postData.put("new_password", passwordET.getText().toString());
        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute(add.getIpaddress() + "ESCMOBILE/update_user.php");
    }

    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            Toast.makeText(this, "Updated Success!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("userid", userid);
            i.putExtra("username", username);
            i.putExtra("password", passwordET.getText().toString());
            startActivity(i);
            finish();
        }
        else{
            //Toast.makeText(this, "Incorrect Username/Password.", Toast.LENGTH_LONG).show();
        }
    }

    public void back(View v){
        Intent i = new Intent(this,ProfileActivity.class);
        i.putExtra("userid", userid);
        i.putExtra("username", username);
        i.putExtra("password", password);
        startActivity(i);
        finish();

    }
}
