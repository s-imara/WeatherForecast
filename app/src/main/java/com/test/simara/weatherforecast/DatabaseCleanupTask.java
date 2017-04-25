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
    private SQLiteDatabase db;
    private String dbPath;

    public DatabaseCleanupTask(Context context) {
        this.context = context;
        dbPath = context.getApplicationInfo().dataDir + "/databases/";
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (db == null || !db.isOpen()) {
            db = context.openOrCreateDatabase(dbPath + DatabaseManager.DB_NAME, Context.MODE_PRIVATE, null);
        }
        Date today = new Date(new Date().getTime());
        int d = Utils.dateToInt(today);
        String deleteQuery = DatabaseManager.DATE + " < " + d;
        db.delete(DatabaseManager.TABLE_NAME, deleteQuery, null);
        return null;
    }
}
