package com.softcrypt.weather.base;

import android.app.Application;
import android.content.Context;

import com.softcrypt.weather.di.components.AppComponent;
import com.softcrypt.weather.di.components.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {
    private String $BASE_NAME = "locations.realm";
    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.create();
        initializeRealm(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void initializeRealm(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name($BASE_NAME)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
