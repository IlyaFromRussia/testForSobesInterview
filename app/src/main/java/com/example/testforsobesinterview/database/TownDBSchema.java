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
}