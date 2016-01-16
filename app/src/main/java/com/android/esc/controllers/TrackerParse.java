package com.android.esc.controllers;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackerParse {

    String ori,dis;

    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        JSONObject jDistance = null;
        JSONObject jDuration = null;
        JSONObject jStart = null;
        JSONObject jEnd = null;

        try {

            jRoutes = jObject.getJSONArray("routes");



            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){

                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();



                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Getting distance from the json data */
                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    HashMap<String, String> hmDistance = new HashMap<String, String>();
                    hmDistance.put("distance", jDistance.getString("text"));

                    /** Getting duration from the json data */
                    jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    HashMap<String, String> hmDuration = new HashMap<String, String>();
                    hmDuration.put("duration", jDuration.getString("text"));
/*
                    jStart = ((JSONObject) jLegs.get(i)).getJSONObject("start_address");
                    HashMap<String, String> starts = new HashMap<String, String>();
                    starts.put("start", jStart.getString("start_address"));

                    Log.v("TrackerParse","Added a new Path and paths size is : " + jStart.getString("start_address"));
*/
                    ///  Start Address from the json data
                    jStart = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs").getJSONObject(0);
                    HashMap<String, String> fPoint = new HashMap<String, String>();
                    fPoint.put("start_address", jStart.getString("start_address"));
                    String s =jStart.getString("start_address");
                    setOri(s);


/*
                    // End Address from the json data
                    jEnd = ((JSONObject) jLegs.get(j)).getJSONObject("end_address");
                    HashMap<String, String> lPoint = new HashMap<String, String>();
                    lPoint.put("end_address", jEnd.getString("end_address"));
*/
                    //adding items


                    path.add(hmDistance); // distance

                    path.add(hmDuration); // duration

                    //path.add(fPoint); //firstpoint

                    // path.add(lPoint); // lastpoint


                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");


                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return routes;
    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public String getOri() {
        return ori;
    }

    public String getDis() {
        return dis;
    }

    public void setOri(String ori) {
        this.ori = ori;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }
}