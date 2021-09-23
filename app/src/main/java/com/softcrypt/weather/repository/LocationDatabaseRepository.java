package com.softcrypt.weather.repository;

import android.content.Context;

import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.database.LocationsDatabaseHelper;
import com.softcrypt.weather.models.ItemLocation;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;

public class LocationDatabaseRepository {

    private LocationsDatabaseHelper locationsDatabaseHelper;
    private Realm realm;
    private Context context;

    @Inject
    public LocationDatabaseRepository(BaseApplication context, Realm realm) {
        this.locationsDatabaseHelper = new LocationsDatabaseHelper(context, realm);
        this.context = context;
        this.realm = realm;
    }

    public void modelInsertLocation(ItemLocation itemLocation){
        locationsDatabaseHelper.insertLocation(itemLocation);
    }

    public void modelDeleteLocation(String uniqueId) {
        locationsDatabaseHelper.deleteLocation(uniqueId);
    }

    public void modelDeleteAllLocations(){
        locationsDatabaseHelper.deleteAllLocations();
    }

    public ArrayList modelGetAllLocations() {
        return locationsDatabaseHelper.getAllLocations();
    }

    public ItemLocation modelGetLocation(String name) {
        return locationsDatabaseHelper.getLocation(name);
    }

    public int modelGetLocationsCount(){
        return locationsDatabaseHelper.getLocationsCount();
    }

    public void modelRestore(String filePath) {
        locationsDatabaseHelper.restore(filePath);
    }

    public void modelBackup() {
        locationsDatabaseHelper.backup();
    }
}
