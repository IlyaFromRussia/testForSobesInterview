package com.example.testforsobesinterview.database;
/*
 * author Lobov-IR
 */

import android.database.Cursor;
import android.database.CursorWrapper;

public class WeatherCursorWrapper extends CursorWrapper {
    public WeatherCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WeatherForDB getWeather(){
        Integer id = getInt(getColumnIndex(TownDBSchema.WeatherTable.Cols.id));
        String town = getString(getColumnIndex(TownDBSchema.WeatherTable.Cols.townName));
        String date = getString(getColumnIndex(TownDBSchema.WeatherTable.Cols.date));
        String day = getString(getColumnIndex(TownDBSchema.WeatherTable.Cols.day));
        Double temp = getDouble(getColumnIndex(TownDBSchema.WeatherTable.Cols.temperature));
        String type = getString(getColumnIndex(TownDBSchema.WeatherTable.Cols.type));

        return new WeatherForDB(id, town, date, day, temp, type);
    }
}
