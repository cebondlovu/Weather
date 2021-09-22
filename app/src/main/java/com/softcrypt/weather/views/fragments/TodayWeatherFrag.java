package com.softcrypt.weather.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softcrypt.weather.common.Common;
import com.softcrypt.weather.database.LocationsDatabaseHelper;
import com.softcrypt.weather.models.ItemLocation;
import com.softcrypt.weather.R;
import com.softcrypt.weather.models.WeatherResult;
import com.softcrypt.weather.viewModels.MainViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayWeatherFrag#getInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayWeatherFrag extends Fragment {

    private static MainViewModel mainViewModel;
    ImageView img_weather;
    TextView txt_city_name,
             txt_humidity,
             txt_sunrise,
             txt_sunset,
             txt_pressure,
             txt_temperature,
             txt_description,
             txt_date_time,
             txt_wind,
             txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;

    LocationsDatabaseHelper dbHelper;
    ArrayList<ItemLocation> myList;

    static TodayWeatherFrag instance;

    public static TodayWeatherFrag getInstance(){
        if(instance == null)
            instance = new TodayWeatherFrag(mainViewModel);
        return instance;
    }

    public TodayWeatherFrag(MainViewModel mainViewModel) {
        TodayWeatherFrag.mainViewModel = mainViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        img_weather = itemView.findViewById(R.id.img_weather);
        txt_city_name = itemView.findViewById(R.id.txt_city_name);
        txt_humidity = itemView.findViewById(R.id.txt_humidity);
        txt_sunrise = itemView.findViewById(R.id.txt_sunrise);
        txt_sunset = itemView.findViewById(R.id.txt_sunset);
        txt_pressure = itemView.findViewById(R.id.txt_pressure);
        txt_temperature = itemView.findViewById(R.id.txt_temperature);
        txt_description = itemView.findViewById(R.id.txt_description);
        txt_date_time = itemView.findViewById(R.id.txt_date_time);
        txt_wind = itemView.findViewById(R.id.txt_wind);
        txt_geo_coord = itemView.findViewById(R.id.txt_geo_coord);

        weather_panel = itemView.findViewById(R.id.weather_panel);
        loading = itemView.findViewById(R.id.progress);

        getWeatherInformation();

        return itemView;
    }

    private void getWeatherInformation() {
        mainViewModel.getLatLngModelMutableLivData(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),Common.$API_KEY, "metric")
                .observe(requireActivity(), new Observer<WeatherResult>(){

                    @Override
                    public void onChanged(WeatherResult weatherResult) {
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                .append(weatherResult.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(img_weather);

                        txt_city_name.setText(weatherResult.getName());
                        txt_description.setText(new StringBuilder("Weather In ")
                                .append(weatherResult.getName()).toString());
                        txt_temperature.setText(new StringBuilder(String.valueOf(
                                weatherResult.getMain().getTemp())).append("°C").toString());
                        txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        txt_pressure.setText(new StringBuilder(String.valueOf(
                                weatherResult.getMain().getPressure())).append(" hpa").toString());
                        txt_humidity.setText(new StringBuilder(String.valueOf(
                                weatherResult.getMain().getHumidity())).append(" %").toString());
                        txt_wind.setText(new StringBuilder(String.valueOf(weatherResult.getWind().getSpeed()))
                        .append(" km/h").toString());
                        txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        txt_geo_coord.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());

                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                });
    }
}