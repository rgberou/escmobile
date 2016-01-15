package com.android.esc;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rg on 1/15/2016.
 */
public class Users {
    @SerializedName("userid")
    public String userid;

    @SerializedName("account_firstname")
    public String account_firstname;

    @SerializedName("account_lastname")
    public String account_lastname;

    @SerializedName("caption")
    public String caption;

}
