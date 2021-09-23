package com.softcrypt.weather.database;

import android.app.Dialog;
import android.os.Environment;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.models.ItemLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.internal.IOException;

public class LocationsDatabaseHelper {

    private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private static final String EXPORT_DATABASE_NAME = "locations.realm";
    private static final String IMPORT_DATABASE_NAME = "locations.realm";
    private BaseApplication context;
    private Realm realm;

    public LocationsDatabaseHelper(BaseApplication context, Realm realm) {
        this.context = context;
        this.realm = realm;
    }

    public int getNextLocationKey() {
        if (realm.where(ItemLocation.class).count() > 0)
            return Objects.requireNonNull(realm.where(ItemLocation.class).max("id")).intValue() + 1;
        else
            return 0;
    }

    public int getLocationsCount() {
        return (int) realm.where(ItemLocation.class).count();
    }

    public void insertLocation(ItemLocation itemLocation) {
        ItemLocation modal = realm.where(ItemLocation.class)
                .equalTo("name", itemLocation.getName())
                .findFirst();
        if(modal == null) {
            realm.beginTransaction();
            ItemLocation itemLocationData = realm.createObject(ItemLocation.class, getNextLocationKey());
            itemLocationData.setUniqueId(itemLocation.getUniqueId());
            itemLocationData.setName(itemLocation.getName());
            itemLocationData.setLat(itemLocation.getLat());
            itemLocationData.setLng(itemLocation.getLng());
            realm.commitTransaction();
        }
    }

    public void updateLocation(ItemLocation itemLocation) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(itemLocation);
        realm.commitTransaction();
    }

    public void deleteLocation(String uniqueId) {
        ItemLocation modal = realm.where(ItemLocation.class)
                .equalTo("uniqueId", uniqueId)
                .findFirst();
        realm.executeTransaction(realm -> {
              modal.deleteFromRealm();
        });
    }

    public void deleteAllLocations(){
        realm.beginTransaction();
        realm.delete(ItemLocation.class);
        realm.commitTransaction();
    }

    public ArrayList getAllLocations(){
        return new ArrayList(realm.where(ItemLocation.class).findAll());
    }

    public ItemLocation getLocation(String name) {
        ItemLocation modal = realm.where(ItemLocation.class)
                .equalTo("name", name)
                .findFirst();

        return modal;
    }

    public void backup() {
        File exportRealmFile;
        exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_DATABASE_NAME);
        exportRealmFile.delete();
        realm.writeCopyTo(exportRealmFile);
        String msg = "File exported to Path: " + EXPORT_REALM_PATH + "/" + EXPORT_DATABASE_NAME;
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public void restore(String restoreFilePath){
        copyBundledRealmFile(restoreFilePath, IMPORT_DATABASE_NAME);
        Toast.makeText(context,"Done",Toast.LENGTH_LONG).show();


    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(context.getApplicationContext().getFilesDir(), outFileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(oldFilePath);

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException | java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
