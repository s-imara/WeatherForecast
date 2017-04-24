package com.test.simara.weatherforecast;

import java.util.ArrayList;

/**
 * Created by Simara on 24.04.2017.
 */

public interface ModelChangeListener {
    void onDataChanged(ArrayList<WeatherModel> model);
}
