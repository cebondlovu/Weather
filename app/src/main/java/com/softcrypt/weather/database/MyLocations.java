package com.softcrypt.weather.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyLocations {

    public static final String TABLE_LOCATIONS = "locations";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_UNIQUE_ID = "uniqueID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LNG = "longitude";
    public static final String COLUMN_LAT = "latitude";

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_LOCATIONS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_UNIQUE_ID + " VARCHAR(250) NOT NULL, "
            + COLUMN_NAME + " VARCHAR(250) NOT NULL, "
            + COLUMN_LNG + " VARCHAR(250) NOT NULL, "
            + COLUMN_LAT + " VARCHAR(250) NOT NULL"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(MyLocations.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        onCreate(database);
    }
}
