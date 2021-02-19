package com.example.testforsobesinterview;
/*
 * author Lobov-IR
 */

import android.location.Location;

public class Town {
    private String name;
    private Location location;

    public Town(String name){
        this.name = name;
        location = new Location("47.42509761005917, 40.110525369348494");
    }

    public double getLongitude(){
        return location.getLongitude();
    }
    public double getLatitude(){
        return location.getLatitude();
    }
}