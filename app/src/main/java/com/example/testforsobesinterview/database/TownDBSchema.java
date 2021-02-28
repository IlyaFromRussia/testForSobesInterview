package com.example.testforsobesinterview.database;
/*
 * author Lobov-IR
 */

import android.content.ContentValues;
import com.example.testforsobesinterview.Town;

public class TownDBSchema {
    public static final class TownTable{
        public static final String NAME = "towns";

        public static final class Cols{
            public static String townName = "townName";
            public static String lastTown = "lastTown";
            public static String latitude = "latitude";
            public static String longitude = "longitude";
            public static String id = "id";
        }
    }

    public static ContentValues getContentValues(Town town){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TownTable.Cols.id, town.getId());
        contentValues.put(TownTable.Cols.townName,town.getName());
        contentValues.put(TownTable.Cols.latitude,town.getLatitude());
        contentValues.put(TownTable.Cols.longitude,town.getLongitude());
        contentValues.put(TownTable.Cols.lastTown,town.isLast());
        return contentValues;
    }

    public static final class WeatherTable{
        public static final String NAME = "weather";

        public static final class Cols{
            public static String townName = "townName";
            public static String id = "id";
            public static String day = "day";
            public static String date = "date";
            public static String type = "type";
            public static String temperature = "temperature";
        }

        public static ContentValues getContentValues(WeatherForDB weather){
            ContentValues contentValues = new ContentValues();

            contentValues.put(WeatherTable.Cols.id, weather.getId());
            contentValues.put(WeatherTable.Cols.townName,weather.getTown());
            contentValues.put(Cols.date,weather.getDate());
            contentValues.put(Cols.day, weather.getDay());
            contentValues.put(Cols.temperature, weather.getTemp());
            contentValues.put(Cols.type, weather.getType());

            return contentValues;
        }
    }

    public static final class ServiceResult{
        public static final String NAME = "serviceResult";

        public static final class Cols{
            public static String id = "id";
            public static String flag = "flag";
        }
    }
}