package com.test.simara.weatherforecast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Simara on 22.04.2017.
 */

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private static final String TAG = ImageLoadTask.class.getCanonicalName();

    private WeatherModel model;

    public ImageLoadTask(WeatherModel model) {
        this.model = model;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap iconFromSite = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) model.getIconUrl().openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            iconFromSite = BitmapFactory.decodeStream(input);
            model.setIconFromSite(iconFromSite);
            return iconFromSite;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            iconFromSite = model.getIconFromSite();
        }
        return iconFromSite;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
    }

}
