package com.test.simara.weatherforecast;

import android.graphics.Bitmap;
import android.util.Log;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simara on 22.04.2017.
 */

public class WeatherModel {
    private static final String TAG = WeatherModel.class.getCanonicalName();

    private String city;
    private String cityForSearch;
    private String country;
    private Date date;
    private int temperature;
    private String description;
    private String pressure;
    private String humidity;
    private int id;
    private String icon;
    private URL iconUrl;
    private Date lastUpdated;

    private Bitmap iconFromSite;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityForSearch() {
        return cityForSearch;
    }

    public void setCityForSearch(String cityForSearch) {
        this.cityForSearch = cityForSearch;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public URL getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Bitmap getIconFromSite() {
        return iconFromSite;
    }

    public void setIconFromSite(Bitmap iconFromSite) {
        this.iconFromSite = iconFromSite;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(String dateString) {
        try {
            setDate(new Date(Long.parseLong(dateString)));
        } catch (Exception e) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                setDate(inputFormat.parse(dateString));
            } catch (ParseException e2) {
                setDate(new Date()); // make the error somewhat obvious
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}

