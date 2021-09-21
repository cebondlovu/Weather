package com.softcrypt.weather.repository;

import com.softcrypt.weather.models.WeatherForecastResult;
import com.softcrypt.weather.models.WeatherResult;
import com.softcrypt.weather.remote.IOpenWeatherAPI;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.http.Query;

public class OpenWeatherRepository {

    private IOpenWeatherAPI iOpenWeatherAPI;

    @Inject
    public OpenWeatherRepository(IOpenWeatherAPI iOpenWeatherAPI) {this.iOpenWeatherAPI = iOpenWeatherAPI;}

    public Single<WeatherResult> modelGetWeatherByCityName(String cityName, String appid, String unit){
        return iOpenWeatherAPI.getWeatherByCityName(cityName, appid, unit);
    }

    public Single<WeatherResult> modelgetWeatherByLatLng(String lat,String lng,String appid,String unit){
        return iOpenWeatherAPI.getWeatherByLatLng(lat,lng,appid,unit);
    }

    public Single<WeatherForecastResult> modelForecastWeatherByLatLng(String lat, String lng, String appid, String unit){
        return iOpenWeatherAPI.getForecastWeatherByLatLng(lat, lng, appid, unit);
    }
}
