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
    private static final String TABLE_NAME = "weather";
    private static final String DB_NAME = "weather_database.db";

    private static final String ACTUAL_ID = "actual_id";
    private static final String CITY = "city";
    private static final String CITY_FOR_SEARCH = "city_for_search";
    private static final String COUNTRY = "country";
    private static final String DATE = "date";
    private static final String TEMPERATURE = "temperature";
    private static final String DESCRIPTION = "description";
    private static final String PRESSURE = "pressure";
    private static final String HUMIDITY = "humidity";
    private static final String ICON = "icon";
    private static final String ICON_FROM_SITE = "icon_from_site";
    private static final String LAST_UPDATE = "last_updated";

    private static String dbPath = "";
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
            openDb();
                 /* Create a Table in the Database. */
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TABLE_NAME
                    + " (" + query + "PRIMARY KEY ( " + CITY_FOR_SEARCH + ", " + DATE + "));");

        } catch (SQLiteException e) {
            Log.e("DatabaseManager", "Error", e);
            if (db != null)
                db.close();
        }
    }

    public void addDataToDatabase(ArrayList<WeatherModel> models) {
        openDb();
        for (WeatherModel model : models) {
            ContentValues values = new ContentValues();
            values.put(ACTUAL_ID, model.getId());
            values.put(CITY, model.getCity());
            values.put(CITY_FOR_SEARCH, model.getCityForSearch());
            values.put(COUNTRY, model.getCountry());
            values.put(DATE, Utils.dateToInt(model.getDate()));
            values.put(TEMPERATURE, model.getTemperature());
            values.put(DESCRIPTION, model.getDescription());
            values.put(PRESSURE, model.getPressure());
            values.put(HUMIDITY, model.getHumidity());
            values.put(ICON, model.getIcon());
            values.put(ICON_FROM_SITE, Utils.bitmapToByteArray(model.getIconFromSite()));
            values.put(LAST_UPDATE, Utils.dateToInt(model.getLastUpdated()));
            try {
                db.insertOrThrow(TABLE_NAME, null, values);
            } catch (SQLiteConstraintException ex) {
                Log.d(TAG, ex.getMessage());
                db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (SQLException ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    //get data on current day and on four next ones
    public ArrayList<WeatherModel> getDataFromDb(String city) {
        ArrayList<WeatherModel> models = new ArrayList<WeatherModel>();
        openDb();
        for (int i = 0; i < numberOfItemForForecast; i++) {
            Date today = new Date(new Date().getTime());
            int dayNow = today.getDay();
            int hoursNow = today.getHours();
            today.setHours(i * 3);
            today.setMinutes(0);
            today.setSeconds(0);
            if (dayNow == today.getDay() && hoursNow > today.getHours()) {
                continue;
            }
            int d = Utils.dateToInt(today);
            String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + DATE + " = " + d
                    + " AND " + CITY_FOR_SEARCH + " LIKE " + "'" + city + "'";
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

    private void openDb() {
        if (db == null || !db.isOpen()) {
            dbPath = context.getApplicationInfo().dataDir + "/databases/";
            db = context.openOrCreateDatabase(dbPath + DB_NAME, Context.MODE_PRIVATE, null);
        }
    }

    public void cleanupDb() {
        openDb();
        Date today = new Date(new Date().getTime());
        int d = Utils.dateToInt(today);
        String deleteQuery = DatabaseManager.DATE + " < " + d;
        db.delete(DatabaseManager.TABLE_NAME, deleteQuery, null);
    }
}
