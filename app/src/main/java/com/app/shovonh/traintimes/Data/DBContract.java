package com.app.shovonh.traintimes.Data;

import android.provider.BaseColumns;

/**
 * Created by Shovon on 6/14/16.
 */
public class DBContract {

    public static final class StationEntry implements BaseColumns{
        public static final String TABLE_NAME = "stations";
        public static final String COLUMN_STATION_NAME = "station_name";
    }
}
