package com.test.simara.weatherforecast;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Simara on 21.04.2017.
 */

public class WeatherFragment extends Fragment {
    private Typeface weatherFont;
    private TextView cityField;
    private TextView updatedField;
    private TextView detailsField;
    private TextView currentTemperatureField;
    private TextView dateField;
    private TextView weatherIcon;
    private Handler handler;

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
        dateField = (TextView)  rootView.findViewById(R.id.date_field);
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
                final ArrayList<WeatherModel> weatherModel = RemoteDataManager.getInstance().getFilledModel(getActivity(), city);
                if (weatherModel == null) {
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
                            renderWeather(weatherModel);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(ArrayList<WeatherModel> weatherModelList) {

        WeatherModel currentModel = weatherModelList.get(0);
        cityField.setText(currentModel.getCity() + ", " + currentModel.getCountry());
        DateFormat dateStyle = DateFormat.getDateTimeInstance();
        dateField.setText(dateStyle.format(currentModel.getDate()));
        detailsField.setText(currentModel.getDescription() +
                "\n" + "Humidity: " + currentModel.getHumidity() +
                "\n" + "Pressure: " + currentModel.getPressure());

        currentTemperatureField.setText(currentModel.getTemperature());
        updatedField.setText("Last update: " + currentModel.getLastUpdated());
        weatherIcon.setText(currentModel.getIcon());
        ((MainActivity)getActivity()).setDataAdapter(weatherModelList);
    }
    public void renderWeather(WeatherModel model) {
        cityField.setText(model.getCity() + ", " + model.getCountry());
        DateFormat dateStyle = DateFormat.getDateTimeInstance();
        dateField.setText(dateStyle.format(model.getDate()));
        detailsField.setText(model.getDescription() +
                "\n" + "Humidity: " + model.getHumidity() +
                "\n" + "Pressure: " + model.getPressure());

        currentTemperatureField.setText(model.getTemperature());
        updatedField.setText("Last update: " + model.getLastUpdated());
       String icon = model.getIcon();
        weatherIcon.setText(model.getIcon());
    }

    public void changeCity(String city) {
        updateWeatherData(city);
    }
}
