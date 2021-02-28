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
import com.example.testforsobesinterview.Weather;

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

        for (Town town : towns){
            db.insert(TownDBSchema.TownTable.NAME,null,TownDBSchema.getContentValues(town));
        }

        // для хранения ранее полученных прогнозов.
        db.execSQL("create table " + TownDBSchema.WeatherTable.NAME + "( id integer primary key autoincrement, "
        + TownDBSchema.WeatherTable.Cols.townName + ", " + TownDBSchema.WeatherTable.Cols.date + ", " +
                TownDBSchema.WeatherTable.Cols.day + ", " + TownDBSchema.WeatherTable.Cols.temperature
                + ", " + TownDBSchema.WeatherTable.Cols.type + " )");
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

    public void setWeatherForTown(List<WeatherForDB> weatherList){
        // Если есть записи, то update, иначе insert
        if (weatherList.get(0) != null){
            String name = weatherList.get(0).getTown();
            if (sqLiteDatabaseRead == null)
                sqLiteDatabaseRead = getReadableDatabase();

            Cursor cursor = sqLiteDatabaseRead.rawQuery("SELECT COUNT(*) FROM WEATHER WHERE townName=?",new String[]{name});
            cursor.moveToFirst();
            int i = cursor.getInt(0);
            if (i == 0){ // не вернулись записи. значит для этого города нет записей. делаем insert
                SQLiteDatabase database = getWritableDatabase();
                for (WeatherForDB w : weatherList){
                    database.insert(TownDBSchema.WeatherTable.NAME,null, TownDBSchema.WeatherTable.getContentValues(w));
                }
            }
            else { // записи уже есть. просто обновляем их
                SQLiteDatabase database = getWritableDatabase();
                for (WeatherForDB w : weatherList){
                     database.update(TownDBSchema.WeatherTable.NAME, TownDBSchema.WeatherTable.getContentValues(w),
                             TownDBSchema.WeatherTable.Cols.id + "=?", new String[]{w.getId().toString()});
                }
            }
            cursor.close();
        }
    }

    public int getMaxWeatherId(){
        if (sqLiteDatabaseRead == null)
            sqLiteDatabaseRead = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseRead.rawQuery("select max(id) from " + TownDBSchema.WeatherTable.NAME, null);
        cursor.moveToFirst();
        int i = cursor.getInt(0);
        cursor.close();
        return i;
    }

    public List<Weather> getOldWeather(String name){
        ArrayList<Weather> weathers = new ArrayList<>();
        if (sqLiteDatabaseRead == null)
            sqLiteDatabaseRead = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseRead.query(TownDBSchema.WeatherTable.NAME, null, "townName=?", new String[]{name},
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            weathers.add(new Weather(cursor.getString(cursor.getColumnIndex(TownDBSchema.WeatherTable.Cols.day)),
                    cursor.getString(cursor.getColumnIndex(TownDBSchema.WeatherTable.Cols.date)),
                    cursor.getDouble(cursor.getColumnIndex(TownDBSchema.WeatherTable.Cols.temperature)),
                    cursor.getString(cursor.getColumnIndex(TownDBSchema.WeatherTable.Cols.type))
            ));
            cursor.moveToNext();
        }

    cursor.close();
        return weathers;
    }

    public Town getCurrentTown(){
        Town town = null;
        if(sqLiteDatabaseRead == null)
            sqLiteDatabaseRead = getReadableDatabase();
        Cursor cursor = sqLiteDatabaseRead.query(TownDBSchema.TownTable.NAME, null, "lastTown=1",
                null,null,null,null);
        TownCursorWrapper wrapper = new TownCursorWrapper(cursor);
        wrapper.moveToFirst();
        while (!wrapper.isAfterLast()) {
            town = wrapper.getTown();
            wrapper.moveToNext();
        }
        return town;
    }
}