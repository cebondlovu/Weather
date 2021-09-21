package com.softcrypt.weather.di.modules;

import com.softcrypt.weather.remote.IOpenWeatherAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public abstract class NetworkModule {

    private static String $BASE_URL = "https://api.openweathermap.org/data/2.5/";

    @Provides
    @Singleton
    static Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl($BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    static IOpenWeatherAPI provideIOpenWeatherServiceApi(Retrofit retrofit){
        return retrofit.create(IOpenWeatherAPI.class);
    }
}
