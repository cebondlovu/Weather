package com.softcrypt.weather.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softcrypt.weather.models.WeatherResult;
import com.softcrypt.weather.repository.OpenWeatherRepository;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class TodayWeatherViewModel extends ViewModel {
    private OpenWeatherRepository openWeatherRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<WeatherResult> modelMutableLivData = new MutableLiveData<>();

    @Inject
    public TodayWeatherViewModel(OpenWeatherRepository openWeatherRepository){
        this.openWeatherRepository = openWeatherRepository;
    }
}
