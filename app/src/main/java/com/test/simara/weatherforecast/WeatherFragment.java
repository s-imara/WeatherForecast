package com.test.simara.weatherforecast;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Simara on 21.04.2017.
 */

public class WeatherFragment extends Fragment {
    Typeface weatherFont;
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;

    Handler handler;

    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragment, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        detailsField = (TextView) rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) rootView.findViewById(R.id.weather_icon);

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        updateWeatherData(new LocationPreference(getActivity()).getCity());
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteDataManager.getJSON(getActivity(), city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        try {
            String city = json.getJSONObject("city").getString("name").toUpperCase(Locale.US);
            String country = json.getJSONObject("city").getString("country");

            JSONArray weatherArray = json.getJSONArray("list");
            ArrayList<WeatherModel> weatherList = new ArrayList<WeatherModel>();
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
                model.setCity(city);
                model.setCountry(country);
                model.setDescription(description);
                model.setDate(forecastDate);
                model.setHumidity(humidity);
                model.setLastUpdated(lastUpdateDate);
                model.setPressure(pressure);
                model.setId(actualId);
                setWeatherIcon(actualId, model);
                model.setTemperature(String.valueOf(temperature) + " ℃");
                weatherList.add(model);
            }
            WeatherModel currentModel = weatherList.get(0);
            cityField.setText(city + ", " + country);
            detailsField.setText(currentModel.getDescription() +
                    "\n" + "Humidity: " + currentModel.getHumidity() +
                    "\n" + "Pressure: " + currentModel.getPressure());

            currentTemperatureField.setText(currentModel.getTemperature());
            updatedField.setText("Last update: " + currentModel.getLastUpdated());
        } catch (Exception e) {
            Log.e("WeatherForecast", "One or more fields not found in the JSON data");
        }
    }


    private void setWeatherIcon(int actualId, WeatherModel model) {
        int id = actualId / 100;
        String icon = "";
        switch (id) {
            case 2:
                icon = getActivity().getString(R.string.weather_thunder);
                break;
            case 3:
                icon = getActivity().getString(R.string.weather_drizzle);
                break;
            case 7:
                icon = getActivity().getString(R.string.weather_foggy);
                break;
            case 8:
                icon = getActivity().getString(R.string.weather_cloudy);
                break;
            case 6:
                icon = getActivity().getString(R.string.weather_snowy);
                break;
            case 5:
                icon = getActivity().getString(R.string.weather_rainy);
                break;
        }
        model.setIcon(icon);
        weatherIcon.setText(icon);
    }

    public void changeCity(String city) {
        updateWeatherData(city);
    }
}
