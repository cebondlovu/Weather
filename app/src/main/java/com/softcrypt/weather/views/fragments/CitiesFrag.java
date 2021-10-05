package com.softcrypt.weather.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.softcrypt.weather.common.Common;
import com.softcrypt.weather.R;
import com.softcrypt.weather.viewModels.MainViewModel;
import com.softcrypt.weather.views.MainActivity;
import com.softcrypt.weather.views.ViewPopup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CitiesFrag#getInstance} factory method to
 * create an instance of this fragment.
 */
public class CitiesFrag extends Fragment {

    private static MainViewModel mainViewModel;

    private List<String> lstCities;
    private MaterialSearchBar searchBar;
    ImageView img_weather;
    Button btn_add_location;
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
    private String latitude, longitude;

    static CitiesFrag instance;

    public CitiesFrag(MainViewModel mainViewModel) {
        CitiesFrag.mainViewModel = mainViewModel;
    }

    public static CitiesFrag getInstance() {
        if(instance == null)
            instance = new CitiesFrag(mainViewModel);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_cities, container, false);

        img_weather = itemView.findViewById(R.id.img_weather);
        txt_city_name = itemView.findViewById(R.id.txt_city_name);
        txt_humidity = itemView.findViewById(R.id.txt_humidity);
        txt_sunrise = itemView.findViewById(R.id.txt_sunrise);
        txt_sunset = itemView.findViewById(R.id.txt_sunset);
        btn_add_location = itemView.findViewById(R.id.add_location);
        btn_add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewPopup.class);
                intent.putExtra(ViewPopup.$CALL_TYPE, "CONFIRM");
                intent.putExtra(ViewPopup.LA_T, latitude);
                intent.putExtra(ViewPopup.LN_G, longitude);
                startActivity(intent);
            }
        });

        txt_pressure = itemView.findViewById(R.id.txt_pressure);
        txt_temperature = itemView.findViewById(R.id.txt_temperature);
        txt_description = itemView.findViewById(R.id.txt_description);
        txt_date_time = itemView.findViewById(R.id.txt_date_time);
        txt_wind = itemView.findViewById(R.id.txt_wind);
        txt_geo_coord = itemView.findViewById(R.id.txt_geo_coord);
        weather_panel = itemView.findViewById(R.id.weather_panel);
        loading = itemView.findViewById(R.id.progress);
        searchBar = itemView.findViewById(R.id.searchBar);
        searchBar.setEnabled(false);

        lstCities = new ArrayList<>();

        loadCities();

        return itemView;
    }

    private void loadCities() {
        if (MainActivity.list != null) {
            lstCities.clear();
            for (String city : MainActivity.list) {
                lstCities.add(city);
            }
            searchBar.setEnabled(true);
        }

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();
                for (String city : lstCities) {
                    if (city.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(city);
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                getWeatherInformation(text.toString());
                searchBar.setLastSuggestions(lstCities);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        searchBar.setLastSuggestions(lstCities);

        loading.setVisibility(View.GONE);

    }

    private void getWeatherInformation(String cityName) {
        mainViewModel.getCityModelMutableLivData(cityName, Common.$API_KEY, "metric")
                .observe(requireActivity(), weatherResult -> {
                    Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                            .append(weatherResult.getWeather().get(0).getIcon())
                            .append(".png").toString()).into(img_weather);

                    txt_city_name.setText(weatherResult.getName());
                    txt_description.setText(new StringBuilder("Weather In ")
                            .append(weatherResult.getName()).toString());
                    txt_temperature.setText(new StringBuilder(String.valueOf(
                            weatherResult.getMain().getTemp())).append("°C").toString());
                    txt_wind.setText(new StringBuilder(String.valueOf(
                            weatherResult.getWind().getSpeed())).append(" km/h").toString());
                    txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                    txt_pressure.setText(new StringBuilder(String.valueOf(
                            weatherResult.getMain().getPressure())).append(" hpa").toString());
                    txt_humidity.setText(new StringBuilder(String.valueOf(
                            weatherResult.getMain().getHumidity())).append(" %").toString());
                    txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                    txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                    txt_geo_coord.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());

                    latitude = String.valueOf(weatherResult.getCoord().getLat());
                    longitude = String.valueOf(weatherResult.getCoord().getLon());
                    weather_panel.setVisibility(View.VISIBLE);
                    btn_add_location.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                });
    }

}