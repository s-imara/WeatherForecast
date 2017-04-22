package com.test.simara.weatherforecast;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by Simara on 22.04.2017.
 */

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {
    private ArrayList<WeatherModel> models;
    private Context context;

    public WeatherRecyclerViewAdapter(ArrayList<WeatherModel> models) {
        this.models = models;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WeatherRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, null);
        context = parent.getContext();
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txtViewTemp.setText(models.get(position).getTemperature());
        new ImageLoadTask( models.get(position).getIconUrl(),viewHolder.imgViewIcon).execute();
        DateFormat dateStyle = DateFormat.getDateInstance();
        viewHolder.txtViewDate.setText(dateStyle.format(models.get(position).getDate()));
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtViewTemp;
        public ImageView imgViewIcon;
        public TextView txtViewDate;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTemp = (TextView) itemLayoutView.findViewById(R.id.item_temp);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            txtViewDate = (TextView) itemLayoutView.findViewById(R.id.item_date);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            WeatherModel model = models.get(position);
            ((MainActivity)view.getContext()).getWeatherFragment().renderWeather(model);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}



