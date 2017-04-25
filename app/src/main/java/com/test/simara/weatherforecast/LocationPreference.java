package com.test.simara.weatherforecast;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Simara on 22.04.2017.
 */

public class LocationPreference {
    SharedPreferences prefs;

    public LocationPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    // If the user has not chosen a city yet, return
    // Zaporizhzhya as the default city
    public String getCity() {
        return prefs.getString("city", "Zaporizhzhya");
    }

    public void setCity(String city) {
        prefs.edit().putString("city", city).commit();
    }
}
