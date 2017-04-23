package com.test.simara.weatherforecast;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    @Override
    public void onResume() {
        super.onResume();
        updateWeatherData(new LocationPreference(getActivity()).getCity());
    }

    private void updateWeatherData(final String city) {

        new Thread() {
            public void run() {
                final ArrayList<WeatherModel> weatherModels;
                DatabaseManager manager =((MainActivity)getActivity()).getDatabaseManager();
                if(isOnline()) {
                   weatherModels = RemoteDataManager.getInstance().getFilledModel(getActivity(), city);
                    if(manager != null) {
                        manager.addDataToDatabase(weatherModels);
                    }
                }
                else {
                    if(manager != null) {
                        weatherModels = manager.getDataFromDb();
                    }
                    else {
                        weatherModels = null;
                    }
                }
                if (weatherModels == null || weatherModels.size() == 0) {
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
                            renderWeather(weatherModels.get(0));
                            ((MainActivity)getActivity()).setDataAdapter(weatherModels);
                        }
                    });
                }
            }
        }.start();
    }
    public void renderWeather(WeatherModel model) {
        cityField.setText(model.getCity() + ", " + model.getCountry());
        dateField.setText(Utils.dateToString(model.getDate()));
        detailsField.setText(model.getDescription() +
                "\n" + "Humidity: " + model.getHumidity() +
                "\n" + "Pressure: " + model.getPressure());
        currentTemperatureField.setText(Utils.getTemperatureInC(model.getTemperature()));
        updatedField.setText("Last update: " + model.getLastUpdated());
        weatherIcon.setText(model.getIcon());
    }

    public void changeCity(String city) {
        updateWeatherData(city);
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
