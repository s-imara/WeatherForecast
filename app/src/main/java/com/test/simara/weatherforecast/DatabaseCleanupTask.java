package com.test.simara.weatherforecast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.Date;

/**
 * Created by Simara on 25.04.2017.
 */

public class DatabaseCleanupTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private DatabaseManager manager;

    public DatabaseCleanupTask(Context context, DatabaseManager manager) {
        this.context = context;
        this.manager = manager;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        manager.cleanupDb();
        return null;
    }
}
