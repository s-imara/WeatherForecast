package com.test.simara.weatherforecast;

import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
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
        return Math.round(date.getTime() / 1000);
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
