package com.softcrypt.weather.models;

public class ItemLocation {
    private String uniqueID;
    private String name;
    private String lng;
    private String lat;

    public ItemLocation() {
    }

    public ItemLocation(String uniqueID, String name, String lng, String lat) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.lng = lng;
        this.lat = lat;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
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
