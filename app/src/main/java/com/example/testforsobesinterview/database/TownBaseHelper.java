package com.example.testforsobesinterview.database;
/*
 * author Lobov-IR
 *
 * Чтобы не проверять существование файла БД или номер версии.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.testforsobesinterview.Town;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TownBaseHelper  extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabaseRead;
    private SQLiteDatabase sqLiteDatabaseWrite;
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "townBase.db";
    private final List<Town> towns = Arrays.asList(new Town("Moscow", 0, 0),new Town("Shanghai", 0, 1),
            new Town("Mumbai", 0, 2), new Town("Prague", 1, 3),
            new Town("Amsterdam",0, 4));

    public TownBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TownDBSchema.TownTable.NAME + " ( " +
                "id integer primary key autoincrement, " + TownDBSchema.TownTable.Cols.townName + ", "
                + TownDBSchema.TownTable.Cols.lastTown + ", " + TownDBSchema.TownTable.Cols.latitude + ", " +
                        TownDBSchema.TownTable.Cols.longitude + " )"
                  );
        // первый раз заполняю таблицу.
        for (Town town : towns){
            db.insert(TownDBSchema.TownTable.NAME,null,TownDBSchema.getContentValues(town));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertTown(Town town){
        if (sqLiteDatabaseWrite == null)
            sqLiteDatabaseWrite = getWritableDatabase();
        sqLiteDatabaseWrite.insert(TownDBSchema.TownTable.NAME,null,TownDBSchema.getContentValues(town));
    }

    public List<Town> getAllTowns(){
        List<Town> returnList = new ArrayList<>();
        try (TownCursorWrapper townCursorWrapper = queryTowns(null, null)) {
           townCursorWrapper.moveToFirst();
            while (!townCursorWrapper.isAfterLast()) {
                returnList.add(townCursorWrapper.getTown());
                townCursorWrapper.moveToNext();
            }
        }
        return returnList;
    }

    private TownCursorWrapper queryTowns(String where, String[] whereArgs){
        if (sqLiteDatabaseRead == null)
            sqLiteDatabaseRead = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseRead.query(TownDBSchema.TownTable.NAME, null, where, whereArgs,
                null, null, null);
        return new TownCursorWrapper(cursor);
    }

    public void reloadMark(Town newTown){
        Town town = null;
        Cursor cursor = sqLiteDatabaseRead.query(TownDBSchema.TownTable.NAME, null, "lastTown=1",
                null,null,null,null);
        TownCursorWrapper wrapper = new TownCursorWrapper(cursor);
        wrapper.moveToFirst();
        while (!wrapper.isAfterLast()) {
            town = wrapper.getTown();
            wrapper.moveToNext();
        }
        // найден id записи, где нужно снять "галочку"
//        Log.d("###@#@#@#@#@#",String.valueOf(id));

        town.setLastTown(0);
        String idStr = String.valueOf(town.getId());
        ContentValues values = TownDBSchema.getContentValues(town);
        if (sqLiteDatabaseWrite == null)
            sqLiteDatabaseWrite = getWritableDatabase();
        sqLiteDatabaseWrite.update(TownDBSchema.TownTable.NAME,values,"id="+idStr,null);

        // выставляю новую галочку.
        values = TownDBSchema.getContentValues(newTown);
        idStr = String.valueOf(newTown.getId());
        sqLiteDatabaseWrite.update(TownDBSchema.TownTable.NAME,values,"id="+idStr,null);
    }
}