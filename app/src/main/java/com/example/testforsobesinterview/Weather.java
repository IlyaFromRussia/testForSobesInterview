package com.example.testforsobesinterview;
/*
 * author Lobov-IR
 */


public class Weather {
    private String dayOwWeek;
    private String date;
    private double temperature;
    private WeatherCode weatherCode;

    public Weather(String dayOwWeek, String date, double temperature, String code){
        this.dayOwWeek = dayOwWeek;
        this.date = date;
        this.temperature = temperature;
        switch (code){
            case "Rain":{
                weatherCode = WeatherCode.RAIN;
                break;
            }
            case "Clear":{
                weatherCode = WeatherCode.CLEAR;
                break;
            }
            case "Clouds":{
                weatherCode = WeatherCode.CLOUDS;
                break;
            }
            case "Snow":{
                weatherCode = WeatherCode.SNOW;
                break;
            }
            default:
                weatherCode = WeatherCode.ALLERT;
        }
    }

    public enum WeatherCode{
        ALLERT,
        SNOW,
        RAIN,
        CLEAR,
        CLOUDS;
    }

    public String getDayOwWeek() {
        return dayOwWeek;
    }

    public String getDate() {
        return date;
    }

    public double getTemperature() {
        return temperature;
    }

    public WeatherCode getWeatherCode() {
        return weatherCode;
    }
}