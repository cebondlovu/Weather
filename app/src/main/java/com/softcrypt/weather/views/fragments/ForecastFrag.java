package com.softcrypt.weather.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softcrypt.weather.adapters.WeatherForecastAdapter;
import com.softcrypt.weather.common.Common;
import com.softcrypt.weather.models.WeatherForecastResult;
import com.softcrypt.weather.R;
import com.softcrypt.weather.remote.IOpenWeatherAPI;
import com.softcrypt.weather.viewModels.MainViewModel;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFrag#getInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFrag extends Fragment {

    private static MainViewModel mainViewModel;

    TextView txt_city_name,txt_geo_coord;
    RecyclerView recycler_forecast;
    LinearLayout main_info;

    static ForecastFrag instance;

    public ForecastFrag(MainViewModel mainViewModel) {
        ForecastFrag.mainViewModel = mainViewModel;
    }

    public static ForecastFrag getInstance() {
            if(instance == null)
                instance = new ForecastFrag(mainViewModel);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //
        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);
        main_info = itemView.findViewById(R.id.main_info);
        txt_city_name = itemView.findViewById(R.id.txt_city_name);
        txt_geo_coord = itemView.findViewById(R.id.txt_geo_coord);
        recycler_forecast = itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));

        getForecastWeatherInformation();

        return itemView;
    }


    private void getForecastWeatherInformation() {
        mainViewModel.getForcastModelMutableLivData(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),Common.$API_KEY, "metric")
                .observe(requireActivity(), new Observer<WeatherForecastResult>(){

                    @Override
                    public void onChanged(WeatherForecastResult weatherForecastResult) {
                        if(weatherForecastResult == null){
                            main_info.setVisibility(View.INVISIBLE);
                        } else {
                            main_info.setVisibility(View.VISIBLE);
                            displayForecastWeather(weatherForecastResult);
                        }
                    }
                });
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        txt_city_name.setText(new StringBuilder(weatherForecastResult.city.name));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(),weatherForecastResult);
        recycler_forecast.setAdapter(adapter);
    }
}