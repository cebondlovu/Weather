package com.softcrypt.weather.common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Common {
    public static final String $API_KEY = "03f34a39df28c108e767b6d1ebdb4cb8";
    public static Location current_location = null;
    public static List<String> globalCityList = new ArrayList<>();

    public static String convertUnixToDate(long dt) {
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String formatted = sdf.format(date);

        return formatted;
    }

    public static String convertUnixToHour(long value) {
        Date date = new Date(value*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);

        return formatted;
    }
}
