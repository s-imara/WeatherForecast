package com.test.simara.weatherforecast;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Simara on 21.04.2017.
 */

public class RemoteDataManager {
    private static RemoteDataManager instance;

    public static RemoteDataManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return new RemoteDataManager();
    }

    private RemoteDataManager() {
    }

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/forecast?q=%s&&APPID=%s";
    private static final String ICON_URL = "http://openweathermap.org/img/w/%s.png";

    private JSONObject getJSON(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city, context.getString(R.string.open_weather_maps_app_id)));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<WeatherModel> getFilledModel(Context context, String city) {
        final JSONObject json = getJSON(context, city);
        ArrayList<WeatherModel> weatherList = new ArrayList<WeatherModel>();
        try {
            String foundCity = json.getJSONObject("city").getString("name").toUpperCase(Locale.US);
            String country = json.getJSONObject("city").getString("country");
            JSONArray weatherArray = json.getJSONArray("list");

            for (int i = 0; i < weatherArray.length(); i++) {
                WeatherModel model = new WeatherModel();
                JSONObject details = weatherArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                JSONObject main = weatherArray.getJSONObject(i).getJSONObject("main");
                String description = details.getString("description").toUpperCase(Locale.US);
                String humidity = main.getString("humidity") + "%";
                String pressure = main.getString("pressure") + " hPa";
                int temperature = (int) Math.round(main.getDouble("temp")) - 273;
                DateFormat df = DateFormat.getDateTimeInstance();
                String lastUpdateDate = df.format(new Date().getTime());
                String forecastDate = weatherArray.getJSONObject(i).getString("dt_txt");
                int actualId = details.getInt("id");
                URL iconUrl = new URL(String.format(ICON_URL, details.getString("icon")));
                model.setCity(foundCity);
                model.setCountry(country);
                model.setDescription(description);
                model.setDate(forecastDate);
                model.setHumidity(humidity);
                model.setLastUpdated(lastUpdateDate);
                model.setPressure(pressure);
                model.setId(actualId);
                setWeatherIcon(context, actualId, model);
                model.setIconUrl(iconUrl);
                model.setTemperature(String.valueOf(temperature) + " ℃");
                weatherList.add(model);
            }
        } catch (Exception e) {
            Log.e("WeatherForecast", "One or more fields not found in the JSON data");
        }
        return weatherList;
    }

    private void setWeatherIcon(Context context, int actualId, WeatherModel model) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            icon = context.getString(R.string.weather_sunny);
        } else {
            switch (id) {
                case 2:
                    icon = context.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = context.getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = context.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = context.getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = context.getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = context.getString(R.string.weather_rainy);
                    break;
            }
        }
        model.setIcon(icon);
    }
}


