package com.android.esc;

/**
 * Created by Rg on 1/3/2016.
 */
public class User {
    private String lastname;
    private String firstname;

    public User() {
        lastname = null;
        firstname = null;
    }
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
