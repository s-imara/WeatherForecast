package com.test.simara.weatherforecast;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements ModelChangeListener {

    DatabaseManager databaseManager = null;
    WeatherRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherFragment())
                    .commit();
        }
        databaseManager = new DatabaseManager(this);
        new DatabaseCleanupTask(this, databaseManager).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecyclerViewAdapter();
    }

    private void initRecyclerViewAdapter() {
        adapter = new WeatherRecyclerViewAdapter();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        WeatherFragment fragment = (WeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.setModelChangeListener(this);
        RemoteDataManager.getInstance().setModelChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_city) {
            showInputDialog();
        }
        return true;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city) {
        WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);
        wf.changeCity(city);
        new LocationPreference(this).setCity(city);
    }

    public WeatherFragment getWeatherFragment() {
        return (WeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void onDataChanged(ArrayList<WeatherModel> models) {
        if(adapter != null) {
            adapter.updateData(MainActivity.this, models);
        }
    }
}


