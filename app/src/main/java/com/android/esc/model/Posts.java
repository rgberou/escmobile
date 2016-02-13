package com.android.esc.model;

import com.google.gson.annotations.SerializedName;


public class Posts {

    @SerializedName("image_id")
    public String image_id;

    @SerializedName("image_name")
    public String image_name;

    @SerializedName("image_path")
    public String image_path;

    @SerializedName("caption")
    public String caption;

    @SerializedName("lat")
    public String lat;

    @SerializedName("lng")
    public String lng;

    @SerializedName("account_username")
    public String account_username;

    @SerializedName("date_posted")
    public String date_posted;

    @SerializedName("dist_type")
    public String dist_type;


}
