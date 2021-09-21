package com.softcrypt.weather.remote;

import com.softcrypt.weather.models.WeatherForecastResult;
import com.softcrypt.weather.models.WeatherResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherAPI {
    @GET("weather")
    Single<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                             @Query("lon") String lng,
                                             @Query("appid") String appid,
                                             @Query("units") String unit);

    @GET("weather")
    Single<WeatherResult> getWeatherByCityName(@Query("q") String cityName,
                                                   @Query("appid") String appid,
                                                   @Query("units") String unit);

    @GET("forecast")
    Single<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit);
}
