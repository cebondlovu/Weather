package com.softcrypt.weather.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softcrypt.weather.models.WeatherForecastResult;
import com.softcrypt.weather.models.WeatherResult;
import com.softcrypt.weather.repository.OpenWeatherRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private OpenWeatherRepository openWeatherRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<WeatherResult> modelCityMutableLivData = new MutableLiveData<>();
    private MutableLiveData<WeatherResult> modelLatLngMutableLivData = new MutableLiveData<>();
    private MutableLiveData<WeatherForecastResult> modelForecastMutableLivData = new MutableLiveData<>();

    @Inject
    public MainViewModel(OpenWeatherRepository openWeatherRepository){
        this.openWeatherRepository = openWeatherRepository;
    }

    public MutableLiveData<WeatherResult> getCityModelMutableLivData(String cityName, String appid,
                                                                 String unit) {
        getWeatherByCity(cityName,appid,unit);
        return modelCityMutableLivData;

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

    public void clearDisposable(){
        if(disposable != null)
            disposable.clear();
    }

    private void getWeatherByCity(String cityName, String appid, String unit) {
        disposable.add(openWeatherRepository.modelGetWeatherByCityName(cityName, appid, unit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResult>(){

                    @Override
                    public void onSuccess(WeatherResult weatherResult) {
                        modelCityMutableLivData.setValue(weatherResult);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }
}
