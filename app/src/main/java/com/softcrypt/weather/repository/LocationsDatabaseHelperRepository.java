package com.softcrypt.weather.repository;

import android.app.Application;
import android.content.Context;

import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.database.LocationsDatabaseHelper;
import com.softcrypt.weather.models.ItemLocation;

import java.util.ArrayList;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class LocationsDatabaseHelperRepository {
    private final LocationsDatabaseHelper locationsDatabaseHelper;

    //@Inject
    public LocationsDatabaseHelperRepository(@Nullable BaseApplication application){
        this.locationsDatabaseHelper = new LocationsDatabaseHelper(application);
    }

    public ArrayList<ItemLocation> getAllUserLocations() {
        return locationsDatabaseHelper.getAllUserLocations();
    }

    public ItemLocation getSelectedLocationCoord(String name){
        return locationsDatabaseHelper.getSelectedLocationCoord(name);
    }

    public void deleteAllPlaces(){
        locationsDatabaseHelper.deleteAllPlaces();
    }

    public void deletePlace(String uniqueID){
        locationsDatabaseHelper.deletePlace(uniqueID);
    }

    public void insertNewPlace(ItemLocation itemLocation){
        locationsDatabaseHelper.insertNewPlace(itemLocation);
    }
}
