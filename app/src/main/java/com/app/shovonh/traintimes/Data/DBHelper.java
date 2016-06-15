package com.app.shovonh.traintimes.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.shovonh.traintimes.Data.DBContract.StationEntry;

import java.util.ArrayList;


/**
 * Created by Shovon on 6/10/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DBHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "traintimes.db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_STATION_TABLE = "CREATE TABLE " +
                StationEntry.TABLE_NAME + " (" +
                StationEntry._ID + " INTEGER PRIMARY KEY, " +
                StationEntry.COLUMN_STATION_NAME + " TEXT UNIQUE NOT NULL" +
                " );";
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StationEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertData(String stationName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StationEntry.COLUMN_STATION_NAME, stationName);

        db.insert(StationEntry.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> getAllStations() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + StationEntry.TABLE_NAME, null);
        ArrayList<String> stations = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int stationName = cursor.getColumnIndex(StationEntry.COLUMN_STATION_NAME);
            do {
                stations.add(cursor.getString(stationName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stations;
    }

    public void deleteEnrty(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(StationEntry.TABLE_NAME, StationEntry.COLUMN_STATION_NAME + " = ?", new String[]{name});
        db.close();
    }
}
