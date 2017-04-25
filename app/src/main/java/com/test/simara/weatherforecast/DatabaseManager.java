package com.test.simara.weatherforecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Simara on 23.04.2017.
 */

public class DatabaseManager {

    private static final String TAG = DatabaseManager.class.getCanonicalName();
    private static final int numberOfItemForForecast = 40;
    public static final String TABLE_NAME = "weather";
    public static final String DB_NAME = "weather_database.db";
    public static final String DATE = "date";

    private static String DB_PATH = "";
    private static String ACTUAL_ID = "actual_id";
    private static String CITY = "city";
    private static String CITY_FOR_SEARCH = "city_for_search";
    private static String COUNTRY = "country";
    private static String TEMPERATURE = "temperature";
    private static String DESCRIPTION = "description";
    private static String PRESSURE = "pressure";
    private static String HUMIDITY = "humidity";
    private static String ICON = "icon";
    private static String ICON_FROM_SITE = "icon_from_site";
    private static String LAST_UPDATE = "last_updated";
    private Context context;
    private SQLiteDatabase db = null;


    public DatabaseManager(Context context) {
        this.context = context;
        try {
            StringBuilder query = new StringBuilder();
            createQuery(query, ACTUAL_ID, "INTEGER");
            createQuery(query, CITY, "TEXT");
            createQuery(query, CITY_FOR_SEARCH, "TEXT");
            createQuery(query, COUNTRY, "TEXT");
            createQuery(query, DATE, "INTEGER");
            createQuery(query, TEMPERATURE, "INTEGER");
            createQuery(query, DESCRIPTION, "TEXT");
            createQuery(query, PRESSURE, "TEXT");
            createQuery(query, HUMIDITY, "TEXT");
            createQuery(query, ICON, "TEXT");
            createQuery(query, ICON_FROM_SITE, "BLOB");
            createQuery(query, LAST_UPDATE, "INTEGER");

            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            db = context.openOrCreateDatabase(DB_PATH + DB_NAME, Context.MODE_PRIVATE, null);
            if (db.isOpen()) {
                 /* Create a Table in the Database. */
                db.execSQL("CREATE TABLE IF NOT EXISTS "
                        + TABLE_NAME
                        + " (" + query + "PRIMARY KEY ( "+ CITY_FOR_SEARCH + ", "+ DATE + "));");
            }
        } catch (SQLiteException e) {
            Log.e("DatabaseManager", "Error", e);
            if (db != null)
                db.close();
        }
    }

    public void addDataToDatabase(ArrayList<WeatherModel> models) {
        if (db.isOpen()) {
            for (int i = 0; i < models.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(ACTUAL_ID, models.get(i).getId());
                values.put(CITY, models.get(i).getCity());
                values.put(CITY_FOR_SEARCH, models.get(i).getCityForSearch());
                values.put(COUNTRY, models.get(i).getCountry());
                values.put(DATE, Utils.dateToInt(models.get(i).getDate()));
                values.put(TEMPERATURE, models.get(i).getTemperature());
                values.put(DESCRIPTION, models.get(i).getDescription());
                values.put(PRESSURE, models.get(i).getPressure());
                values.put(HUMIDITY, models.get(i).getHumidity());
                values.put(ICON, models.get(i).getIcon());
                values.put(ICON_FROM_SITE, Utils.bitmapToByteArray(models.get(i).getIconFromSite()));
                values.put(LAST_UPDATE, Utils.dateToInt(models.get(i).getLastUpdated()));
                try {
                    db.insertOrThrow(TABLE_NAME, null, values);
                }
                catch (SQLiteConstraintException ex){
                    Log.d(TAG, ex.getMessage());
                    db.insertWithOnConflict(TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                }

                catch (SQLException ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }
    }

    //get data on current day and on four next ones
    public ArrayList<WeatherModel> getDataFromDb(String city) {
        ArrayList<WeatherModel> models = new ArrayList<WeatherModel>();
        if (db == null || !db.isOpen()) {
            db = context.openOrCreateDatabase(DB_PATH + DB_NAME, Context.MODE_PRIVATE, null);
        }
        for (int i = 0; i < numberOfItemForForecast; i++) {
            Date today = new Date(new Date().getTime());
            int dayNow =  today.getDay();
            int hoursNow = today.getHours();
            today.setHours(i * 3);
            today.setMinutes(0);
            today.setSeconds(0);
            if (dayNow == today.getDay() && hoursNow > today.getHours()) {
                continue;
            }
            int d = Utils.dateToInt(today);
            String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + DATE + " = " + d
                    + " AND " + CITY_FOR_SEARCH + " LIKE " +"'"+ city+"'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    WeatherModel model = new WeatherModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(ACTUAL_ID)));
                    model.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
                    model.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
                    model.setDate(Utils.intToDate(cursor.getInt(cursor.getColumnIndex(DATE))));
                    model.setTemperature(cursor.getInt(cursor.getColumnIndex(TEMPERATURE)));
                    model.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                    model.setPressure(cursor.getString(cursor.getColumnIndex(PRESSURE)));
                    model.setHumidity(cursor.getString(cursor.getColumnIndex(HUMIDITY)));
                    model.setIcon(cursor.getString(cursor.getColumnIndex(ICON)));
                    model.setIconFromSite(Utils.byteArrayToBitmap(cursor.getBlob(cursor.getColumnIndex(ICON_FROM_SITE))));
                    model.setLastUpdated(Utils.intToDate((cursor.getInt(cursor.getColumnIndex(LAST_UPDATE)))));
                    models.add(model);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return models;
    }

    private void createQuery(StringBuilder query, String fieldName, String type) {
        query.append(fieldName);
        query.append(" ");
        query.append(type);
        query.append(", ");
    }
}
