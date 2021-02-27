package com.example.testforsobesinterview.database;
/*
 * author Lobov-IR
 */

public class WeatherForDB {
    private Integer id;
    private String town;
    private String date;
    private String day;
    private Double temp;
    private String type;

    public WeatherForDB(Integer id, String town, String date, String day, Double temp, String type){
        this.id=id;
        this.town=town;
        this.date=date;
        this.day=day;
        this.temp=temp;
        this.type=type;
    }

    public Integer getId() {
        return id;
    }

    public String getTown() {
        return town;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public Double getTemp() {
        return temp;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public void setType(String type) {
        this.type = type;
    }
}
