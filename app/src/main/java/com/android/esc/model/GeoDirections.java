package com.android.esc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rg on 2/7/2016.
 */
public class GeoDirections implements Parcelable {
    private double lat;
    private double lng;
    private String get_address;

    public GeoDirections() {

    }
    public GeoDirections(double lat, double lng,String add) {
        this.lat=lat;
        this.lng=lng;
        this.get_address=add;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getGet_address() {
        return get_address;
    }

    public void setGet_address(String get_address) {
        this.get_address = get_address;
    }

    private GeoDirections(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();

    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeDouble(lat);
        dest.writeDouble(lng);


    }

    public static final Parcelable.Creator<GeoDirections> CREATOR = new Parcelable.Creator<GeoDirections>() {
        public GeoDirections createFromParcel(Parcel in) {
            return new GeoDirections(in);
        }

        public GeoDirections[] newArray(int size) {
            return new GeoDirections[size];

        }
    };

    // all get , set method
}

