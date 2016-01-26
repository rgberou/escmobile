package com.android.esc.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.esc.R;
import com.android.esc.controllers.TrackerParse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    GoogleMap map;
    Spinner spinner;
    //Info
    String totalDistance = " ";
    String totalDuration = " ";
    String dFrom = " ";
    String dTo = " ";
    String selectedMode = "";

    private LatLng mand = new LatLng(10.32361, 123.92222);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.choice, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        ArrayList<LatLng> locList = new ArrayList<LatLng>();

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

        map.setTrafficEnabled(true);// Enable Traffic T_T donno why

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mand, 15.5f));

        map.setIndoorEnabled(true);

        map.setBuildingsEnabled(true);


    }

//-------------------------------------------Url's For Man's Sake -------------------------------------------

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

    //---------------------------------4 Functions -------------------
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        String item = myText.getText().toString();

        EditText startL = (EditText) findViewById(R.id.first);
        String firstL = startL.getText().toString();
        //dFrom = capitalizeFirstLetter(firstL);

        EditText endL = (EditText) findViewById(R.id.editText2);
        String lastL = endL.getText().toString();
        //dTo = capitalizeFirstLetter(lastL);

        if (position == 0) {
            Context context = getApplicationContext();
            String textk = " Fill out Both Forms \n Then Select";
            Toast.makeText(context, textk, Toast.LENGTH_LONG).show();

        } else if (position == 1) {
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

                Address addressF = firstlist.get(0);
                dFrom = ((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();

                LatLng Flatlng = new LatLng(addressF.getLatitude(), addressF.getLongitude());
                LatLng Llatlng = new LatLng(addressL.getLatitude(), addressL.getLongitude());

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

                selectedMode = " Driving ";
                //line
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);


            } else {
                Context context = getApplicationContext();
                String textk = " Fill out Both Forms ";
                Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
            }
        } else if (position == 2) {
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

                Address addressF = firstlist.get(0);
                dFrom = ((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();

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

                selectedMode = " Driving ( Alternative ) ";

                //line
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);


            } else {
                Context context = getApplicationContext();
                String textk = " Fill out Both Forms ";
                Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
            }
        } else if (position == 3) {
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

                Address addressF = firstlist.get(0);
                dFrom = ((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();

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

                selectedMode = " Transit ";

                //line
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);


            } else {
                Context context = getApplicationContext();
                String textk = " Fill out Both Forms ";
                Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
            }
        } else if (position == 4) {
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

                Address addressF = firstlist.get(0);
                dFrom = ((addressF.getThoroughfare() == null) ? "" : addressF.getThoroughfare()) + "," + addressF.getLocality();
                Address addressL = lastlist.get(0);
                dTo = ((addressL.getThoroughfare() == null) ? "" : addressL.getThoroughfare()) + "," + addressL.getLocality();
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

                selectedMode = " Transit ( Alternative ) ";

                //line
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);


            } else {
                Context context = getApplicationContext();
                String textk = " Fill out Both Forms ";
                Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    }/*else if(j==3){ // Get duration from the list
                        lPoint = (String)point.get("end_address");
                        continue;
                    }else if(j==4){ // Get duration from the list
                        fPoint = (String)point.get("start_address");
                        continue;
                    }*/
                    TrackerParse trackerParse = new TrackerParse();
                    fPoint = trackerParse.getOri();


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                /*
                Context context = getApplicationContext();
                String textk = "Distance:"+distance + ", Duration:"+duration;
                Toast.makeText(context, textk, Toast.LENGTH_LONG).show();
                */

                totalDistance = distance;
                totalDuration = duration;
                //dFrom =fPoint;
                //dTo=lPoint;


                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(3);
                lineOptions.color(Color.CYAN).geodesic(true);
            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

    //-------------------- Buttons Here -----------------------------------------------

    //button click Search
    public void Bsearch(View a) {

        map.clear();

        EditText startL = (EditText) findViewById(R.id.first);
        String firstL = startL.getText().toString();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location myLokasyun = locationManager.getLastKnownLocation(provider);

        double lat = myLokasyun.getLatitude();

        double lon = myLokasyun.getLongitude();

        LatLng latlng = new LatLng(lat,lon);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.5f));

        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(" You're Here "));

        String cityName = null;

        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(lat,
                    lon, 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());
            cityName = addresses.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = lat + "\n" + lon + "\n\n My Currrent City is: "
                +cityName;

        Context context = getApplicationContext();

        Toast.makeText(context,s, Toast.LENGTH_LONG).show();


    }

    public void infoSearch(View a) {


        Intent i = new Intent(MapsActivity.this, infoDetails.class);
        i.putExtra("distance", totalDistance);
        i.putExtra("duration", totalDuration);
        i.putExtra("mode", selectedMode);
        i.putExtra("start", dFrom);
        i.putExtra("end", dTo);
        startActivity(i);

    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
