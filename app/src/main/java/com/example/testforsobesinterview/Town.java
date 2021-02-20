package com.example.testforsobesinterview;
/*
 * author Lobov-IR
 */

import android.location.Location;

public class Town {
    private String name;
    private String latitude;
    private String longitude;

    public Town(String name){
        this.name = name;
        latitude = "47.42509761005917";
        longitude = "40.110525369348494";
    }

    public String getLongitude(){
        return longitude;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getName(){
        return name;
    }
}