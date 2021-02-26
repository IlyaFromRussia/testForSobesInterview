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
    private final List<Town> towns = Arrays.asList(new Town("Moscow", 0, 0,String.valueOf(55.75115080026314), String.valueOf(37.640865232607055)),
            new Town("Shanghai", 0, 1, String.valueOf(31.24243582623752), String.valueOf(121.46291303492524)),
            new Town("Mumbai", 0, 2,String.valueOf(19.150972503817602),String.valueOf(72.8819674319197)),
            new Town("Prague", 1, 3, String.valueOf(50.10898436884388), String.valueOf(14.53838779778374)),
            new Town("Amsterdam",0, 4, String.valueOf(52.37764296471469), String.valueOf(4.900520505707462)),
            new Town("Rostov-on-Don",0,5,String.valueOf(47.254385830547506), String.valueOf(39.67278326208904)));

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
        setMark(newTown);
    }

    public void setMark(Town town){
        ContentValues values = TownDBSchema.getContentValues(town);
        String idStr = String.valueOf(town.getId());
        sqLiteDatabaseWrite.update(TownDBSchema.TownTable.NAME,values,"id="+idStr,null);
    }
}