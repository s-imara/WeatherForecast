package com.test.simara.weatherforecast;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by Simara on 23.04.2017.
 */

public class Utils {
    public static String dateToString(Date date){
        DateFormat dateStyle = new DateFormat();
        return dateStyle.format("yyyy-MM-dd HH:mm:ss", date).toString();
    }
    public static String getTemperatureInC(int temp){
       return (String.valueOf(temp - 273) + " ℃");
    }
    public static int dateToInt(Date date){
        return (int)date.getTime();
    }
}
