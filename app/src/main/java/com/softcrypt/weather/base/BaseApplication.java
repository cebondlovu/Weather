package com.softcrypt.weather.base;

import android.app.Application;

import com.softcrypt.weather.di.components.AppComponent;
import com.softcrypt.weather.di.components.DaggerAppComponent;

public class BaseApplication extends Application {

    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.create();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
