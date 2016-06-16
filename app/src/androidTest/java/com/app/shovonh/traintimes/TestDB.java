package com.app.shovonh.traintimes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.app.shovonh.traintimes.Data.DBContract;
import com.app.shovonh.traintimes.Data.DBHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Shovon on 6/15/16.
 */
public class TestDB extends AndroidTestCase {
    void deleteTheDatabase(){
        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteTheDatabase();
    }

    public void testCreateDB() throws Throwable{
        final HashSet<String> tableNameHash = new HashSet<String>();
        tableNameHash.add(DBContract.StationEntry.TABLE_NAME);

        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
        SQLiteDatabase db = new DBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: Databse has not been created corectly", c.moveToFirst());

        c = db.rawQuery("PRAGMA table_info (" + DBContract.StationEntry.TABLE_NAME + ")", null);
        assertTrue("Error: Unable to query the database for table information", c.moveToFirst());

        final HashSet<String> columnHashSet = new HashSet<String>();
        columnHashSet.add(DBContract.StationEntry._ID);
        columnHashSet.add(DBContract.StationEntry.COLUMN_STATION_NAME);

        int columnNameIndex = c.getColumnIndex("name");
        do{
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        }while(c.moveToNext());

        assertTrue("Error: The database doesnt contain all columns.", columnHashSet.isEmpty());

        db.close();
    }

    public void testMovieTable(){
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues testValues = new ContentValues();
        testValues.put(DBContract.StationEntry.COLUMN_STATION_NAME, "stationnameyo");

        long locationRowID = db.insert(DBContract.StationEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowID != -1);


        Cursor cursor = db.query(
                DBContract.StationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: Not records returned from query", cursor.moveToFirst());

        validateCurrentRecord("Error: Validation failed", cursor, testValues);

        assertFalse( "Error: More than one record returned from location query",
                cursor.moveToNext() );
        cursor.close();
        db.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
    public void testGetByID(){
        deleteTheDatabase();
        DBHelper dbHelper = new DBHelper(mContext);
        dbHelper.insertData("one");
        String a = dbHelper.getByID(1);
        assertEquals("one", a);
    }

}
