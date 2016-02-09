package com.android.esc.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.addholder.AddressHolder;
import com.android.esc.controllers.PlaceParse;
import com.android.esc.controllers.TrackerParse;
import com.android.esc.model.Posts;
import com.android.esc.model.Routes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
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
import java.util.Locale;

//import com.android.esc.controllers.ChurvaFilter;

public class MapsActivity extends FragmentActivity implements AsyncResponse {

    GoogleMap map;
    String puj;
    String finalDistance = "";
    String finalDuration = "";
    String totalDistance = "";
    String totalDuration = "";
    String totalDistanceB = "";
    String totalDurationB = "";
    String totalDistanceC = "";
    String totalDurationC = "";
    String totalDistanceD = "";
    String totalDurationD = "";
    String dFrom = " ";
    String dTo = " ";
    String selectedMode = "";
    String aa, b, c, d;
    String[] coord;
    double lat, lng;

    AutoCompleteTextView firstT, secondT;
    PlacesTask placesTask;
    ParseTask parserTask;
    Marker marker;
    AddressHolder add=new AddressHolder();


    private LatLng mand = new LatLng(10.32361, 123.92222);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        PostResponseAsyncTask task = new PostResponseAsyncTask(this);
        task.execute(add.getIpaddress() + "Escape/index.php/mobileuser/TrafficMarker");



        ArrayList<LatLng> locList = new ArrayList<LatLng>();

        firstT = (AutoCompleteTextView) findViewById(R.id.first);
        firstT.setThreshold(1);
        secondT = (AutoCompleteTextView) findViewById(R.id.editText2);
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

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mand, 15.5f));

        map.setIndoorEnabled(true);

        map.setBuildingsEnabled(true);


    }

