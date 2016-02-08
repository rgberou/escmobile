package com.android.esc.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.controllers.PlaceParse;
import com.android.esc.controllers.TrackerParse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.Locale;

public class GetDirection extends Activity {
    Geocoder geocoder;
    AutoCompleteTextView firstT, secondT;
    PlacesTask placesTask;
    ParseTask parserTask;
    String totalDistance = " ";
    String totalDuration = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_direction);

        ArrayList<LatLng> locList = new ArrayList<LatLng>();

        firstT = (AutoCompleteTextView) findViewById(R.id.firstF);
        firstT.setThreshold(1);
        secondT = (AutoCompleteTextView) findViewById(R.id.secondF);
        secondT.setThreshold(1);

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


    public void btnClick(View a)
    {
        EditText startL = (EditText) findViewById(R.id.firstF);
        String firstL = startL.getText().toString();
        //dFrom = capitalizeFirstLetter(firstL);

        EditText endL = (EditText) findViewById(R.id.secondF);
        String lastL = endL.getText().toString();
        //dTo = capitalizeFirstLetter(lastL);



        List<Address> firstlist = null;
        List<Address> lastlist = null;

        if (!firstL.isEmpty() || !lastL.isEmpty()) {

            Geocoder gcode = new Geocoder(this);
            try {
                firstlist = gcode.getFromLocationName(firstL, 1);
                lastlist = gcode.getFromLocationName(lastL, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address addressF = firstlist.get(0);
            // dFrom = ((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
            Address addressL = lastlist.get(0);
            //dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();
                if (addressF.getLocality().equals("Mandaue City")&&addressL.getLocality().equals("Mandaue City")) {
                    LatLng Flatlng = new LatLng(addressF.getLatitude(), addressF.getLongitude());
                    LatLng Llatlng = new LatLng(addressL.getLatitude(), addressL.getLongitude());
                    Toast.makeText(getApplicationContext(), "Mandaue ni ", Toast.LENGTH_SHORT).show();
                    //url
                    Intent i = new Intent(this, MapsActivity.class);
                    startActivity(i);
                    String url = getDirectionsUrl(Flatlng, Llatlng);

                    //zoom lvl
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    builder.include(Flatlng);
                    builder.include(Llatlng);

                    LatLngBounds bounds = builder.build();


                    //line
                   // DownloadTask downloadTask = new DownloadTask();
                    //downloadTask.execute(url);

                }
                else {
                    Toast.makeText(getApplicationContext(), "Places must be not be outside Mandaue City", Toast.LENGTH_LONG).show();
                    startL.getText().clear();
                    endL.getText().clear();
                }

        } else {
            Context context = getApplicationContext();
            String textk = " Fill out Both Forms ";
            Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
        }

    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                TrackerParse parser = new TrackerParse();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            String fPoint = "";
            String lPoint = "";

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    Geocoder geocoder;
                    List<Address> addresses=null;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //ArrayList<GeoDirections> coordinate = new ArrayList<GeoDirections>();
                    //String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    //String city = addresses.get(0).getLocality();
                    //points.add(position);
                    //GeoDirections geo=new GeoDirections(lat,lng,address+city);
                    //coordinate.add(geo);



                    /*for(int ind=0;ind<coordinate.toArray().length;ind++){

                        String add= String.valueOf(coordinate.get(ind).getGet_address());
                        Toast.makeText(getApplicationContext(),""+add, Toast.LENGTH_LONG).show();
                    }*/
                    //coordinate.add(coord);

                }


                totalDistance = distance;
                totalDuration = duration;

            }

        }
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





}
