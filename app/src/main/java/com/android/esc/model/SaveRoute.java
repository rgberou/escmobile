package com.android.esc.model;

/**
 * Created by Rg on 2/16/2016.
 */
public class SaveRoute {

    private String PUJ_id;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPUJ_id() {
        return PUJ_id;
    }

    public void setPUJ_id(String PUJ_id) {
        this.PUJ_id = PUJ_id;
    }

    public SaveRoute(String PUJ_id) {
        this.PUJ_id = PUJ_id;
    }
}
