package com.android.esc.views;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.addholder.AddressHolder;
import com.android.esc.controllers.PlaceParse;
import com.android.esc.model.Routes;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetOrigin extends Activity implements AsyncResponse {

    AutoCompleteTextView firstT, secondT;
    PlacesTask placesTask;
    ParseTask parserTask;
    AddressHolder add=new AddressHolder();
    //ArrayList<Routes> routesList =  new ArrayList<>();
    String puj2[];
    ArrayList<String> pujlist= new ArrayList<String>();
    String puj;
    String jproute="";
    String dis="", mode;
    Spinner choice;

    double lng;
    double lat;
    StringBuffer sb=new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_get_origin);

        firstT = (AutoCompleteTextView) findViewById(R.id.firstF);
        firstT.setThreshold(1);
        secondT = (AutoCompleteTextView) findViewById(R.id.secondF);
        secondT.setThreshold(1);

        choice = (Spinner)findViewById(R.id.choice);
        choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                mode = choice.getSelectedItem().toString();
                Toast.makeText(GetOrigin.this, mode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        firstT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secondT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    public void back(View v){
        finish();

    }



    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            //String key = "key=AIzaSyBZYAJlRp_k7AXKs3UWwi30MCm_jmivY8Y";
            String key = "key=AIzaSyAT2PCnHit0zW7tl7NgyVS0EryMR0O-QQ4";
            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            String limiter = "components=country:PH";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key+"&"+limiter;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParseTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    private class ParseTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceParse placeJsonParser = new PlaceParse();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            firstT.setAdapter(adapter);
            secondT.setAdapter(adapter);

        }
    }
    @Override
    public void processFinish(String result) {
        sb.setLength(0);
        ArrayList<Routes> routesLists  =
                new JsonConverter<Routes>().toArrayList(result, Routes.class);
        for (Routes value: routesLists) {

            //puj2=new String[routesList.size()];
            if(pujlist.contains(value.PUJ_id)){

            }else {
                pujlist.add(value.PUJ_id);
            }

        }
        jproute = String.valueOf(sb.append(pujlist + " "));
        dis = jproute.replace("[", "").replace("]", "");

        //jproute = String.valueOf(sb.append(pujlist));
        Intent i = new Intent(this,MapsActivity.class);
        i.putExtra("startPoint",firstT.getText().toString());
        i.putExtra("endPoint", secondT.getText().toString());
        i.putExtra("jeep",dis.toString());
        i.putExtra("latdest",lat);
        i.putExtra("lngdest",lng);
        startActivity(i);
        finish();
    }



    public void btnClick(View v)
    {

        String firstL = firstT.getText().toString();
        String lastL = secondT.getText().toString();

        List<Address> firstlist = null;
        List<Address> lastlist = null;

        Geocoder gcode = new Geocoder(this);
        try {
            firstlist = gcode.getFromLocationName(firstL, 1);
            lastlist = gcode.getFromLocationName(lastL, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            Address addressF = firstlist.get(0);
            Address addressL = lastlist.get(0);

        //    Toast.makeText(getApplicationContext(), addressF.getLatitude()+" - "+ addressF.getLongitude(), Toast.LENGTH_LONG).show();

            if(addressF.getLocality().equals("Mandaue City")&&addressL.getLocality().equals("Mandaue City")){
               // HashMap postData = new HashMap();

             /*   postData.put("flat", addressF.getLatitude());
                postData.put("flng", addressF.getLongitude());
                postData.put("llat", addressL.getLatitude());
                postData.put("llng", addressF.getLongitude()); */


                PostResponseAsyncTask taskmark = new PostResponseAsyncTask(this);
                taskmark.execute(add.getIpaddress() + "Escape/index.php/mobileuser/fetchRoutes/"+ addressF.getLatitude() + "/" + addressF.getLongitude()+"/" + addressL.getLatitude() + "/" + addressL.getLongitude());
                //taskmark.execute(add.getIpaddress() + "Escape/index.php/mobileuser/fetchRoutes");
                lat=addressL.getLatitude();
                lng=addressL.getLongitude();
                Toast.makeText(getApplicationContext(), addressF.getLatitude()+" "+addressF.getLongitude(), Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(), "Places limited only for Mandaue City", Toast.LENGTH_LONG).show();
            }

        }catch (Exception err){
            Toast.makeText(getApplicationContext(), "Unable to connect to Server", Toast.LENGTH_LONG).show();
        }






    }

}
