package com.test.simara.weatherforecast;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Simara on 22.04.2017.
 */

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {
    private ArrayList<WeatherModel> models;

    public WeatherRecyclerViewAdapter() {
        models = new  ArrayList<WeatherModel>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WeatherRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txtViewTemp.setText(Utils.getTemperatureInC(models.get(position).getTemperature()));
        viewHolder.imgViewIcon.setImageBitmap(models.get(position).getIconFromSite());
        viewHolder.txtViewDate.setText(Utils.dateToString(models.get(position).getDate()));

    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtViewTemp;
        private ImageView imgViewIcon;
        private TextView txtViewDate;

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
            ((MainActivity) view.getContext()).getWeatherFragment().renderWeather(model);
        }
    }

    public void updateData(Context context, ArrayList<WeatherModel> newModels) {
        if(newModels != null) {
            models.clear();
            models.addAll(newModels);
            Handler mainHandler = new Handler(context.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() { notifyDataSetChanged();}
            };
            mainHandler.post(myRunnable);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}



