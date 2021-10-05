package com.softcrypt.weather.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.softcrypt.weather.R;
import com.softcrypt.weather.base.BaseApplication;
import com.softcrypt.weather.models.WeatherForecastResult;
import com.softcrypt.weather.models.WeatherResult;
import com.softcrypt.weather.repository.OpenWeatherRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private final OpenWeatherRepository openWeatherRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<WeatherResult> modelCityMutableLivData = new MutableLiveData<>();
    private final MutableLiveData<WeatherResult> modelLatLngMutableLivData = new MutableLiveData<>();
    private final MutableLiveData<WeatherForecastResult> modelForecastMutableLivData = new MutableLiveData<>();

    @Inject
    public MainViewModel(OpenWeatherRepository openWeatherRepository){
        this.openWeatherRepository = openWeatherRepository;
    }

    public MutableLiveData<WeatherResult> getLatLngModelMutableLivData(String lat,String lng,
                                                                     String appid,String unit) {
        getWeatherByLatLng(lat,lng,appid,unit);
        return modelLatLngMutableLivData;
    }

    public MutableLiveData<WeatherForecastResult> getForcastModelMutableLivData(String lat, String lng,
                                                                               String appid, String unit) {
        getWeatherByForecast(lat,lng,appid,unit);
        return modelForecastMutableLivData;
    }

    public LiveData<WeatherResult> getCityModelMutableLivData(String cityName, String appid,
                                                              String unit) {
        getWeatherByCity(cityName,appid,unit);
        return modelCityMutableLivData;
    }

    private void getWeatherByForecast(String lat, String lng, String appid, String unit) {
        disposable.add(openWeatherRepository.modelForecastWeatherByLatLng(lat,lng, appid, unit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherForecastResult>(){

                    @Override
                    public void onSuccess(WeatherForecastResult weatherForecastResult) {
                        modelForecastMutableLivData.setValue(weatherForecastResult);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    private void getWeatherByLatLng(String lat, String lng, String appid, String unit) {
        disposable.add(openWeatherRepository.modelgetWeatherByLatLng(lat,lng, appid, unit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResult>(){

                    @Override
                    public void onSuccess(WeatherResult weatherResult) {
                        modelLatLngMutableLivData.setValue(weatherResult);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    private void getWeatherByCity(String cityName, String appid, String unit) {
        disposable.add(openWeatherRepository.modelGetWeatherByCityName(cityName, appid, unit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResult>() {

                    @Override
                    public void onSuccess(@NonNull WeatherResult weatherResult) {
                        modelCityMutableLivData.setValue(weatherResult);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                }));
    }

    public void clearDisposable(){
        if(disposable != null)
            disposable.clear();
    }
}
