package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.esc.R;
import com.android.esc.addholder.AddressHolder;
import com.android.esc.model.Users;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends Activity implements AsyncResponse  {
    AddressHolder add=new AddressHolder();
    String username, password;
    TextView u_name, lname, fname, gender, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        u_name = (TextView)findViewById(R.id.usernameTxt);
        lname = (TextView)findViewById(R.id.lnameTxt);
        fname = (TextView)findViewById(R.id.fnameTxt);
        gender = (TextView)findViewById(R.id.genderTxt);
        email = (TextView)findViewById(R.id.emailTxt);

        HashMap postData = new HashMap();
        postData.put("username", username);
        postData.put("password", password);
        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute(add.getIpaddress() + "ESCMOBILE/display_loggedin.php");
    }

    @Override
    public void processFinish(String result) {

        ArrayList<Users> userList =
                new JsonConverter<Users>().toArrayList(result, Users.class);

        for(Users value: userList) {
            u_name.setText(value.account_username);
            lname.setText(value.account_lastname);
            fname.setText(value.account_firstname );
            email.setText(value.account_email);
        }


    }

    public void back(View v){
        Intent i = new Intent(this,NewsfeedActivity.class);
        i.putExtra("username", username);
        i.putExtra("password", password);
        startActivity(i);
        finish();

    }
}
