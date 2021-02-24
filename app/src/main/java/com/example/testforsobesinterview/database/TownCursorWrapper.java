package com.example.testforsobesinterview.database;
/*
 * author Lobov-IR
 */

/*
   Для удобного извлечения данных из курсора
*/

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.testforsobesinterview.Town;

public class TownCursorWrapper extends CursorWrapper{
    public TownCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Town getTown(){
        int id = getInt(getColumnIndex(TownDBSchema.TownTable.Cols.id));
        String name = getString(getColumnIndex(TownDBSchema.TownTable.Cols.townName));
        String latitude = getString(getColumnIndex(TownDBSchema.TownTable.Cols.latitude));
        String longitude = getString(getColumnIndex(TownDBSchema.TownTable.Cols.longitude));
        int isLast = getInt(getColumnIndex(TownDBSchema.TownTable.Cols.lastTown));

        return new Town(name, isLast, id, latitude, longitude);
    }
}
