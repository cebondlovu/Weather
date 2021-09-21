package com.softcrypt.weather.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softcrypt.weather.models.WeatherResult;
import com.softcrypt.weather.repository.OpenWeatherRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CitiesViewModel extends ViewModel {

    private OpenWeatherRepository openWeatherRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<WeatherResult> modelMutableLivData = new MutableLiveData<>();

    @Inject
    public CitiesViewModel(OpenWeatherRepository openWeatherRepository){
        this.openWeatherRepository = openWeatherRepository;
    }

    public MutableLiveData<WeatherResult> getModelMutableLivData(String cityName, String appid,
                                                                 String unit) {
        getWeatherByCity(cityName,appid,unit);
        return modelMutableLivData;

    }

    public void clearDisposable(){
        disposable.clear();
    }

    private void getWeatherByCity(String cityName, String appid, String unit) {
        disposable.add(openWeatherRepository.modelGetWeatherByCityName(cityName, appid, unit)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableSingleObserver<WeatherResult>(){

            @Override
            public void onSuccess(WeatherResult weatherResult) {
                modelMutableLivData.setValue(weatherResult);
            }

            @Override
            public void onError(Throwable e) {

            }
        }));
    }


}
