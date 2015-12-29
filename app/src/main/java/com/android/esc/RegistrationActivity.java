package com.android.esc;

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
                }else{
                    gender = "F";
                }

            }
        });


    }

    @Override
    public void onClick(View view) {
        HashMap postData = new HashMap();
        //postData.put("escape", "esc_mobile");
        postData.put("username", username.getText().toString());
        postData.put("lastname", lname.getText().toString());
        postData.put("firstname", fname.getText().toString());
        postData.put("password", password.getText().toString());
        postData.put("gender", gender.toString());
        postData.put("email", email.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
        task.execute("http://10.0.3.2/ESCMOBILE/register.php");
    }

    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            Toast.makeText(this, "Registration Success!", Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    /*public void user_register(View arg0){
        String method = "register";
        String u_name = username.getText().toString();
        String f_name = fname.getText().toString();
        String l_name = lname.getText().toString();
        String pass = password.getText().toString();
        String gen = gender.toString();
        String e_add = email.getText().toString();



        if(f_name.length()==0){
            fname.requestFocus();
            fname.setError("Input Firstname");
        }
        else if(l_name.length()==0){
            lname.requestFocus();
            lname.setError("Input Lastname");
        }
        else if(u_name.length()==0){
            username.requestFocus();
            username.setError("Input Username");
        }

        else if(pass.length()==0){
            password.requestFocus();
            password.setError("Input Password");
        }
        else if(e_add.length()==0){
            email.requestFocus();
            email.setError("Input Email");
        }
        else{
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(method, u_name, l_name, f_name, pass, gen, e_add);
            finish();
        }

    }*/
}
