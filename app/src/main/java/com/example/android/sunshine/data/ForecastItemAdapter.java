package com.example.android.sunshine.data;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.R;
import com.example.android.sunshine.databinding.ForecastListItemBinding;
import com.example.android.sunshine.views.ForecastItemClickListener;
import com.example.android.sunshine.views.ForecastItemViewHolder;

public class ForecastItemAdapter extends RecyclerView.Adapter<ForecastItemViewHolder> {
    WeatherData[] mWeatherData;
    ForecastItemClickListener mClickListener;

    public ForecastItemAdapter(ForecastItemClickListener clickListener) {
        mWeatherData = null;
        mClickListener = clickListener;
    }

    @Override
    public ForecastItemViewHolder onCreateViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parentViewGroup.getContext());
        ForecastListItemBinding forecastListItemBinding =
                ForecastListItemBinding.inflate(layoutInflater, parentViewGroup, false);
        return new ForecastItemViewHolder(forecastListItemBinding, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastItemViewHolder viewHolder, int position) {
        Log.d("ForecastItemVH", "Bind ViewHolder: " + position);
        viewHolder.bind(mWeatherData[position]);
    }

    @Override
    public int getItemCount() {
        return mWeatherData == null ? 0 : mWeatherData.length;
    }

    public void setWeatherData(WeatherData[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }
}
