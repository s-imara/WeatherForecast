package com.test.simara.weatherforecast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by Simara on 23.04.2017.
 */

public class Utils {
    public static String dateToString(Date date) {
        DateFormat dateStyle = new DateFormat();
        return dateStyle.format("yyyy-MM-dd HH:mm", date).toString();
    }

    public static String getTemperatureInC(int temp) {
        return (String.valueOf(temp - 273) + " ℃");
    }

    public static int dateToInt(Date date) {
        return  (int) (date.getTime()/1000);
    }
    public static Date intToDate(int date){
        return new Date(((long)date)*1000L);
    }

    public static byte[] bitmapToByteArray(Bitmap icon) {
        if(icon != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
        return null;
    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        if(byteArray != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        }
        return null;
    }
}
