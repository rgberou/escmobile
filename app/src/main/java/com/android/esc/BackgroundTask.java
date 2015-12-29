package com.android.esc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


@SuppressLint("NewApi")
public class BackgroundTask extends AsyncTask<String, Void, String> {
	AlertDialog alertdialog;
	Context ctx;

	BackgroundTask(Context ctx){
		this.ctx = ctx;
	}

	@Override
	protected void onPreExecute() {
		alertdialog = new AlertDialog.Builder(ctx).create();
		alertdialog.setTitle("USER INFORMATION");
	}

	@Override
	protected String doInBackground(String... params) {
		String register_user = "http://10.0.3.2/ESCMOBILE/register.php";
		String method = params[0];

		if(method.equals("register")){
			String username = params[1];
			String lname = params[2];
			String fname = params[3];
			String password = params[4];
			String gender = params[5];
			String email = params[6];

			try {
				URL url = new URL(register_user);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.setDoOutput(true);
				OutputStream OS = httpURLConnection.getOutputStream();
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
				String data = URLEncoder.encode("username","UTF-8") +"="+URLEncoder.encode(username,"UTF-8")+"&"+
						URLEncoder.encode("lname","UTF-8") +"="+URLEncoder.encode(lname,"UTF-8")+"&"+
						URLEncoder.encode("fname","UTF-8") +"="+URLEncoder.encode(fname,"UTF-8")+"&"+
						URLEncoder.encode("password","UTF-8") +"="+URLEncoder.encode(password,"UTF-8")+"&"+
						URLEncoder.encode("gender","UTF-8") +"="+URLEncoder.encode(gender,"UTF-8")+"&"+
						URLEncoder.encode("email","UTF-8") +"="+URLEncoder.encode(email,"UTF-8");
				bufferedWriter.write(data);
				bufferedWriter.flush();
				bufferedWriter.close();
				OS.close();
				InputStream IS = httpURLConnection.getInputStream();
				IS.close();

				return "Registration Success";


			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}


		}


		return null;
	}


	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override

	protected void onPostExecute(String result) {
		if(result.equals("Registration Success")) {
			Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
		}
		else{
			alertdialog.setMessage(result);
			alertdialog.show();
		}
	}


}
