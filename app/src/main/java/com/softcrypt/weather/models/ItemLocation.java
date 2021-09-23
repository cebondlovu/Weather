package com.softcrypt.weather.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemLocation extends RealmObject {

    @PrimaryKey
    private int id;
    private String uniqueId;
    private String name;
    private String lng;
    private String lat;

    public ItemLocation() {
    }

    public ItemLocation(int id, String uniqueId, String name, String lng, String lat) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.name = name;
        this.lng = lng;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
