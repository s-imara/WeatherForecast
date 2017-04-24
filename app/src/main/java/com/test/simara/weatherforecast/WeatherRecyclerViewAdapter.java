package com.test.simara.weatherforecast;

import android.content.Context;
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
    private Context context;
    ViewHolder viewHolder;

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
        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(viewHolder != null) {
            viewHolder.txtViewTemp.setText(Utils.getTemperatureInC(models.get(position).getTemperature()));
            viewHolder.imgViewIcon.setImageBitmap(models.get(position).getIconFromSite());
            viewHolder.txtViewDate.setText(Utils.dateToString(models.get(position).getDate()));
        }
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
            ((MainActivity) view.getContext()).getWeatherFragment().renderWeather(model);
        }
    }
public void updateAdapter(WeatherModel model){
    if(models.contains(model)){
        int pos =  models.indexOf(model);
        models.get(pos).setIconFromSite(model.getIconFromSite());
        this.onBindViewHolder(viewHolder,pos);
    }
}
    public void updateAdapter(ArrayList<WeatherModel> weatherModels){
        if(models !=null && weatherModels != null) {
            for (int i = 0; i < models.size(); i++) {
                models.get(i).setIconFromSite(weatherModels.get(i).getIconFromSite());
                this.onBindViewHolder(viewHolder, i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}



