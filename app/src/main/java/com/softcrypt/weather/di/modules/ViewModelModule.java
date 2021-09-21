package com.softcrypt.weather.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.softcrypt.weather.di.ViewModelKey;
import com.softcrypt.weather.viewModels.ForecastViewModel;
import com.softcrypt.weather.viewModels.MainViewModel;
import com.softcrypt.weather.viewModels.TodayWeatherViewModel;
import com.softcrypt.weather.viewModels.ViewModelFactory;
import com.softcrypt.weather.viewModels.CitiesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindFactory(ViewModelFactory factory);
}
