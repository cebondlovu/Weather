package com.softcrypt.weather.views.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.softcrypt.weather.common.Common;
import com.softcrypt.weather.models.WeatherResult;
import com.softcrypt.weather.R;
import com.softcrypt.weather.viewModels.MainViewModel;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_cities, container, false);
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
        searchBar = itemView.findViewById(R.id.searchBar);
        searchBar.setEnabled(false);

        new LoadCities().execute();

        return itemView;
    }

    private class LoadCities extends SimpleAsyncTask<List<String>> {
        @Override
        protected List<String> doInBackground() {
            lstCities = new ArrayList<>();
            try {
                StringBuilder builder = new StringBuilder();
                InputStream is = getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream = new GZIPInputStream(is);

                InputStreamReader reader = new InputStreamReader(gzipInputStream);
                BufferedReader in = new BufferedReader(reader);

                String read;
                while ((read = in.readLine()) != null)
                    builder.append(read);
                lstCities = new Gson().fromJson(builder.toString(),
                        new TypeToken<List<String>>(){}.getType());
            }catch (IOException e){
                e.printStackTrace();
            }

            return lstCities;
        }

        @Override
        protected void onSuccess(final List<String> listCity) {
            super.onSuccess(listCity);

            searchBar.setEnabled(true);

            searchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
/*                    List<String> suggest = new ArrayList<>();
                    for(String search : listCity){
                        if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                            suggest.add(search);
                    }
                    searchBar.setLastSuggestions(suggest);*/
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
                    searchBar.setLastSuggestions(listCity);
                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });

            searchBar.setLastSuggestions(listCity);

            loading.setVisibility(View.GONE);

        }
    }

    private void getWeatherInformation(String cityName) {
        mainViewModel.getCityModelMutableLivData(cityName, Common.$API_KEY, "metric")
                .observe(requireActivity(), new Observer<WeatherResult>() {
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

                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                });
    }

}