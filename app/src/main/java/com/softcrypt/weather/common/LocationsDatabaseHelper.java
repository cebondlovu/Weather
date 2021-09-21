package com.softcrypt.weather.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.softcrypt.weather.database.MyLocations;
import com.softcrypt.weather.models.ItemLocation;

import java.util.ArrayList;

public class LocationsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mylocationsA";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;

    public LocationsDatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        MyLocations.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        MyLocations.onUpgrade(sqLiteDatabase, i, i1);
    }

    public  void insertNewPlace(ItemLocation itemLocation) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MyLocations.COLUMN_UNIQUE_ID, itemLocation.getUniqueID());
        values.put(MyLocations.COLUMN_NAME, itemLocation.getName());
        values.put(MyLocations.COLUMN_LAT, itemLocation.getLat());
        values.put(MyLocations.COLUMN_LNG, itemLocation.getLng());
        try {
            db.beginTransaction();
            db.insertWithOnConflict(MyLocations.TABLE_LOCATIONS, null, values,
                    SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deletePlace(String uniqueID){
        db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(MyLocations.TABLE_LOCATIONS, MyLocations.COLUMN_UNIQUE_ID+" = ? ",
                    new String[]{uniqueID});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllPlaces(){
        db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(MyLocations.TABLE_LOCATIONS, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public ArrayList<ItemLocation> getAllUserLocations(){
        ArrayList<ItemLocation> itemLocationsList = new ArrayList<>();
        db = this.getReadableDatabase();

        Cursor cursor = db.query(MyLocations.TABLE_LOCATIONS,new String[]{MyLocations.COLUMN_UNIQUE_ID,
                        MyLocations.COLUMN_NAME,MyLocations.COLUMN_LAT,
                        MyLocations.COLUMN_LNG},
                null,null,null,null,null);

        while(cursor.moveToNext()){
            int id = cursor.getColumnIndex(MyLocations.COLUMN_UNIQUE_ID);
            int name = cursor.getColumnIndex(MyLocations.COLUMN_NAME);
            int latitude = cursor.getColumnIndex(MyLocations.COLUMN_LAT);
            int longitude = cursor.getColumnIndex(MyLocations.COLUMN_LNG);
            itemLocationsList.add(new ItemLocation(
                    cursor.getString(id),cursor.getString(name), cursor.getString(latitude),
                    cursor.getString(longitude)
            ));
        }

        cursor.close();
        db.close();

        return  itemLocationsList;
    }

    public ItemLocation getSelectedLocationCoord(String name){

        ItemLocation toDoItemInfo = new ItemLocation();
        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+MyLocations.TABLE_LOCATIONS+" WHERE "+
                MyLocations.COLUMN_NAME+" =? ",new String[]{name});

        while(cursor.moveToNext()){

            int lat = cursor.getColumnIndex(MyLocations.COLUMN_LAT);
            int lon = cursor.getColumnIndex(MyLocations.COLUMN_LNG);

            toDoItemInfo.setLat(cursor.getString(lat));
            toDoItemInfo.setLng(cursor.getString(lon));
        }

        cursor.close();

        return  toDoItemInfo;
    }
}
