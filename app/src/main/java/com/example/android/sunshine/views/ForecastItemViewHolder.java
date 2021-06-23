package com.example.android.sunshine.views;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.R;
import com.example.android.sunshine.data.WeatherData;
import com.example.android.sunshine.databinding.ForecastListItemBinding;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class ForecastItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ForecastListItemBinding mItemBinding;
    WeatherData mItemData;
    ForecastItemClickListener mClickListener;

    public ForecastItemViewHolder(@NonNull ForecastListItemBinding forecastListItemBinding,
                                  ForecastItemClickListener clickListener) {
        super(forecastListItemBinding.getRoot());
        this.mItemBinding = forecastListItemBinding;
        this.mClickListener = clickListener;
        mItemBinding.getRoot().setOnClickListener(this);
    }

    public void bind(WeatherData itemData) {
        mItemData = itemData;

        int weatherImageResourceId =
                SunshineWeatherUtils.getIconResourceForWeatherCondition(mItemData.getWeatherCode());

        if (weatherImageResourceId == -1) {
            Log.d("ForecastItemViewHolder", "Bind : " + itemData);
            Log.d("ForecastItemViewHolder", "Negative Resource Id");
            weatherImageResourceId = R.drawable.art_clear;
        }

        mItemBinding.ivWeatherImage.setImageResource(weatherImageResourceId);
        mItemBinding.tvWeatherDay.setText(mItemData.getDayAsShortString());
        mItemBinding.tvWeatherLabel.setText(mItemData.getWeatherLabel());
        mItemBinding.tvLowTemp.setText(SunshineWeatherUtils.formatTemperature(mItemBinding.getRoot().getContext(), mItemData.getLowTemp()));
        mItemBinding.tvHighTemp.setText(SunshineWeatherUtils.formatTemperature(mItemBinding.getRoot().getContext(), mItemData.getHighTemp()));
    }

    @Override
    public void onClick(View view) {
        mClickListener.onForecastItemClicked(getBindingAdapterPosition(), mItemData);
    }
}
