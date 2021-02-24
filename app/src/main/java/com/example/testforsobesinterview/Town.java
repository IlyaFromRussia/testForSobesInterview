package com.example.testforsobesinterview;
/*
 * author Lobov-IR
 */


public class Town {
    private int id;
    private String name;
    private String latitude;
    private String longitude;
    private int lastTown;

    public Town(String name, int lastTown, int id){
        this.name = name;
        latitude = "47.42509761005917";
        longitude = "40.110525369348494";
        this.lastTown = lastTown;
        this.id = id;
    }

    // конструктор для чтения из базы.
    public Town(String name, int lastTown, int id, String latitude, String longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastTown = lastTown;
        this.id = id;
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
    public int isLast(){return lastTown;}
    public int getId(){return id;}
    public void setLastTown(int lastTown){
        this.lastTown = lastTown;
    }
    public void setLast(int i){
        this.lastTown = i;
    }
}