package com.android.esc.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rg on 1/15/2016.
 */
public class Users {
    @SerializedName("userid")
    public String userid;

    @SerializedName("account_username")
    public String account_username;

    @SerializedName("account_lastname")
    public String account_lastname;

    @SerializedName("account_firstname")
    public String account_firstname;

    @SerializedName("account_email")
    public String account_email;

    @SerializedName("account_type")
    public String account_type;



}
