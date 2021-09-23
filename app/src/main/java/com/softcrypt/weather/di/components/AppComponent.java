package com.softcrypt.weather.di.components;

import com.softcrypt.weather.di.modules.ContextModule;
import com.softcrypt.weather.di.modules.NetworkModule;
import com.softcrypt.weather.views.LocationsActivity;
import com.softcrypt.weather.views.MainActivity;
import com.softcrypt.weather.views.MapsActivity;
import com.softcrypt.weather.views.ViewPopup;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, ContextModule.class})
public interface AppComponent {

    void injectMainAct(MainActivity mainActivity);
    void injectMapsAct(MapsActivity mapsActivity);
    void injectLocationsAct(LocationsActivity locationsActivity);
    void injectViewPopup(ViewPopup viewPopup);

}
