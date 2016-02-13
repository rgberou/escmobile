package com.android.esc.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.addholder.AddressHolder;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class RegistrationActivity extends Activity implements  AsyncResponse, View.OnClickListener {
    EditText username, password, fname, lname, email;
    Button registerBtn;
    String gender="M";
    RadioGroup radioGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username = (EditText)findViewById(R.id.usernametxt);
        password = (EditText)findViewById(R.id.passwordtxt);
        fname = (EditText)findViewById(R.id.fnametxt);
        lname = (EditText)findViewById(R.id.lnametxt);
        radioGender = (RadioGroup)findViewById(R.id.radioGender);
        email = (EditText)findViewById(R.id.emailtxt);
        registerBtn = (Button) findViewById(R.id.registerbtn);
        registerBtn.setOnClickListener(this);

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton male = (RadioButton)findViewById(R.id.radioMale);
                RadioButton female = (RadioButton)findViewById(R.id.radioFemale);
                if(male.isChecked()){
                    gender = "M";
                }else if(female.isChecked()){
                    gender = "F";
                }

            }
        });


    }

    public boolean isEmail(String email) {
        return email.contains("@") && email.contains(".com") && email.indexOf("@") < email.indexOf(".com");
    }

    @Override
    public void onClick(View view) {
        HashMap postData = new HashMap();
        AddressHolder add=new AddressHolder();
        if(fname.length()==0){
            fname.requestFocus();
            fname.setError("Input Firstname");
        }
        else if(lname.length()==0){
            lname.requestFocus();
            lname.setError("Input Lastname");
        }
        else if(username.length()==0){
            username.requestFocus();
            username.setError("Input Username");
        }

        else if(password.length()==0){
            password.requestFocus();
            password.setError("Input Password");
        }
        else if(email.length()==0){
            email.requestFocus();
            email.setError("Input Email");
        }
        else {

            postData.put("username", username.getText().toString());
            postData.put("lastname", lname.getText().toString());
            postData.put("firstname", fname.getText().toString());
            postData.put("password", password.getText().toString());
            postData.put("gender", gender.toString());
            postData.put("email", email.getText().toString());

            PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
            task.execute(add.getIpaddress() + "Escape/index.php/mobileuser/mobile_reg");
            //task.execute("http://10.0.3.2/ESCMOBILE/register.php");
        }
    }

    @Override
    public void processFinish(String result) {
        if(result.equals("Registration Success!")){
            //Toast.makeText(this, "Registration Success!", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            Toast.makeText(this, "Registration failed!", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }


}