//-------------------------------------------Url's For Man's Sake -------------------------------------------

    public void back(View v){
        finish();

    }

    //Deafault Url by Google-sama u B*tch
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

    //
    private String transitUrl(LatLng origin, LatLng dest) {

        // Origin
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String mode = "mode=transit";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


    //----------------------------------Alternative Url's------------------------


    private String getDirectionsAltUrl(LatLng origin, LatLng dest) {

        // Origin
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";

        String alter = "alternatives=true";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + alter;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    //
    private String transitAltUrl(LatLng origin, LatLng dest) {

        // Origin
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        String sensor = "sensor=false";

        String mode = "mode=transit";

        String alter = "alternatives=true";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + alter;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


//-------------------------------------------------------End URL's----------------------------------------------

    /** A method to download json data from url */
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

    @Override
    public void processFinish(String result) {
        try {
            ArrayList<Posts> postList =
                    new JsonConverter<Posts>().toArrayList(result, Posts.class);
            for(Posts value: postList) {
                double lat = Double.parseDouble(value.lat);
                double lng = Double.parseDouble(value.lng);
                LatLng latlng = new LatLng(lat,lng);


                map.addMarker(new MarkerOptions().position(latlng).title(value.caption).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map)));

            }
        }catch (Exception e){

        }
        try {
            ArrayList<Routes> routesList = new JsonConverter<Routes>().toArrayList(result, Routes.class);
            for(Routes value: routesList) {
                Toast.makeText(getApplicationContext(),value.PUJ_id, Toast.LENGTH_LONG).show();
                puj = (value.PUJ_id);
                Toast.makeText(getApplicationContext(), puj, Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), puj, Toast.LENGTH_LONG).show();
        }








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
            Geocoder geocoder;
            List<Address> addresses=null;

            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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
                    TrackerParse trackerParse = new TrackerParse();
                    fPoint = trackerParse.getOri();


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);



                }


                totalDistance = distance;
                totalDuration = duration;
                finalDistance = distance;
                finalDuration = duration;
                aa = totalDuration;



                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);


                //lineOptions.width(3);
                lineOptions.color(Color.CYAN).geodesic(true);
            }

            // Drawing polyline in the Google Map for the i-th route
            try{
                map.addPolyline(lineOptions);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Unable to connect to Server", Toast.LENGTH_LONG).show();
            }
        }
    }
    //---------------------------------------------------------------------------------------------------------------------

    // Fetches data from url passed
    private class DownloadTaskC extends AsyncTask<String, Void, String> {

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

            ParserTaskC parserTask = new ParserTaskC();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTaskC extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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
                    TrackerParse trackerParse = new TrackerParse();
                    fPoint = trackerParse.getOri();


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                    //Toast.makeText(getApplicationContext(),points.toArray().length, Toast.LENGTH_LONG).show();
                }


                totalDistanceC = distance;
                totalDurationC = duration;
                finalDistance = distance;
                finalDuration = duration;
                c = totalDurationC;

                //Toast.makeText(getApplicationContext(),points.toArray().length, Toast.LENGTH_LONG).show();

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);


                //lineOptions.width(3);
                lineOptions.color(Color.BLACK).geodesic(true);
            }

            // Drawing polyline in the Google Map for the i-th route
            try{
                map.addPolyline(lineOptions);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Unable to connect to Server", Toast.LENGTH_LONG).show();
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------------
// Fetches data from url passed
    private class DownloadTaskB extends AsyncTask<String, Void, String> {

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

            ParserTaskB parserTask = new ParserTaskB();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTaskB extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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
                coord = new String[path.size()];
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
                    TrackerParse trackerParse = new TrackerParse();
                    fPoint = trackerParse.getOri();


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                    //String city = addresses.get(0).getLocality();

                    //Toast.makeText(getApplicationContext(),points.toArray().length, Toast.LENGTH_LONG).show();
                }




                totalDistanceB = distance;
                totalDurationB = duration;
                finalDistance = distance;
                finalDuration = duration;

                b = totalDurationB;

                //Toast.makeText(getApplicationContext(),points.toArray().length, Toast.LENGTH_LONG).show();

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);


                //lineOptions.width(3);
                lineOptions.color(Color.BLUE).geodesic(true);
            }

            // Drawing polyline in the Google Map for the i-th route
            try{
                map.addPolyline(lineOptions);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Unable to connect to Server", Toast.LENGTH_LONG).show();
            }
        }
    }

    //---------------------------------------------------------------------------------------

    // Fetches data from url passed
    private class DownloadTaskD extends AsyncTask<String, Void, String> {

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

            ParserTaskD parserTask = new ParserTaskD();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTaskD extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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
                    TrackerParse trackerParse = new TrackerParse();
                    fPoint = trackerParse.getOri();


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                    //Toast.makeText(getApplicationContext(),points.toArray().length, Toast.LENGTH_LONG).show();
                }


                totalDistanceD = distance;
                totalDurationD = duration;
                finalDistance = distance;
                finalDuration = duration;

                d = totalDurationD;




                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);


                //lineOptions.width(3);
                lineOptions.color(Color.GREEN).geodesic(true);
            }

            // Drawing polyline in the Google Map for the i-th route
            try{
                map.addPolyline(lineOptions);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Unable to connect to Server", Toast.LENGTH_LONG).show();
            }

        }
    }



    //-------------------- Buttons Here -----------------------------------------------

    //button click Search
    public void Bsearch(View a) {

        EditText startL = (EditText) findViewById(R.id.first);
        String firstL = startL.getText().toString();


        EditText endL = (EditText) findViewById(R.id.editText2);
        String lastL = endL.getText().toString();
        dFrom = firstL;
        dTo = lastL;

        map.clear();


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
            try {
                Address addressF = firstlist.get(0);
                // dFrom = ((addressF.getPremises() == null) ? "" : addressF.getPremises())+","+((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                //dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();

                LatLng Flatlng = new LatLng(addressF.getLatitude(), addressF.getLongitude());
                LatLng Llatlng = new LatLng(addressL.getLatitude(), addressL.getLongitude());

                map.addMarker(new MarkerOptions().position(Flatlng).title(firstL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                map.addMarker(new MarkerOptions().position(Llatlng).title(lastL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                //url
                String url = getDirectionsUrl(Flatlng, Llatlng);
                String urlB = getDirectionsAltUrl(Flatlng, Llatlng);
                String urlC = transitUrl(Flatlng, Llatlng);
                String urlD = transitAltUrl(Flatlng, Llatlng);
                //zoom lvl
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                builder.include(Flatlng);
                builder.include(Llatlng);

                LatLngBounds bounds = builder.build();

                int padding = 10; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                map.animateCamera(cu);

                selectedMode = " All ";
                //line
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);

                DownloadTaskB downloadTaskB = new DownloadTaskB();
                downloadTaskB.execute(urlB);

                DownloadTaskC downloadTaskC = new DownloadTaskC();
                downloadTaskC.execute(urlC);

                DownloadTaskD downloadTaskD = new DownloadTaskD();
                downloadTaskD.execute(urlD);
            }catch (Exception E)
            {

            }

        } else {
            Context context = getApplicationContext();
            String textk = " Fill out Both Forms ";
            Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
        }

    }

    public void infoSearch(View a) {
        try {
            EditText startL = (EditText) findViewById(R.id.first);
            String firstL = startL.getText().toString();

            EditText endL = (EditText) findViewById(R.id.editText2);
            String lastL = endL.getText().toString();

            List<Address> firstlist = null;
            List<Address> lastlist = null;

            Geocoder gcode = new Geocoder(this);
            try {
                firstlist = gcode.getFromLocationName(firstL, 1);
                lastlist = gcode.getFromLocationName(lastL, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address addressF = firstlist.get(0);
            Address addressL = lastlist.get(0);

            LatLng Flatlng = new LatLng(addressF.getLatitude(), addressF.getLongitude());
            LatLng Llatlng = new LatLng(addressL.getLatitude(), addressL.getLongitude());

            //ChurvaFilter c = new ChurvaFilter();

            //  String A=c.filterA(Flatlng, Llatlng);
            //  String B=c.filterB(Flatlng, Llatlng);
            //  String C=c.filterC(Flatlng, Llatlng);
            // String D=c.filterD(Flatlng, Llatlng);




            Intent i = new Intent(MapsActivity.this, infoDetails.class);
            i.putExtra("distance", finalDistance);
            i.putExtra("duration", finalDuration);
            i.putExtra("mode", selectedMode);
            i.putExtra("start", dFrom);
            i.putExtra("end", dTo);
            i.putExtra("jeep", puj);

            // i.putExtra("A", A);
            // i.putExtra("B", B);
            // i.putExtra("C", C);
            //i.putExtra("D", D);

            startActivity(i);
        }catch (Exception e){

        }


    }

    public void routeA(View a) throws IOException {

        EditText startL = (EditText) findViewById(R.id.first);
        String firstL = startL.getText().toString();


        EditText endL = (EditText) findViewById(R.id.editText2);
        String lastL = endL.getText().toString();
        dFrom = firstL;
        dTo = lastL;

        map.clear();


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
            try {
                Address addressF = firstlist.get(0);
                // dFrom = ((addressF.getPremises() == null) ? "" : addressF.getPremises())+","+((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                //dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();
                if(addressF.getLocality().equals("Mandaue City")&&addressL.getLocality().equals("Mandaue City")){
                    LatLng Flatlng = new LatLng(addressF.getLatitude(), addressF.getLongitude());
                    LatLng Llatlng = new LatLng(addressL.getLatitude(), addressL.getLongitude());

                    lat = addressF.getLatitude();
                    lng = addressF.getLongitude();


                    map.addMarker(new MarkerOptions().position(Flatlng).title(firstL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    map.addMarker(new MarkerOptions().position(Llatlng).title(lastL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    //url
                    String url = getDirectionsUrl(Flatlng, Llatlng);

                    //zoom lvl
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    builder.include(Flatlng);
                    builder.include(Llatlng);

                    LatLngBounds bounds = builder.build();

                    int padding = 10; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    map.animateCamera(cu);

                    selectedMode = " A ";
                    //line
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);

                    PostResponseAsyncTask task = new PostResponseAsyncTask(this);
                    task.execute(add.getIpaddress() + "Escape/index.php/mobileuser/fetchRoutes/"+addressF.getLatitude()+"/"+addressF.getLongitude());

                }else{
                    Toast.makeText(getApplicationContext(), "Places limited only for Mandaue City", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Context context = getApplicationContext();
            String textk = " Fill out Both Forms ";
            Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
        }

    }



    public void routeB(View a)
    {
        EditText startL = (EditText) findViewById(R.id.first);
        String firstL = startL.getText().toString();
        //dFrom = capitalizeFirstLetter(firstL);

        EditText endL = (EditText) findViewById(R.id.editText2);
        String lastL = endL.getText().toString();
        //dTo = capitalizeFirstLetter(lastL);

        dFrom = firstL;
        dTo = lastL;

        map.clear();


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
            try {
                Address addressF = firstlist.get(0);
                // dFrom = ((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                //dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();

                LatLng Flatlng = new LatLng(addressF.getLatitude(), addressF.getLongitude());
                LatLng Llatlng = new LatLng(addressL.getLatitude(), addressL.getLongitude());

                map.addMarker(new MarkerOptions().position(Flatlng).title(firstL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                map.addMarker(new MarkerOptions().position(Llatlng).title(lastL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                //url
                String url = getDirectionsAltUrl(Flatlng, Llatlng);

                //zoom lvl
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                builder.include(Flatlng);
                builder.include(Llatlng);

                LatLngBounds bounds = builder.build();

                int padding = 10; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                map.animateCamera(cu);

                selectedMode = " B ";

                //line
                DownloadTaskB downloadTask = new DownloadTaskB();
                downloadTask.execute(url);
            }catch (Exception e)
            {

            }

        } else {
            Context context = getApplicationContext();
            String textk = " Fill out Both Forms ";
            Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
        }
    }

    public void routeC(View a)
    {
        EditText startL = (EditText) findViewById(R.id.first);
        String firstL = startL.getText().toString();
        //dFrom = capitalizeFirstLetter(firstL);

        EditText endL = (EditText) findViewById(R.id.editText2);
        String lastL = endL.getText().toString();
        //dTo = capitalizeFirstLetter(lastL);

        dFrom = firstL;
        dTo = lastL;

        map.clear();


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
            try {
                Address addressF = firstlist.get(0);
                // dFrom = ((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                //  dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();

                LatLng Flatlng = new LatLng(addressF.getLatitude(), addressF.getLongitude());
                LatLng Llatlng = new LatLng(addressL.getLatitude(), addressL.getLongitude());

                map.addMarker(new MarkerOptions().position(Flatlng).title(firstL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                map.addMarker(new MarkerOptions().position(Llatlng).title(lastL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                //url
                String url = transitUrl(Flatlng, Llatlng);

                //zoom lvl
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                builder.include(Flatlng);
                builder.include(Llatlng);

                LatLngBounds bounds = builder.build();

                int padding = 10; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                map.animateCamera(cu);

                selectedMode = " C ";

                //line
                DownloadTaskC downloadTask = new DownloadTaskC();
                downloadTask.execute(url);
            }catch (Exception e)
            {

            }

        } else {
            Context context = getApplicationContext();
            String textk = " Fill out Both Forms ";
            Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
        }
    }

    public void routeD(View a)
    {
        EditText startL = (EditText) findViewById(R.id.first);
        String firstL = startL.getText().toString();


        EditText endL = (EditText) findViewById(R.id.editText2);
        String lastL = endL.getText().toString();
        dFrom = firstL;
        dTo = lastL;



        map.clear();


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
            try {
                Address addressF = firstlist.get(0);
                // dFrom = ((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                // dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();
                LatLng Flatlng = new LatLng(addressF.getLatitude(), addressF.getLongitude());
                LatLng Llatlng = new LatLng(addressL.getLatitude(), addressL.getLongitude());

                map.addMarker(new MarkerOptions().position(Flatlng).title(firstL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                map.addMarker(new MarkerOptions().position(Llatlng).title(lastL).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                //url
                String url = transitAltUrl(Flatlng, Llatlng);

                //zoom lvl
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                builder.include(Flatlng);
                builder.include(Llatlng);

                LatLngBounds bounds = builder.build();

                int padding = 10; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                map.animateCamera(cu);

                selectedMode = " D ";

                //line
                DownloadTaskD downloadTask = new DownloadTaskD();
                downloadTask.execute(url);
            }catch (Exception e)
            {

            }

        } else {
            Context context = getApplicationContext();
            String textk = " Fill out Both Forms ";
            Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
        }
    }

    private class PlacesTask extends AsyncTask<String, Void, String>{

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
                Log.d("Background Task",e.toString());
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
