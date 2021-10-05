package com.softcrypt.weather.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LocalCity extends RealmObject {

    @PrimaryKey
    public int id;
    public String name;

    public LocalCity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public LocalCity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
